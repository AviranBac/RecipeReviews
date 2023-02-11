package com.example.recipereviews.utils;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BulletSpan;

import java.util.List;

public class BulletListUtils {

    public static SpannableStringBuilder buildBulletList(int bulletGap, List<String> lines) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

        lines.forEach(line -> {
            SpannableString spannableString = new SpannableString(line);
            spannableString.setSpan(new BulletSpan(bulletGap), 0, line.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableStringBuilder.append(spannableString).append("\n");
        });

        return spannableStringBuilder;
    }
}
