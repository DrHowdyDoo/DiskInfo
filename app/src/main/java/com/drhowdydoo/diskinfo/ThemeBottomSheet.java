package com.drhowdydoo.diskinfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.color.DynamicColors;

public class ThemeBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {


    private MaterialCardView themePurple, themeRed, themeYellow, themeGreen, themeDynamic, themeOrange, themePink;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private MaterialButtonToggleGroup dynamicColors, appTheme;
    private MaterialButton t1, t2, x1, x2, x3;
    private TextView dynamicColorsTitle;

    private ImageView imgPurple, imgRed, imgYellow, imgGreen, imgOrange, imgPink;

    public ThemeBottomSheet() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_layout,
                container, false);


        themePurple = v.findViewById(R.id.theme_purple);
        themeRed = v.findViewById(R.id.theme_red);
        themeYellow = v.findViewById(R.id.theme_yellow);
        themeGreen = v.findViewById(R.id.theme_green);
        themeDynamic = v.findViewById(R.id.theme_dynamic);
        themeOrange = v.findViewById(R.id.theme_orange);
        themePink = v.findViewById(R.id.theme_pink);

        imgPurple = v.findViewById(R.id.img_purple);
        imgRed = v.findViewById(R.id.img_red);
        imgYellow = v.findViewById(R.id.img_yellow);
        imgGreen = v.findViewById(R.id.img_green);
        imgOrange = v.findViewById(R.id.img_orange);
        imgPink = v.findViewById(R.id.img_pink);

        Drawable purple_circle = AppCompatResources.getDrawable(requireActivity(), R.drawable.ic_circle_24);
        Drawable red_circle = AppCompatResources.getDrawable(requireActivity(), R.drawable.ic_circle_24);
        Drawable yellow_circle = AppCompatResources.getDrawable(requireActivity(), R.drawable.ic_circle_24);
        Drawable green_circle = AppCompatResources.getDrawable(requireActivity(), R.drawable.ic_circle_24);
        Drawable orange_circle = AppCompatResources.getDrawable(requireActivity(), R.drawable.ic_circle_24);
        Drawable pink_circle = AppCompatResources.getDrawable(requireActivity(), R.drawable.ic_circle_24);

        if (purple_circle != null) {
            purple_circle.setTint(Util.getColorAttr(new ContextThemeWrapper(requireActivity(), R.style.Theme_DiskInfo_Purple), R.attr.colorPrimary));
            imgPurple.setBackground(purple_circle);
        }

        if (red_circle != null) {
            red_circle.setTint(Util.getColorAttr(new ContextThemeWrapper(requireActivity(), R.style.Theme_DiskInfo_Red), R.attr.colorPrimary));
            imgRed.setBackground(red_circle);
        }

        if (yellow_circle != null) {
            yellow_circle.setTint(Util.getColorAttr(new ContextThemeWrapper(requireActivity(), R.style.Theme_DiskInfo_Yellow), R.attr.colorPrimary));
            imgYellow.setBackground(yellow_circle);
        }

        if (green_circle != null) {
            green_circle.setTint(Util.getColorAttr(new ContextThemeWrapper(requireActivity(), R.style.Theme_DiskInfo_Green), R.attr.colorPrimary));
            imgGreen.setBackground(green_circle);
        }

        if (orange_circle != null) {
            orange_circle.setTint(Util.getColorAttr(new ContextThemeWrapper(requireActivity(), R.style.Theme_DiskInfo_Orange), R.attr.colorPrimary));
            imgOrange.setBackground(orange_circle);
        }

        if (pink_circle != null) {
            pink_circle.setTint(Util.getColorAttr(new ContextThemeWrapper(requireActivity(), R.style.Theme_DiskInfo_Pink), R.attr.colorPrimary));
            imgPink.setBackground(pink_circle);
        }


        dynamicColors = v.findViewById(R.id.toggleButton);
        appTheme = v.findViewById(R.id.appTheme);
        t1 = v.findViewById(R.id.dynamic_on);
        t2 = v.findViewById(R.id.dynamic_off);
        dynamicColorsTitle = v.findViewById(R.id.txtView_dynamic_colors);

        sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();


        if (Build.VERSION.SDK_INT < 31) {
            dynamicColors.setVisibility(View.GONE);
            dynamicColorsTitle.setVisibility(View.GONE);
        } else {
            if (sharedPref.getBoolean("DiskInfo.DynamicColors", false)) {
                dynamicColors.check(R.id.dynamic_on);
                themeDynamic.setVisibility(View.VISIBLE);
                themeDynamic.setChecked(true);
            } else {
                dynamicColors.check(R.id.dynamic_off);
                themeDynamic.setChecked(false);
                themeDynamic.setVisibility(View.GONE);
            }
        }

        switch (sharedPref.getString("DiskInfo.Theme", "purple")) {
            case "purple":
                themePurple.setChecked(true);
                break;

            case "red":
                themeRed.setChecked(true);
                break;

            case "yellow":
                themeYellow.setChecked(true);
                break;

            case "green":
                themeGreen.setChecked(true);
                break;

            case "orange":
                themeOrange.setChecked(true);
                break;

            case "pink":
                themePink.setChecked(true);
                break;

        }

        switch (sharedPref.getInt("DiskInfo.MODE", -1)) {
            case -1:
                appTheme.check(R.id.theme_auto);
                break;

            case 1:
                appTheme.check(R.id.theme_light);
                break;

            case 2:
                appTheme.check(R.id.theme_dark);
                break;
        }


        appTheme.addOnButtonCheckedListener(((group, checkedId, isChecked) -> {
            if (checkedId == R.id.theme_auto) {
                editor.putInt("DiskInfo.MODE", -1).apply();
                dismiss();
                restart();
            }
            if (checkedId == R.id.theme_light) {
                editor.putInt("DiskInfo.MODE", 1).apply();
                dismiss();
                restart();
            }
            if (checkedId == R.id.theme_dark) {
                editor.putInt("DiskInfo.MODE", 2).apply();
                dismiss();
                restart();
            }
        }));

        dynamicColors.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (checkedId == R.id.dynamic_on) {
                editor.putBoolean("DiskInfo.DynamicColors", true).apply();
                unCheckAll();
                dynamicColors.check(R.id.dynamic_on);
                themeDynamic.setVisibility(View.VISIBLE);
                themeDynamic.setChecked(true);
                editor.putString("DiskInfo.Theme", "dynamic").apply();
                DynamicColors.applyIfAvailable(requireActivity());
            }
            if (checkedId == R.id.dynamic_off) {
                editor.putBoolean("DiskInfo.DynamicColors", false).apply();
                editor.putString("DiskInfo.Theme", "purple").apply();
                themePurple.setChecked(true);
                themeDynamic.setVisibility(View.GONE);
                dismiss();
                restart();
            }
        });

        themePurple.setOnClickListener(this);
        themeRed.setOnClickListener(this);
        themeYellow.setOnClickListener(this);
        themeGreen.setOnClickListener(this);
        themeOrange.setOnClickListener(this);
        themePink.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.theme_purple:
                unCheckAll();
                if (!themePurple.isChecked()) themePurple.setChecked(true);
                editor.putString("DiskInfo.Theme", "purple").apply();
                editor.putBoolean("DiskInfo.DynamicColors", false).apply();
                dismiss();
                restart();
                break;

            case R.id.theme_red:
                unCheckAll();
                if (!themeRed.isChecked()) themeRed.setChecked(true);
                editor.putString("DiskInfo.Theme", "red").apply();
                editor.putBoolean("DiskInfo.DynamicColors", false).apply();
                dismiss();
                restart();
                break;

            case R.id.theme_yellow:
                unCheckAll();
                if (!themeYellow.isChecked()) themeYellow.setChecked(true);
                editor.putString("DiskInfo.Theme", "yellow").apply();
                editor.putBoolean("DiskInfo.DynamicColors", false).apply();
                dismiss();
                restart();
                break;

            case R.id.theme_green:
                unCheckAll();
                if (!themeGreen.isChecked()) themeGreen.setChecked(true);
                editor.putString("DiskInfo.Theme", "green").apply();
                editor.putBoolean("DiskInfo.DynamicColors", false).apply();
                dismiss();
                restart();
                break;

            case R.id.theme_orange:
                unCheckAll();
                if (!themeOrange.isChecked()) themeOrange.setChecked(true);
                editor.putString("DiskInfo.Theme", "orange").apply();
                editor.putBoolean("DiskInfo.DynamicColors", false).apply();
                dismiss();
                restart();
                break;

            case R.id.theme_pink:
                unCheckAll();
                if (!themePink.isChecked()) themePink.setChecked(true);
                editor.putString("DiskInfo.Theme", "pink").apply();
                editor.putBoolean("DiskInfo.DynamicColors", false).apply();
                dismiss();
                restart();
                break;
        }

    }


    private void restart() {
        ((MainActivity) requireActivity()).restartToApply(100);
    }

    private void unCheckAll() {
        themePurple.setChecked(false);
        themeRed.setChecked(false);
        themeYellow.setChecked(false);
        themeGreen.setChecked(false);
        themeOrange.setChecked(false);
        themePink.setChecked(false);
        themeDynamic.setChecked(false);

    }

//    @Override
//    public void onDismiss(@NonNull DialogInterface dialog) {
//        super.onDismiss(dialog);
//        ((MainActivity)requireActivity()).resetThemeFabLocation();
//    }
}
