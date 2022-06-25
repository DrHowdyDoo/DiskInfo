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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

@SuppressWarnings("FieldCanBeLocal")
public class LanguageSelector extends BottomSheetDialogFragment {

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private RecyclerView recyclerView;
    private LanguageSelectorAdaptor recyclerViewAdapter;
    private MainActivity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.language_selector_bottom_sheet, container, false);

        sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        activity = (MainActivity) requireActivity();

        recyclerView = v.findViewById(R.id.recyclerView_language);

        ArrayList<LanguageInfo> languageInfo = new ArrayList<>();

        LanguageInfo l1 = new LanguageInfo("en", "Avi Saxena");
        LanguageInfo l2 = new LanguageInfo("es", "Google Translate");
        LanguageInfo l3 = new LanguageInfo("in", "Google Translate");
        LanguageInfo l4 = new LanguageInfo("ja", "Google Translate");
        LanguageInfo l5 = new LanguageInfo("ko", "Prachi Saxena");
        LanguageInfo l6 = new LanguageInfo("pt", "Google Translate");
        LanguageInfo l7 = new LanguageInfo("ru", "Google Translate");
        LanguageInfo l8 = new LanguageInfo("vi", "Khang Nguyễn");

        languageInfo.add(l1);
        languageInfo.add(l2);
        languageInfo.add(l3);
        languageInfo.add(l4);
        languageInfo.add(l5);
        languageInfo.add(l6);
        languageInfo.add(l7);
        languageInfo.add(l8);


        recyclerViewAdapter = new LanguageSelectorAdaptor(languageInfo, requireActivity(), LanguageSelector.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setAdapter(recyclerViewAdapter);


        return v;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        SettingsBottomSheet settingsBottomSheet = (SettingsBottomSheet) activity.getSupportFragmentManager().findFragmentByTag("Settings");
        if (settingsBottomSheet != null)
            settingsBottomSheet.updateLanguage();
    }


}
