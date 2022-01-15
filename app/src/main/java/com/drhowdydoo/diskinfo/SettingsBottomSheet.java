package com.drhowdydoo.diskinfo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsBottomSheet extends BottomSheetDialogFragment {

    private SwitchMaterial animation, blockSize, advanceMode;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private boolean isChanged = false, isCheckedPreviously, advanceModeOn = false;

    public SettingsBottomSheet() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings_bottom_sheet_layout,
                container, false);

        sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();


        animation = v.findViewById(R.id.switchMaterial_animation);
        blockSize = v.findViewById(R.id.switchMaterial_blockSize);
        advanceMode = v.findViewById(R.id.switchMaterial_advance_mode);

        animation.setChecked(sharedPref.getBoolean("animation", true));
        blockSize.setChecked(sharedPref.getBoolean("blockSize", true));
        advanceMode.setChecked(sharedPref.getBoolean("advanceMode", false));

        isCheckedPreviously = blockSize.isChecked();
        advanceModeOn = advanceMode.isChecked();

        animation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("animation", isChecked).apply();
        });

        blockSize.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("blockSize", isChecked).apply();
            isChanged = (isCheckedPreviously ^ isChecked);
        });

        advanceMode.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            editor.putBoolean("advanceMode", isChecked).apply();
            advanceModeOn ^= isChecked;
        }));


        return v;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (isChanged) {
            ((MainActivity) requireActivity()).recreateRecyclerView();
        }
        isChanged = false;
        if (advanceModeOn) ((MainActivity) requireActivity()).advanceModeOn();
    }

}
