package bhushan.userservice.exception;

public class PasswordDoesNotMatchException extends RuntimeException{
    public PasswordDoesNotMatchException(String massage) {
        super(massage);
    }
}
