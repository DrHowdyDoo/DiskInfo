package com.drhowdydoo.diskinfo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.divider.MaterialDividerItemDecoration;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MaterialToolbar materialToolbar;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        materialToolbar = findViewById(R.id.materialToolBar);
        fab = findViewById(R.id.fab_theme);

        String version = "v" + BuildConfig.VERSION_NAME;
        materialToolbar.setSubtitle(version);

        ArrayList<DataStore> storeArrayList = new ArrayList<>();

        FileSystem filesystem = FileSystems.getDefault();
        for (FileStore store : filesystem.getFileStores()) {
            try {
                long totalSpace = store.getTotalSpace();
                long unusedSpace = store.getUnallocatedSpace();
                long usedSpace = totalSpace - unusedSpace;
                DataStore dataStore = new DataStore(store.toString(), store.type(), store.isReadOnly(), totalSpace, unusedSpace, usedSpace);
                storeArrayList.add(dataStore);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this, storeArrayList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MaterialDividerItemDecoration divider = new MaterialDividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        divider.setDividerInsetStart(32);
        divider.setDividerInsetEnd(32);
        recyclerView.addItemDecoration(divider);
        recyclerView.setAdapter(recyclerViewAdapter);

        fab.setOnClickListener(view -> {
            BottomSheet bottomSheet = new BottomSheet(this);
            bottomSheet.show(getSupportFragmentManager(), "ThemeSwitcher");
        });

    }

}