package com.example.prevedo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.HashMap;

public class IAResultActivity extends AppCompatActivity{

    String citySelected, activitySelected, daySelected;
    String previsioni_comune_url = "", answer;
    TextView answerTxt, helloTxt;
    TextView prevTxt, windTxt, rainTxt, snowTxt, visibilityTxt, humidityTxt, degreesTxt;
    ImageView happySadIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iaresult);

        citySelected = getIntent().getStringExtra("citySelected");
        activitySelected = getIntent().getStringExtra("activitySelected");
        daySelected = getIntent().getStringExtra("daySelected");
        answerTxt = findViewById(R.id.answerTxt);
        helloTxt = findViewById(R.id.helloTxt);
        helloTxt.setText("Ciao, ecco la previsione in base alle tue scelte.");

        degreesTxt = findViewById(R.id.degreesTxt);
        prevTxt = findViewById(R.id.prevTxt);
        rainTxt = findViewById(R.id.rainTxt);
        snowTxt = findViewById(R.id.snowTxt);
        windTxt = findViewById(R.id.windTxt);
        visibilityTxt = findViewById(R.id.visibilityTxt);
        humidityTxt = findViewById(R.id.humidityTxt);
        happySadIv = findViewById(R.id.happySadIv);

        if(daySelected.equalsIgnoreCase("OGGI")){
            previsioni_comune_url = "http://www.materameteo.it/function/app/previsioni/" + citySelected;
            GetCityInfoTask task = new GetCityInfoTask( IAResultActivity.this, previsioni_comune_url, citySelected);
            task.execute();
        } else if(daySelected.equalsIgnoreCase("DOMANI")){
            previsioni_comune_url = "http://www.materameteo.it/function/app/previsioni/" + citySelected;
            GetTomorrowCityInfoTask task = new GetTomorrowCityInfoTask(IAResultActivity.this, previsioni_comune_url, citySelected);
            task.execute();
        }

    }

    private void isThisActivityPossible(String temp_c, String view_rain, String view_snow, String view_hum, String wind_speed){

        if(activitySelected.equalsIgnoreCase("CALCIO") || activitySelected.equalsIgnoreCase("PASSEGGIATA") || activitySelected.equalsIgnoreCase("CORSA ALL'APERTO")) {
            if(Double.parseDouble(temp_c) > 5 && Double.parseDouble(view_hum) < 80 && Double.parseDouble(wind_speed) < 10 && Double.parseDouble(view_rain) < 2
                    && Double.parseDouble(view_snow) == 0){
                String firstPart = "";
                if(activitySelected.equalsIgnoreCase("CALCIO")){
                    firstPart = "Puoi giocare a calcio ";
                } else if(activitySelected.equalsIgnoreCase("PASSEGGIATA")){
                    firstPart = "Puoi fare una passeggiata all'aperto ";
                } else if(activitySelected.equalsIgnoreCase("CORSA ALL'APERTO")){
                    firstPart = "Puoi fare una corsa all'aperto ";
                }
                answer = firstPart +  "perch?? la temperatur?? sar?? di " + temp_c + "?? e ci sar?? un umidit?? pari al " + view_hum + "%" +
                        " e la velocit?? del vento sar?? di " + wind_speed + "km/h e c'?? una pioggia pari a " + view_rain + "mm e una neve pari a " + view_snow + "cm. BUONA ATTIVITA' FISICA! Non esagerare.";
                answerTxt.setText(answer);

                happySadIv.setBackgroundResource(R.drawable.happy);
            } else {

                String answer = "";
                if(activitySelected.equalsIgnoreCase("CALCIO")){
                    answer = "Non puoi giocare a calcio ";
                } else if(activitySelected.equalsIgnoreCase("PASSEGGIATA")){
                    answer = "Non puoi fare una passeggiata all'aperto ";
                } else if(activitySelected.equalsIgnoreCase("CORSA ALL'APERTO")){
                    answer = "Non puoi fare una corsa all'aperto ";
                }

                if(Double.parseDouble(temp_c) <= 5){
                    answer = answer + "perch?? c'?? una temperatura di " + temp_c + "??. ";
                }

                if(Double.parseDouble(view_hum) >= 80){
                    answer = answer + "perche c'?? una percentuale di umidit?? pari a " + view_hum + "%. ";
                }

                if(Double.parseDouble(wind_speed) >= 10){
                    answer = answer + "perch?? c'?? molto vento. Velocit?? prevista: " + wind_speed + "km/h. ";

                }

                if(Double.parseDouble(view_rain) >= 2){
                    answer = answer + "perch?? ?? prevista pioggia, pari a: " + view_rain + "mm. ";
                }

                if(Double.parseDouble(view_snow) > 0){
                    answer = answer + "perch?? ?? prevista neve, pari a: " + view_rain + "cm. ";
                }

                answer = answer + "MI DISPIACE, ORGANIZZA PER UN ALTRO GIORNO. Non ?? la giornata giusta per farlo.";
                answerTxt.setText(answer);
                happySadIv.setBackgroundResource(R.drawable.sad);
            }
        }  if(activitySelected.equalsIgnoreCase("RISTORANTE")) {
            if (Double.parseDouble(wind_speed) < 50 && Double.parseDouble(view_rain) < 15
                    && Double.parseDouble(view_snow) == 0 && Double.parseDouble(temp_c) > 5) {
                System.out.println("PUOI ANDARE AL RISTORANTE");
                answer = "Puoi andare al ristorante perch?? la temperatur?? sar?? di " + temp_c + "?? e ci sar?? un umidit?? pari al " + view_hum + "%" +
                        " e la velocit?? del vento sar?? di " + wind_speed + "km/h e c'?? una pioggia pari a " + view_rain + "mm e una neve pari a " + view_snow + "cm. Goditi il momento. BUON PRANZO/CENA!";
                answerTxt.setText(answer);
                happySadIv.setBackgroundResource(R.drawable.happy);
            } else {
                answer = "Non puoi andare al ristorante perch??: ";

                if (Double.parseDouble(wind_speed) >= 50) {
                    answer = answer + "c'?? una velocit?? di vento pari a " + wind_speed + "km/h. ";
                }

                if (Double.parseDouble(view_rain) >= 15) {
                    answer = answer + "piove abbondantemente. Ci saranno " + view_rain + "mm di pioggia. ";
                }

                if (Double.parseDouble(view_snow) > 0) {
                    answer = answer + "nevica. Ci saranno " + view_snow + "cm di neve. ";
                }

                if (Double.parseDouble(temp_c) <= 5) {
                    answer = answer + "fa freddo, vi ?? una temperatura molto bassa. ";
                }

                answer = answer + "MI DISPIACE, OGGI MANGIA A CASA. Non ?? il caso di uscire.";
                answerTxt.setText(answer);
                happySadIv.setBackgroundResource(R.drawable.sad);
            }
        } else if(activitySelected.equalsIgnoreCase("MARE")){
            if (Double.parseDouble(wind_speed) < 5 && Double.parseDouble(view_rain) == 0
                    && Double.parseDouble(view_snow) == 0 && Double.parseDouble(temp_c) >= 28) {
                answer = "Puoi andare al mare perch?? la temperatur?? sar?? di " + temp_c + "?? e ci sar?? un umidit?? pari al " + view_hum + "%" +
                        " e la velocit?? del vento sar?? di " + wind_speed + "km/h e c'?? una pioggia pari a " + view_rain + "mm e una neve pari a " + view_snow + "cm. NON ABBRONZARTI TROPPO! Ricorda la crema solare. ";
                answerTxt.setText(answer);
                happySadIv.setBackgroundResource(R.drawable.happy);
            } else {

                answer = "Non puoi andare al mare perch?? ";

                if (Double.parseDouble(wind_speed) >= 5) {
                    answer = answer + "c'?? una velocit?? di vento pari a " + wind_speed + "km/h. ";
                }

                if (Double.parseDouble(view_rain) > 0) {
                    answer = answer + "piove. Ci saranno " + view_rain + "mm di pioggia. ";
                }

                if (Double.parseDouble(view_snow) > 0) {
                    answer = answer + "nevica. Ci saranno " + view_snow + "cm di neve. ";
                }

                if (Double.parseDouble(temp_c) <= 25) {
                    answer = answer + "fa freddo. Ci saranno " + temp_c + "??. Non la temperatura ideale per farsi un bagno senza ammalarsi. ";
                }

                answer = answer + "Prova ad andarci un altro giorno, oggi fai qualcos'altro.";
                answerTxt.setText(answer);

                happySadIv.setBackgroundResource(R.drawable.sad);

            }

        } else if(activitySelected.equalsIgnoreCase("MONTAGNA")){
            if (Double.parseDouble(wind_speed) < 5 && Double.parseDouble(view_rain) == 0
                    && Double.parseDouble(view_snow) > 0 && Double.parseDouble(temp_c) < 10) {
                answer = "Puoi andare in montagna perch?? la temperatur?? sar?? di " + temp_c + "?? e ci sar?? un umidit?? pari al " + view_hum + "%" +
                        " e la velocit?? del vento sar?? di " + wind_speed + "km/h e c'?? una pioggia pari a " + view_rain + "mm e una neve pari a " + view_snow + "cm. BUONA SCIATA! Presta attenzione.";
                answerTxt.setText(answer);
            } else {
                answer = "Non puoi andare in montagna perch?? ";

                if (Double.parseDouble(wind_speed) >= 5) {
                    answer = answer + "c'?? una velocit?? di vento pari a " + wind_speed + "km/h. ";
                }

                if (Double.parseDouble(view_rain) > 0) {
                    answer = answer + "piove. Ci saranno " + view_rain + "mm di pioggia. ";
                }

                if (Double.parseDouble(view_snow) == 0) {
                    answer = answer + "non nevica. Ci saranno " + view_snow + "cm di neve. ";
                }

                if (Double.parseDouble(temp_c) >= 10) {
                    answer = answer + "fa caldo per sciare. Ci saranno " + temp_c + "??. Non la temperatura ideale per andare in montagna. ";
                }

                answer = answer + "Prova ad andarci un altro giorno. Le condizioni non sono ottimali per sciare.";
                answerTxt.setText(answer);

                happySadIv.setBackgroundResource(R.drawable.sad);
            }
        } else if(activitySelected.equalsIgnoreCase("STENDERE IL BUCATO")){
            if (Double.parseDouble(wind_speed) < 5 && Double.parseDouble(view_rain) == 0
                    && Double.parseDouble(view_snow) == 0 && Double.parseDouble(temp_c) >= 10 && Double.parseDouble(view_hum) < 60) {
                answer = "Puoi stendere il bucato perch?? la temperatur?? sar?? di " + temp_c + "?? e ci sar?? un umidit?? pari al " + view_hum + "%" +
                        " e la velocit?? del vento sar?? di " + wind_speed + "km/h e c'?? una pioggia pari a " + view_rain + "mm e una neve pari a " + view_snow + "cm. Stendere i panni al rovescio ?? una buona abitudine per proteggerne il colore. ";
                answerTxt.setText(answer);
                happySadIv.setBackgroundResource(R.drawable.happy);
            } else {
                answer = "Non puoi stendere il bucato perch?? ";

                if (Double.parseDouble(wind_speed) >= 5) {
                    answer = answer + "c'?? una velocit?? di vento pari a " + wind_speed + "km/h. ";
                }

                if (Double.parseDouble(view_rain) > 0) {
                    answer = answer + "piove. Ci saranno " + view_rain + "mm di pioggia. ";
                }

                if (Double.parseDouble(view_snow) > 0) {
                    answer = answer + "nevica. Ci saranno " + view_snow + "cm di neve. ";
                }

                if (Double.parseDouble(temp_c) < 10) {
                    answer = answer + "fa troppo freddo per stendere all'aperto. Ci saranno " + temp_c +"??. ";
                }

                if(Double.parseDouble(view_hum) >= 60){
                    answer = answer + "c'?? troppa umidit??, pari a " + view_hum + "%. Non si asciugher?? la roba. ";
                }

                answer = answer + "Prova a fare il bucato un altro giorno o non stendere il bucato all'aperto.";
                System.out.println("ANSWER " + answer);
                answerTxt.setText(answer);

                happySadIv.setBackgroundResource(R.drawable.sad);
            }
        }else if(activitySelected.equalsIgnoreCase("BIMBI AL PARCO")){
            if (Double.parseDouble(wind_speed) < 5 && Double.parseDouble(view_rain) == 0
                    && Double.parseDouble(view_snow) == 0 && Double.parseDouble(temp_c) > 15) {
                answer = "Puoi andare al parco con i bimbi perch?? la temperatur?? sar?? di " + temp_c + "?? e ci sar?? un umidit?? pari al " + view_hum + "%" +
                        " e la velocit?? del vento sar?? di " + wind_speed + "km/h e c'?? una pioggia pari a " + view_rain + "mm e una neve pari a " + view_snow + "cm. Giornata perfetta per i tuoi bimbi. BUON DIVERTIMENTO !";
                answerTxt.setText(answer);
                happySadIv.setBackgroundResource(R.drawable.happy);
            } else {
                answer = "Non puoi andare al parco perch?? ";

                if (Double.parseDouble(wind_speed) >= 5) {
                    answer = answer + "c'?? una velocit?? di vento pari a " + wind_speed + "km/h. ";
                }

                if (Double.parseDouble(view_rain) > 0) {
                    answer = answer + "piove. Ci saranno " + view_rain + "mm di pioggia. ";
                }

                if (Double.parseDouble(view_snow) > 0) {
                    answer = answer + "nevica. Ci saranno " + view_snow + "cm di neve. ";
                }

                if (Double.parseDouble(temp_c) <= 15) {
                    answer = answer + "fa troppo freddo per andare al parco con i bimbi. Ci saranno " + temp_c + "??. Non la temperatura ideale per farli uscire. ";
                }



                answer = answer + "Prova ad andare al parco un altro giorno. Oggi non ?? consigliato. Puoi farli divertire anche a casa.";
                answerTxt.setText(answer);

                happySadIv.setBackgroundResource(R.drawable.sad);
            }
        }else if(activitySelected.equalsIgnoreCase("APERITIVO ALL'APERTO")){
            if (Double.parseDouble(wind_speed) < 5 && Double.parseDouble(view_rain) == 0
                    && Double.parseDouble(view_snow) == 0 && Double.parseDouble(temp_c) >= 15 && Double.parseDouble(view_hum) < 70) {
                answer = "Puoi andare a fare apertivo all'aperto perch?? la temperatur?? sar?? di " + temp_c + "?? e ci sar?? un umidit?? pari al " + view_hum + "%" +
                        " e la velocit?? del vento sar?? di " + wind_speed + "km/h e c'?? una pioggia pari a " + view_rain + "mm e una neve pari a " + view_snow + "cm. Non ti resta che organizzarti. BUON APERITIVO!";
                answerTxt.setText(answer);
                happySadIv.setBackgroundResource(R.drawable.happy);
            } else {
                answer = "Non puoi andare a fare aperitivo all'aperto perch?? ";

                if (Double.parseDouble(wind_speed) >= 5) {
                    answer = answer + "c'?? una velocit?? di vento pari a " + wind_speed + "km/h. ";
                }

                if (Double.parseDouble(view_rain) > 0) {
                    answer = answer + "piove. Ci saranno " + view_rain + "mm di pioggia. ";
                }

                if (Double.parseDouble(view_snow) > 0) {
                    answer = answer + "nevica. Ci saranno " + view_snow + "cm di neve. ";
                }

                if (Double.parseDouble(view_hum) >= 70) {
                    answer = answer + "c'?? troppa umidit??, pari al " + view_hum + "%. ";
                }

                if (Double.parseDouble(temp_c) <= 10) {
                    answer = answer + "fa troppo freddo per andare a fare aperitivo. Ci saranno " + temp_c + "??. Non la temperatura ideale per stare all'aperto tanto tempo. ";
                }

                answer = answer + "Prova ad andare a fare aperitivo all'aperto un altro giorno. Oggi non ?? cosigliato";
                answerTxt.setText(answer);

                happySadIv.setBackgroundResource(R.drawable.sad);
            }
        } else if(activitySelected.equalsIgnoreCase("USCIRE IN MOTO")){
            if (Double.parseDouble(wind_speed) < 10 && Double.parseDouble(view_rain) == 0
                    && Double.parseDouble(view_snow) == 0 && Double.parseDouble(temp_c) >= 10) {
                answer = "Puoi uscire in moto perch?? la temperatur?? sar?? di " + temp_c + "?? e ci sar?? un umidit?? pari al " + view_hum + "%" +
                        " e la velocit?? del vento sar?? di " + wind_speed + "km/h e c'?? una pioggia pari a " + view_rain + "mm e una neve pari a " + view_snow + "cm. Si prudente. BUON GIRO IN MOTO! ";
                answerTxt.setText(answer);
                happySadIv.setBackgroundResource(R.drawable.happy);
            } else {
                answer = "Non puoi uscire in moto perch?? ";

                if (Double.parseDouble(wind_speed) >= 10) {
                    answer = answer + "c'?? una velocit?? di vento pari a " + wind_speed + "km/h. ";
                }

                if (Double.parseDouble(view_rain) > 0) {
                    answer = answer + "piove. Ci saranno " + view_rain + "mm di pioggia. ";
                }

                if (Double.parseDouble(view_snow) > 0) {
                    answer = answer + "nevica. Ci saranno " + view_snow + "cm di neve. ";
                }

                if (Double.parseDouble(temp_c) < 10) {
                    answer = answer + "fa troppo freddo per uscire in moto. Ci saranno " + temp_c + "??. Non la temperatura ideale per uscire in moto. Fa troppo freddo. ";
                }

                answer = answer + "Prova ad uscire in moto un altro giorno. ";
                answerTxt.setText(answer);

                happySadIv.setBackgroundResource(R.drawable.sad);
            }

        } else {
        }

    }

    public void onCompleted(HashMap<String, String> list){
        String temp_c, view_hum, view_rain, view_snow, view_visibility, wind_speed, prev_text;

        temp_c = list.get("temp_c");
        view_rain = list.get("view_rain");
        view_snow = list.get("view_snow");
        view_visibility = list.get("view_visibility");
        wind_speed = list.get("wind_speed");
        prev_text = list.get("prev_text");
        view_hum = list.get("view_hum");


        degreesTxt.setText(temp_c + "?? gradi");
        rainTxt.setText(view_rain + " di pioggia");
        snowTxt.setText(view_snow + " di neve");
        visibilityTxt.setText(view_visibility + " di visibilit??");
        windTxt.setText(wind_speed + " km/h");
        prevTxt.setText("Previsione: " + prev_text);
        humidityTxt.setText("Umidit??: " + view_hum);

        if(view_snow.contains("mm")){
            view_snow = view_snow.replace("mm", "");
        }

        if(view_snow.contains("cm")){
            view_snow = view_snow.replace("cm", "");
        }

        if(view_rain.contains("mm")){
            view_rain = view_rain.replace("mm", "");
        }

        if(view_rain.contains("cm")){
            view_rain = view_rain.replace("cm", "");
        }

        isThisActivityPossible(temp_c,view_rain,view_snow,view_hum.replace("%", ""),wind_speed);


    }

}