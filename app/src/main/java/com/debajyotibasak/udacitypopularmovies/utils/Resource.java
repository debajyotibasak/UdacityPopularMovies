package com.debajyotibasak.udacitypopularmovies.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.debajyotibasak.udacitypopularmovies.utils.Status.ERROR;
import static com.debajyotibasak.udacitypopularmovies.utils.Status.LOADING;
import static com.debajyotibasak.udacitypopularmovies.utils.Status.SUCCESS;

public class Resource<T> {
    @NonNull
    private final Status status;

    @Nullable
    private final String message;

    @Nullable
    private final T response;

    private Resource(@NonNull Status status, @Nullable T response, @Nullable String msg) {
        this.status = status;
        this.response = response;
        this.message = msg;
    }

    public static <T> Resource<T> success(@Nullable T data) {
        return new Resource<>(SUCCESS, data, null);
    }

    public static <T> Resource<T> error(String msg, @Nullable T data) {
        return new Resource<>(ERROR, data, msg);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(LOADING, data, null);
    }

    @NonNull
    public Status getStatus() {
        return status;
    }

    @Nullable
    public String getMessage() {
        return message;
    }

    @Nullable
    public T getResponse() {
        return response;
    }
}