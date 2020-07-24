package com.example.gptask3;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
public class MainActivity extends AppCompatActivity implements SensorEventListener{


    public boolean flag = false;

    private static final String FILENAME = "accData.txt";
    private TextView xAccTxt, yAccTxt, zAccTxt, prompt,testText;
    private Sensor accelerometerSensor, gyroscopeSensor;
    private SensorManager SM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sBtn = findViewById(R.id.startBtn);
        Button endBtn = findViewById(R.id.stopBtn);


        // Create out sensor maneger
        SM = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Accelerometer sensor
        accelerometerSensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Gyroscope sensor
        gyroscopeSensor = SM.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        // Register Accelerometer sensor listener
        SM.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);

        // Register Gyroscope sensor listener
        SM.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);


        // Assign TextView
        xAccTxt = (TextView) findViewById(R.id.xAcc);
        yAccTxt = (TextView) findViewById(R.id.yAcc);
        zAccTxt = (TextView) findViewById(R.id.zAcc);
        prompt = (TextView) findViewById(R.id.prompt);
        testText = (TextView) findViewById(R.id.testText);


    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;

        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            xAccTxt.setText("X : " + sensorEvent.values[0]);
            yAccTxt.setText("Z : " + sensorEvent.values[1]);
            zAccTxt.setText("Z : " + sensorEvent.values[2]);

            if (this.flag) {
                Point point;
                // assign values to db
                try{

                    point = new Point(-1,sensorEvent.values[0],sensorEvent.values[1] , sensorEvent.values[2],false);

                    Log.d("Object is : " , String.valueOf(point));
                    testText.setText("X : " + String.valueOf(point.getX()) + " , Y : " + String.valueOf(point.getY()) + " Z : " + String.valueOf(point.getZ()));
                }catch (Exception e){
                    Toast.makeText(this, "Error making the object", Toast.LENGTH_SHORT).show();
                    point = new Point(-1,0,0 , 0,false);
                }

                DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
                boolean success  = dataBaseHelper.addRecord(point);
            }
            else
            {

            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // not in use
    }

    public void startRecording(View view) {
        this.flag = true;
        prompt.setText("Capturing stream ... \nStream Flag is now False");
        DataBaseHelper dbh = new DataBaseHelper(MainActivity.this);
        dbh.streamStandBy();
        Toast.makeText(this, "Recording Stream", Toast.LENGTH_SHORT).show();
    }

    public void stopRecording(View view) {
        this.flag = false;
        Point point;
        point = new Point(-1,0,0 , 0,true);
        DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
        boolean success  = dataBaseHelper.addRecord(point);
        prompt.setText("");
        Toast.makeText(this, "Stream Captured Successfully", Toast.LENGTH_SHORT).show();
        boolean x = dataBaseHelper.steamArrived();
        prompt.setText("Stream Captured . \nStream Flag is now True ");

    }
}