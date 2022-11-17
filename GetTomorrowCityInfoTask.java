package com.example.prevedo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;


public class GetTomorrowCityInfoTask extends AsyncTask<String, String, String> {

    private Activity parentActivity;
    private ProgressDialog progressDialog = null;
    private StringBuilder answer;
    private String url, nome_comune;

    public GetTomorrowCityInfoTask(Activity parentActivity, String url, String nome_comune) {
        this.parentActivity = parentActivity;
        this.url = url;
        this.nome_comune = nome_comune;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected String doInBackground(String... params) {
        URL url;
        String response = "";
        String result = new String();
        OutputStream os;
        InputStream is;
        HttpURLConnection conn = null;

        try {
            url = new URL(this.url);
            System.out.println("URL " + url);

            try {
                //creo la connessione tramite l'url e gli passo i parametri chiave
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(5000 /* milliseconds */);
                conn.setConnectTimeout(5000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setRequestProperty("MATERAMETEO-SHARED-KEY", "M4t3R4m3T3o-2012");
                conn.connect();

            } catch (Exception ex) {
            }

            int responseCode = conn.getResponseCode();
            answer = new StringBuilder();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                is = conn.getInputStream();

                String line;
                if (is != null) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    try {
                        //prendo tutto quello che arriva dall'inputstream e lo metto in una stringa
                        while(((line = reader.readLine()) != null)) {
                            answer.append(line);
                        }
                        result = answer.toString();
                        is.close();
                    } catch (Exception e) {
                        Log.d("readJSONFeed", e.getLocalizedMessage());
                    }
                } else {
                    result = "No connection";
                }

            } else {
                conn.disconnect();
                result = "No connection";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //chiama in automatico il metodo sotto e gli passa result
        return result;
    }


    @Override
    protected void onPostExecute(String result) {
        HashMap<String,String> cityInfoHashMap = new HashMap<>();

        try{

            if (answer != null) {
                //creo oggetto json da una stringa(result)
                JSONObject jObject = new JSONObject(result);

                int code = jObject.getInt("code");
                //chiamata andata a buon fine, ho avuto il risultato


                if(code == 200){
                    JSONObject jsonObject = jObject.getJSONObject("data");

                    //qui all'interno di data c'è solo un oggetto, non un array..
                    //l'oggetto si chiama daily..
                    JSONArray infoArray = jsonObject.getJSONArray("daily");
                    System.out.println("TOMORROW JSON " + infoArray.toString());

                    JSONObject infoObj = infoArray.getJSONObject(1);//in pos 0 c'è oggi, pos 1 domani

                    //leggo i valori
                    String temp_c = infoObj.getString("temp_day_c");
                    String view_rain = infoObj.getString("view_rain");
                    String view_snow = infoObj.getString("view_snow");
                    String prev_text = infoObj.getString("prev_text");
                    String wind_speed = infoObj.getString("wind_speed");
                    String view_visibility = "10000km";
                    String view_wind_speed = infoObj.getString("view_wind_speed");
                    String view_hum = infoObj.getString("view_hum");

                    //inserisco i valori nell'hashmap
                    cityInfoHashMap.put("temp_c", temp_c);
                    cityInfoHashMap.put("view_rain", view_rain);
                    cityInfoHashMap.put("view_snow", view_snow);
                    cityInfoHashMap.put("prev_text", prev_text);
                    cityInfoHashMap.put("wind_speed", wind_speed);
                    cityInfoHashMap.put("view_visibility", view_visibility);
                    cityInfoHashMap.put("view_wind_speed", view_wind_speed);
                    cityInfoHashMap.put("view_hum", view_hum);

                }
            }
        }catch(JSONException e ){
        }

        //faccio il cast da Activity a MainActivity, quando finisco gli passo la hashmap
        ((IAResultActivity)parentActivity).onCompleted(cityInfoHashMap);
        super.onPostExecute(result);
    }

}

