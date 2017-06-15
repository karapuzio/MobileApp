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

public class InfoFileReader {
    private File inFile;
    private FileInputStream fis;
    private InputStreamReader isr;
    private BufferedReader br;
    private int curNum = 0;
    private String cur;

    public InfoFileReader(File inFile){
        this.inFile = inFile;
    }

    public void openReadFile() throws IOException {
        try {
            /*Create and open necessary stream for reading information */
            fis = new FileInputStream(inFile);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void closeReadFile(){
        try {
            br.close();
            isr.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    };

    public String startReading(int numLine) throws IOException{
        String instr = "";
        while ((cur = br.readLine()) != null){
            if (numLine == curNum){
                break;
            }
            curNum++;
        }
        if (cur != null && curNum == numLine){
            instr = cur;
            curNum++;
        }
        return instr;
    }
}
