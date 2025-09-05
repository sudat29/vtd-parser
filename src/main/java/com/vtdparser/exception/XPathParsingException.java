package com.vtdparser.exception;

public class XPathParsingException extends VTDParserException {
    private static final long serialVersionUID = 1L;

    public XPathParsingException(String message) {
        super(message);
    }

    public XPathParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public XPathParsingException(Throwable cause) {
        super(cause);
    }
}