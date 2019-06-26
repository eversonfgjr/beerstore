package com.hibicode.bearstore.service;

import org.springframework.http.HttpStatus;

public class BeerNotFoundException extends BusinessException {

    public BeerNotFoundException() {
        super("beers-6", HttpStatus.NOT_FOUND);
    }
}
