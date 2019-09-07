package com.lorenzo.controlleringss;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.StringTokenizer;

import javax.xml.datatype.Duration;

public class MqttConnection {



    private MqttAndroidClient client;
    private String LOGTAG = "";
    private TextView noticePanel;
    Context context;

    public MqttConnection(Context context, TextView noticePanel){
        this.noticePanel = noticePanel;
        this.context = context;
        client = new MqttAndroidClient(
                context, "tcp://192.168.43.72:1883", "smartphoneLorenzoBar");
        try{
            client.connect(context, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                        try {
                            client.subscribe("unibo/qak/frontend", 1);
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                    Log.println(0, "", "ok");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.println(0, "", "failed connect");
                }
            });
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {

                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    notice(message.toString());
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("can't connect");
        }
    }

    public void notice(String notice){
        if(notice.startsWith("msg(roomStateEvent")){
            StringTokenizer token = new StringTokenizer(notice, "(");
            token.nextToken();
            token.nextToken();
            String payload = token.nextToken("(");
            payload = new StringTokenizer(payload, ")").nextToken();
            token = new StringTokenizer(payload);
            String fullText = "";

            fullText += "piatti puliti nella pantry      = " + token.nextToken(";") + "\n";
            fullText += "piatti puliti nella dishwasher  = " + token.nextToken(";") + "\n";

            if(token.hasMoreTokens()) {
                StringTokenizer internalTokenizer = new StringTokenizer((token.nextToken(";")));

                String foodCode = internalTokenizer.nextToken(",");
                String qt = internalTokenizer.nextToken(",");
                String description = internalTokenizer.nextToken(",");
                fullText += foodCode + "  ";
                fullText += qt + "  ";
                fullText += description + "  ";
                fullText += "\n";

                String actualToken = "";
                while (token.hasMoreTokens()) {
                    internalTokenizer = new StringTokenizer(token.nextToken(";"));
                    foodCode = internalTokenizer.nextToken(",");
                    qt = internalTokenizer.nextToken(",");
                    description = internalTokenizer.nextToken(",");
                    fullText += foodCode + "  ";
                    fullText += qt + "  ";
                    fullText += description + "  ";

                    fullText += "\n";
                }
            }
            noticePanel.setText(fullText);
        }
        else if(notice.startsWith("msg(recvFoodMsgEvent")) {
            String payload = notice.split("(?<=Payload) ")[1];
            StringTokenizer token = new StringTokenizer(payload);
            payload = token.nextToken(")");
            token = new StringTokenizer(payload);

            String fullText = "";

            StringTokenizer internalTokenizer = new StringTokenizer((token.nextToken(";")));
            String foodCode = internalTokenizer.nextToken(",");
            String qt = internalTokenizer.nextToken(",");
            String description = internalTokenizer.nextToken(",");
            fullText += foodCode + "  ";
            fullText += qt + "  ";
            fullText += description + "  ";
            fullText += "\n";

            String actualToken = "";
            while (token.hasMoreTokens()) {
                internalTokenizer = new StringTokenizer(token.nextToken(";"));
                foodCode = internalTokenizer.nextToken(",");
                qt = internalTokenizer.nextToken(",");
                description = internalTokenizer.nextToken(",");
                fullText += foodCode + "  ";
                fullText += qt + "  ";
                fullText += description + "  ";

                fullText += "\n";
            }
            noticePanel.setText(fullText);
        }
        else if(notice.startsWith("msg(maitreWarning")) {
            StringTokenizer token = new StringTokenizer(notice, "(");
            token.nextToken();
            token.nextToken();
            String payload = token.nextToken("(");
            payload = new StringTokenizer(payload).nextToken(")");

            if(payload.equals("food_unavailable")){
                Toast toast = Toast.makeText(context, "cibo richiesto non disponibile nel frigo!",
                        Toast.LENGTH_LONG);
                toast.show();
            }


        }
    }

    public void subscribe(String topic){
        if(client.isConnected()) {
            try {
                client.subscribe(topic, 1);
            } catch (MqttException e) {
                e.printStackTrace();
            }
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
