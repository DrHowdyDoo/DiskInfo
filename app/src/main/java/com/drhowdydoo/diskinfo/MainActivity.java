package com.drhowdydoo.diskinfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.StatFs;
import android.text.format.Formatter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.color.DynamicColors;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TEST";
    private RecyclerView recyclerView;
    private MaterialToolbar materialToolbar;
    private FloatingActionButton fab;
    private SharedPreferences sharedPref;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton settings;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<Object> storeArrayList, basicPartition, advancePartition;
    private boolean expanded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        int mode = sharedPref.getInt("DiskInfo.MODE", -1);
        AppCompatDelegate.setDefaultNightMode(mode);

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

            case "orange":
                setTheme(R.style.Theme_DiskInfo_Orange);
                break;

            case "pink":
                setTheme(R.style.Theme_DiskInfo_Pink);
                break;

            default:
                if (DynamicColors.isDynamicColorAvailable()) {
                    DynamicColors.applyIfAvailable(this);
                } else setTheme(R.style.Theme_DiskInfo_Purple);
                break;

        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.recyclerView);
        materialToolbar = findViewById(R.id.materialToolBar);
        appBarLayout = findViewById(R.id.appBar);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolBar);
        fab = findViewById(R.id.fab_theme);
        settings = findViewById(R.id.settings);

        storeArrayList = new ArrayList<>();
        basicPartition = new ArrayList<>();
        advancePartition = new ArrayList<>();

        DataStore cacheStore = null, rootStore = null;


        advancePartition.add("Partitions");

        FileSystem filesystem = FileSystems.getDefault();
        for (FileStore store : filesystem.getFileStores()) {
            try {
                long totalSpace = store.getTotalSpace();
                long unusedSpace = store.getUnallocatedSpace();
                long usedSpace = totalSpace - unusedSpace;
                String totalSize = Formatter.formatFileSize(this, totalSpace);
                String usedSize = Formatter.formatFileSize(this, usedSpace);
                String freeSize = Formatter.formatFileSize(this, unusedSpace);

                int progress = Util.getUsedSpace(totalSpace, usedSpace);

                String fileStorePath = store.toString().replaceFirst(" .*", "");
                StatFs statFs = new StatFs(fileStorePath);
                long blockSpace = statFs.getBlockSizeLong();
                String blockSize = Formatter.formatFileSize(this, blockSpace);
                DataStore dataStore = new DataStore(store.toString(), store.type(), totalSize, usedSize, freeSize, blockSize, store.isReadOnly(), totalSpace, unusedSpace, usedSpace, blockSpace, progress);
                advancePartition.add(dataStore);
                if (store.toString().startsWith("/cache")) {
                    cacheStore = dataStore;
                }
                if (store.toString().startsWith("/data ")) {
                    rootStore = dataStore;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int totalPartitions = advancePartition.size() - 1;
        String titleWithCount = "Partitions " + "(" + totalPartitions + ")";
        advancePartition.set(0, titleWithCount);

        basicPartition.add("Basic Partitions");

        if (rootStore != null && cacheStore != null) {
            rootStore.setMount_name("Data");
            cacheStore.setMount_name("Cache");
            basicPartition.add(rootStore);
            basicPartition.add(cacheStore);
        }

        if (sharedPref.getBoolean("advanceMode", false)) {
            storeArrayList = advancePartition;
        } else {
            storeArrayList = basicPartition;
        }

        storeArrayList.add("Memory");
        String line;
        Map<String, Long> map = new HashMap<>();

        try {
            RandomAccessFile reader = new RandomAccessFile("/proc/meminfo", "r");
            while ((line = reader.readLine()) != null) {
                String[] entry = line.split(":");
                map.put(entry[0], Long.parseLong(entry[1].substring(0, entry[1].length() - 2).trim()));
            }
            reader.close();

            long totalMem = 0, availMem = 0, usedMem;
            long cahced = 0;

            if (map.containsKey("MemTotal")) {
                totalMem = map.get("MemTotal") * 1000;
            }
            if (map.containsKey("MemAvailable")) {
                availMem = map.get("MemAvailable") * 1000;
            }
            if (map.containsKey("Cached")) {
                cahced = map.get("Cached") * 1000;
            }

            String cache = "Cached : " + Formatter.formatFileSize(this, cahced);

            usedMem = totalMem - availMem;
            int memTrack = totalMem != 0 ? (int) (((double) usedMem / totalMem) * 100) : 0;
            MemInfo memInfo = new MemInfo("Memory (RAM)", Formatter.formatFileSize(this, totalMem), Formatter.formatFileSize(this, availMem), Formatter.formatFileSize(this, usedMem), cache, memTrack);
            storeArrayList.add(memInfo);

            long totalSwap = 0, availSwap = 0, usedSwap, swapCached = 0;

            if (map.containsKey("SwapTotal")) {
                totalSwap = map.get("SwapTotal") * 1000;
            }
            if (map.containsKey("SwapFree")) {
                availSwap = map.get("SwapFree") * 1000;
            }

            if (map.containsKey("SwapCached")) {
                swapCached = map.get("SwapCached") * 1000;
            }

            String swapCache = "SwapCached : " + Formatter.formatFileSize(this, swapCached);
            usedSwap = totalSwap - availSwap;
            int swapTrack = totalSwap != 0 ? (int) (((double) usedSwap / totalSwap) * 100) : 0;
            MemInfo swapInfo = new MemInfo("Swap (ZRAM)", Formatter.formatFileSize(this, totalSwap), Formatter.formatFileSize(this, availSwap), Formatter.formatFileSize(this, usedSwap), swapCache, swapTrack);
            storeArrayList.add(swapInfo);


        } catch (Exception e) {
            e.printStackTrace();
        }


        recyclerViewAdapter = new RecyclerViewAdapter(this, storeArrayList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerViewAdapter);

        Intent intent = getIntent();
        if (intent != null) {
            Parcelable state = intent.getParcelableExtra("scroll_state");
            if (state != null) {
                Objects.requireNonNull(recyclerView.getLayoutManager()).onRestoreInstanceState(state);
            }
            if (intent.getBooleanExtra("theme_changed", false))
                appBarLayout.setExpanded(intent.getBooleanExtra("collapsing_toolbar_state", true));
        }
        new FastScrollerBuilder(recyclerView).useMd2Style().build();

        fab.setOnClickListener(view -> {
            ThemeBottomSheet themeBottomSheet = new ThemeBottomSheet();
            themeBottomSheet.show(getSupportFragmentManager(), "ThemeSwitcher");
        });

        settings.setOnClickListener(view -> {
            settings.animate().rotationBy(180f).setDuration(460).setStartDelay(0);
            SettingsBottomSheet settingsBottomSheet = new SettingsBottomSheet();
            settingsBottomSheet.show(getSupportFragmentManager(), "Settings");
        });

        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> expanded = verticalOffset == 0);

    }

    public void restartToApply(long delay) {

        Parcelable state;
        state = Objects.requireNonNull(recyclerView.getLayoutManager()).onSaveInstanceState();

        new Handler().postDelayed(() -> {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                finish();
            }
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("scroll_state", state);
            intent.putExtra("collapsing_toolbar_state", expanded);
            intent.putExtra("theme_changed", true);
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
                ((SimpleItemAnimator) Objects.requireNonNull(recyclerView.getItemAnimator())).setSupportsChangeAnimations(false);
                recyclerViewAdapter.notifyItemRangeChanged(firstVisible, itemsChanged);
            }
        }
    }

    public void advanceModeOn() {
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
        }, 0);

    }

}