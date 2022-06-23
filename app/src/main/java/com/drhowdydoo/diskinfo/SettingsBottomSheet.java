package com.drhowdydoo.diskinfo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsBottomSheet extends BottomSheetDialogFragment {

    private SwitchMaterial animation, blockSize, advanceMode;
    private MaterialButtonToggleGroup unitToggle;
    private TextView versionName;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private boolean isChanged = false, isCheckedPreviously, advanceModeOn = false, isModeChanged = false, useSI = false;

    public SettingsBottomSheet() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings_bottom_sheet_layout,
                container, false);

        sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();


        versionName = v.findViewById(R.id.txtView_version);
        animation = v.findViewById(R.id.switchMaterial_animation);
        blockSize = v.findViewById(R.id.switchMaterial_blockSize);
        advanceMode = v.findViewById(R.id.switchMaterial_advance_mode);
        unitToggle = v.findViewById(R.id.unitToggle);

        versionName.setText("v " + BuildConfig.VERSION_NAME);


        animation.setChecked(sharedPref.getBoolean("animation", true));
        blockSize.setChecked(sharedPref.getBoolean("blockSize", true));
        advanceMode.setChecked(sharedPref.getBoolean("advanceMode", false));

        if (sharedPref.getBoolean("useSI", false)) {
            unitToggle.check(R.id.btn_si);
            useSI = true;
        } else {
            unitToggle.check(R.id.btn_iec);
            useSI = false;
        }

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
            isModeChanged = advanceModeOn ^ isChecked;
        }));


        unitToggle.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (checkedId == R.id.btn_si) {
                editor.putBoolean("useSI", true).apply();
            }
            if (checkedId == R.id.btn_iec) {
                editor.putBoolean("useSI", false).apply();
            }

        });


        return v;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);


        if (useSI ^ sharedPref.getBoolean("useSI", false))
            ((MainActivity) requireActivity()).restartToApply(0);

        if (isModeChanged) ((MainActivity) requireActivity()).advanceModeOn();
        isModeChanged = false;

        if (isChanged) {
            ((MainActivity) requireActivity()).recreateRecyclerView();
        }
        isChanged = false;


    }

}
