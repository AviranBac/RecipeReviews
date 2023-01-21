package com.example.recipereviews.fragments.guest;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.recipereviews.R;
import com.example.recipereviews.databinding.FragmentRegisterBinding;
import com.example.recipereviews.models.Model;
import com.example.recipereviews.models.entities.User;
import com.example.recipereviews.validators.InputValidator;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private ShapeableImageView profileImageView;
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
    private ActivityResultLauncher<Void> cameraLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.registerForCameraLaunchEvent();
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
        profileImageView = binding.imageIcon;
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
        this.setOnKeyListener(firstNameEditText, this::validateFirstName);
        this.setOnKeyListener(lastNameEditText, this::validateLastName);
        this.setOnKeyListener(emailEditText, this::validateEmail);
        this.setOnKeyListener(passwordEditText, this::validatePassword);
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
        Model.getInstance().doesEmailExist(emailEditText.getText().toString(),
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

        Drawable profileImage = profileImageView.getDrawable();
        if (profileImage == null) {
            Model.getInstance().register(user, passwordEditText.getText().toString(), () -> this.navigateToMainPageAfterRegister(view));
        } else {
            binding.imageIcon.setDrawingCacheEnabled(true);
            binding.imageIcon.buildDrawingCache();
            Model.getInstance().uploadUserImage(((BitmapDrawable) profileImage).getBitmap(), user.getEmail(), (String url) -> {
                user.setImageUrl(url);
                Model.getInstance().register(user, passwordEditText.getText().toString(), () -> this.navigateToMainPageAfterRegister(view));
            });
        }
    }

    private void navigateToMainPageAfterRegister(View view) {
        Navigation.findNavController(view).navigate(RegisterFragmentDirections.actionRegisterFragmentToUserNavGraph());
    }

    private boolean isFormValid() {
        return this.validateFirstName() &&
                this.validateLastName() &&
                this.validateEmail() &&
                this.validatePassword();
    }

    private void setProfileImageViewOnClickListener() {
        binding.imageIcon.setOnClickListener((View view1) -> cameraLauncher.launch(null));
    }

    private void setOnKeyListener(TextInputEditText editText, Runnable validator) {
        editText.setOnKeyListener((view, i, keyEvent) -> {
            validator.run();
            return false;
        });
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

    private void registerForCameraLaunchEvent() {
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), (Bitmap result) -> {
            if (result != null) {
                binding.imageIcon.setImageBitmap(result);
            }
        });
    }
}