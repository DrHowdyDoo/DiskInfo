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

public class LanguageSelectorAdaptor extends RecyclerView.Adapter<LanguageSelectorAdaptor.LanguageViewHolder> {


    private ArrayList<LanguageInfo> languageList;
    private final SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private final Context context;
    private LanguageSelector languageSelector;

    public LanguageSelectorAdaptor(ArrayList<LanguageInfo> languageList, Context context, LanguageSelector languageSelector) {
        this.languageList = new ArrayList<>(languageList);
        this.context = context;
        this.languageSelector = languageSelector;
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

        holder.languageName.setText(new Locale(languageInfo.getLanguageCode()).getDisplayLanguage());
        holder.languageTranslator.setText(languageList.get(position).getLanguageTranslator());
        if (sharedPref.getString("DiskInfo.Language", Locale.getDefault().getLanguage()).equalsIgnoreCase(languageInfo.getLanguageCode())) {
            holder.checked.setVisibility(View.VISIBLE);
        } else {
            holder.checked.setVisibility(View.INVISIBLE);
        }

    }


    @Override
    public int getItemCount() {
        return languageList.size();
    }

    public class LanguageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ConstraintLayout parentLayout;
        public ImageView checked;
        public TextView languageName, languageTranslator;

        public LanguageViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            parentLayout = itemView.findViewById(R.id.parent_language_selector);
            checked = itemView.findViewById(R.id.ic_checked);
            languageName = itemView.findViewById(R.id.txt_language_name);
            languageTranslator = itemView.findViewById(R.id.txt_language_translator);
        }

        @Override
        public void onClick(View v) {
            int position = this.getBindingAdapterPosition();
            editor.putString("DiskInfo.Language", languageList.get(position).getLanguageCode()).apply();
            languageSelector.dismiss();
        }
    }

}
