package edu.jhuapl.dorset.users;

public class UserException extends Exception {

    private static final long serialVersionUID = 1L;

    public UserException(String errorMessage) {
        super(errorMessage);
    }

}
