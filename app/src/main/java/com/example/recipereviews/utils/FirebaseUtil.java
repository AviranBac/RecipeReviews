package com.example.recipereviews.utils;

import androidx.annotation.NonNull;

public class FirebaseUtil {
    public static double parseDouble(@NonNull Object data) {
        double parsed;
        if (data.getClass().equals(Double.class)) {
            parsed = ((Double) data);
        } else {
            parsed = ((Long) data).doubleValue();
        }
        return parsed;
    }
}
