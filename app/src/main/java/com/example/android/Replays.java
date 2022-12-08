package com.example.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

public class Replays extends AppCompatActivity {

    public static final String FILE_NAME = "file_name";

    private Toolbar myToolbar;
    private ListView listView;
    private ArrayAdapter<String> adapter;

    private String[] titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.replays);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.inflateMenu(R.menu.menu);
        listView = findViewById(R.id.replays_list);

        File dir = getExternalFilesDir(null);
        File[] files = dir.listFiles();

        // Sort files by date
        if (files.length > 0) Arrays.sort(files, Comparator.comparingLong(File::lastModified));
        String[] titles = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            titles[i] = files[i].getName().replaceFirst("[.][^.]+$", "");
        }

        adapter = new ArrayAdapter<>(this, R.layout.replay_file, titles);
        listView.setAdapter(adapter);

        myToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (titles.length == 0) return false;
                if (item.getItemId() == R.id.date) {

                    for (int i = 0; i < files.length; i++) {
                        titles[i] = files[i].getName();
                    }
                }
                else if (item.getItemId() == R.id.title) {
                    System.out.println(R.id.date);
                    Arrays.sort(titles, String.CASE_INSENSITIVE_ORDER);
                }

                updateAdapter(titles);
                return false;
            }
        });

        listView.setOnItemClickListener((list, text, pos, id) -> playReplay(titles, pos));
    }

    public void updateAdapter(String[] titles) {
        adapter = new ArrayAdapter<>(this, R.layout.replay_file, titles);
        listView.setAdapter(adapter);
    }

    public void playReplay(String[] titles, int pos) {
        Bundle bundle = new Bundle();
        bundle.putString(FILE_NAME,titles[pos] + ".txt");
        Intent intent = new Intent(this, ReplayBoard.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}