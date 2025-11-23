package com.domain.validators;


public abstract class Validator {

    private final ValidationHandlerDomain handler;


    protected Validator(ValidationHandlerDomain handler) {
        this.handler = handler;
    }

    public abstract void validate();

    protected ValidationHandlerDomain validatorHandler() {
        return this.handler;
    }
}
