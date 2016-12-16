package edu.jhuapl.dorset.fileuserservice;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.jhuapl.dorset.users.User;
import edu.jhuapl.dorset.users.UserService;

public class CsvFileUserServiceTest {

    @Test
    public void testLoadUser() {
        User user = null;
        UserService userService = new CsvFileUserService("MockUserCsv.csv");
        user = userService.load("jdoe", null);
        assertEquals(user.getFirstName(), "John");
    }

    @Test
    public void testNoUser() {
        User user = null;
        UserService userService = new CsvFileUserService("MockUserCsv.csv");
        user = userService.load("not a userName", null);
        assertEquals(user, null);
    }

    @Test
    public void testGetCurrentUser() {
        User user = null;
        UserService userService = new CsvFileUserService("MockUserCsv.csv");
        user = userService.load("jdoe", null);
        assertEquals(userService.getCurrentUser().getUserName(), user.getUserName());
    }

    @Test
    public void testBadUserCsvFile() {
        UserService userService = new CsvFileUserService("badCsvFile.csv");
        User user = userService.load("jdoe", null);
        assertEquals(user, null);
    }

    @Test
    public void testGetUserName() {
        User user = null;
        UserService userService = new CsvFileUserService("MockUserCsv.csv");
        user = userService.load("jdoe", null);
        assertEquals(user.getFirstName() + " " + user.getLastName(), "John Doe");
    }

    @Test
    public void testGetUserLocation() {
        User user = null;
        UserService userService = new CsvFileUserService("MockUserCsv.csv");
        user = userService.load("doe_jane", null);
        assertEquals(user.getLocation(), "NYC");
    }

    @Test
    public void testGetUserAge() {
        User user = null;
        UserService userService = new CsvFileUserService("MockUserCsv.csv");
        user = userService.load("doe_jane", null);
        assertEquals(user.getAge(), "24");
    }
}
