package com.example.recipereviews.utils;

import androidx.annotation.NonNull;

public class FirebaseUtils {

    public static double parseDouble(@NonNull Object data) {
        return data.getClass().equals(Double.class) ?
                ((Double) data) :
                ((Long) data).doubleValue();
    }
}
