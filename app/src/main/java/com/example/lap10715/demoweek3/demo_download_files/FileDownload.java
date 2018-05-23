package com.example.lap10715.demoweek3.demo_download_files;

import java.io.Serializable;

public class FileDownload implements Serializable {
    public FileDownload(String url, String fileName) {
        this.url = url;
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private String url;
    private String fileName;

}
