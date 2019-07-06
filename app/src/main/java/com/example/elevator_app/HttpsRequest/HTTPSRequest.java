package com.example.elevator_app.HttpsRequest;

import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class HTTPSRequest {

    public static String pullJSONFromHTTPSRequest(URL url) {
        StringBuilder sb = new StringBuilder();

        Thread thread = new Thread() {
            public void run() {
                try {
                    Scanner scan = new Scanner(url.openStream());
                    while (scan.hasNext()) sb.append(scan.nextLine());
                    Log.d("JSON", sb.toString());
                    scan.close();
                } catch (IOException e) {
                    sb.append("HTTPS Connection did not work!");
                    e.printStackTrace();
                }
            }
        };

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}

