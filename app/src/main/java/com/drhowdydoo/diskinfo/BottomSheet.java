package com.drhowdydoo.diskinfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;

public class BottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {


    private MaterialCardView themePurple, themeRed, themeYellow, themeGreen;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    public BottomSheet() {

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

        themePurple.setOnClickListener(this);
        themeRed.setOnClickListener(this);
        themeYellow.setOnClickListener(this);
        themeGreen.setOnClickListener(this);

        sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();

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
        }

        return v;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.theme_purple:
                unCheckAll();
                if (!themePurple.isChecked()) themePurple.setChecked(true);
                editor.putString("DiskInfo.Theme", "purple").apply();
                dismiss();
                restart();
                break;

            case R.id.theme_red:
                unCheckAll();
                if (!themeRed.isChecked()) themeRed.setChecked(true);
                editor.putString("DiskInfo.Theme", "red").apply();
                dismiss();
                restart();
                break;

            case R.id.theme_yellow:
                unCheckAll();
                if (!themeYellow.isChecked()) themeYellow.setChecked(true);
                editor.putString("DiskInfo.Theme", "yellow").apply();
                dismiss();
                restart();
                break;

            case R.id.theme_green:
                unCheckAll();
                if (!themeGreen.isChecked()) themeGreen.setChecked(true);
                editor.putString("DiskInfo.Theme", "green").apply();
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
    }

}
