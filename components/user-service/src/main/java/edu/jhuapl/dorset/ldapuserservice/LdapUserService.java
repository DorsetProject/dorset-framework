package edu.jhuapl.dorset.ldapuserservice;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import javax.naming.AuthenticationException;
import javax.naming.AuthenticationNotSupportedException;
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
import com.typesafe.config.ConfigFactory;

import edu.jhuapl.dorset.users.User;
import edu.jhuapl.dorset.users.UserException;
import edu.jhuapl.dorset.users.UserService;

public class LdapUserService implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(LdapUserService.class);

    private Map<String, User> users;

    private String ldapServer;
    private String ldapSearchBase;
    // private String ldapUsername;
    private String ldapPassword;
    private String ldapSecurityPrinciple;

    private String[] userAttributes;

    /**
     * 
     * LdapUserService
     * 
     */
    public LdapUserService() {
        Config conf = ConfigFactory.load();

        this.ldapServer = conf.getString("ldap-context.ldapServer");
        this.ldapSearchBase = conf.getString("ldap-context.ldapSearchBase");
        //this.ldapUsername = conf.getString("ldap-context.ldapUserName");
        this.ldapPassword = conf.getString("ldap-context.ldapPassword");
        this.ldapSecurityPrinciple = conf.getString("ldap-context.ldapSecurityPrinciple");

        String userAttributesStr = conf.getString("ldap-search-controls.userAttributes");
        this.userAttributes = userAttributesStr.replaceAll("\"", "").split(",");
        for (int i = 0; i < this.userAttributes.length; i++) {
            this.userAttributes[i] = this.userAttributes[i].trim();
        }

        this.users = new HashMap<String, User>();

    }

    private SearchControls getSearchControls(String[] attributes) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        searchControls.setReturningAttributes(attributes);
        return searchControls;
    }

    @Override
    public String create(User user) throws UserException {
        throw new UnsupportedOperationException("Invalid operation for LdapUserService. "
                        + "This interface currently only allows read only methods.");
    }

    @Override
    public void delete(String userName) {
        throw new UnsupportedOperationException("Invalid operation for LdapUserService. "
                        + "This interface currently only allows read only methods.");
    }

    @Override
    public User getUser(String userName) {
        User user = null;

        // check if user exists in the hash map
        user = this.users.get(userName);

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

        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");

        env.put(Context.PROVIDER_URL, this.ldapServer);
        env.put(Context.SECURITY_PRINCIPAL, this.ldapSecurityPrinciple);
        env.put(Context.SECURITY_CREDENTIALS, this.ldapPassword);

        try {
            DirContext ctx = new InitialDirContext(env);
            NamingEnumeration<SearchResult> answer = ctx.search(this.ldapSearchBase,
                            "sAMAccountName=" + userName, searchControls);
            if (answer.hasMore()) {
                Attributes attrs = answer.next().getAttributes();
                NamingEnumeration<String> attrsKeys = attrs.getIDs();
                while (attrsKeys.hasMoreElements()) {
                    key = attrsKeys.next();
                    value = attrs.get(key);
                    user.setUserInformation(key, value.toString());
                }
            } else {
                logger.info("user not found: " + userName);
                return null; 
            }

        } catch (AuthenticationNotSupportedException ex) {
            logger.debug("The authentication is not supported by the server");
        } catch (AuthenticationException ex) {
            logger.debug("Incorrect Username or Password");
        } catch (NamingException ex) {
            logger.debug("Error when trying to establish the context");
        }

        this.users.put(userName, user);

        return userName;
    }

    @Override
    public void update(String userName, User user) throws UserException {
        throw new UnsupportedOperationException("Invalid operation for LdapUserService. "
                        + "This interface currently only allows read only methods.");
    }
}
