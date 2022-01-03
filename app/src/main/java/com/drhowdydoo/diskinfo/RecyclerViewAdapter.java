package com.drhowdydoo.diskinfo;

import android.annotation.SuppressLint;
import android.content.Context;
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

    public RecyclerViewAdapter(Context context, ArrayList<DataStore> storeArrayList) {
        this.storeArrayList = new ArrayList<>(storeArrayList);
        this.context = context;
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
        holder.totalSpace.setText(Util.FormatBytes(curr.getTotal()) + " total");
        holder.chip_fileSystem.setText(curr.getFileSystem());
        holder.chip_access.setText(access_type);
        holder.usedSpace.setText(Util.FormatBytes(curr.getUsed()) + " used");
        holder.freeSpace.setText(Util.FormatBytes(curr.getUnused()) + " free");
        holder.track_bar.setProgress(Util.getUsedSpace(curr.getTotal(), curr.getUsed()));

    }

    @Override
    public int getItemCount() {
        return storeArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mountName, usedSpace, totalSpace, freeSpace;
        public Chip chip_fileSystem, chip_access;
        public LinearProgressIndicator track_bar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mountName = itemView.findViewById(R.id.txtView_mount);
            track_bar = itemView.findViewById(R.id.track_bar);
            chip_fileSystem = itemView.findViewById(R.id.chip_fileSystem);
            chip_access = itemView.findViewById(R.id.chip_access);
            usedSpace = itemView.findViewById(R.id.txtView_used_size);
            totalSpace = itemView.findViewById(R.id.txtView_total_size);
            freeSpace = itemView.findViewById(R.id.txtView_unused);
        }
    }
}
