package com.example.asus.project;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class ImageActivity extends Activity{
    private ImageView imageView = null;
    private TextView textView = null;
    private File infoFile = null;
    private String infoFileName = "info.txt";
    private String mCurrentPhotoPath = null;
    public static final int LOCATION_POSITION = 0;
    public static final int ADDRESS_POSITION = 1;
    public static final int NAME_POSITION = 2;
    public static final int DESCRIPTION_POSITION = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

       String s = getIntent().getStringExtra("position");

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

        textView = (TextView) findViewById(R.id.Description);
        imageView = (ImageView) findViewById(R.id.photo);

        final int pos = Integer.parseInt(s);
        String[] paths = null;
        try{
            InfoFileReader infoFileReader = new InfoFileReader(infoFile);
            infoFileReader.openReadFile();
            String information = infoFileReader.startReading(pos);
            paths = information.split(" ");
            infoFileReader.closeReadFile();
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
        File imageFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), paths[0]);
        File textFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), paths[1]);
        String desc = "";
        String locationDescription = "";
        try{
            InfoFileReader fileReader = new InfoFileReader(textFile);
            fileReader.openReadFile();
            locationDescription = fileReader.startReading(LOCATION_POSITION) + '\n' + fileReader.startReading(ADDRESS_POSITION) +
                    '\n' + fileReader.startReading(NAME_POSITION);
            desc = fileReader.startReading(DESCRIPTION_POSITION);
            fileReader.closeReadFile();
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
        textView.setText(desc);
        mCurrentPhotoPath = imageFile.getAbsolutePath();
        setPic();

        final String stringFromFile = locationDescription;
        FloatingActionButton showOnMap = (FloatingActionButton) findViewById(R.id.showOnMap);
        showOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Take data about item and write in stringFromFile
                Intent myintent = new Intent(ImageActivity.this, MapsActivity.class).putExtra("item description", stringFromFile);
                startActivity(myintent);
            }
        });
        final FloatingActionButton delete = (FloatingActionButton) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            //boolean flag = true;
            @Override
            public void onClick(View view) {
                updateByDeleteString(pos);
                startActivity(new Intent(ImageActivity.this, MainActivity.class));
            }
        });
    }
    private void updateByDeleteString(int pos){
        File newInfoFile = null;
        String newFileName = "newInfo.txt";
        try {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                newInfoFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), newFileName);
                if (!newInfoFile.exists()) {
                    //File does not exists
                    newInfoFile.createNewFile();
                }
            }
            BufferedReader reader = new BufferedReader(new FileReader(infoFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(newInfoFile));
            String currentLine;
            int curNum = 0;
            while((currentLine = reader.readLine()) != null) {
                // trim newline when comparing with lineToRemove
                if(curNum == pos){
                    curNum++;
                    continue;
                }
                writer.write(currentLine + System.getProperty("line.separator"));
                curNum++;
            }
            writer.close();
            reader.close();
            newInfoFile.renameTo(infoFile);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void setPic() {
        // Get the dimensions of the View
//        int targetW = imageView.getWidth();
//        int targetH = imageView.getHeight();
        int targetW = 200;
        int targetH = 200;
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

        int degree = 90;
        Matrix mat = new Matrix();
        mat.postRotate(degree);
        Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, 0,bitmap.getWidth(),bitmap.getHeight(), mat, true);

        imageView.setImageBitmap(bitmap1);
    }
}
