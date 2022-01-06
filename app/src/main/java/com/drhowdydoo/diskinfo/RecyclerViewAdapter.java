package com.drhowdydoo.diskinfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<DataStore> storeArrayList;
    private Context context;
    private SharedPreferences sharedPref;

    public RecyclerViewAdapter(Context context, ArrayList<DataStore> storeArrayList) {
        this.storeArrayList = new ArrayList<>(storeArrayList);
        this.context = context;
        sharedPref = context.getSharedPreferences("MainActivity", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {

        DataStore curr = storeArrayList.get(position);
        String readOnly = "r", readWrite = "r/w";
        String access_type = curr.isAccess_type() ? readOnly : readWrite;

        holder.mountName.setText(curr.getMount_name());
        holder.totalSpace.setText(curr.getTotalSize() + " total");
        holder.chip_fileSystem.setText(curr.getFileSystem());
        holder.chip_access.setText(access_type);
        holder.usedSpace.setText(curr.getUsedSize() + " used");
        holder.freeSpace.setText(curr.getFreeSize() + " free");
        holder.track_bar.setProgress(curr.getProgress(), sharedPref.getBoolean("animation", true));
        holder.chip_blockSize.setText(curr.getBlockSize());
        if (sharedPref.getBoolean("blockSize", true)) {
            holder.chip_blockSize.setVisibility(View.VISIBLE);
        } else {
            holder.chip_blockSize.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return storeArrayList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mountName, usedSpace, totalSpace, freeSpace;
        public Chip chip_fileSystem, chip_access, chip_blockSize;
        public LinearProgressIndicator track_bar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mountName = itemView.findViewById(R.id.txtView_mount);
            track_bar = itemView.findViewById(R.id.track_bar);
            chip_fileSystem = itemView.findViewById(R.id.chip_fileSystem);
            chip_access = itemView.findViewById(R.id.chip_access);
            chip_blockSize = itemView.findViewById(R.id.chip_blockSize);
            usedSpace = itemView.findViewById(R.id.txtView_used_size);
            totalSpace = itemView.findViewById(R.id.txtView_total_size);
            freeSpace = itemView.findViewById(R.id.txtView_unused);
        }
    }

}
