package com.example.lap10715.demoweek3.demo_download_files;

import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lap10715.demoweek3.R;

import io.reactivex.functions.Consumer;


public class ObserverUpdateProgressDownload implements Consumer<Integer> {
    private Button btnDownload;
    private ProgressBar pbProgress;

    public ObserverUpdateProgressDownload(Button btnDownload,
                                          ProgressBar pbProgress) {
        this.btnDownload = btnDownload;
        this.pbProgress = pbProgress;
    }

    @Override
    public void accept(Integer integer) throws Exception {
        pbProgress.setProgress(integer);
        btnDownload.setBackgroundResource(R.drawable.download_btn_doing);
    }

}
