package io.github.patternatlas.api.exception;

public class NullRoleException extends RuntimeException {

    public NullRoleException() {
        super("Role is null");
    }

    public NullRoleException(String message) {
        super(message);
    }
}
