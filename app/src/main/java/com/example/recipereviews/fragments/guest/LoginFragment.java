package com.example.recipereviews.fragments.guest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.recipereviews.R;
import com.example.recipereviews.databinding.FragmentLoginBinding;
import com.example.recipereviews.models.Model;
import com.example.recipereviews.validators.InputValidator;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginFragment extends Fragment {

    private TextInputLayout emailTextInput;
    private TextInputEditText emailEditText;
    private TextInputLayout passwordTextInput;
    private TextInputEditText passwordEditText;
    private Button loginButton;
    private Button registerButton;
    private CircularProgressIndicator progressIndicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentLoginBinding binding = FragmentLoginBinding.inflate(inflater, container, false);
        this.initializeMembers(binding);
        
        View view = binding.getRoot();
        this.setListeners(view);

        return view;
    }

    private void initializeMembers(FragmentLoginBinding binding) {
        emailTextInput = binding.emailTextInput;
        emailEditText = binding.emailEt;
        passwordTextInput = binding.passwordTextInput;
        passwordEditText = binding.passwordEt;
        loginButton = binding.loginBtn;
        registerButton = binding.registerBtn;
        progressIndicator = binding.progressIndicator;
    }

    private void setListeners(View view) {
        this.setEmailEditTextOnKeyListener();
        this.setPasswordEditTextOnKeyListener();
        this.setLoginButtonOnClickListener(view);
        this.setRegisterButtonOnClickListener(view);
    }

    private void setLoginButtonOnClickListener(View view) {
        loginButton.setOnClickListener((View view1) -> {
            if (isFormValid()) {
                loginButton.setEnabled(false);
                registerButton.setEnabled(false);

                String loginText = loginButton.getText().toString();
                loginButton.setText("");
                progressIndicator.show();

                Model.getInstance().login(
                        emailEditText.getText().toString(),
                        passwordEditText.getText().toString(),
                        () -> Navigation.findNavController(view).navigate(LoginFragmentDirections.actionLoginFragmentToUserNavGraph()),
                        errorMessage -> {
                            Snackbar.make(view, errorMessage, Snackbar.LENGTH_SHORT).show();
                            loginButton.setEnabled(true);
                            registerButton.setEnabled(true);
                            loginButton.setText(loginText);
                            progressIndicator.hide();
                        });
            }
        });
    }

    private boolean isFormValid() {
        return this.validateEmail() && this.validatePassword();
    }

    private void setRegisterButtonOnClickListener(View view) {
        registerButton.setOnClickListener((View view1) ->
                Navigation.findNavController(view).navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        );
    }

    private void setEmailEditTextOnKeyListener() {
        emailEditText.setOnKeyListener((view, i, keyEvent) -> {
            validateEmail();
            return false;
        });
    }

    private void setPasswordEditTextOnKeyListener() {
        passwordEditText.setOnKeyListener((view, i, keyEvent) -> {
            validatePassword();
            return false;
        });
    }

    private boolean validateEmail() {
        if (InputValidator.isEmailValid(emailEditText.getText())) {
            emailTextInput.setError(null);
            return true;
        } else {
            emailTextInput.setError(getString(R.string.invalid_email));
            return false;
        }
    }

    private boolean validatePassword() {
        if (InputValidator.isPasswordValid(passwordEditText.getText())) {
            passwordTextInput.setError(null);
            return true;
        } else {
            passwordTextInput.setError(getString(R.string.invalid_password));
            return false;
        }
    }
}