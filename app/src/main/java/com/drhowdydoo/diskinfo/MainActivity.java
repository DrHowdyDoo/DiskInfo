package com.drhowdydoo.diskinfo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.StatFs;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.color.DynamicColors;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.play.core.splitcompat.SplitCompat;
import com.google.android.play.core.splitinstall.SplitInstallManager;
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory;
import com.google.android.play.core.splitinstall.SplitInstallRequest;
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

@SuppressWarnings("FieldCanBeLocal")
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TEST";
    private RecyclerView recyclerView;
    private MaterialToolbar materialToolbar;
    private CoordinatorLayout coordinatorLayout;
    private FloatingActionButton fab;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton settings;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<Object> storeArrayList, basicPartition, advancePartition, backupList;
    private boolean expanded;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String _partition, _basic_partitions, _data, _cache, _cached, _swap, _swap_cached, _ram, _system, _memory, _sdcard_internal, _sdcard_portable, _otg;
    private int unit_flag, unit;
    private Map<String, Long> map;
    private RandomAccessFile reader;
    private BroadcastReceiver mUsbReceiver;
    private LayoutAnimationController animation;
    private TextInputLayout searchView;
    private TextInputEditText searchField;
    private LinearLayout not_found;
    private SplitInstallManager splitInstallManager;
    private Set<String> installedLangs;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        setLocale(this, sharedPref.getString("DiskInfo.Language", Locale.getDefault().getLanguage()));

        int mode = sharedPref.getInt("DiskInfo.MODE", -1);
        AppCompatDelegate.setDefaultNightMode(mode);

        switch (sharedPref.getString("DiskInfo.Theme", "purple")) {
            case "purple":
                if (sharedPref.getBoolean("amoledMode", false)) {
                    setTheme(R.style.Theme_DiskInfo_Purple_Amoled);
                } else {
                    setTheme(R.style.Theme_DiskInfo_Purple);
                }

                break;

            case "red":
                if (sharedPref.getBoolean("amoledMode", false))
                    setTheme(R.style.Theme_DiskInfo_Red_Amoled);
                else setTheme(R.style.Theme_DiskInfo_Red);
                break;

            case "yellow":
                if (sharedPref.getBoolean("amoledMode", false))
                    setTheme(R.style.Theme_DiskInfo_Yellow_Amoled);
                else setTheme(R.style.Theme_DiskInfo_Yellow);
                break;

            case "green":
                if (sharedPref.getBoolean("amoledMode", false))
                    setTheme(R.style.Theme_DiskInfo_Green_Amoled);
                else setTheme(R.style.Theme_DiskInfo_Green);
                break;

            case "orange":
                if (sharedPref.getBoolean("amoledMode", false))
                    setTheme(R.style.Theme_DiskInfo_Orange_Amoled);
                else setTheme(R.style.Theme_DiskInfo_Orange);
                break;

            case "pink":
                if (sharedPref.getBoolean("amoledMode", false))
                    setTheme(R.style.Theme_DiskInfo_Pink_Amoled);
                else setTheme(R.style.Theme_DiskInfo_Pink);
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
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        coordinatorLayout = findViewById(R.id.coordinator_layout);
        searchView = findViewById(R.id.searchView);
        searchField = findViewById(R.id.searchField);
        not_found = findViewById(R.id.not_found);

        splitInstallManager = SplitInstallManagerFactory.create(this);
        installedLangs = splitInstallManager.getInstalledLanguages();

        if (!installedLangs.contains(sharedPref.getString("DiskInfo.Language", Locale.getDefault().getLanguage()))) {
            editor.putString("DiskInfo.Language", Locale.getDefault().getLanguage());
        }

        int progressBackgroundColor = MaterialColors.getColor(this, com.google.android.material.R.attr.colorBackgroundFloating, Color.WHITE);
        int progressIndicatorColor = MaterialColors.getColor(this, com.google.android.material.R.attr.colorPrimary, Color.BLACK);

        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(progressBackgroundColor);
        swipeRefreshLayout.setColorSchemeColors(progressIndicatorColor);

        if (sharedPref.getBoolean("advanceMode", false)) {
            searchView.setVisibility(View.VISIBLE);
        } else {
            searchView.setVisibility(View.GONE);
        }

        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    if (s.toString().toLowerCase().startsWith("size:"))
                        searchView.setHelperText("Filter by size");
                    if (s.toString().toLowerCase().startsWith("fs:"))
                        searchView.setHelperText("Filter by filesystem");
                    if (s.toString().toLowerCase().startsWith("free:"))
                        searchView.setHelperText("Filter by free space");
                    if (s.toString().toLowerCase().startsWith("used:"))
                        searchView.setHelperText("Filter by used space");
                    if (s.toString().toLowerCase().startsWith("accesstype:"))
                        searchView.setHelperText("Filter by access type");
                } else {
                    searchView.setHelperText("");
                }
                filter(s.toString());

            }
        });


        if (sharedPref.getBoolean("useSI", false)) {
            unit_flag = FormatterX.FLAG_SI_UNITS;
            unit = 1000;
        } else {
            unit_flag = FormatterX.FLAG_IEC_UNITS;
            unit = 1024;
        }


