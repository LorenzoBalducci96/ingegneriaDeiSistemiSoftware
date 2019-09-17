package it.dbelvedere.belvederemobilesensorsdata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "MainActivity";
    private SensorManager sensorManager;
    Sensor accellerometro , pressione, giroscopio, luce, temperatura, battitoCardiaco , magnetico;

    TextView xAccellValue, yAccellValue , xGyroValue, yGyroValue, zGyroValue, xMagnValue, yMagnValue, zMagnValue, pressure , light;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Initializing Sensor Services");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        xAccellValue = findViewById(R.id.xAccelValue);
        yAccellValue = findViewById(R.id.yAccelValue);

        xGyroValue = findViewById(R.id.xGyro);
        yGyroValue = findViewById(R.id.yGyro);
        zGyroValue = findViewById(R.id.zGyro);

        pressure = findViewById(R.id.pressure);

        light = findViewById(R.id.light);

        xMagnValue = findViewById(R.id.xMagnValue);
        yMagnValue = findViewById(R.id.yMagnValue);
        zMagnValue = findViewById(R.id.zMagnValue);



        accellerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        pressione = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        giroscopio = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        luce = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        temperatura = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        battitoCardiaco = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_BEAT);
        magnetico = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);


        //Controlli sull'effettiva presenza dei sensori sul dispositivo mobile
        if(accellerometro != null)
            sensorManager.registerListener(this, accellerometro, SensorManager.SENSOR_DELAY_NORMAL);
        else
            Log.d(TAG, "Accellerometro non presente nel dispositivo");

        if(pressione != null)
            sensorManager.registerListener(this, pressione, SensorManager.SENSOR_DELAY_NORMAL);
        else
            Log.d(TAG, "Sensore di pressione non presente nel dispositivo");


        if(giroscopio != null)
            sensorManager.registerListener(this, giroscopio, SensorManager.SENSOR_DELAY_NORMAL);
        else
            Log.d(TAG, "giroscopio non presente nel dispositivo");


        if(luce != null)
            sensorManager.registerListener(this, luce, SensorManager.SENSOR_DELAY_NORMAL);
        else
            Log.d(TAG, "Sensore di luminosita' non presente nel dispositivo");

        if(magnetico != null)
            sensorManager.registerListener(this, magnetico, SensorManager.SENSOR_DELAY_NORMAL);
        else
            Log.d(TAG, "Sensore magnetico non presente nel dispositivo");

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        switch (sensor.getType()) {

            case Sensor.TYPE_ACCELEROMETER :
                Log.d(TAG, "Valori accelerometro letti : X : " + sensorEvent.values[0] + " Y : " + sensorEvent.values[1]);
                xAccellValue.setText("X accelerometro: " + sensorEvent.values[0]);
                yAccellValue.setText("Y accelerometro: " + sensorEvent.values[1]);
                break;

            case Sensor.TYPE_PRESSURE:
                Log.d(TAG, "MBar letti : " + sensorEvent.values[0]);
                pressure.setText("MBar letti nell'ambiente : " + sensorEvent.values[0]);
                break;

            case Sensor.TYPE_GYROSCOPE:
                Log.d(TAG, "Valori giroscopio letti : X : " + sensorEvent.values[0] + " Y : " + sensorEvent.values[1] + "Z : " + sensorEvent.values[2]);
                xGyroValue.setText("X giroscopio: " + sensorEvent.values[0]);
                yGyroValue.setText("Y giroscopio: " + sensorEvent.values[1]);
                zGyroValue.setText("Z giroscopio: " + sensorEvent.values[2]);
                break;

            case Sensor.TYPE_LIGHT:
                Log.d(TAG, "Valori accelerometro letti : X : " + sensorEvent.values[0]);
                light.setText("Lux letti nell'ambiente: " + sensorEvent.values[0]);
                break;

            case Sensor.TYPE_MAGNETIC_FIELD:
                Log.d(TAG, "Valori sensore magnetico letti : X : " + sensorEvent.values[0] + " Y : " + sensorEvent.values[1]  + " Z : " + sensorEvent.values[2]);
                xMagnValue.setText("X magnetico: " + sensorEvent.values[0]);
                yMagnValue.setText("Y magnetico: " + sensorEvent.values[1]);
                zMagnValue.setText("Z magnetico: " + sensorEvent.values[2]);
        }
    }

    @Override
    //Not used
    public void onAccuracyChanged(Sensor sensor, int i){
    }
}
