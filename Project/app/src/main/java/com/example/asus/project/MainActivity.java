package com.example.asus.project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private ImageAdapter myImageAdapter;
    private File infoFile = null;
    private String infoFileName = "info.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                infoFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), infoFileName);
                if (!infoFile.exists()) {
                    //File does not exists
                    infoFile.createNewFile();
                }
            }
        }
        catch (IOException e){
            //Never catch.
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddingActivity.class));
            }
        });

        GridView gridview = (GridView) findViewById(R.id.gridview);
        myImageAdapter = new ImageAdapter(this);
        gridview.setAdapter(myImageAdapter);
        String externalStorageDirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        Toast.makeText(getApplicationContext(), externalStorageDirectoryPath, Toast.LENGTH_LONG).show();
//        File targetDirector = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File[] files = targetDirector.listFiles();
//        for (File file : files){
//            myImageAdapter.add(file.getAbsolutePath());
//        }
        String[] paths = null;
        String cur = "";
        try {
            InfoFileReader infoFileReader = new InfoFileReader(infoFile);
            infoFileReader.openReadFile();
            int i = 0;
            while (!(cur = infoFileReader.startReading(i)).equals("")) {
                paths = cur.split(" ");
                File imageFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), paths[0]);
                myImageAdapter.add(imageFile.getAbsolutePath());
                i++;
            }
            infoFileReader.closeReadFile();
        }
        catch (IOException e){
        }
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                startActivity(new Intent(MainActivity.this, ImageActivity.class).putExtra("position",Integer.toString(position)));
            }
        });
    }
}
