package com.example.myapplication2.app;

import android.os.AsyncTask;

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
public class RepsAsyncTask extends AsyncTask <String, Integer, Boolean>{

    RepsCallbackInterface repsCallbackInterface;

    public RepsAsyncTask(RepsCallbackInterface repsCallbackInterface) {
        this.repsCallbackInterface = repsCallbackInterface;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = null;
        try {
            response = httpclient.execute(new HttpGet("http://whoismyrepresentative.com/getall_mems.php?zip=84020"));
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                return true;
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean didReceiveResponse) {
        repsCallbackInterface.onRepsAsyncReceived(didReceiveResponse);
    }
}
