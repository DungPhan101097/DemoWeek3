package com.example.lap10715.demoweek3.demo_download_files;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lap10715.demoweek3.R;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DownloadFilesActivity extends AppCompatActivity {

    private Button btnDownFile1, btnDownFile2, btnDownFile3;
    private TextView tvFileName1, tvFileName2, tvFileName3,
            tvDisplayUrl1, tvDisplayUrl2, tvDisplayUrl3;
    private ProgressBar pbFile1, pbFile2, pbFile3;
    private ImageButton iBtnStopFile1, iBtnStopFile2, iBtnStopFile3;
    private boolean isChecked = false;


    private FileDownload[] fileDownloads = {
            new FileDownload("https://drive.google.com/uc?export=download&id=0B1rVEnAlVmVvWGxQNmxGRFBXSEU", "icon_android_java.jpg"),
            new FileDownload("https://drive.google.com/uc?export=download&id=0B1rVEnAlVmVvRndfV3RNN1pvOVU", "application_life_cycle.pdf"),
            new FileDownload("https://drive.google.com/uc?export=download&id=0B1rVEnAlVmVvRndfV3RNN1pvOVU", "database_android.pdf")};

    private List<DownloadInfo> downloadInfos = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_files);

        initViews();
        initDownloadInfo();

        final Disposable[] disposable1 = {null};
        final Disposable[] disposable2 = {null};
        final Disposable[] disposable3 = {null};

        btnDownFile1.setOnClickListener(v -> {
            initStartDoing(btnDownFile1, pbFile1, tvDisplayUrl1, iBtnStopFile1);

            disposable1[0] =
                    downloadFile(btnDownFile1, pbFile1, tvDisplayUrl1, downloadInfos.get(0));
        });


        btnDownFile2.setOnClickListener((View v) -> {
            initStartDoing(btnDownFile2, pbFile2, tvDisplayUrl2, iBtnStopFile2);

            disposable2[0] =
                    downloadFile(btnDownFile2, pbFile2, tvDisplayUrl2, downloadInfos.get(1));
        });

        btnDownFile3.setOnClickListener(v -> {
            initStartDoing(btnDownFile3, pbFile3, tvDisplayUrl3, iBtnStopFile3);

            disposable3[0] =
                    downloadFile(btnDownFile3, pbFile3, tvDisplayUrl3, downloadInfos.get(2));
        });

        iBtnStopFile1.setOnClickListener(v -> {
            if (disposable1[0] != null) {
                iBtnStopFile1.setImageResource(R.drawable.icon_delete_pressed);
                disposable1[0].dispose();
            }
        });

        iBtnStopFile2.setOnClickListener(v -> {
            if (disposable2[0] != null) {
                iBtnStopFile2.setImageResource(R.drawable.icon_delete_pressed);
                disposable2[0].dispose();
            }

        });

        iBtnStopFile3.setOnClickListener(v -> {

            if (disposable3[0] != null) {
                iBtnStopFile3.setImageResource(R.drawable.icon_delete_pressed);
                disposable3[0].dispose();
            }
        });
    }

    private Disposable downloadFile(Button btnDownFile, ProgressBar pbFile,
                                    TextView tvDisplayUrl, DownloadInfo curDownloadInfo) {
        return Observable.create(new ObservableDownloadFile(curDownloadInfo))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverUpdateProgressDownload(btnDownFile, pbFile),
                        Throwable::printStackTrace,
                        () -> {
                            tvDisplayUrl.setText("Your file in: " + curDownloadInfo
                                    .getDownloadedFile().getAbsolutePath());
                            btnDownFile.setBackgroundResource(R.drawable.download_btn_finish);
                        });
    }

    private void initStartDoing(Button btnDownFile, ProgressBar pbFile,TextView tvDisplayUrl,
                                ImageButton btnStop) {
        btnDownFile.setBackgroundResource(R.drawable.download_btn_idle);
        pbFile.setProgress(0);
        btnStop.setImageResource(R.drawable.icon_delete_normal);
        tvDisplayUrl.setText("");
    }

    private void initDownloadInfo() {
        for (FileDownload elem : fileDownloads) {
            downloadInfos.add(new DownloadInfo(elem));
        }
    }

    private void initViews() {
        btnDownFile1 = findViewById(R.id.btn_downfile_1);
        btnDownFile2 = findViewById(R.id.btn_downfile_2);
        btnDownFile3 = findViewById(R.id.btn_downfile_3);

        tvFileName1 = findViewById(R.id.tv_file_name_1);
        tvFileName2 = findViewById(R.id.tv_file_name_2);
        tvFileName3 = findViewById(R.id.tv_file_name_3);

        tvDisplayUrl1 = findViewById(R.id.tv_display_url_1);
        tvDisplayUrl2 = findViewById(R.id.tv_display_url_2);
        tvDisplayUrl3 = findViewById(R.id.tv_display_url_3);

        iBtnStopFile1 = findViewById(R.id.ibtn_stop_file_1);
        iBtnStopFile2 = findViewById(R.id.ibtn_stop_file_2);
        iBtnStopFile3 = findViewById(R.id.ibtn_stop_file_3);

        pbFile1 = findViewById(R.id.pb_progress_file_1);
        pbFile2 = findViewById(R.id.pb_progress_file_2);
        pbFile3 = findViewById(R.id.pb_progress_file_3);

        tvFileName1.setText(fileDownloads[0].getFileName());
        tvFileName2.setText(fileDownloads[1].getFileName());
        tvFileName3.setText(fileDownloads[2].getFileName());
    }
}
