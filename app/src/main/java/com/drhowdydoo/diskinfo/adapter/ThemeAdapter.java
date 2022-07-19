package com.drhowdydoo.diskinfo.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.drhowdydoo.diskinfo.R;
import com.drhowdydoo.diskinfo.bottomsheet.ThemeBottomSheet;
import com.drhowdydoo.diskinfo.model.Theme;
import com.drhowdydoo.diskinfo.util.Util;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.ThemeViewHolder> {

    private ArrayList<Theme> themeList;
    private final Context context;
    private final SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private int lastSelectedTheme = 0;
    private ThemeBottomSheet themeBottomSheet;

    public ThemeAdapter(ArrayList<Theme> themes, Context context, ThemeBottomSheet themeBottomSheet) {
        this.themeList = themes;
        this.context = context;
        sharedPref = context.getSharedPreferences("com.drhowdydoo.diskinfo", Context.MODE_PRIVATE);
        this.themeBottomSheet = themeBottomSheet;
        editor = sharedPref.edit();
    }

    @NonNull
    @Override
    public ThemeAdapter.ThemeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_theme_box, parent, false);
        return new ThemeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThemeAdapter.ThemeViewHolder holder, int position) {

        Theme themeObj = themeList.get(position);
        int theme = themeObj.getTheme();
        Drawable drawable;
        if (theme == Util.Theme_Dynamic && Build.VERSION.SDK_INT >= 31) {
            drawable = AppCompatResources.getDrawable(context, R.drawable.ic_pie);
            if (drawable != null) {
                drawable = drawable.mutate();
            }
        } else {
            int color = Util.getColorAttr(new ContextThemeWrapper(context, theme), com.google.android.material.R.attr.colorPrimary);
            drawable = AppCompatResources.getDrawable(context, R.drawable.ic_circle_24);
            if (drawable != null) {
                drawable = drawable.mutate();
                drawable.setTint(color);
            }
        }
        holder.themeDot.setBackground(drawable);
        if (sharedPref.getInt("DiskInfo.Theme.Id", 0) == themeObj.getId())
            holder.themeDotContainer.setChecked(true);
        if (themeObj.getId() == -1 && sharedPref.getInt("DiskInfo.Theme.Id", 0) != -1)
            holder.itemView.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return themeList.size();
    }


    public class ThemeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public MaterialCardView themeDotContainer;
        public ImageView themeDot;

        public ThemeViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            themeDotContainer = itemView.findViewById(R.id.theme_dot_container);
            themeDot = itemView.findViewById(R.id.theme_dot);
        }

        @Override
        public void onClick(View view) {
            int position = this.getBindingAdapterPosition();
            Theme themeObj = themeList.get(position);
            if (themeObj.getId() != -1) {
                int theme;
                if (sharedPref.getBoolean("amoledMode", false)) theme = themeObj.getAmoledVersion();
                else theme = themeObj.getTheme();
                editor.putInt("DiskInfo.Theme", theme).apply();
                editor.putInt("DiskInfo.Theme.Id", themeObj.getId()).apply();
                ((MaterialCardView) view).setChecked(true);
                themeBottomSheet.dismiss();
                themeBottomSheet.restart();
            }
        }
    }


}
