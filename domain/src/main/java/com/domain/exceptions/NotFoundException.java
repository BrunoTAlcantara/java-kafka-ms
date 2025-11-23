package com.domain.exceptions;

import com.domain.Aggregation;
import com.domain.UniqueIdentifier;
import com.domain.validators.Error;

import java.util.Collections;
import java.util.List;

public class NotFoundException extends DomainException {

    protected NotFoundException(final String message, final List<Error> errors) {
        super(message, errors);
    }

    public static NotFoundException with(
            final Class<? extends Aggregation<?>> aggregate, final UniqueIdentifier id) {
        final var error = "%s The %s id wat not found!".formatted(aggregate.getSimpleName(), id.valueId());
        return new NotFoundException(error, Collections.emptyList());
    }

}
