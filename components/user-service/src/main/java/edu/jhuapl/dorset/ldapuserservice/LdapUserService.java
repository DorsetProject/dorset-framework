/*
 * Copyright 2017 The Johns Hopkins University Applied Physics Laboratory LLC
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.jhuapl.dorset.ldapuserservice;

import java.util.HashMap;

import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;

import edu.jhuapl.dorset.users.User;
import edu.jhuapl.dorset.users.UserException;
import edu.jhuapl.dorset.users.UserService;

/**
 * LdapUserService userservice
 *
 * LdapUserService is a UserService that pulls users' information from a LDAP server. The
 * configuration information for the LDAP server, as well as the LDAP search controls, are stored in
 * a configuration object. The LdapUserService uses the Typesafe configuration library.
 * 
 * <p>
 * The LDAP server configuration is used and should be passed in through the constructor:
 * <pre>
 * ldapServer = "ldaps://..."
 * ldapSearchBase = "..."
 * ldapPassword = "..."
 * ldapBindDn = "..."
 * ldapFilterAttribute = "..."
 * </pre>
 *
 * <p>
 * User attributes (e.g. name, email, etc.) pulled from LDAP needs to be specified in the
 * configuration object to match the LDAP search controls:
 * <pre>
 * userAttributes = "firstName, lastName, email, phoneNumber, ..."
 * </pre>
 */
public class LdapUserService implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(LdapUserService.class);

    private Map<String, User> users;

    private static final String LDAP_SERVER_KEY = "ldapServer";
    private static final String LDAP_SEARCH_BASE_KEY = "ldapSearchBase";
    private static final String LDAP_PASSWORD_KEY = "ldapPassword";
    private static final String LDAP_BIND_DN_KEY = "ldapBindDn";
    private static final String LDAP_FILTER_ATTRIBUTE_KEY = "ldapFilterAttribute";
    private static final String LDAP_SEARCH_CONTROLS_KEY = "userAttributes";

    private final String ldapServer;
    private final String ldapSearchBase;
    private final String ldapPassword;
    private final String ldapBindDn;
    private final String ldapFilterAttribute;

    private String[] userAttributes;
    private DirContext ctx;
    
    /**
     * 
     * LdapUserService
     * 
     * @param  config Configuration object that stores the LDAP server information and user attributes
     * @throws NamingException when Context cannot be instantiated
     */
    public LdapUserService(Config config) throws NamingException {
        this.users = new HashMap<String, User>();
        
        Config conf = config;

        this.ldapServer = conf.getString(LDAP_SERVER_KEY);
        this.ldapSearchBase = conf.getString(LDAP_SEARCH_BASE_KEY);
        this.ldapPassword = conf.getString(LDAP_PASSWORD_KEY);
        this.ldapBindDn = conf.getString(LDAP_BIND_DN_KEY);
        this.ldapFilterAttribute = conf.getString(LDAP_FILTER_ATTRIBUTE_KEY);

        String userAttributesStr = conf.getString(LDAP_SEARCH_CONTROLS_KEY);
        userAttributes = userAttributesStr.replaceAll("\"", "").split(",");
        for (int i = 0; i < userAttributes.length; i++) {
            userAttributes[i] = userAttributes[i].trim();
        }
        this.setUserAttributes(userAttributes);
  
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");

        env.put(Context.PROVIDER_URL, this.ldapServer);
        env.put(Context.SECURITY_PRINCIPAL, this.ldapBindDn);
        env.put(Context.SECURITY_CREDENTIALS, this.ldapPassword);

        try {
            this.ctx = new InitialDirContext(env);
        } catch (NamingException ex) {
            throw new NamingException("Context could not be created. " + ex);
        }    
        
        this.setContext(this.ctx);
    }

    /**
     * 
     * LdapUserService
     * 
     * @param context Context object that is instantiated with LDAP server information
     * @param userAttributes String array containing User attributes that is used to generate LDAP
     *        search controls
     */
    public LdapUserService(DirContext context, String[] userAttributes) {
        this.ctx = context;

        this.ldapServer = null;
        this.ldapSearchBase = null;
        this.ldapPassword = null;
        this.ldapBindDn = null;
        this.ldapFilterAttribute = null;

        this.userAttributes = userAttributes;

        this.users = new HashMap<String, User>();
    }

    @Override
    public String create(User user) throws UserException {
        throw new UnsupportedOperationException("LdapUserService does not implement Create.");
    }

    @Override
    public void delete(String userName) {
        throw new UnsupportedOperationException("LdapUserService does not implement Delete.");
    }

    @Override
    public User getUser(String userName) {
        // check if user exists in the hash map
        User user = this.users.get(userName);

        if (user == null) {

            Properties props = new Properties();
            props.setProperty("userName", userName);

            try {
                this.retrieve(props);
            } catch (UserException e) {
                logger.debug("User not found: " + userName);
                return null;
            }
            user = this.users.get(userName);
        }

        return user;
    }
    
    @Override
    public String retrieve(Properties properties) throws UserException {
        User user = new User();
        String userName = properties.getProperty("userName");

        String key;
        Attribute value;
        
        SearchControls searchControls = getSearchControls(this.userAttributes);
        ctx = getContext();

        try {
            NamingEnumeration<SearchResult> answer = ctx.search(this.ldapSearchBase,
                            this.ldapFilterAttribute + "=" + userName, searchControls);
            if (answer.hasMore()) {
                Attributes attrs = answer.next().getAttributes();
                NamingEnumeration<String> attrsKeys = attrs.getIDs();
                while (attrsKeys.hasMoreElements()) {
                    key = attrsKeys.next();
                    value = attrs.get(key);
                    user.setUserInformation(key, value.toString());
                }
            } else { 
                throw new UserException("User not found: " + userName + ". ");
            }
        } catch (NamingException | NullPointerException ex) {
            throw new UserException("Could not make connection to LDAP Server. User not found: " + userName + ". ");
        }
        
        this.users.put(userName, user);

        return userName;
    }

    @Override
    public void update(String userName, User user) throws UserException {
        throw new UnsupportedOperationException("LdapUserService does not implement Update.");
    }

    private SearchControls getSearchControls(String[] attributes) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        searchControls.setReturningAttributes(attributes);
        return searchControls;
    }
    
    protected void setContext(DirContext ctx) {
        this.ctx = ctx;        
    }
    
    protected DirContext getContext() {
        return this.ctx;
    }
    
    protected String[] getUserAttributes() {
        return this.userAttributes;
    }

    protected void setUserAttributes(String[] userAttributes) {
        this.userAttributes = userAttributes;
    }
    
}
