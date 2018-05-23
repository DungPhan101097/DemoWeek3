package com.example.lap10715.demoweek3;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.lap10715.demoweek3.demo_download_files.DownloadFilesActivity;
import com.example.lap10715.demoweek3.demo_live_facebook.FbLiveReactionActivity;


public class MainActivity extends AppCompatActivity {

    private Button btnStartFbLive;
    private Button btnStartDownloadFile;

    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "Please provide permission!", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
            }
        } else {
            initViews();
        }



    }

    private void initViews() {
        btnStartFbLive = findViewById(R.id.btn_live_fb);
        btnStartDownloadFile = findViewById(R.id.btn_download_file);

        btnStartFbLive.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this,
                FbLiveReactionActivity.class)));

        btnStartDownloadFile.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this,
                DownloadFilesActivity.class)));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initViews();
                } else {
                    Toast.makeText(this, "Application can't access any data!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }

        }
    }
}
