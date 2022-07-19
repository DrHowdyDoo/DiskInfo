package com.drhowdydoo.diskinfo.bottomsheet;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;

import com.drhowdydoo.diskinfo.R;
import com.drhowdydoo.diskinfo.activity.MainActivity;
import com.drhowdydoo.diskinfo.adapter.ThemeAdapter;
import com.drhowdydoo.diskinfo.model.Theme;
import com.drhowdydoo.diskinfo.util.Util;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.color.DynamicColors;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;

public class ThemeBottomSheet extends BottomSheetDialogFragment {


    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private MaterialButtonToggleGroup dynamicColors, appTheme;
    private TextView dynamicColorsTitle, amoledModeBody;
    private SwitchMaterial amoledMode;
    private RecyclerView themeRecyclerView;
    private ArrayList<Theme> themes;
    private MaterialCardView amoledModeContainer;

    private boolean isAmoledModeChanged = false, prevState;

    public ThemeBottomSheet() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.theme_bottom_sheet_layout,
                container, false);


        themeRecyclerView = v.findViewById(R.id.theme_recycler_view);
        amoledMode = v.findViewById(R.id.switch_amoledMode);
        amoledModeContainer = v.findViewById(R.id.cardView_amoledMode);
        amoledModeBody = v.findViewById(R.id.txtView_amoledMode_body);
        dynamicColors = v.findViewById(R.id.toggleButton);
        appTheme = v.findViewById(R.id.appTheme);
        dynamicColorsTitle = v.findViewById(R.id.txtView_dynamic_colors);


        sharedPref = requireActivity().getSharedPreferences("com.drhowdydoo.diskinfo", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        themes = new ArrayList<>();
        Theme theme1 = new Theme(R.style.Theme_DiskInfo_Purple, R.style.Theme_DiskInfo_Purple_Amoled, 0);
        Theme theme2 = new Theme(R.style.Theme_DiskInfo_Red, R.style.Theme_DiskInfo_Red_Amoled, 1);
        Theme theme3 = new Theme(R.style.Theme_DiskInfo_Yellow, R.style.Theme_DiskInfo_Yellow_Amoled, 2);
        Theme theme4 = new Theme(R.style.Theme_DiskInfo_Green, R.style.Theme_DiskInfo_Green_Amoled, 3);
        Theme theme5 = new Theme(R.style.Theme_DiskInfo_Orange, R.style.Theme_DiskInfo_Orange_Amoled, 4);
        Theme theme6 = new Theme(R.style.Theme_DiskInfo_Pink, R.style.Theme_DiskInfo_Pink_Amoled, 5);
        Theme dynamicTheme = new Theme(Util.Theme_Dynamic, Util.Theme_Dynamic, -1);

        themes.add(theme1);
        themes.add(theme2);
        themes.add(theme3);
        themes.add(theme4);
        themes.add(theme5);
        themes.add(theme6);
        themes.add(dynamicTheme);


        ThemeAdapter themeAdapter = new ThemeAdapter(themes, requireActivity(), ThemeBottomSheet.this);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(requireActivity());
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.CENTER);
        layoutManager.setAlignItems(AlignItems.CENTER);
        themeRecyclerView.setLayoutManager(layoutManager);
        themeRecyclerView.setHasFixedSize(false);
        themeRecyclerView.setAdapter(themeAdapter);


        if (Build.VERSION.SDK_INT < 31) {
            dynamicColors.setVisibility(View.GONE);
            dynamicColorsTitle.setVisibility(View.GONE);
        } else {
            if (sharedPref.getInt("DiskInfo.Theme.Id", 0) == -1) {
                dynamicColors.check(R.id.dynamic_on);
            } else {
                dynamicColors.check(R.id.dynamic_off);
            }
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
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            }
            if (checkedId == R.id.theme_light) {
                editor.putInt("DiskInfo.MODE", 1).apply();
                dismiss();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            if (checkedId == R.id.theme_dark) {
                editor.putInt("DiskInfo.MODE", 2).apply();
                dismiss();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        }));


        dynamicColors.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (checkedId == R.id.dynamic_on) {
                editor.putBoolean("DiskInfo.DynamicColors", true).apply();
                dynamicColors.check(R.id.dynamic_on);
                editor.putInt("DiskInfo.Theme", Util.Theme_Dynamic).apply();
                editor.putInt("DiskInfo.Theme.Id", -1).apply();
                DynamicColors.applyIfAvailable(requireActivity());
            }
            if (checkedId == R.id.dynamic_off) {
                editor.putBoolean("DiskInfo.DynamicColors", false).apply();
                if (sharedPref.getBoolean("amoledMode", false))
                    editor.putInt("DiskInfo.Theme", R.style.Theme_DiskInfo_Purple_Amoled).apply();
                else editor.putInt("DiskInfo.Theme", R.style.Theme_DiskInfo_Purple).apply();
                editor.putInt("DiskInfo.Theme.Id", 0).apply();
                dismiss();
                restart();
            }
        });


        if (sharedPref.getBoolean("DiskInfo.DynamicColors", false)) {
            amoledMode.setChecked(false);
            amoledMode.setEnabled(false);
            amoledModeContainer.setEnabled(false);
            amoledModeBody.setVisibility(View.VISIBLE);
        } else {
            amoledMode.setChecked(sharedPref.getBoolean("amoledMode", false));
            prevState = amoledMode.isChecked();
        }

        amoledMode.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            editor.putBoolean("amoledMode", isChecked).apply();
            int theme;
            if (isChecked)
                theme = themes.get(sharedPref.getInt("DiskInfo.Theme.Id", 0)).getAmoledVersion();
            else theme = themes.get(sharedPref.getInt("DiskInfo.Theme.Id", 0)).getTheme();
            editor.putInt("DiskInfo.Theme", theme).apply();
            isAmoledModeChanged = isChecked ^ prevState;
        }));

        amoledModeContainer.setOnClickListener(view -> {
            amoledMode.setChecked(!amoledMode.isChecked());
        });

        return v;
    }


    public void restart() {
        ((MainActivity) requireActivity()).restartToApply(100);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (isAmoledModeChanged) restart();
        isAmoledModeChanged = false;
    }
}
