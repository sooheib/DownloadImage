package com.vivianaranha.downloadimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    ImageView iv;
    Bitmap bmp = null;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = (ImageView) findViewById(R.id.imageView);

        handler = new Handler();

    }

    public void downloadImage(View view) {
        String url = "http://www.planwallpaper.com/static/images/Winter-Tiger-Wild-Cat-Images.jpg";
        //Create thread
        Thread myThread = new Thread(new DownloadImageThread(url));
        //Run thread
        myThread.start();

    }

    public class DownloadImageThread implements Runnable {

        private String url;
        public DownloadImageThread(String url){
            this.url = url;
        }

        @Override
        public void run() {
            URL downloadURL = null;
            HttpURLConnection conn = null;
            InputStream inputStream = null;


            try {
                downloadURL = new URL(url);
                conn = (HttpURLConnection) downloadURL.openConnection();
                inputStream = conn.getInputStream();
                bmp = BitmapFactory.decodeStream(inputStream);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
//Run in the main threa
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iv.setImageBitmap(bmp);
                    }
                });

//                MainActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        iv.setImageBitmap(bmp);
//                    }
//                });
            }

            if(conn !=null){
                conn.disconnect();
            }
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
