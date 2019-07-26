package com.lorenzo.controlleringss;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttConnection {



    private MqttAndroidClient client;
    private String LOGTAG = "";

    public MqttConnection(Context context){
        client = new MqttAndroidClient(
                context, "tcp://192.168.43.72:1883", "smartphoneLorenzoBar");
        try{
            client.connect();
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("can't connect");
        }
    }

    public void SendCommand(String topic, String messageString){
        MqttMessage message = new MqttMessage(messageString.getBytes());
        message.setQos(2);
        message.setRetained(false);

        try {
            client.publish(topic, message);
            Log.i(LOGTAG, "Message published");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("can't publish message");
        }
    }

}
