package com.example.recipereviews.models;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {

    private static final Model instance = new Model();
    private final Executor executor = Executors.newSingleThreadExecutor();

    private Model() {}

    public static Model getInstance() {
        return instance;
    }

    public Executor getExecutor() {
        return this.executor;
    }

    public boolean isSignedIn() {
        return false;
    }
}