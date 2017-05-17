package com.androrz.lopnor.elements;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androrz.lopnor.R;

import java.io.File;

public class He extends AppCompatActivity {

    private File file;
    private File jsonFile;
    public static final int EXTERNAL_STORAGE_REQ_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_he);
        requestPermission();
    }

    private void createfile() {
        String filePath = null;
        String jsonfilePath = null;
        boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (hasSDCard) { // SD root
            filePath = Environment.getExternalStorageDirectory().toString() + File.separator + "position.txt";
            jsonfilePath = Environment.getExternalStorageDirectory().toString() + File.separator + "locations.txt";
        } else {
            filePath = Environment.getDownloadCacheDirectory().toString() + File.separator + "position.txt";
            jsonfilePath = Environment.getDownloadCacheDirectory().toString() + File.separator + "locations.txt";
        }
        Log.i("AAA", filePath + "\n" + jsonfilePath);
        try {
            file = new File(filePath);
            jsonFile = new File(jsonfilePath);
            if (!file.exists()) {
                new File(file.getParent()).mkdirs();
                file.createNewFile();
            }
            if (!jsonFile.exists()) {
                jsonFile.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doAction(View view) {
        createfile();
    }

    public void requestPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "缓存定位信息需要SD卡读取权限", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    EXTERNAL_STORAGE_REQ_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case EXTERNAL_STORAGE_REQ_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createfile();
                } else {

                }
                return;
            }
        }
    }
}
