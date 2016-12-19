package edu.jhuapl.dorset.users;

import java.rmi.AlreadyBoundException;

@SuppressWarnings("serial")
public class UserException extends AlreadyBoundException {

    public UserException(String userId) {
        super("User ID (" + userId + ") already exists.");
    }

}
