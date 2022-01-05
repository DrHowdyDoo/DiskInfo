package com.drhowdydoo.diskinfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StatFs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.color.DynamicColors;
import com.google.android.material.divider.MaterialDividerItemDecoration;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.ArrayList;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MaterialToolbar materialToolbar;
    private FloatingActionButton fab;
    private SharedPreferences sharedPref;
    private AppBarLayout appBarLayout;
    private MaterialButton settings;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<DataStore> storeArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        switch (sharedPref.getString("DiskInfo.Theme", "purple")) {
            case "purple":
                setTheme(R.style.Theme_DiskInfo_Purple);
                break;

            case "red":
                setTheme(R.style.Theme_DiskInfo_Red);
                break;

            case "yellow":
                setTheme(R.style.Theme_DiskInfo_Yellow);
                break;

            case "green":
                setTheme(R.style.Theme_DiskInfo_Green);
                break;

            default:
                if (DynamicColors.isDynamicColorAvailable()) {
                    DynamicColors.applyIfAvailable(this);
                } else setTheme(R.style.Theme_DiskInfo_Purple);
                break;

        }
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        materialToolbar = findViewById(R.id.materialToolBar);
        appBarLayout = findViewById(R.id.appBar);
        fab = findViewById(R.id.fab_theme);
        settings = findViewById(R.id.settings);

        String version = "v" + BuildConfig.VERSION_NAME;
        materialToolbar.setSubtitle(version);

        storeArrayList = new ArrayList<>();

        FileSystem filesystem = FileSystems.getDefault();
        for (FileStore store : filesystem.getFileStores()) {
            try {
                long totalSpace = store.getTotalSpace();
                long unusedSpace = store.getUnallocatedSpace();
                long usedSpace = totalSpace - unusedSpace;
                String fileStorePath = store.toString().replaceFirst(" .*", "");
                StatFs statFs = new StatFs(fileStorePath);
                long blockSize = statFs.getBlockSizeLong();
                DataStore dataStore = new DataStore(store.toString(), store.type(), store.isReadOnly(), totalSpace, unusedSpace, usedSpace, blockSize);
                storeArrayList.add(dataStore);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        recyclerViewAdapter = new RecyclerViewAdapter(this, storeArrayList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MaterialDividerItemDecoration divider = new MaterialDividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        divider.setDividerInsetStart(32);
        divider.setDividerInsetEnd(32);
        recyclerView.addItemDecoration(divider);
        recyclerView.setAdapter(recyclerViewAdapter);
        new FastScrollerBuilder(recyclerView).useMd2Style().build();

        fab.setOnClickListener(view -> {
            ThemeBottomSheet themeBottomSheet = new ThemeBottomSheet();
            themeBottomSheet.show(getSupportFragmentManager(), "ThemeSwitcher");
        });

        settings.setOnClickListener(view -> {
            SettingsBottomSheet settingsBottomSheet = new SettingsBottomSheet();
            settingsBottomSheet.show(getSupportFragmentManager(), "Settings");
        });


    }

    public void restartToApply(long delay) {
        if (recyclerView != null)
            new Handler().postDelayed(() -> {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                    finish();
                }
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    finish();
                }
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }, delay);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void recreateRecyclerView() {
        if (recyclerViewAdapter != null) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            if (layoutManager != null) {
                int firstVisible = layoutManager.findFirstVisibleItemPosition();
                int lastVisible = layoutManager.findLastVisibleItemPosition();
                int itemsChanged = lastVisible - firstVisible + 1;
                recyclerViewAdapter.notifyItemRangeChanged(firstVisible, itemsChanged);
            }
        }
    }

}