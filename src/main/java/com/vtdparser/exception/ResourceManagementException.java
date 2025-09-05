package com.vtdparser.exception;

public class ResourceManagementException extends VTDParserException {
    public ResourceManagementException(String message) {
        super(message);
    }

    public ResourceManagementException(String message, Throwable cause) {
        super(message, cause);
    }
}