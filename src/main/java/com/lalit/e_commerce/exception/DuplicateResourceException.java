package com.lalit.e_commerce.exception;

public class DuplicateResourceException extends RuntimeException{

    public DuplicateResourceException(String msg){
        super(msg);
    }
}
