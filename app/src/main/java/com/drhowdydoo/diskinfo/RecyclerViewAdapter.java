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

public class RecyclerViewAdapter extends RecyclerView.Adapter {

    private ArrayList<Object> storeArrayList;
    private Context context;
    private SharedPreferences sharedPref;

    public RecyclerViewAdapter(Context context, ArrayList<Object> storeArrayList) {
        this.storeArrayList = new ArrayList<>(storeArrayList);
        this.context = context;
        sharedPref = context.getSharedPreferences("MainActivity", Context.MODE_PRIVATE);
    }

    @Override
    public int getItemViewType(int position) {
        if (storeArrayList.get(position) instanceof DataStore) {
            return 0;
        } else if (storeArrayList.get(position) instanceof MemInfo) {
            return 1;
        } else {
            return 2;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_partion_info, parent, false);
            return new PartitionViewHolder(view);
        } else if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meminfo, parent, false);
            return new MemInfoViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false);
            return new HeaderViewHolder(view);
        }

    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        String _total = " " + context.getString(R.string.total),
                _used = " " + context.getString(R.string.used),
                _free = " " + context.getString(R.string.free);

        if (this.getItemViewType(position) == 0) {

            DataStore curr = (DataStore) storeArrayList.get(position);
            String readOnly = "r", readWrite = "r/w";
            String access_type = curr.isAccess_type() ? readOnly : readWrite;

            PartitionViewHolder partitionViewHolder = (PartitionViewHolder) holder;

            partitionViewHolder.mountName.setText(curr.getMount_name());
            partitionViewHolder.totalSpace.setText(curr.getTotalSize() + _total);
            partitionViewHolder.chip_fileSystem.setText(curr.getFileSystem());
            partitionViewHolder.chip_access.setText(access_type);
            partitionViewHolder.usedSpace.setText(curr.getUsedSize() + _used);
            partitionViewHolder.freeSpace.setText(curr.getFreeSize() + _free);
            partitionViewHolder.track_bar.setProgress(curr.getProgress(), sharedPref.getBoolean("animation", true));
            partitionViewHolder.chip_blockSize.setText(curr.getBlockSize());
            if (sharedPref.getBoolean("blockSize", true)) {
                partitionViewHolder.chip_blockSize.setVisibility(View.VISIBLE);
            } else {
                partitionViewHolder.chip_blockSize.setVisibility(View.GONE);
            }
        } else if (this.getItemViewType(position) == 1) {
            MemInfoViewHolder memInfoViewHolder = (MemInfoViewHolder) holder;
            MemInfo memInfo = (MemInfo) storeArrayList.get(position);
            memInfoViewHolder.memName.setText(memInfo.getName());
            memInfoViewHolder.memTotal.setText(memInfo.getTotalMem() + _total);
            memInfoViewHolder.memUsed.setText(memInfo.getUsedMem() + _used);
            memInfoViewHolder.memAvail.setText(memInfo.getAvailMem() + _free);
            memInfoViewHolder.memTrack.setProgress(memInfo.getMemBar(), sharedPref.getBoolean("animation", true));
            memInfoViewHolder.chipCache.setText(memInfo.getCache());
        } else {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            String header = (String) storeArrayList.get(position);
            headerViewHolder.headerView.setText(header);
        }

    }

    @Override
    public int getItemCount() {
        return storeArrayList.size();
    }


    public static class PartitionViewHolder extends RecyclerView.ViewHolder {

        public TextView mountName, usedSpace, totalSpace, freeSpace;
        public Chip chip_fileSystem, chip_access, chip_blockSize;
        public LinearProgressIndicator track_bar;

        public PartitionViewHolder(@NonNull View itemView) {
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

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView headerView;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            headerView = itemView.findViewById(R.id.txtView_header);
        }
    }

    public static class MemInfoViewHolder extends RecyclerView.ViewHolder {

        TextView memName, memTotal, memUsed, memAvail;
        LinearProgressIndicator memTrack;
        Chip chipCache;

        public MemInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            memName = itemView.findViewById(R.id.txtView_name_mem);
            memTotal = itemView.findViewById(R.id.txtView_total_mem);
            memAvail = itemView.findViewById(R.id.txtView_free_mem);
            memUsed = itemView.findViewById(R.id.txtView_used_mem);
            memTrack = itemView.findViewById(R.id.track_bar_mem);
            chipCache = itemView.findViewById(R.id.chip_cached);
        }
    }

}
