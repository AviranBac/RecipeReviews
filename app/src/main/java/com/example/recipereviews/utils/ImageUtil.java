package com.example.recipereviews.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.recipereviews.ApplicationContext;
import com.google.android.gms.common.util.Strings;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class ImageUtil {

    public static void loadImage(ImageView imageView, String imageName) {
        if (!Strings.isEmptyOrWhitespace(imageName)) {
            Picasso.get().load(Uri.parse(imageName)).into(imageView);
        } else {
            imageView.setVisibility(View.GONE);
        }
    }

    public static void loadImage(ImageView imageView, String imageName, Integer defaultImage) {
        if (!Strings.isEmptyOrWhitespace(imageName)) {
            Picasso.get().load(Uri.parse(imageName)).into(imageView);
        } else {
            imageView.setImageResource(defaultImage);
        }
    }

    public static void loadImage(MenuItem menuItem, String imageName, Integer defaultImage) {
        if (!Strings.isEmptyOrWhitespace(imageName)) {
            Target imageTarget = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(ApplicationContext.getContext().getResources(), createCircleBitmap(bitmap));
                    menuItem.setIcon(bitmapDrawable);
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            };

            Picasso.get().load(Uri.parse(imageName)).into(imageTarget);
        } else {
            menuItem.setIcon(defaultImage);
        }
    }

    private static Bitmap createCircleBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        canvas.drawARGB(0, 0, 0, 0);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(0xff424242);

        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}
