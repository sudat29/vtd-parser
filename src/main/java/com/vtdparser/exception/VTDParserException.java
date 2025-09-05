package com.vtdparser.exception;

public class VTDParserException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public VTDParserException(String message) {
        super(message);
    }

    public VTDParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public VTDParserException(Throwable cause) {
        super(cause);
    }
}