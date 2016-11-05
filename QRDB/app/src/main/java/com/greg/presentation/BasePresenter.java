package com.greg.presentation;

/**
 * Created by Greg on 05-11-2016.
 */
public interface BasePresenter<T> {
    void setView(T view);
    void resume();
    void pause();
    void destroy();
}