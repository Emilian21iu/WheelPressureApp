package com.example.btest.wheelpressureapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;


public class MainActivity extends Activity {


    //Declaratii de variabile
    private NotificationCompat.Builder notification;
    private Firebase mRef;
    TextView textView;
    private LinearLayout trLayout;
    private Switch notificationSwitch;

    //Setarea limitelor pentru parametri
    private Double minPressure = 1.9;
    private Double maxPressure = 2.3;
    private Integer minBattery = 15;

    private Double currentPressure;
    private Integer currentBattery;
    private boolean notificationStatus = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        notificationSwitch = findViewById(R.id.notificationSwitch);
        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);

        mRef = new Firebase("https://licenta-01.firebaseio.com");//legatura cu firebase
        //Handler pentru a verifica cand o singura variabila se modifica in firebase
        mRef.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                notificationStatus = Boolean.parseBoolean(dataSnapshot.child("presure").getValue().toString());
                notificationSwitch.setChecked(notificationStatus);
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        //Handler de evenimente pentru controlul switch, cand acesta se modifica se actualizeaza valoarea variabilei in baza de date
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                notificationStatus = b;
                mRef.child("presure").setValue(b);
                Log.i("MainActivity","" + b + " " + notificationStatus);
            }
        });
        trLayout = findViewById(R.id.trLayout);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //Handler pentru verificarea schimbarilor din baza de date
        //Cand datele se actualizeaza in baza de date, actualizarea are loc si in aplicatie
        mRef.addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                notificationStatus = notificationSwitch.isChecked();
                //Testare
                //textView.setText(dataSnapshot.child("licenta-01").child("Procent").getValue().toString());
                //Creeare unui obiect care contine date despre valorile senzorilor
                SensorAtributes sensorAtributes = new SensorAtributes();
                sensorAtributes.setHpa(dataSnapshot.child("licenta-01").child("hpa").getValue().toString());
                sensorAtributes.setBat(dataSnapshot.child("licenta-01").child("bat").getValue().toString());
                sensorAtributes.setProcent(dataSnapshot.child("licenta-01").child("Procent").getValue().toString());
                sensorAtributes.setTemp(dataSnapshot.child("licenta-01").child("temp").getValue().toString());
                sensorAtributes.setVoltage(dataSnapshot.child("licenta-01").child("voltage").getValue().toString());


                 currentBattery = Integer.parseInt(sensorAtributes.getProcent());
                 currentPressure = Double.parseDouble(sensorAtributes.getHpa());

                //Verificarea parametrilor pentru generarea notificarilor
                if(currentBattery <= minBattery && notificationStatus){
                    NotificationMessage not = new NotificationMessage(ParametersAlertStatus.LOW_BATTERY);
                    not.SendNotification(getApplicationContext());
                }if(currentPressure <=minPressure && notificationStatus){
                    NotificationMessage not = new NotificationMessage(ParametersAlertStatus.LOW_PRESSURE);
                    not.SendNotification(getApplicationContext());
                }if(currentPressure >= maxPressure && notificationStatus){
                    NotificationMessage not = new NotificationMessage(ParametersAlertStatus.HIGH_PRESSURE);
                    not.SendNotification(getApplicationContext());
                }

                //Se actualizeaza campurile de date din aplicatie
                setView(sensorAtributes);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    //Aceasta metoda face update la interfata utilizator cu noile date
    private void setView(SensorAtributes arg){

        TextView temp3 = findViewById(R.id.temperatureTopLeft);
        TextView press4 = findViewById(R.id.pressureTopLeft);
        TextView battery3 = findViewById(R.id.voltageTopLeft);
        battery3.setText(arg.getBat()+" V");
        temp3.setText(arg.getTemp() + " °C");
        press4.setText(arg.getHpa() + " bar");

        ImageView batImg = findViewById(R.id.battery_status);
        currentBattery =new Integer(Integer.parseInt(arg.getProcent()));
        if(currentBattery == null)currentBattery = 0;

        //Verifica valoarea bateriei si seteaza iconita potrivita
        if(currentBattery <=100 && currentBattery > 90){
            batImg.setImageResource(R.drawable.baseline_battery_full_black_48dp);
        }else if(currentBattery <=80 && currentBattery > 60){
            batImg.setImageResource(R.drawable.baseline_battery_80_black_48dp);
        }else if(currentBattery <=60 && currentBattery > 50) {
            batImg.setImageResource(R.drawable.baseline_battery_60_black_48dp);
        }else if(currentBattery <=50 && currentBattery > 30) {
            batImg.setImageResource(R.drawable.baseline_battery_50_black_48dp);
        }else if(currentBattery <=30 && currentBattery > 20) {
            batImg.setImageResource(R.drawable.baseline_battery_30_black_48dp);
        }else{
            batImg.setImageResource(R.drawable.baseline_battery_20_black_48dp);
        }
    }
}
