package com.domain.exceptions;

import com.domain.validators.Error;

import java.util.List;

public class DomainException extends NoStackTraceException {

    protected List<Error> errors;

    protected DomainException(final String message, final List<Error> errors) {
        super(message);
        this.errors = errors;
    }

    public static DomainException with(final Error errors) {
        return new DomainException(errors.msg(), List.of(errors));
    }

    public static DomainException with(final List<Error> errors) {
        return new DomainException("", errors);
    }

    public List<Error> getErrors() {
        return errors;
    }

}
