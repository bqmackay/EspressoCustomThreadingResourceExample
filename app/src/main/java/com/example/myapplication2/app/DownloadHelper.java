package com.example.myapplication2.app;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by byronmackay on 4/25/14.
 */
public class DownloadHelper {

    private final RepsCallbackInterface repsCallbackInterface;

    public DownloadHelper(RepsCallbackInterface repsCallbackInterface) {
        this.repsCallbackInterface = repsCallbackInterface;
    }

    public void getReps() {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {

                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = null;
                try {
                    response = httpclient.execute(new HttpGet("http://whoismyrepresentative.com/getall_mems.php?zip=84020"));
                    StatusLine statusLine = response.getStatusLine();
                    if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        response.getEntity().writeTo(out);
                        out.close();
                        repsCallbackInterface.onRepsThreadReceived(true);
                    } else{
                        //Closes the connection.
                        response.getEntity().getContent().close();
                        repsCallbackInterface.onRepsThreadReceived(false);
                        throw new IOException(statusLine.getReasonPhrase());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(runnable).start();

    }

    public void getRepsWithAsyncTask() {
        RepsAsyncTask asyncTask = new RepsAsyncTask(repsCallbackInterface);
        asyncTask.execute("");
    }
}
