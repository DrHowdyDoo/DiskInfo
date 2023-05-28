package com.drhowdydoo.diskinfo.bottomsheet;

import android.content.Context;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.content.res.AppCompatResources;

import com.drhowdydoo.diskinfo.R;
import com.drhowdydoo.diskinfo.activity.MainActivity;
import com.drhowdydoo.diskinfo.util.Util;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.materialswitch.MaterialSwitch;

public class ThemeBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {


    private MaterialCardView themePurple, themeRed, themeYellow, themeGreen, themeDynamic, themeOrange, themePink;
    private MaterialCardView amoledModeContainer;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private MaterialButtonToggleGroup dynamicColors, appTheme;
    private MaterialButton t1, t2, x1, x2, x3;
    private TextView dynamicColorsTitle, amoledModeBody;
    private MaterialSwitch amoledMode;

    private ImageView imgPurple, imgRed, imgYellow, imgGreen, imgOrange, imgPink;
    private boolean isAmoledModeChanged = false, prevState;

    public ThemeBottomSheet() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.theme_bottom_sheet_layout,
                container, false);


        themePurple = v.findViewById(R.id.theme_purple);
        themeRed = v.findViewById(R.id.theme_red);
        themeYellow = v.findViewById(R.id.theme_yellow);
        themeGreen = v.findViewById(R.id.theme_green);
        themeDynamic = v.findViewById(R.id.theme_dynamic);
        themeOrange = v.findViewById(R.id.theme_orange);
        themePink = v.findViewById(R.id.theme_pink);

        themePurple.setCheckedIconTint(null);
        themeRed.setCheckedIconTint(null);
        themeYellow.setCheckedIconTint(null);
        themeGreen.setCheckedIconTint(null);
        themeDynamic.setCheckedIconTint(null);
        themeOrange.setCheckedIconTint(null);
        themePink.setCheckedIconTint(null);


        imgPurple = v.findViewById(R.id.img_purple);
        imgRed = v.findViewById(R.id.img_red);
        imgYellow = v.findViewById(R.id.img_yellow);
        imgGreen = v.findViewById(R.id.img_green);
        imgOrange = v.findViewById(R.id.img_orange);
        imgPink = v.findViewById(R.id.img_pink);

        amoledMode = v.findViewById(R.id.switch_amoledMode);
        amoledModeBody = v.findViewById(R.id.txtView_amoledMode_body);
        amoledModeContainer = v.findViewById(R.id.cardView_amoledMode);


        Drawable[] mDrawables = new Drawable[6];
        int[] themeRes = {
                R.style.Theme_DiskInfo_Purple,
                R.style.Theme_DiskInfo_Red,
                R.style.Theme_DiskInfo_Yellow,
                R.style.Theme_DiskInfo_Green,
                R.style.Theme_DiskInfo_Orange,
                R.style.Theme_DiskInfo_Pink
        };

        for (int i = 0; i < mDrawables.length; i++) {
            mDrawables[i] = createDrawable();
            assert mDrawables[i] != null;
            mDrawables[i].setTint(Util.getColorAttr(new ContextThemeWrapper(requireActivity(), themeRes[i]), com.google.android.material.R.attr.colorPrimary));
        }

        imgPurple.setBackground(mDrawables[0]);
        imgRed.setBackground(mDrawables[1]);
        imgYellow.setBackground(mDrawables[2]);
        imgGreen.setBackground(mDrawables[3]);
        imgOrange.setBackground(mDrawables[4]);
        imgPink.setBackground(mDrawables[5]);


        dynamicColors = v.findViewById(R.id.toggleButton);
        appTheme = v.findViewById(R.id.appTheme);
        t1 = v.findViewById(R.id.dynamic_on);
        t2 = v.findViewById(R.id.dynamic_off);
        dynamicColorsTitle = v.findViewById(R.id.txtView_dynamic_colors);

        sharedPref = requireActivity().getSharedPreferences("com.drhowdydoo.diskinfo", Context.MODE_PRIVATE);
        editor = sharedPref.edit();


        if (Build.VERSION.SDK_INT < 31) {
            dynamicColors.setVisibility(View.GONE);
            dynamicColorsTitle.setVisibility(View.GONE);
        } else {
            if (sharedPref.getBoolean("DiskInfo.DynamicColors", false)) {
                System.out.println("Dynamic toggle restore : ");
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
            if (checkedId == R.id.theme_auto && isChecked) {
                editor.putInt("DiskInfo.MODE", -1).apply();
                dismiss();
                //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                restart();
            }
            if (checkedId == R.id.theme_light && isChecked) {
                editor.putInt("DiskInfo.MODE", 1).apply();
                dismiss();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            if (checkedId == R.id.theme_dark && isChecked) {
                editor.putInt("DiskInfo.MODE", 2).apply();
                dismiss();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        }));

        dynamicColors.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (checkedId == R.id.dynamic_on && isChecked) {
                editor.putBoolean("DiskInfo.DynamicColors", true).apply();
                unCheckAll();
                dynamicColors.check(R.id.dynamic_on);
                themeDynamic.setVisibility(View.VISIBLE);
                themeDynamic.setChecked(true);
                editor.putString("DiskInfo.Theme", "dynamic").apply();
                dismiss();
                restart();
            }
            if (checkedId == R.id.dynamic_off && isChecked) {
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

        if (sharedPref.getBoolean("DiskInfo.DynamicColors", false)) {
            amoledMode.setChecked(false);
            amoledMode.setEnabled(false);
            amoledModeContainer.setEnabled(false);
            amoledModeBody.setVisibility(View.VISIBLE);
        } else {
            amoledMode.setChecked(sharedPref.getBoolean("amoledMode", false));
            amoledModeContainer.setEnabled(true);
            prevState = amoledMode.isChecked();
        }

        amoledMode.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            editor.putBoolean("amoledMode", isChecked).apply();
            isAmoledModeChanged = isChecked ^ prevState;
        }));

        amoledModeContainer.setOnClickListener(view -> amoledMode.setChecked(!amoledMode.isChecked()));

        return v;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.theme_purple) {
            unCheckAll();
            if (!themePurple.isChecked()) themePurple.setChecked(true);
            editor.putString("DiskInfo.Theme", "purple").apply();
            editor.putBoolean("DiskInfo.DynamicColors", false).apply();
            dismiss();
            restart();
        } else if (id == R.id.theme_red) {
            unCheckAll();
            if (!themeRed.isChecked()) themeRed.setChecked(true);
            editor.putString("DiskInfo.Theme", "red").apply();
            editor.putBoolean("DiskInfo.DynamicColors", false).apply();
            dismiss();
            restart();
        } else if (id == R.id.theme_yellow) {
            unCheckAll();
            if (!themeYellow.isChecked()) themeYellow.setChecked(true);
            editor.putString("DiskInfo.Theme", "yellow").apply();
            editor.putBoolean("DiskInfo.DynamicColors", false).apply();
            dismiss();
            restart();
        } else if (id == R.id.theme_green) {
            unCheckAll();
            if (!themeGreen.isChecked()) themeGreen.setChecked(true);
            editor.putString("DiskInfo.Theme", "green").apply();
            editor.putBoolean("DiskInfo.DynamicColors", false).apply();
            dismiss();
            restart();
        } else if (id == R.id.theme_orange) {
            unCheckAll();
            if (!themeOrange.isChecked()) themeOrange.setChecked(true);
            editor.putString("DiskInfo.Theme", "orange").apply();
            editor.putBoolean("DiskInfo.DynamicColors", false).apply();
            dismiss();
            restart();
        } else if (id == R.id.theme_pink) {
            unCheckAll();
            if (!themePink.isChecked()) themePink.setChecked(true);
            editor.putString("DiskInfo.Theme", "pink").apply();
            editor.putBoolean("DiskInfo.DynamicColors", false).apply();
            dismiss();
            restart();
        }

    }

    private Drawable createDrawable() {
        Drawable mDrawable = AppCompatResources.getDrawable(requireActivity(), R.drawable.ic_circle_24);
        if (mDrawable != null)
            return mDrawable.mutate();
        else return null;
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

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (isAmoledModeChanged) restart();
        isAmoledModeChanged = false;
    }
}
