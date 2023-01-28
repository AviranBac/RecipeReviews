package com.example.recipereviews.utils;

import android.net.Uri;
import android.widget.ImageView;

import com.google.android.gms.common.util.Strings;
import com.squareup.picasso.Picasso;

public class ImageUtil {
    public static void loadImage(ImageView imageView, String imageName, Integer defaultImage) {
        if (!Strings.isEmptyOrWhitespace(imageName)) {
            Picasso.get().load(Uri.parse(imageName)).into(imageView);
        } else {
            imageView.setImageResource(defaultImage);
        }
    }
}
