package com.example.elevator_app.httpsrequest;

import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class HTTPSRequest {

    public static String pullJSONFromHTTPSRequest(String urlString) {
        URL urlStations;
        try{
            urlStations = new URL(urlString);
        } catch (MalformedURLException e){
            return "";
        }

        final StringBuilder sb = new StringBuilder();

        Thread thread = new Thread() {
            public void run() {
                try {
                    Scanner scan = new Scanner(urlStations.openStream());
                    Log.d("1", "1");
                    while (scan.hasNext()) sb.append(scan.nextLine());
                    Log.d("JSON", sb.toString());
                    scan.close();
                } catch (IOException e) {
                    sb.append("");
                }
            }
        };

        thread.start();
        try {
            thread.join(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}

