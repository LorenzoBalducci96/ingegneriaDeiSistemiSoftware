package com.lorenzo.controlleringss;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button left;
    Button forward;
    Button backward;
    Button right;
    Button stop;
    Button forward1Square;
    Button sparecchia;

    ImageButton record;
    TextView insertedText;

    static int requestCode = 100;

    MqttConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String clientId = MqttClient.generateClientId();

        left = findViewById(R.id.left_button);
        right = findViewById(R.id.right_button);
        forward = findViewById(R.id.forward_button);
        backward = findViewById(R.id.backward_button);
        stop = findViewById(R.id.stop);
        forward1Square = findViewById(R.id.forward1Square);
        sparecchia = findViewById(R.id.sparecchia);

        connection = new MqttConnection(this.getApplicationContext());

        record = findViewById(R.id.record);
        insertedText = findViewById(R.id.inserted_text);

        //final MqttAndroidClient client = new MqttAndroidClient(
        //        getApplicationContext(), "tcp://192.168.43.72:1883", "smartphoneLorenzoBar");
        //final String LOGTAG = "";


        forward1Square.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connection.SendCommand("unibo/qak/events", "msg(userCmd,event,frontend,none,userCmd(i),14)");
            }
        });

        sparecchia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connection.SendCommand("unibo/qak/events", "msg(maitreCmd,event,frontend,none,maitreCmd(c),10)");
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connection.SendCommand("unibo/qak/events", "msg(userCmd,event,frontend,none,userCmd(h),14)");
            }
        });




        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });
    }

    private void speak(){
        Intent myIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        myIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        myIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, Locale.getDefault());
        myIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "dimmi qualcosa...");
        try{
            startActivityForResult(myIntent, requestCode);

        }catch(Exception e){
            System.out.println("porco porco");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);

            switch(requestCode) {
                case 100:
                    if (resultCode == RESULT_OK && data != null) {
                        ArrayList<String> string = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        insertedText.setText(string.get(0));

                        for (String inputVoice : string) {
                            if (inputVoice.toLowerCase().contains("avanti")) {
                                connection.SendCommand("unibo/qak/events", "msg(userCmd,event,frontend,none,userCmd(w),14)");
                            }
                            if(inputVoice.toLowerCase().contains("indietro")){
                                connection.SendCommand("unibo/qak/events", "msg(userCmd,event,fronend,none,userCmd(s),30)");
                            }
                            if(inputVoice.toLowerCase().contains("destra")){
                                connection.SendCommand("unibo/qak/events", "msg(userCmd,event,fronend,none,userCmd(d),38)");
                            }
                            if(inputVoice.toLowerCase().contains("sinistra")){
                                connection.SendCommand("unibo/qak/events", "msg(userCmd,event,fronend,none,userCmd(a),42)");
                            }
                            if(inputVoice.toLowerCase().contains("stop") || inputVoice.toLowerCase().contains("ferma") || inputVoice.toLowerCase().contains("fermati")){
                                connection.SendCommand("unibo/qak/events", "msg(userCmd,event,fronend,none,userCmd(h),44)");
                            }
                        }
                    }
            }
    }

    private void moveForward(){

    }

    private void moveBackward(){

    }

    private void rotatingRight(){

    }

    private void rotatingLeft(){

    }

    private void stop(){

    }

}

