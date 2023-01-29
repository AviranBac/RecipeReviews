package com.example.recipereviews.utils;

import com.google.android.material.textfield.TextInputEditText;

public class ValidatorListenerUtils {
    public static void setOnKeyListener(TextInputEditText editText, Runnable validator) {
        editText.setOnKeyListener((view, i, keyEvent) -> {
            validator.run();
            return false;
        });
    }
}
