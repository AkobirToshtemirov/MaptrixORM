package com.maptrix.orm.exceptions;

public class TransactionException extends RuntimeException{
    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }
}
