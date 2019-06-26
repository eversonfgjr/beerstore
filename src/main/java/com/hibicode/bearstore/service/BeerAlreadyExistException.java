package com.hibicode.bearstore.service;

import org.springframework.http.HttpStatus;

public class BeerAlreadyExistException extends BusinessException {

    public BeerAlreadyExistException() {
        super("beers-5", HttpStatus.BAD_REQUEST);
    }
}
