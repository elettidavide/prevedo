package com.example.prevedo;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ActivitiesAdapter activitiesAdapter;
    ListView cityLv;
    CityAdapter cityAdapter;
    ProgressDialog pd;
    ArrayList<String> cityList = new ArrayList<>();
    RelativeLayout transparentRelative;
    TextView cityTxt, proceedTxt;
    String activitySelected, city_selected;

    //setto a false le tre variabili
    Boolean whenSelected = false;
    Boolean whereSelected = false;
    Boolean whatSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Collegamento con il file xml
        RelativeLayout todayTomorrowRelative = findViewById(R.id.todayTomorrowRelative);
        todayTomorrowRelative.setVisibility(View.GONE);
        TextView todayTxt = findViewById(R.id.todayTxt);
        TextView todayTomorrowTxt = findViewById(R.id.todayTomorrowTxt);
        TextView tomorrowTxt = findViewById(R.id.tomorrowTxt);
        transparentRelative = findViewById(R.id.transparentLayout);
        transparentRelative.setVisibility(View.GONE);
        proceedTxt = findViewById(R.id.proceedTxt);
        proceedTxt.setVisibility(View.GONE);
        ListView activitiesLv = findViewById(R.id.activityLv);
        cityLv = findViewById(R.id.cityLv);
        cityLv.setVisibility(View.GONE);

        //pd gestisce il loading
        pd = new ProgressDialog(MainActivity.this);

        //url per avere il nome dei comuni
        String city_list_url = "http://www.materameteo.it/function/app/comuni";


        //setto tutti i listener, per il giorno, l'attività e la città

        //sarebbe inserisci il giorno.. quando l'utente clicca oggi o domani questa text si riempie con il valore oggi o domani
        todayTomorrowTxt.setOnClickListener(v->{
                todayTomorrowRelative.setVisibility(View.VISIBLE);
                transparentRelative.setVisibility(View.VISIBLE);
                cityLv.setVisibility(View.GONE);
                activitiesLv.setVisibility(View.GONE);
        });

        todayTxt.setOnClickListener(v->{
            todayTomorrowTxt.setText("OGGI");
            todayTomorrowRelative.setVisibility(View.GONE);
            transparentRelative.setVisibility(View.GONE);
            whenSelected = true;
            if(whatSelected && whenSelected && whereSelected) {
                proceedTxt.setVisibility(View.VISIBLE);
            }
        });

        tomorrowTxt.setOnClickListener(v->{
            todayTomorrowTxt.setText("DOMANI");
            todayTomorrowRelative.setVisibility(View.GONE);
            transparentRelative.setVisibility(View.GONE);
            whenSelected = true;
            if(whatSelected && whenSelected && whereSelected) {
                proceedTxt.setVisibility(View.VISIBLE);
            }
        });

        //popolo la lista delle activities
        ArrayList<String> listActivities = new ArrayList<>();
        listActivities.add("Calcio");
        listActivities.add("Passeggiata");
        listActivities.add("Mare");
        listActivities.add("Ristorante");
        listActivities.add("Corsa all'aperto");
        listActivities.add("Montagna");
        listActivities.add("Stendere il bucato");
        listActivities.add("Bimbi al parco");
        listActivities.add("Uscire in moto");
        listActivities.add("Aperitivo all'aperto");

        activitiesLv.setVisibility(View.GONE);
        activitiesAdapter = new ActivitiesAdapter(this, listActivities, MainActivity.this);
        activitiesLv.setAdapter(activitiesAdapter);
        activitiesAdapter.notifyDataSetChanged();
        TextView selectActivityTxt = findViewById(R.id.selectActivityTxt);

        //seleziona attività..
        selectActivityTxt.setOnClickListener(v->{
            activitiesLv.setVisibility(View.VISIBLE);
            transparentRelative.setVisibility(View.VISIBLE);
            cityLv.setVisibility(View.GONE);
            todayTomorrowRelative.setVisibility(View.GONE);
        });

        // quando l'utente clicca su uno degli item della lista activities questa text si riempie con il valore cliccato
        activitiesLv.setOnItemClickListener((parent, view, position, id) -> {
            activitiesLv.setVisibility(View.GONE);
            whatSelected = true;
            selectActivityTxt.setText(listActivities.get(position));
            transparentRelative.setVisibility(View.GONE);
            activitySelected = listActivities.get(position);
            if(whatSelected && whenSelected && whereSelected) {
                proceedTxt.setVisibility(View.VISIBLE);
            }
        });

        //seleziona città
        cityTxt = findViewById(R.id.selectCityTxt);
        cityTxt.setOnClickListener(v->{
            todayTomorrowRelative.setVisibility(View.GONE);
            activitiesLv.setVisibility(View.GONE);
            //lo scarico una volta solo
            if(cityList.size() == 0) {
                //invoco il task, passandogli l'activity
                GetCityListTask task = new GetCityListTask(MainActivity.this, city_list_url);
                task.execute();
                pd.setMessage("Scarico la lista delle città, interrogando il database..");
                pd.show();
            } else {
                transparentRelative.setVisibility(View.VISIBLE);
                cityLv.setVisibility(View.VISIBLE);
            }
        });

        proceedTxt.setOnClickListener(v->{
            if(whatSelected && whenSelected && whereSelected) {
                Intent intent = new Intent(MainActivity.this, IAResultActivity.class);
                intent.putExtra("daySelected", todayTomorrowTxt.getText().toString());
                intent.putExtra("activitySelected", activitySelected);
                intent.putExtra("citySelected", city_selected.replace(" ", "-"));
                startActivity(intent);
            }
        });

    }
    public void onCompleted(ArrayList<HashMap<String, String>> list){
        String nome_comune;

        if(pd != null){
            pd.dismiss();
        }

        if (list.size() > 0) {
            //arriva la lista delle città, itero elemento per elemento e lo inserisco in cityList (arraylist di stringhe, uguale ad listactivities)
            for (int i = 0; i < list.size(); i++) {
                nome_comune = list.get(i).get("nome_comune");
                cityList.add(nome_comune);
            }
        }

        cityLv = findViewById(R.id.cityLv);
        cityLv.setVisibility(View.VISIBLE);
        //setto il listener per gli items della lista delle città
        cityLv.setOnItemClickListener((parent, view, position, id) -> {
            //quando clicco un item della lista, prendo la position cliccata, e dall'array prendo il valore nella posizione position
            transparentRelative.setVisibility(View.GONE);
            cityLv.setVisibility(View.GONE);
            whereSelected = true;
            cityTxt.setText(cityList.get(position));
            //cityTxt sarebbe il text dove inserisco la città selezionata dall'utente
            city_selected = cityList.get(position);
            if(whatSelected && whenSelected && whereSelected) {
                proceedTxt.setVisibility(View.VISIBLE);
            }
        });

        cityAdapter = new CityAdapter(this, cityList, MainActivity.this);
        cityLv.setAdapter(cityAdapter);
        cityAdapter.notifyDataSetChanged();
        transparentRelative.setVisibility(View.VISIBLE);
    }

}