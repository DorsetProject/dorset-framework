package edu.jhuapl.dorset.fileuserservice;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import edu.jhuapl.dorset.users.User;
import edu.jhuapl.dorset.users.UserService;

public class FileUserServiceTest {

    protected String fileBaseName;

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
    
    public FileUserServiceTest() {
        this.fileBaseName = "MockUserFile";
    }
    
    private String getTempFilePath() {
        File tmpFolder = testFolder.getRoot();
        return tmpFolder.getAbsolutePath();
    }
    
    private String createTempUserFile() throws FileNotFoundException, IOException{
        String userName = "Mock-UserName";
        String filePath = getTempFilePath();
        File file = new File(filePath, fileBaseName + "-" + userName + ".properties");
        OutputStream output = new FileOutputStream(file);
        
        Properties prop = new Properties();        
        prop.setProperty("Dorset-firstName", "Mock-FirstName");
        prop.setProperty("Dorset-lastName", "Mock-LastName");
        prop.setProperty("Dorset-userName", userName);
        prop.store(output, null);
       
        return userName;
    }

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
    public void testCreateUser() {
        String tempFilePath = getTempFilePath();
        User new_user = mockUser();
        
        UserService user_service = new FileUserService(tempFilePath, fileBaseName);

        String userName = user_service.create(new_user);

        assertEquals(user_service.getUser(userName).getUserName(), new_user.getUserName());
        assertEquals(userName, new_user.getUserName());
    }

    @Test
    public void testCreateUserFailure() {
        String tempFilePath = getTempFilePath();
        User new_user = mockUser();
        
        UserService user_service = new FileUserService(tempFilePath, fileBaseName);

        String userName = user_service.create(new_user);
        String userNameDuplicate = user_service.create(new_user);

        assertEquals(userName, "mock_UserName");
        assertEquals(userNameDuplicate, null);
    }

    @Test
    public void testRetrieveUser() throws FileNotFoundException, IOException {
        String userName = createTempUserFile();
        String tempFilePath = getTempFilePath();
        UserService user_service = new FileUserService(tempFilePath, fileBaseName);
        
        Properties properties = new Properties();
        properties.setProperty("userName", userName);
        
        String userNameRetrieved = user_service.retrieve(properties);
        User user = user_service.getUser(userNameRetrieved);
        
        assertEquals(userName, user.getUserName());
    }

    @Test
    public void testRetrieveUserFailure() {
        String userName = "bad_userName";
        String tempFilePath = getTempFilePath();

        Properties properties = new Properties();
        properties.setProperty("userName", userName);

        UserService user_service = new FileUserService(tempFilePath, fileBaseName);
        
        String userNameRetrieved = user_service.retrieve(properties);

        assertEquals(userNameRetrieved, null);
        assertEquals(user_service.getUser(userName), null);
    }

    @Test
    public void testUpdateUser() throws FileNotFoundException, IOException {
        String userName = createTempUserFile();
        String tempFilePath = getTempFilePath();
        UserService user_service = new FileUserService(tempFilePath, fileBaseName);
        
        Properties properties = new Properties();
        properties.setProperty("userName", userName);

        String userNameRetreived = user_service.retrieve(properties);

        assertEquals(user_service.getUser(userName).getUserName(), userName);

        User userRetrieved = user_service.getUser(userNameRetreived);

        userRetrieved.setFirstName("Updated First Name");
        userRetrieved.setLastName("Updated Last Name");
        String emailNotUpdated = userRetrieved.getEmail();

        user_service.update(userNameRetreived, userRetrieved);

        assertEquals(user_service.getUser(userName).getFirstName(), "Updated First Name");
        assertEquals(user_service.getUser(userName).getLastName(), "Updated Last Name");
        assertEquals(user_service.getUser(userName).getEmail(), emailNotUpdated);
    }


    @Test
    public void testDeleteUser() throws FileNotFoundException, IOException {
        String userName = createTempUserFile();
        String tempFilePath = getTempFilePath();
        UserService user_service = new FileUserService(tempFilePath, fileBaseName);
       
        Properties properties = new Properties();
        properties.setProperty("userName", userName);

        user_service.retrieve(properties);

        assertEquals(user_service.getUser(userName).getUserName(), userName);

        user_service.delete(userName);
        assertEquals(user_service.getUser(userName), null);
    }

}
