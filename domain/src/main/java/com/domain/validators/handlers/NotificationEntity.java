package com.domain.validators.handlers;


import com.domain.exceptions.DomainException;
import com.domain.validators.Error;
import com.domain.validators.ValidationHandlerDomain;

import java.util.ArrayList;
import java.util.List;

public class NotificationEntity implements ValidationHandlerDomain {

    private final List<Error> errors;

    private NotificationEntity(final List<Error> errors) {
        this.errors = errors;
    }

    public static NotificationEntity create() {
        return new NotificationEntity(new ArrayList<>());
    }

    public static NotificationEntity create(final Throwable t) {
        return create(new Error(t.getMessage()));
    }

    public static NotificationEntity create(final Error error) {
        return new NotificationEntity(new ArrayList<>()).append(error);
    }

    @Override
    public NotificationEntity append(Error error) {
        this.errors.add(error);
        return this;
    }

    @Override
    public List<Error> getErrorsEntity() {
        return this.errors;
    }

    @Override
    public <T> T validate(Validation<T> validation) {
        try {
            return validation.validate();
        } catch (final DomainException cause) {
            cause.printStackTrace();

        } catch (final Throwable cause) {
            cause.printStackTrace();
        }
        return null;
    }
}
