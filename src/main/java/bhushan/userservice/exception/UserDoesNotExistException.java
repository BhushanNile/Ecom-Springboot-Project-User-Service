package bhushan.userservice.exception;

public class UserDoesNotExistException extends RuntimeException{
    public UserDoesNotExistException(String massage) {
        super(massage);
    }
}
