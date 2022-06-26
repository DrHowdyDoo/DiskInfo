package com.drhowdydoo.diskinfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public class LanguageSelectorAdaptor extends RecyclerView.Adapter<LanguageSelectorAdaptor.LanguageViewHolder> {


    private ArrayList<LanguageInfo> languageList;
    private final SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private final Context context;
    private LanguageSelector languageSelector;
    private Set<String> installedLangs;

    public LanguageSelectorAdaptor(ArrayList<LanguageInfo> languageList, Context context, LanguageSelector languageSelector, Set<String> installedLangs) {
        this.languageList = new ArrayList<>(languageList);
        this.context = context;
        this.languageSelector = languageSelector;
        this.installedLangs = installedLangs;
        sharedPref = this.context.getSharedPreferences("MainActivity", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    @NonNull
    @Override
    public LanguageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.language_selector_item_view, parent, false);
        return new LanguageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageViewHolder holder, int position) {

        LanguageInfo languageInfo = languageList.get(position);
        boolean isInstalled = installedLangs.contains(languageInfo.getLanguageCode());

        holder.languageName.setText(languageInfo.getLanguageName());
        holder.languageDisplayName.setText(new Locale(languageInfo.getLanguageCode()).getDisplayLanguage());
        if (sharedPref.getString("DiskInfo.Language", Locale.getDefault().getLanguage()).equalsIgnoreCase(languageInfo.getLanguageCode())) {
            holder.checked.setVisibility(View.VISIBLE);
        } else {
            holder.checked.setVisibility(View.INVISIBLE);
        }

        if (languageInfo.isShowTranslator()) {
            holder.languageTranslator.setVisibility(View.VISIBLE);
            holder.languageTranslator.setText(languageInfo.getLanguageTranslator());
        } else {
            holder.languageTranslator.setVisibility(View.GONE);
        }

        if (!isInstalled) {
            holder.download.setVisibility(View.VISIBLE);
        } else {
            holder.download.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return languageList.size();
    }

    public class LanguageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ConstraintLayout parentLayout;
        public ImageView checked, download;
        public TextView languageName, languageDisplayName, languageTranslator;

        public LanguageViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            parentLayout = itemView.findViewById(R.id.parent_language_selector);
            checked = itemView.findViewById(R.id.ic_checked);
            languageName = itemView.findViewById(R.id.txt_language_name);
            languageDisplayName = itemView.findViewById(R.id.txt_language_display_name);
            languageTranslator = itemView.findViewById(R.id.txt_language_translator);
            download = itemView.findViewById(R.id.ic_download);
        }

        @Override
        public void onClick(View v) {
            int position = this.getBindingAdapterPosition();
            editor.putString("DiskInfo.Language", languageList.get(position).getLanguageCode()).apply();
            languageSelector.dismiss();
        }
    }

}
