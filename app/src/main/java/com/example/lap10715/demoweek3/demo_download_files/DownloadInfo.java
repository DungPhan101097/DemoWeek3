package com.example.lap10715.demoweek3.demo_download_files;

import android.os.Environment;

import java.io.File;

public class DownloadInfo {
    private String internetPath;
    private String fileName;
    private File downloadedFile;


    public DownloadInfo(FileDownload fileDownload) {
        fileName = fileDownload.getFileName();
        internetPath = fileDownload.getUrl();

        downloadedFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                fileName);
    }

    public File getDownloadedFile() {
        return downloadedFile;
    }

    public void setDownloadedFile(File downloadedFile) {
        this.downloadedFile = downloadedFile;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getInternetPath() {
        return internetPath;
    }

    public void setInternetPath(String internetPath) {
        this.internetPath = internetPath;
    }
}
