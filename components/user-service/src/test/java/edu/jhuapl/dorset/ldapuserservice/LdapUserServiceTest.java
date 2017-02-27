/*
 * Copyright 2016 The Johns Hopkins University Applied Physics Laboratory LLC
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Properties;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.junit.Test;

import edu.jhuapl.dorset.users.User;
import edu.jhuapl.dorset.users.UserException;

public class LdapUserServiceTest {

    public User mockUser() {
        User user = new User();
        user.setFirstName("mock_FirstName");
        user.setLastName("mock_LastName");
        user.setUserName("mock_UserName");
        user.setEmail("mock_Email");
        user.setDob("mock_Dob");
        user.setLocation("mock_Location");
        return user;
    }

    @Test
    public void testGetUserSuccess() throws NamingException, UserException {
        User user = mockUser();
        Properties props = new Properties();
        props.setProperty("userName", user.getUserName());

        LdapUserService lus = new LdapUserService();

        DirContext ctx = mock(InitialDirContext.class);
        lus.setContext(ctx);

        NamingEnumeration<SearchResult> mockSearchResults = mock(NamingEnumeration.class);
        SearchResult mockSearchResult = mock(SearchResult.class);
        Attributes mockAttrs = mock(Attributes.class);

        NamingEnumeration<String> mockAttrsKeys = mock(NamingEnumeration.class);
        Attribute mockAttributeValue = mock(Attribute.class);
        when(ctx.search(any(String.class), any(String.class), any(SearchControls.class)))
                        .thenReturn(mockSearchResults);
        when(mockSearchResults.hasMore()).thenReturn(true);
        when(mockSearchResults.next()).thenReturn(mockSearchResult);
        when(mockSearchResult.getAttributes()).thenReturn(mockAttrs);
        when(mockAttrs.getIDs()).thenReturn(mockAttrsKeys);
        when(mockAttrsKeys.hasMoreElements()).thenReturn(true, false);
        when(mockAttrsKeys.next()).thenReturn("Key");
        when(mockAttrs.get(any(String.class))).thenReturn(mockAttributeValue);

        String userNameRetrieved = lus.retrieve(props);
        User userRetrieved = lus.getUser(userNameRetrieved);

        assertEquals(userRetrieved.getUserInformation("Key"), mockAttributeValue.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateMethod() throws UserException {
        LdapUserService lus = new LdapUserService();
        lus.create(mockUser());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUpdateMethod() throws UserException {
        LdapUserService lus = new LdapUserService();
        User user = mockUser();
        lus.update(user.getUserName(), user);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeleteMethod() {
        LdapUserService lus = new LdapUserService();
        lus.delete(mockUser().getUserName());
    }

    @Test
    public void testRetrieveSuccess() throws UserException, NamingException {
        User user = mockUser();
        Properties props = new Properties();
        props.setProperty("userName", user.getUserName());

        LdapUserService lus = new LdapUserService();

        DirContext ctx = mock(InitialDirContext.class);
        lus.setContext(ctx);

        NamingEnumeration<SearchResult> mockSearchResults = mock(NamingEnumeration.class);
        SearchResult mockSearchResult = mock(SearchResult.class);
        Attributes mockAttrs = mock(Attributes.class);

        NamingEnumeration<String> mockAttrsKeys = mock(NamingEnumeration.class);
        Attribute mockAttributeValue = mock(Attribute.class);
        when(ctx.search(any(String.class), any(String.class), any(SearchControls.class)))
                        .thenReturn(mockSearchResults);
        when(mockSearchResults.hasMore()).thenReturn(true);
        when(mockSearchResults.next()).thenReturn(mockSearchResult);
        when(mockSearchResult.getAttributes()).thenReturn(mockAttrs);
        when(mockAttrs.getIDs()).thenReturn(mockAttrsKeys);
        when(mockAttrsKeys.hasMoreElements()).thenReturn(true, false);
        when(mockAttrsKeys.next()).thenReturn("Key");
        when(mockAttrs.get(any(String.class))).thenReturn(mockAttributeValue);

        String userNameRetrieved = lus.retrieve(props);
        User userRetrieved = lus.getUser(user.getUserName());

        assertEquals(userNameRetrieved, user.getUserName());

    }

    @Test(expected = UserException.class)
    public void testBadUsername() throws NamingException, UserException {
        String userName = "testUsername";
        Properties props = new Properties();
        props.setProperty("userName", userName);

        LdapUserService lus = new LdapUserService();

        DirContext ctx = mock(InitialDirContext.class);
        lus.setContext(ctx);

        NamingEnumeration<SearchResult> mockSearchResults = mock(NamingEnumeration.class);

        when(ctx.search(any(String.class), any(String.class), any(SearchControls.class)))
                        .thenReturn(mockSearchResults);

        lus.retrieve(props);
    }

    @Test
    public void testBadConfig() {
        LdapUserService lus = new LdapUserService();
        assertEquals(lus.getContext(), null);

    }

    @Test(expected = UserException.class)
    public void testBadConnection() throws UserException, NamingException {
        String userName = "testUsername";
        Properties props = new Properties();
        props.setProperty("userName", userName);

        LdapUserService lus = new LdapUserService();
        lus.retrieve(props);

    }

}

