package com.nxc122430.stockapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikesh Chapagain on 2/4/17.
 */

public class StockActivity extends Activity {

    ListView lView;
    ArrayList<String> list;
    String symbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_activity);

        // getting stock symbol from main activity
        Intent i = getIntent();
        symbol = i.getStringExtra("symbol");

        // get list view
        lView = (ListView)findViewById(R.id.listView);

        setupAdapter();

        // excute async task
        new FetchDataItem().execute();

    }

    // method looks at the current model state and configures the adapter appropriately on your list view
    private void setupAdapter() {
        // if view is null
        if (lView == null) {
            return;
        }

        // when there are data in the list
        if (list != null) {
            lView.setAdapter(new ArrayAdapter<String>(StockActivity.this, android.R.layout.simple_list_item_1, list));
        }
        else {
            lView.setAdapter(null);
        }
    }

    // class handler to connect
    public class DataFetchr {
        // method fetches raw data from a URL and returns it as an array of bytes
        byte[] getUrlBytes(String urls) throws IOException {
            URL url = new URL (urls);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            try {
                // create and output array stream
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                // input stream that get file from url connection
                InputStream input = connection.getInputStream();

                // if you can not connect
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return null;
                }

                // read in the file and put it into an byte array
                int count = 0;
                byte data [] = new byte [1024];
                // loop through the file
                while ((count = input.read(data)) > 0) {
                    output.write(data, 0, count);
                }

                // close output buffer
                output.close();
                // return
                return output.toByteArray();
            }
            finally {
                // close the connection
                connection.disconnect();
            }
        }

        // method converts the result from getUrlBytes to a String
        public String getUrl(String urls) throws IOException {
            return new String(getUrlBytes(urls));
        }
    }

    private class FetchDataItem extends AsyncTask<Void, Data, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            ArrayList<String> tempList = new ArrayList<String>();
            Data newData = new Data ();
            try {
                // user the data fetcher to get the data in string
                String data = new DataFetchr().getUrl("http://utdallas.edu/~jxc064000/2017Spring/" + symbol + ".txt");
                // store result into log if success
                Log.i("STOCKAPP", "Contents of URL: " + data);

                // break up data into Date,Open,High,Low,Close,Volume,Adj Close
                // loop throught data and add it into the list
                while (true) {
                    String lines = data.split("\n").toString();
                    if (lines != null) {
                        tempList.add(lines);
                    }
                    else {
                        break;
                    }
                }
            }
            catch (IOException ioe) {
                // store failed into log
                Log.i("STOCKAPP", "Failed to get URL: " + ioe);

            }
            return tempList;
        }

        protected void onPostExecute(ArrayList<String> mList) {
            list = mList;
            setupAdapter();
        }
    }

    // object for the data
    public class Data {
        String date;
        String open;
        String high;
        String low;
        String close;
        String volume;
        String adjClose;

        public String createData (String d, String o, String h, String l, String c, String v, String a) {
            date = d;
            open = o;
            high = h;
            low = l;
            close = c;
            volume = v;
            adjClose = a;

            return toString();
        }

        // override the to string
        public String toString() {
            return date + " " + open + " " + high + " " + low + " " + close + " " + volume + " " + adjClose;
        }
    }
}
