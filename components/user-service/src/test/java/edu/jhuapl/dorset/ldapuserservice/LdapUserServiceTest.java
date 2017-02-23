package edu.jhuapl.dorset.ldapuserservice;

import java.util.Properties;

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
    public void testGetUserSuccess() {

    }

    @Test
    public void testRetrieveSuccess() throws UserException {

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


    @Test(expected = UserException.class)
    public void testBadConnection() throws UserException {
        String userName = "testUsername";
        Properties props = new Properties();
        props.setProperty("userName", userName);

        LdapUserService lus = new LdapUserService();
        lus.retrieve(props);
    }

    public void testBadSearchControls() {

    }


    @Test
    public void testBadUsername() {

    }

    @Test
    public void testBadConfig() {

    }

}

