package zli.safeway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Sensor acceleratorSensor;
    private SensorManager sensorManager;
    private Button editButton;
    private Button addButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editButton = findViewById(R.id.editButton1);
        addButton = findViewById(R.id.addButton1);
    }

    SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    public void goToAddNumberActivity(View v){
        Intent intent = new Intent(this, AddNumberActivity.class);
        startActivity(intent);
    }

    public void goToEditNumbersActivity(View v){
        Intent intent = new Intent(this, EditNumberActivity.class);
        startActivity(intent);
    }
}