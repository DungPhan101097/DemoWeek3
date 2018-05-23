package com.example.lap10715.demoweek3.demo_download_files;

import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ObservableDownloadFile implements ObservableOnSubscribe<Integer> {

    private DownloadInfo fileDownload;
    private File myDownloadFile;

    public ObservableDownloadFile(DownloadInfo fileDownload) {
        this.fileDownload = fileDownload;
        myDownloadFile = fileDownload.getDownloadedFile();
    }

    @Override
    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {

        DownloadInfo myFile = fileDownload;

        URLConnection connection = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        byte[] buffer = new byte[1024];

        try {
            connection = new URL(myFile.getInternetPath()).openConnection();
            inputStream = connection.getInputStream();

            int fileLen = connection.getContentLength();

            outputStream = new BufferedOutputStream(new FileOutputStream(myDownloadFile));

            int numRead = -1;
            int accummulate = 0;
            while ((numRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, numRead);
                accummulate += numRead;
                emitter.onNext(Math.round(accummulate * 100 *1.0f/ fileLen));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //emitter.onError(new Throwable());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            //emitter.onError(new Throwable());

        } catch (IOException e) {
            e.printStackTrace();
            //emitter.onError(new Throwable());

        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        emitter.onComplete();
    }
}
