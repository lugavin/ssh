package com.ssh.common.validation;

import org.springframework.stereotype.Service;

@Service
public class BusinessServiceImpl implements BusinessService {

    @Override
    public String convertToUpperCase(String input) {
        return input.toUpperCase();
    }

}
