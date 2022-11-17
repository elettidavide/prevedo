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
import java.util.ArrayList;
import java.util.HashMap;


public class GetCityListTask extends AsyncTask<String, String, String> {

    private Activity parentActivity;
    private ProgressDialog progressDialog = null;
    private StringBuilder answer;
    private String url;

    public GetCityListTask(Activity parentActivity, String url) {
        this.parentActivity = parentActivity;
        this.url = url;
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
            //creo la connessione tramite l'url e gli passo i parametri chiave
            try {
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
        HashMap<String,String> cityListHashMap;
        ArrayList<HashMap<String, String>> cityList = new ArrayList<>();
        String nome_comune;
        System.out.println("ARRAY LISTA COMUNI " + result);

        try{

            if (answer != null) {
                //creo oggetto json da una stringa(result)
                JSONObject jObject = new JSONObject(result);

                int code = jObject.getInt("code");
                //chiamata andata a buon fine, ho avuto il risultato

                if(code == 200){
                    JSONArray jsonArray = jObject.getJSONArray("data");
                    //entro in data dove trovo tutte i nomi delle città, che sono contenute
                    //in un array..

                    System.out.println("JSON CITY " + jsonArray);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        //scorro tutto l'array delle città prese dal json e di ognuna prendo le informazioni
                        // di cui ho bisogno da mostrare a video all'utente...
                        //array di json object
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        nome_comune = jsonObject.getString("title");
                        cityListHashMap = new HashMap<>();
                        cityListHashMap.put("nome_comune", nome_comune);
                        cityList.add(cityListHashMap);
                    }

                }
            }
        }catch(JSONException e ){
        }


        //faccio il cast da Activity a MainActivity, quando finisco gli passo la lista delle città
        ((MainActivity)parentActivity).onCompleted(cityList);

        super.onPostExecute(result);
    }

}

