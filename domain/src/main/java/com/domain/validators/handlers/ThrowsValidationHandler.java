package com.domain.validators.handlers;

import com.domain.exceptions.DomainException;
import com.domain.validators.Error;
import com.domain.validators.ValidationHandlerDomain;

import java.util.List;

public class ThrowsValidationHandler implements ValidationHandlerDomain {

    @Override
    public ValidationHandlerDomain append(Error error) {
        throw DomainException.with(error);
    }

    @Override
    public List<Error> getErrorsEntity() {
        return List.of();
    }

    @Override
    public <T> T validate(Validation<T> validation) {
        try {
            return validation.validate();
        } catch (Exception cause) {
            throw DomainException.with(new Error(cause.getMessage()));
        }
    }


}
