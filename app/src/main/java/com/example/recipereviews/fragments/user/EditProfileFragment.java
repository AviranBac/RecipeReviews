package com.example.recipereviews.fragments.user;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import com.example.recipereviews.R;
import com.example.recipereviews.databinding.FragmentEditProfileBinding;
import com.example.recipereviews.fragments.common.CameraUtilsFragment;
import com.example.recipereviews.models.entities.User;
import com.example.recipereviews.models.models.UserModel;
import com.example.recipereviews.utils.ImageUtil;
import com.example.recipereviews.utils.NavigationUtils;
import com.example.recipereviews.validators.InputValidator;
import com.example.recipereviews.viewModels.EditProfileFragmentViewModel;
import com.example.recipereviews.viewModels.factory.EditProfileFragmentViewModelFactory;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.function.BooleanSupplier;

public class EditProfileFragment extends CameraUtilsFragment {

    private String userId;
    private FragmentEditProfileBinding binding;
    private EditProfileFragmentViewModel viewModel;
    private User currentUser;
    private TextView emailTextView;
    private TextInputLayout firstNameTextInput;
    private TextInputLayout lastNameTextInput;
    private TextInputLayout oldPasswordTextInput;
    private TextInputLayout newPasswordTextInput;
    private TextInputEditText firstNameEditText;
    private TextInputEditText lastNameEditText;
    private TextInputEditText oldPasswordEditText;
    private TextInputEditText newPasswordEditText;
    private Button saveButton;
    private CircularProgressIndicator progressIndicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentEditProfileBinding.inflate(inflater, container, false);
        View view = this.binding.getRoot();
        this.initializeMembers();
        this.observeUser();
        this.setListeners();

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.userId = EditProfileFragmentArgs.fromBundle(getArguments()).getUserId();
        this.viewModel = new ViewModelProvider(this, new EditProfileFragmentViewModelFactory(this.userId)).get(EditProfileFragmentViewModel.class);
    }

    private void initializeMembers() {
        super.avatarImg = binding.imageIcon;
        this.emailTextView = this.binding.emailTv;
        this.firstNameTextInput = this.binding.firstNameTextInput;
        this.lastNameTextInput = this.binding.lastNameTextInput;
        this.oldPasswordTextInput = this.binding.oldPasswordTextInput;
        this.newPasswordTextInput = this.binding.newPasswordTextInput;
        this.firstNameEditText = this.binding.firstNameEt;
        this.lastNameEditText = this.binding.lastNameEt;
        this.oldPasswordEditText = this.binding.oldPasswordEt;
        this.newPasswordEditText = this.binding.newPasswordEt;
        this.saveButton = this.binding.saveBtn;
        this.progressIndicator = this.binding.progressIndicator;
    }

    private void setListeners() {
        this.setImageIconOnClickListener();
        this.setOnKeyListener(firstNameEditText, this::validateFirstName);
        this.setOnKeyListener(lastNameEditText, this::validateLastName);
        this.setOnKeyListener(oldPasswordEditText, this::validatePassword);
        this.setOnKeyListener(newPasswordEditText, this::validatePassword);
        this.setSaveButtonOnClickListener();
    }

    private void setImageIconOnClickListener() {
        this.binding.imageIcon.setOnClickListener(super::showCameraMenu);
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

    private boolean validatePassword() {
        boolean isOldPasswordValid = InputValidator.isPasswordValid(oldPasswordEditText.getText(), true);
        boolean isNewPasswordValid = InputValidator.isPasswordValid(newPasswordEditText.getText(), true);
        BooleanSupplier hasFilledExactlyOnePassword = () ->
                (oldPasswordEditText.getText().length() > 0 && newPasswordEditText.getText().length() == 0) ||
                (oldPasswordEditText.getText().length() == 0 && newPasswordEditText.getText().length() > 0);

        if (isOldPasswordValid && isNewPasswordValid) {
            if (!hasFilledExactlyOnePassword.getAsBoolean()) {
                oldPasswordTextInput.setError(null);
                newPasswordTextInput.setError(null);
                return true;
            }

            String oldPasswordError = oldPasswordEditText.getText().length() > 0 ? null : getString(R.string.update_invalid_password);
            String newPasswordError = newPasswordEditText.getText().length() > 0 ? null : getString(R.string.update_invalid_password);
            oldPasswordTextInput.setError(oldPasswordError);
            newPasswordTextInput.setError(newPasswordError);
        } else {
            String oldPasswordError = isOldPasswordValid ? null : getString(R.string.update_invalid_password);
            String newPasswordError = isNewPasswordValid ? null : getString(R.string.update_invalid_password);
            oldPasswordTextInput.setError(oldPasswordError);
            newPasswordTextInput.setError(newPasswordError);
        }

        return false;
    }

    private void setSaveButtonOnClickListener() {
        this.saveButton.setOnClickListener((View view) -> {
            if (this.isFormValid()) {
                this.saveUser(view);
            }
        });
    }

    private boolean isFormValid() {
        return this.validateFirstName() && this.validateLastName() && this.validatePassword();
    }

    private void saveUser(View view) {
        String saveButtonText = saveButton.getText().toString();
        saveButton.setEnabled(false);
        saveButton.setText("");
        progressIndicator.show();

        User updatedUser = this.currentUser;
        updatedUser.setFirstName(firstNameEditText.getText().toString());
        updatedUser.setLastName(lastNameEditText.getText().toString());
        Drawable profileImage = super.avatarImg.getDrawable();

        if (profileImage == null) {
            UserModel.getInstance().updateUser(
                    updatedUser,
                    oldPasswordEditText.getText().toString(),
                    newPasswordEditText.getText().toString(),
                    this::navigateToProfile,
                    errorMessage -> this.handleUpdateError(view, errorMessage, saveButtonText)
            );
        } else {
            this.avatarImg.setDrawingCacheEnabled(true);
            this.avatarImg.buildDrawingCache();
            UserModel.getInstance().uploadUserImage(((BitmapDrawable) profileImage).getBitmap(), updatedUser.getEmail(), (String url) -> {
                updatedUser.setImageUrl(url);
                UserModel.getInstance().updateUser(
                        updatedUser,
                        oldPasswordEditText.getText().toString(),
                        newPasswordEditText.getText().toString(),
                        this::navigateToProfile,
                        errorMessage -> this.handleUpdateError(view, errorMessage, saveButtonText)
                );
            });
        }
    }

    private void observeUser() {
        this.viewModel.getProfileUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                this.currentUser = user;
                this.setFormData();
            }
        });
    }

    private void setFormData() {
        ImageUtil.loadImage(this.avatarImg, this.currentUser.getImageUrl(), R.drawable.blank_profile_picture);
        this.emailTextView.setText(this.currentUser.getEmail());
        this.emailTextView.setPaintFlags(this.emailTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        this.firstNameEditText.setText(this.currentUser.getFirstName());
        this.lastNameEditText.setText(this.currentUser.getLastName());
    }

    private void handleUpdateError(View view, String errorMessage, String saveButtonText) {
        Snackbar.make(view, errorMessage, Snackbar.LENGTH_SHORT).show();
        saveButton.setEnabled(true);
        saveButton.setText(saveButtonText);
        progressIndicator.hide();
    }

    private void navigateToProfile() {
        NavigationUtils.navigate(requireActivity(), NavController::navigateUp);
    }
}