//        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
//        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//        activityManager.getMemoryInfo(mi);
//
//        Log.d(TAG, "onCreate: mem avail : " + mi.availMem + " \memTotal : " + mi.totalMem);

        storeArrayList = new ArrayList<>();
        basicPartition = new ArrayList<>();
        advancePartition = new ArrayList<>();
        map = new HashMap<>();


        _partition = getString(R.string.partitions);
        _basic_partitions = getString(R.string.basic_partitions);
        _data = getString(R.string.data);
        _cache = getString(R.string.cache);
        _cached = getString(R.string.cached);
        _swap = getString(R.string.swap);
        _swap_cached = getString(R.string.swap_cached);
        _ram = getString(R.string.ram);
        _system = getString(R.string.system);
        _memory = getString(R.string.memory);
        _sdcard_internal = getString(R.string.sdcard_internal);
        _sdcard_portable = getString(R.string.sdcard_portable);
        _otg = getString(R.string.otg);


        getList();
        setList();
        addMemoryDetails();
        setView();

        int resId = R.anim.layout_animation_fall_down;
        animation = AnimationUtils.loadLayoutAnimation(this, resId);


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

        SwipeRefreshLayout.OnRefreshListener onRefreshListener = () -> new Thread(() -> {
            storeArrayList.clear();
            advancePartition.clear();
            basicPartition.clear();
            map.clear();

            getList();
            setList();
            addMemoryDetails();
            runOnUiThread(() -> {
                setView();
                recyclerView.setLayoutAnimation(animation);
                swipeRefreshLayout.setRefreshing(false);
                Objects.requireNonNull(searchField.getText()).clear();
                searchField.clearFocus();
            });
        }).start();
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);

        fab.setOnClickListener(view -> {
            ThemeBottomSheet themeBottomSheet = new ThemeBottomSheet();
            themeBottomSheet.show(getSupportFragmentManager(), "ThemeSwitcher");
        });

        settings.setOnClickListener(view -> {
            SettingsBottomSheet settingsBottomSheet = new SettingsBottomSheet();
            settingsBottomSheet.show(getSupportFragmentManager(), "Settings");
        });

        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> expanded = verticalOffset == 0);

        mUsbReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equalsIgnoreCase("android.hardware.usb.action.USB_DEVICE_ATTACHED")) {

                    UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                    if (device != null) {
                        _otg = device.getManufacturerName() + " " + device.getProductName();
                    }


                    Snackbar.make(coordinatorLayout, "OTG Detected", Snackbar.LENGTH_INDEFINITE).setAction("Refresh", v -> {
                        onRefreshListener.onRefresh();
                    }).show();


                } else if (action.equalsIgnoreCase("android.hardware.usb.action.USB_DEVICE_DETACHED")) {
                    Snackbar.make(coordinatorLayout, "OTG Disconnected", Snackbar.LENGTH_SHORT).show();
                    onRefreshListener.onRefresh();
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
        filter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
        registerReceiver(mUsbReceiver, filter);

    }

    private void filter(String text) {
        if (text.isEmpty()) {
            not_found.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerViewAdapter.updateList(backupList);
        } else {
            ArrayList<Object> temp = new ArrayList<>();
            int searchFx = sharedPref.getInt("DiskInfo.SearchFunction", 1);
            for (Object d : storeArrayList) {
                if (d instanceof DataStore) {
                    if (text.contains(":")) {
                        String[] searchText = text.split(":");
                        if (searchText.length > 1) {
                            if (searchFx == 0) {
                                if (searchWith(searchText[0], (DataStore) d).startsWith(searchText[1]))
                                    temp.add(d);
                            } else if (searchFx == 1) {
                                if (searchWith(searchText[0], (DataStore) d).contains(searchText[1]))
                                    temp.add(d);
                            } else {
                                if (searchWith(searchText[0], (DataStore) d).equalsIgnoreCase(searchText[1]))
                                    temp.add(d);
                            }

                        }
                    } else {
                        if (searchFx == 0) {
                            if (((DataStore) d).getMount_name().startsWith(text)) temp.add(d);
                        } else if (searchFx == 1)
                            if (((DataStore) d).getMount_name().contains(text)) temp.add(d);
                            else {
                                if (((DataStore) d).getMount_name().equalsIgnoreCase(text))
                                    temp.add(d);
                            }

                    }
                }
            }
            if (temp.isEmpty()) {
                not_found.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                not_found.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
            recyclerViewAdapter.updateList(temp);

        }
    }

    private String searchWith(String text, DataStore dataStore) {

        switch (text.toLowerCase()) {

            case "size":
                return dataStore.getTotalSize();

            case "fs":
                return dataStore.getFileSystem();

            case "accesstype":
                if (dataStore.isAccess_type()) return "r";
                else return "r/w";

            case "block":
                return dataStore.getBlockSize();

            case "free":
                return dataStore.getFreeSize();

            case "used":
                return dataStore.getUsedSize();
        }
        return "";
    }

    private void getList() {

        DataStore cacheStore = null, rootStore = null, systemStore = null, sdStore = null, sdExtStore = null, otgStore = null;

        advancePartition.add(_partition);

        FileSystem filesystem = FileSystems.getDefault();
        for (FileStore store : filesystem.getFileStores()) {
            try {
                long totalSpace = store.getTotalSpace();
                long unusedSpace = store.getUnallocatedSpace();
                long usedSpace = totalSpace - unusedSpace;
                String totalSize = FormatterX.formatFileSize(this, totalSpace, unit_flag);
                String usedSize = FormatterX.formatFileSize(this, usedSpace, unit_flag);
                String freeSize = FormatterX.formatFileSize(this, unusedSpace, unit_flag);

                int progress = Util.getUsedSpace(totalSpace, usedSpace);

                String fileStorePath = store.toString().replaceFirst(" .*", "");
                StatFs statFs = new StatFs(fileStorePath);
                long blockSpace = statFs.getBlockSizeLong();
                String blockSize = FormatterX.formatFileSize(this, blockSpace, unit_flag);
                DataStore dataStore = new DataStore(store.toString(), store.type(), totalSize, usedSize, freeSize, blockSize, store.isReadOnly(), totalSpace, unusedSpace, usedSpace, blockSpace, progress);
                advancePartition.add(dataStore);
                if (store.toString().startsWith("/cache")) {
                    cacheStore = new DataStore(dataStore);
                }
                if (store.toString().startsWith("/data ")) {
                    rootStore = new DataStore(dataStore);
                }
                if (store.toString().startsWith("/system ")) {
                    systemStore = new DataStore(dataStore);
                }
                if (store.toString().startsWith("/mnt/expand/")) {
                    sdStore = new DataStore(dataStore);
                }
                if (store.toString().contains("/mnt/media_rw/")) {
                    sdExtStore = new DataStore(dataStore);
                }
                if (store.toString().contains("/dev/fuse") && !store.toString().startsWith("/storage/emulated")) {
                    otgStore = new DataStore(dataStore);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int totalPartitions = advancePartition.size() - 1;
        String titleWithCount = _partition + " (" + totalPartitions + ")";
        advancePartition.set(0, titleWithCount);

        basicPartition.add(_basic_partitions);

        if (rootStore != null) {
            rootStore.setMount_name(_data);
            basicPartition.add(rootStore);
        }

        if (systemStore != null) {
            systemStore.setMount_name(_system);
            basicPartition.add(systemStore);
        }

        if (cacheStore != null) {
            cacheStore.setMount_name(_cache);
            basicPartition.add(cacheStore);
        }

        if (sdStore != null) {
            sdStore.setMount_name(_sdcard_internal);
            basicPartition.add(sdStore);
        }

        if (sdExtStore != null) {
            sdExtStore.setMount_name(_sdcard_portable);
            basicPartition.add(sdExtStore);
        }

        if (otgStore != null) {
            otgStore.setMount_name(_otg);
            basicPartition.add(otgStore);
        }
    }

    private void setList() {
        if (sharedPref.getBoolean("advanceMode", false)) {
            storeArrayList = advancePartition;
        } else {
            storeArrayList = basicPartition;
        }
        backupList = storeArrayList;
    }

    private void addMemoryDetails() {

        storeArrayList.add(_memory);
        String line;

        try {
            reader = new RandomAccessFile("/proc/meminfo", "r");
            while ((line = reader.readLine()) != null) {
                String[] entry = line.split(":");
                map.put(entry[0], Long.parseLong(entry[1].substring(0, entry[1].length() - 2).trim()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {

            long totalMem = 0, availMem = 0, usedMem;
            long cahced = 0;


            if (map.containsKey("MemTotal")) {
                totalMem = map.get("MemTotal") * unit;
            }
            if (map.containsKey("MemAvailable")) {
                availMem = map.get("MemAvailable") * unit;
            }
            if (map.containsKey("Cached")) {
                cahced = map.get("Cached") * unit;
            }


            String cache = _cached + " : " + FormatterX.formatFileSize(this, cahced, unit_flag);


            usedMem = totalMem - availMem;
            int memTrack = totalMem != 0 ? (int) (((double) usedMem / totalMem) * 100) : 0;
            String _memory_ram = _memory + " " + "(" + _ram + ")";
            MemInfo memInfo = new MemInfo(_memory_ram, FormatterX.formatFileSize(this, totalMem, unit_flag), FormatterX.formatFileSize(this, availMem, unit_flag), FormatterX.formatFileSize(this, usedMem, unit_flag), cache, memTrack);
            storeArrayList.add(memInfo);


            long totalSwap = 0, availSwap = 0, usedSwap, swapCached = 0;

            if (map.containsKey("SwapTotal")) {
                totalSwap = map.get("SwapTotal") * unit;
            }
            if (map.containsKey("SwapFree")) {
                availSwap = map.get("SwapFree") * unit;
            }

            if (map.containsKey("SwapCached")) {
                swapCached = map.get("SwapCached") * unit;
            }

            String swapCache = _swap_cached + " : " + FormatterX.formatFileSize(this, swapCached, unit_flag);
            usedSwap = totalSwap - availSwap;
            int swapTrack = totalSwap != 0 ? (int) (((double) usedSwap / totalSwap) * 100) : 0;
            String _swap_zram = _swap;
            MemInfo swapInfo = new MemInfo(_swap_zram, FormatterX.formatFileSize(this, totalSwap, unit_flag), FormatterX.formatFileSize(this, availSwap, unit_flag), FormatterX.formatFileSize(this, usedSwap, unit_flag), swapCache, swapTrack);
            storeArrayList.add(swapInfo);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setView() {
        recyclerViewAdapter = new RecyclerViewAdapter(this, storeArrayList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerViewAdapter);
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

    public static void setLocale(Activity activity, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    public void downloadRes() {
        String languageCode = sharedPref.getString("DiskInfo.Language", Locale.getDefault().getLanguage());
        if (!installedLangs.contains(languageCode)) {
            String downloadMsg = new Locale(languageCode).getDisplayLanguage() + " language is being downloaded";
            Toast.makeText(this, downloadMsg, Toast.LENGTH_SHORT).show();
            SplitInstallRequest request =
                    SplitInstallRequest.newBuilder()
                            .addLanguage(Locale.forLanguageTag(sharedPref.getString("DiskInfo.Language", Locale.getDefault().getLanguage())))
                            .build();
            splitInstallManager.startInstall(request);
            splitInstallManager.registerListener(splitInstallSessionState -> {
                if (splitInstallSessionState.status() == SplitInstallSessionStatus.INSTALLED) {
                    String msg = new Locale(languageCode).getDisplayLanguage() + " language is successfully downloaded";
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    Handler handler = new Handler();
                    handler.postDelayed(this::recreate, 2500);
                }
            });
        }
    }

    @Override
    public void recreate() {
        restartToApply(0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        unregisterReceiver(mUsbReceiver);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        SplitCompat.install(this);
    }
}