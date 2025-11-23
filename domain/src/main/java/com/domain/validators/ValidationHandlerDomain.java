package com.domain.validators;

import java.util.List;

public interface ValidationHandlerDomain {

    ValidationHandlerDomain append(Error error);

    List<Error> getErrorsEntity();

    default boolean hasError() {
        return getErrorsEntity() != null && !getErrorsEntity().isEmpty();
    }

    <T> T validate(Validation<T> validation);

    interface Validation<T> {
        T validate();
    }

}
