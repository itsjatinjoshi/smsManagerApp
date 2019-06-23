package com.example.smsmanagerapp;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText etName, etText;
    int MY_PERMISSIONS_TO_SEND_SMS=1;

    String SENT = "SMS_SENT";
    String DELIVERED= "SMS_DElIVERED";
    PendingIntent sendPI, deliveredPI;
    BroadcastReceiver smsSentReceiver, smsDeliveredReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName= findViewById(R.id.etName);
        etText= findViewById(R.id.etText);

        sendPI= PendingIntent.getBroadcast(this, 0, new Intent(SENT),0);
        deliveredPI= PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED),0);

    }

    @Override
    protected void onResume() {
        super.onResume();

       smsSentReceiver = new BroadcastReceiver() {
           @Override
           public void onReceive(Context context, Intent intent) {

           switch (getResultCode()){
               case Activity.RESULT_OK:
                   Toast.makeText(MainActivity.this, "SMS Sent !", Toast.LENGTH_LONG).show();
                   break;

               case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                   Toast.makeText(MainActivity.this, "Generic Failure !", Toast.LENGTH_LONG).show();
                   break;
               case SmsManager.RESULT_ERROR_NO_SERVICE:
                   Toast.makeText(MainActivity.this, "No Service !", Toast.LENGTH_LONG).show();
                   break;

               case SmsManager.RESULT_ERROR_NULL_PDU:
                   Toast.makeText(MainActivity.this, "No PDU !", Toast.LENGTH_LONG).show();
                   break;
               case SmsManager.RESULT_ERROR_RADIO_OFF:
                   Toast.makeText(MainActivity.this, "Radio Off !!!", Toast.LENGTH_LONG).show();
                   break;
           }

           }
       } ;
       smsDeliveredReceiver = new BroadcastReceiver() {
           @Override
           public void onReceive(Context context, Intent intent) {
               switch (getResultCode()){
                   case Activity.RESULT_OK:
                       Toast.makeText(MainActivity.this, "SMS Sent !", Toast.LENGTH_LONG).show();
                       break;

                   case Activity.RESULT_CANCELED:
                       Toast.makeText(MainActivity.this, "Sms not deliver !!!", Toast.LENGTH_LONG).show();
                       break;

               }

           }
       };

       registerReceiver(smsSentReceiver, new IntentFilter(SENT));
       registerReceiver(smsDeliveredReceiver, new IntentFilter(DELIVERED));



    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(smsSentReceiver);
        unregisterReceiver(smsDeliveredReceiver);
    }

    public void btnSendMessage(View v){

        String message= etText.getText().toString();
        String tellNr= etName.getText().toString();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_TO_SEND_SMS);
        }
        else{
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(tellNr, null, message, sendPI, deliveredPI);
        }

    }
}
