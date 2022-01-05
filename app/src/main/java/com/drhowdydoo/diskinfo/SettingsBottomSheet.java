package com.drhowdydoo.diskinfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsBottomSheet extends BottomSheetDialogFragment {

    private SwitchMaterial animation;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    public SettingsBottomSheet() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings_bottom_sheet_layout,
                container, false);

        sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        Log.d("TEST", "onCreateView: ");

        animation = v.findViewById(R.id.switchMaterial_animation);
        animation.setChecked(sharedPref.getBoolean("animation", true));

        animation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("animation", isChecked).apply();
        });


        return v;
    }
}
