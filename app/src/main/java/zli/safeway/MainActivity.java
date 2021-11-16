package zli.safeway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Sensor acceleratorSensor;
    private SensorManager sensorManager;
    private Button editButton;
    private Button addButton;
    private float accelerator;
    private float currentAccelerator;
    private float lastAccelerator;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //acceleratorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Objects.requireNonNull(sensorManager).registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), sensorManager.SENSOR_DELAY_NORMAL);
        accelerator = 0.00f;
        currentAccelerator = SensorManager.GRAVITY_EARTH;
        lastAccelerator = SensorManager.GRAVITY_EARTH;

        editButton = findViewById(R.id.editButton1);
        addButton = findViewById(R.id.addButton1);
    }

    SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            lastAccelerator = currentAccelerator;
            currentAccelerator = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = currentAccelerator -lastAccelerator;
            accelerator = accelerator * 0.9f + delta + 0.1f;
            if (accelerator > 12) {
                Toast.makeText(getApplicationContext(), "Shake event detected", Toast.LENGTH_SHORT).show();
                editButton.setBackgroundColor(Color.BLUE);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onResume() {
        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }
    @Override
    protected void onPause() {
        sensorManager.unregisterListener(sensorEventListener);
        super.onPause();
    }

    public void goToAddNumberActivity(View v){
        Intent intent = new Intent(this, AddNumberActivity.class);
        startActivity(intent);
    }

    public void goToEditNumbersActivity(View v){
        Intent intent = new Intent(this, EditNumberActivity.class);
        startActivity(intent);
    }
}