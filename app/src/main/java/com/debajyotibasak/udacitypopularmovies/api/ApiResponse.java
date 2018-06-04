package com.debajyotibasak.udacitypopularmovies.api;

public class ApiResponse<T> {
    private T response;
    private Throwable t;

    public ApiResponse(T response) {
        this.response = response;
        this.t = null;
    }

    public ApiResponse(Throwable error) {
        this.t = error;
        this.response = null;
    }

    public T getResponse() {
        return response;
    }

    public Throwable getT() {
        return t;
    }
}