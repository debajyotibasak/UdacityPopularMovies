package com.debajyotibasak.udacitypopularmovies.api;

public class ApiResponse<T> {
    private T response;
    private Status status;
    private Throwable t;

    public ApiResponse(Status status, T response, Throwable t) {
        this.status = status;
        this.response = response;
        this.t = null;
    }

    public T getResponse() {
        return response;
    }

    public Throwable getT() {
        return t;
    }

    public Status getStatus() {
        return status;
    }
}