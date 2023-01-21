package com.example.recipereviews.validators;

import android.text.Editable;
import android.util.Patterns;

public class InputValidator {

    public static boolean isFirstNameValid(Editable text) {
        return !isFieldEmptyOrNull(text);
    }

    public static boolean isLastNameValid(Editable text) {
        return !isFieldEmptyOrNull(text);
    }

    public static boolean isEmailValid(Editable email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isPasswordValid(Editable password) {
        return password != null && password.length() >= 6;
    }

    private static boolean isFieldEmptyOrNull(Editable text) {
        return text == null || text.length() == 0;
    }
}
