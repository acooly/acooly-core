package com.acooly.module.pdf.exception;

/**
 * @author shuijing
 */
public class DocumentGeneratingException extends Exception {

    private static final long serialVersionUID = 1L;

    public DocumentGeneratingException() {
        super();
    }

    public DocumentGeneratingException(String message) {
        super(message);
    }

    public DocumentGeneratingException(String message, Throwable cause) {
        super(message, cause);
    }

    public DocumentGeneratingException(Throwable cause) {
        super(cause);
    }

}
