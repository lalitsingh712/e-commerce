package com.lalit.e_commerce.exception;

public class InsufficientStockException
                                        extends  RuntimeException{

    public InsufficientStockException(String message) {
        super(message);
    }
}
