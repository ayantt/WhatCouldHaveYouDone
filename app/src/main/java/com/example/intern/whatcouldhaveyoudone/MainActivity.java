package com.example.intern.whatcouldhaveyoudone;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ThreadLocalRandom;


public class MainActivity extends AppCompatActivity {

    Button yesBut;
    TextView nameView;
    ImageView poster;
    ProgressDialog pd;
    int totalRuntime, time;
    String test = "Test";
    String omdb;
    String key = "&apikey=3149244e";
    String movieTitle = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        yesBut = findViewById(R.id.yesBut);
        nameView = findViewById(R.id.nameView);
        poster = findViewById(R.id.poster);


        yesBut.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View view) {
                String randomNumber="";
                //if(randomNumber>)
                randomNumber = String.valueOf(ThreadLocalRandom.current().nextInt(83000, 5555555));

                if(randomNumber.length()==7) omdb = "http://www.omdbapi.com/?i=tt";
                if (randomNumber.length()==6) omdb = "http://www.omdbapi.com/?i=tt0";
                if (randomNumber.length()==5) omdb = "http://www.omdbapi.com/?i=tt00";
                Log.d(test," "+omdb+randomNumber+key);
                //nameView.setText(randomNumber);
                new JsonTask().execute(omdb+randomNumber+key);
            }
        });
        nameView.setText(movieTitle);
    }


    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

//            pd = new ProgressDialog(MainActivity.this);
//            pd.setMessage("Please wait");
//            pd.setCancelable(false);
//            pd.show();
//            timerDelayRemoveDialog(100000,"Something went wrong");
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;
            Log.d(test,"Connection created");

            try {
                URL url = new URL(params[0]);
                //Log.d(test, String.valueOf(new URL(params[0])));
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();
                Log.d(test,"Stream done");


                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";
                Log.d(test,"Buffer done");

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                    JSONObject obj = new JSONObject(line);
                    movieTitle = obj.getString("Title");
                    String runtime = obj.getString("Runtime");
                    Log.d("Response: ", "> " + movieTitle+", "+runtime);   //here u ll get whole response...... :-)
                    if(!runtime.equals("N/A")){
                        time = Integer.parseInt(runtime.replaceAll("[^0-9]",""));
                        break;
                    }

                    //nameView.setText(movieTitle);

                }
                Log.d(test,"returning buffer");

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                    Log.d(test,"Connection disconnected");

                }
                try {
                    if (reader != null) {
                        reader.close();
                        Log.d(test,"Reader null");

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }


}

