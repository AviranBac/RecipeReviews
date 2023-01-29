package com.example.recipereviews.fragments.guest;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.example.recipereviews.R;
import com.example.recipereviews.activities.UserActivity;
import com.example.recipereviews.databinding.FragmentRegisterBinding;
import com.example.recipereviews.fragments.common.CameraUtilsFragment;
import com.example.recipereviews.models.entities.User;
import com.example.recipereviews.models.models.UserModel;
import com.example.recipereviews.utils.ValidatorListenerUtils;
import com.example.recipereviews.validators.InputValidator;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterFragment extends CameraUtilsFragment {

    private FragmentRegisterBinding binding;
    private TextInputLayout firstNameTextInput;
    private TextInputEditText firstNameEditText;
    private TextInputLayout lastNameTextInput;
    private TextInputEditText lastNameEditText;
    private TextInputLayout emailTextInput;
    private TextInputEditText emailEditText;
    private TextInputLayout passwordTextInput;
    private TextInputEditText passwordEditText;
    private Button registerButton;
    private CircularProgressIndicator progressIndicator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        this.initializeMembers();

        View view = binding.getRoot();
        this.setListeners(view);

        return view;
    }

    private void initializeMembers() {
        super.avatarImg = binding.imageIcon;
        firstNameTextInput = binding.firstNameTextInput;
        firstNameEditText = binding.firstNameEt;
        lastNameTextInput = binding.lastNameTextInput;
        lastNameEditText = binding.lastNameEt;
        emailTextInput = binding.emailTextInput;
        emailEditText = binding.emailEt;
        passwordTextInput = binding.passwordTextInput;
        passwordEditText = binding.passwordEt;
        registerButton = binding.registerBtn;
        progressIndicator = binding.progressIndicator;
    }

    private void setListeners(View view) {
        setProfileImageViewOnClickListener();
        ValidatorListenerUtils.setOnKeyListener(firstNameEditText, this::validateFirstName);
        ValidatorListenerUtils.setOnKeyListener(lastNameEditText, this::validateLastName);
        ValidatorListenerUtils.setOnKeyListener(emailEditText, this::validateEmail);
        ValidatorListenerUtils.setOnKeyListener(passwordEditText, this::validatePassword);
        setRegisterButtonOnClickListener(view);
    }

    private void setRegisterButtonOnClickListener(View view) {
        registerButton.setOnClickListener((View view1) -> {
            if (isFormValid()) {
                registerIfValid(view);
            }
        });
    }

    private void registerIfValid(View view) {
        UserModel.getInstance().doesEmailExist(emailEditText.getText().toString(),
                (Boolean emailExist) -> {
                    if (emailExist) {
                        emailTextInput.setError(getString(R.string.email_exists));
                    } else {
                        this.register(view);
                    }
                },
                (errorMessage) -> Snackbar.make(view, errorMessage, Snackbar.LENGTH_SHORT).show());
    }

    private void register(View view) {
        registerButton.setEnabled(false);
        registerButton.setText("");
        progressIndicator.show();

        User user = new User(firstNameEditText.getText().toString(),
                             lastNameEditText.getText().toString(),
                             emailEditText.getText().toString());

        Drawable profileImage = super.avatarImg.getDrawable();
        if (profileImage == null) {
            UserModel.getInstance().register(user, passwordEditText.getText().toString(), this::startUserActivity);
        } else {
            binding.imageIcon.setDrawingCacheEnabled(true);
            binding.imageIcon.buildDrawingCache();
            UserModel.getInstance().uploadUserImage(((BitmapDrawable) profileImage).getBitmap(), user.getEmail(), (String url) -> {
                user.setImageUrl(url);
                UserModel.getInstance().register(user, passwordEditText.getText().toString(), this::startUserActivity);
            });
        }
    }

    private boolean isFormValid() {
        return this.validateFirstName() &&
                this.validateLastName() &&
                this.validateEmail() &&
                this.validatePassword();
    }

    private void setProfileImageViewOnClickListener() {
        binding.imageIcon.setOnClickListener(super::showCameraMenu);
    }

    private boolean validateFirstName() {
        if (InputValidator.isFirstNameValid(firstNameEditText.getText())) {
            firstNameTextInput.setError(null);
            return true;
        } else {
            firstNameTextInput.setError(getString(R.string.required_field));
            return false;
        }
    }

    private boolean validateLastName() {
        if (InputValidator.isLastNameValid(lastNameEditText.getText())) {
            lastNameTextInput.setError(null);
            return true;
        } else {
            lastNameTextInput.setError(getString(R.string.required_field));
            return false;
        }
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

    private void startUserActivity() {
        if (getActivity() != null) {
            Intent userActivityIntent = new Intent(getActivity(), UserActivity.class);
            startActivity(userActivityIntent);
            getActivity().finish();
        }
    }
}