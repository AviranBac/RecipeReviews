package com.example.recipereviews.utils;

import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageUtil {
    public static void loadImage(ImageView imageView, String imageName, Integer defaultImage) {
        if (!imageName.equals("")) {
            Picasso.get().load(Uri.parse(imageName)).into(imageView);
        } else {
            imageView.setImageResource(defaultImage);
        }
    }
}
