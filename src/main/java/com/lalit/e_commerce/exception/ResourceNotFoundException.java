package com.lalit.e_commerce.exception;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String msg){
        super(msg);
    }
}
