package com.example.recipereviews.fragments.common;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import com.example.recipereviews.R;
import com.google.android.material.imageview.ShapeableImageView;

public abstract class CameraUtilsFragment extends Fragment {

    protected ShapeableImageView avatarImg;
    protected ActivityResultLauncher<Void> cameraLauncher;
    protected ActivityResultLauncher<String> galleryLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), (Bitmap result) -> {
            if (result != null) {
                avatarImg.setImageBitmap(result);
            }
        });
        this.galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), (Uri result) -> {
            if (result != null){
                avatarImg.setImageURI(result);
            }
        });
    }

    public void showCameraMenu(View view) {
        if (this.getContext() != null) {
            PopupMenu cameraPopupMenu = new PopupMenu(this.getContext(), view, Gravity.CENTER);
            MenuInflater inflater = cameraPopupMenu.getMenuInflater();
            inflater.inflate(R.menu.camera_menu, cameraPopupMenu.getMenu());
            cameraPopupMenu.setOnMenuItemClickListener(this::setOnMenuItemClickListener);
            cameraPopupMenu.show();
        }
    }

    private boolean setOnMenuItemClickListener(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.camera_menu_open_camera) {
            this.cameraLauncher.launch(null);
            return true;
        } else if (menuItem.getItemId() == R.id.camera_menu_open_gallery) {
            this.galleryLauncher.launch("image/*");
            return true;
        }

        return false;
    }
}