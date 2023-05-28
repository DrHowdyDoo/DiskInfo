package com.drhowdydoo.diskinfo.util;

import android.content.Context;
import android.util.TypedValue;

import androidx.annotation.AttrRes;

public class Util {


    public static int getUsedSpace(long total, long used) {

        double totalIndouble = (double) total;
        double usedIndouble = (double) used;

        return total == 0 ? 0 : (int) ((usedIndouble / totalIndouble) * 100);
    }


    public static int dpToPx(Context context, int dp) {
        float density = context.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }

    public static int getColorAttr(Context context, @AttrRes int resId) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(resId, typedValue, true);
        return typedValue.data;
    }

}
