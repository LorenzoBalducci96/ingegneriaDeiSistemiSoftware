package com.lorenzo.controlleringss;

import android.content.Intent;
import android.provider.SyncStateContract;
import android.speech.RecognizerIntent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    Button left;
    Button forward;
    Button backward;
    Button right;
    Button stop;
    Button forward1Square;
    Button sparecchia;
    Button apparecchia;
    Button fridgeRequest;
    Button roomStateRequest;
    Button addFood;
    Button plusFoodQt;
    Button minusFoodQt;
    Spinner foodList;
    EditText foodQt;
    TextView textScrollView;

    ImageButton record;
    TextView insertedText;

    boolean must_stop = true;

    static int requestCode = 100;

    MqttConnection connection;
    MqttAndroidClient client;
    private PahoMqttClient pahoMqttClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String clientId = MqttClient.generateClientId();
        String requestedFood = "";

        left = findViewById(R.id.left_button);
        right = findViewById(R.id.right_button);
        forward = findViewById(R.id.forward_button);
        backward = findViewById(R.id.backward_button);
        stop = findViewById(R.id.stop);
        forward1Square = findViewById(R.id.forward1Square);
        sparecchia = findViewById(R.id.sparecchia);
        apparecchia = findViewById(R.id.apparecchia);
        fridgeRequest = findViewById(R.id.fridgeRequest);
        textScrollView = findViewById(R.id.noticeView);
        roomStateRequest = findViewById(R.id.roomStateRequest);
        addFood = findViewById(R.id.addFood);
        foodList = findViewById(R.id.foodList);
        foodQt = findViewById(R.id.foodQt);
        plusFoodQt = findViewById(R.id.plus);
        minusFoodQt = findViewById(R.id.minus);

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("123,piatto di melanzane");
        arrayList.add("182,salmone alla griglia");
        arrayList.add("188,patatine arrosto");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        foodList.setAdapter(arrayAdapter);


        plusFoodQt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String var = foodQt.getText().toString();
                foodQt.setText(String.valueOf(Integer.parseInt(var.trim()) + 1));
                //foodQt.setText(Integer.parseInt(foodQt.getText().toString()) + 1);
            }
        });

        minusFoodQt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String var = foodQt.getText().toString();
                if(!var.equals("0"))
                    foodQt.setText(String.valueOf(Integer.parseInt(var.trim()) - 1));
            }
        });

        connection = new MqttConnection(this.getApplicationContext(), textScrollView);
        connection.subscribe("unibo/qak/events");

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
                if(must_stop) {
                    must_stop = false;
                    connection.SendCommand("unibo/qak/events", "msg(alarm,event,frontend,none,alarm(h),14)");
                }else{
                    must_stop = true;
                    connection.SendCommand("unibo/qak/events", "msg(situationUnderControl,event,frontend,none,situationUnderControl(h),14)");
                }
            }
        });

        apparecchia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connection.SendCommand("unibo/qak/events", "msg(maitreCmd,event,frontend,none,maitreCmd(v),10)");
            }
        });

        roomStateRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connection.SendCommand("unibo/qak/events", "msg(roomStateRequest,event,frontend,none,roomStateRequest(r),10)");
            }
        });

        fridgeRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connection.SendCommand("unibo/qak/events", "msg(fridgeRequest,event,frontend,none,fridgeRequest(l),10)");
            }
        });


        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connection.SendCommand("unibo/qak/events", "msg(userCmd,event,frontend,none,userCmd(r),10)");
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connection.SendCommand("unibo/qak/events", "msg(userCmd,event,frontend,none,userCmd(l),10)");
            }
        });


        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });

        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodCode = foodList.getItemAtPosition(foodList.getSelectedItemPosition()).toString();
                StringTokenizer tokenizer = new StringTokenizer(foodCode);
                foodCode = tokenizer.nextToken(",");
                String payloadMsg = foodCode + "," + foodQt.getText().toString();
                connection.SendCommand("unibo/qak/events", "msg(maitreCmd,event,frontend,none,maitreCmd(\"a:" + payloadMsg + "\"),10)");
            }
        });
    }

    protected void mqttCallback() {
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                //msg("Connection lost...");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                TextView tvMessage = (TextView) findViewById(R.id.noticeView);
                if(topic.equals("mycustomtopic1")) {
                    //Add custom message handling here (if topic = "mycustomtopic1")
                }
                else if(topic.equals("mycustomtopic2")) {
                    //Add custom message handling here (if topic = "mycustomtopic2")
                }
                else {
                    String msg = "topic: " + topic + "\r\nMessage: " + message.toString() + "\r\n";
                    tvMessage.append( msg);
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

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
}

