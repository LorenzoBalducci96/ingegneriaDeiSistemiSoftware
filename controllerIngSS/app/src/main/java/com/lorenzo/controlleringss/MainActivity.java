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

        connection = new MqttConnection(this.getApplicationContext());

        record = findViewById(R.id.record);
        insertedText = findViewById(R.id.inserted_text);

        //final MqttAndroidClient client = new MqttAndroidClient(
        //        getApplicationContext(), "tcp://192.168.43.72:1883", "smartphoneLorenzoBar");
        //final String LOGTAG = "";



        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });

        /*
        try {
            client.connect(null, new IMqttActionListener() {

                @Override
                public void onSuccess(IMqttToken mqttToken) {

                    //Log.i(LOGTAG, "Client connected");
                    //Log.i(LOGTAG, "Topics="+mqttToken.getTopics());

                    forward.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MqttMessage message = new MqttMessage("msg(userCmd,event,frontend,none,userCmd(w),14)".getBytes());
                            message.setQos(2);
                            message.setRetained(false);

                            try {
                                client.publish("unibo/qak/events", message);
                                Log.i(LOGTAG, "Message published");

                                //client.disconnect();
                                //Log.i(LOGTAG, "client disconnected");
                            } catch (MqttPersistenceException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (MqttException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    });
                    backward.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MqttMessage message = new MqttMessage("msg(userCmd,event,frontend,none,userCmd(s),30)".getBytes());
                            message.setQos(2);
                            message.setRetained(false);

                            try {
                                client.publish("unibo/qak/events", message);
                                Log.i(LOGTAG, "Message published");

                                //client.disconnect();
                                //Log.i(LOGTAG, "client disconnected");
                            } catch (MqttPersistenceException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (MqttException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    });
                    right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MqttMessage message = new MqttMessage("msg(userCmd,event,frontend,none,userCmd(d),38)".getBytes());
                            message.setQos(2);
                            message.setRetained(false);

                            try {
                                client.publish("unibo/qak/events", message);
                                Log.i(LOGTAG, "Message published");

                                //client.disconnect();
                                //Log.i(LOGTAG, "client disconnected");
                            } catch (MqttPersistenceException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (MqttException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    });
                    left.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MqttMessage message = new MqttMessage("msg(userCmd,event,frontend,none,userCmd(a),42)".getBytes());
                            message.setQos(2);
                            message.setRetained(false);

                            try {
                                client.publish("unibo/qak/events", message);
                                Log.i(LOGTAG, "Message published");

                                //client.disconnect();
                                //Log.i(LOGTAG, "client disconnected");
                            } catch (MqttPersistenceException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (MqttException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    });
                    stop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MqttMessage message = new MqttMessage("msg(userCmd,event,frontend,none,userCmd(h),44)".getBytes());
                            message.setQos(2);
                            message.setRetained(false);

                            try {
                                client.publish("unibo/qak/events", message);
                                Log.i(LOGTAG, "Message published");

                                //client.disconnect();
                                //Log.i(LOGTAG, "client disconnected");
                            } catch (MqttPersistenceException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (MqttException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    });
                }

                @Override
                public void onFailure(IMqttToken arg0, Throwable arg1) {
                    // TODO Auto-generated method stub
                    Log.i(LOGTAG, "Client connection failed: "+arg1.getMessage());

                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
        */
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

