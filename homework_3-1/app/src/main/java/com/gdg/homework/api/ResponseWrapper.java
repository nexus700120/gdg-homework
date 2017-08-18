package com.gdg.homework.api;

/**
 * Created by vkirillov on 18.08.2017.
 */
class ResponseWrapper<T> {
    private Throwable mThrowable;
    private T mResponse;

    public ResponseWrapper(T response) {
        this.mResponse = response;
    }

    public ResponseWrapper(Throwable t) {
        this.mThrowable = t;
    }

    public boolean isSuccess() {
        return mThrowable == null;
    }

    public Throwable getThrowable() {
        return mThrowable;
    }

    public T getResponse() {
        return mResponse;
    }
}