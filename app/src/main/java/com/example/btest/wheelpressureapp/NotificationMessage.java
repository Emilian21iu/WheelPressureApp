package com.example.btest.wheelpressureapp;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

//Clasa ce genereaza notificarile
public class NotificationMessage {

    private String thicker;
    private String title;
    private String text;
    private int id;
    private NotificationCompat.Builder notification;

    //Metoda ce construieste notificarea si o trimite catre utilizator
    public void SendNotification(Context context){
        notification = new NotificationCompat.Builder(context);
        notification.setAutoCancel(true);
        notification.setTicker(thicker);
        notification.setContentText(text);
        notification.setContentTitle(title);
        notification.setWhen(System.currentTimeMillis());
        notification.setSmallIcon(R.drawable.car_wheelx64);

        Intent intent = new Intent(context,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(id,notification.build());
    }

    //Seteaza cativa parametri ce tin de notificare in functie de tipul notificarii
    public NotificationMessage(int status) {
        switch (status){
            case ParametersAlertStatus.LOW_BATTERY:
                setId(NotificationIDsClass.LOW_BATTERY_ID);
                setText("Battery is under 15%, please connect the charger.");
                setTitle("Low battery");
                setThicker("Battery is 15% or lower");
                break;
            case ParametersAlertStatus.HIGH_PRESSURE:
                setId(NotificationIDsClass.HIGH_PRESSURE_ID);
                setText("Wheel pressure is to high!");
                setTitle("High pressure");
                setThicker("Pressure alert");
                break;
            case ParametersAlertStatus.LOW_PRESSURE:
                setThicker("Pressure alert");
                setTitle("Low pressure");
                setId(NotificationIDsClass.LOW_PRESSURE_ID);
                setText("Wheel pressure is too low!");
                break;
        }
    }

    //Getteri si setteri
    public void setThicker(String thicker) {
        this.thicker = thicker;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThicker() {
        return thicker;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public int getId() {
        return id;
    }
}
