package com.drhowdydoo.diskinfo.bottomsheet;

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

import com.drhowdydoo.diskinfo.BuildConfig;
import com.drhowdydoo.diskinfo.R;
import com.drhowdydoo.diskinfo.activity.MainActivity;
import com.drhowdydoo.diskinfo.adapter.LanguageSelectorAdaptor;
import com.drhowdydoo.diskinfo.model.LanguageInfo;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.play.core.splitinstall.SplitInstallManager;
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;

@SuppressWarnings("FieldCanBeLocal")
public class LanguageSelector extends BottomSheetDialogFragment {

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private RecyclerView recyclerView;
    private LanguageSelectorAdaptor recyclerViewAdapter;
    private MainActivity activity;
    private Set<String> installedLangs;
    private SplitInstallManager splitInstallManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.language_selector_bottom_sheet, container, false);

        sharedPref = requireActivity().getSharedPreferences("com.drhowdydoo.diskinfo", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        activity = (MainActivity) requireActivity();

        recyclerView = v.findViewById(R.id.recyclerView_language);

        ArrayList<LanguageInfo> languageInfo = new ArrayList<>();

        splitInstallManager = SplitInstallManagerFactory.create(activity);
        installedLangs = splitInstallManager.getInstalledLanguages();

        LanguageInfo l1 = new LanguageInfo("en", "Avi Saxena", "English", true);
        LanguageInfo l2 = new LanguageInfo("es", "Google Translate", "Español", false);
        LanguageInfo l3 = new LanguageInfo("in", "Google Translate", "Indonesia", false);
        LanguageInfo l4 = new LanguageInfo("ja", "Google Translate", "日本語", false);
        LanguageInfo l5 = new LanguageInfo("ko", "Prachi Saxena", "한국어", true);
        LanguageInfo l6 = new LanguageInfo("pt", "Google Translate", "Português", false);
        LanguageInfo l7 = new LanguageInfo("ru", "Rasta Gubaz", "русский", true);
        LanguageInfo l8 = new LanguageInfo("vi", "CuynuTT", "Tiếng Việt", true);
        LanguageInfo l9 = new LanguageInfo("az", "thepoladov", "Azərbaycan", true);
        LanguageInfo l10 = new LanguageInfo("ar", "Montathar A. Hussein", "Arabic", true);
        LanguageInfo l11 = new LanguageInfo("pl", "Bartosz Northen", "Polish", true);

        languageInfo.add(l1);
        languageInfo.add(l2);
        languageInfo.add(l3);
        languageInfo.add(l4);
        languageInfo.add(l5);
        languageInfo.add(l6);
        languageInfo.add(l7);
        languageInfo.add(l8);
        languageInfo.add(l9);
        languageInfo.add(l10);
        languageInfo.add(l11);


        recyclerViewAdapter = new LanguageSelectorAdaptor(languageInfo, requireActivity(), LanguageSelector.this, installedLangs);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setAdapter(recyclerViewAdapter);

        return v;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        SettingsBottomSheet settingsBottomSheet = (SettingsBottomSheet) activity.getSupportFragmentManager().findFragmentByTag("Settings");
        if (settingsBottomSheet != null) {
            settingsBottomSheet.updateLanguage(installedLangs.contains(sharedPref.getString("DiskInfo.Language", Locale.getDefault().getLanguage())));
            if (!BuildConfig.DEBUG) activity.downloadRes();
        }
    }


}
