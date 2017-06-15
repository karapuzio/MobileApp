package com.example.asus.project;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Dell on 18.05.2017.
 */

public class InfoFileWriter {
//    private String mFileName = null;
    private File outFile;
    private FileOutputStream fos;
    private OutputStreamWriter  osw;
    private BufferedWriter bw;

    public InfoFileWriter(File outFile){
        this.outFile = outFile;
    }

    public void OpenFile() throws IOException {
        try {
		/*Create and open necessary stream for writing information */
            fos = new FileOutputStream(outFile);
            osw = new OutputStreamWriter(fos);
            bw = new BufferedWriter(osw);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void CloseFile(){
        try {
            bw.close();
            osw.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    };

    public void startWriting(Object... arg) throws IOException{
        bw.write(arg[0].toString());
        bw.write("\n");
        bw.write(arg[1].toString());
        bw.write("\n");
        bw.write(arg[2].toString());
        bw.write("\n");
        bw.write(arg[3].toString());
    }
}
