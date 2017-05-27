package com.ssh.common.validation;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public interface BusinessService {

    @NotNull(message = "Null returns are not permitted")
    String convertToUpperCase(@NotEmpty(message = "Input must not be null or empty.") String input);

}
