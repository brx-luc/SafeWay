package zli.safeway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Sensor acceleratorSensor;
    private SensorManager sensorManager;
    private Button editButton;
    private Button addButton;
    private Button finish;
    private float accelerator;
    private float currentAccelerator;
    private float lastAccelerator;
    private View view;
    private TextView usage;

    private static final int REQUEST_LOCATION = 1;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 300000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    private static final int REQUEST_CHECK_SETTINGS = 100;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private SettingsClient settingsClient;
    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;
    private LocationCallback locationCallback;
    private Location currentLocation;
    private Boolean requestinLocationUpdates;

    double latitude, longitude;

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    private SmsManager smsManager = SmsManager.getDefault();
    private String phoneNo;
    private String message;

    DBHelper db = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //acceleratorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        accelerator = 0.00f;
        currentAccelerator = SensorManager.GRAVITY_EARTH;
        lastAccelerator = SensorManager.GRAVITY_EARTH;

        editButton = findViewById(R.id.editButton1);
        addButton = findViewById(R.id.addButton1);
        finish = findViewById(R.id.finishButton);
        view = findViewById(R.id.parent);
        usage = findViewById(R.id.usage);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        init();

    }

    SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            lastAccelerator = currentAccelerator;
            currentAccelerator = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = currentAccelerator - lastAccelerator;
            accelerator = accelerator * 0.9f + delta + 0.1f;
            if (accelerator > 2) {
                View root = view.getRootView();
                root.setBackgroundColor(Color.LTGRAY);
                usage.setText("Location is being shared");
                startLocationUpdates();

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

    public void goToAddNumberActivity(View v) {
        Intent intent = new Intent(this, AddNumberActivity.class);
        startActivity(intent);
    }

    public void goToEditNumbersActivity(View v) {
        Intent intent = new Intent(this, EditNumberActivity.class);
        startActivity(intent);
    }


    private void init(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        settingsClient = LocationServices.getSettingsClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                currentLocation = locationResult.getLastLocation();

            }
        };
        requestinLocationUpdates = false;
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        locationSettingsRequest = builder.build();
    }

    private void startLocationUpdates(){
        settingsClient.checkLocationSettings(locationSettingsRequest).addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @SuppressLint("MissingPermission")
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                sendSMS();

            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void sendSMS(){

        DBHelper db = new DBHelper(this);
        ArrayList<String> data = db.getAllNumber();

        if(currentLocation != null){
            latitude = currentLocation.getLatitude();
            longitude = currentLocation.getLongitude();

        }
        message = "lat: "+ latitude +", long:" + longitude;

        Toast.makeText(this, message , Toast.LENGTH_LONG).show();
        for(int i = 0; i < data.size(); i++){
            phoneNo = data.get(i);
            Toast.makeText(getApplicationContext(), phoneNo,
                    Toast.LENGTH_SHORT).show();
            try{
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("smsto: "));
                intent.setType("vnd.android-dir/mms-sms");
                intent.putExtra("address", phoneNo);
                intent.putExtra("sms_body", message);
                startActivity(Intent.createChooser(intent, "Send sms via:"));
            }catch(Exception e){

            }

            /*if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)){
                    Toast.makeText(this, "Test", Toast.LENGTH_LONG).show();
                    SmsManager smsManager = SmsManager.getDefault();

                    for(int i = 0; i < data.size(); i++){
                        phoneNo = data.get(i);
                        smsManager.sendTextMessage(phoneNo, null, message, null, null);
                        Toast.makeText(getApplicationContext(), "SMS Sent!",
                                Toast.LENGTH_LONG).show();
                    }

                }else{
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
                }


        }*/



    }}

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        /*DBHelper db = new DBHelper(this);
        ArrayList<String> data = db.getAllNumber();

        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_SEND_SMS:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    SmsManager smsManager = SmsManager.getDefault();

                    for(int i = 0; i < data.size(); i++){
                        phoneNo = data.get(i);
                        smsManager.sendTextMessage(phoneNo, null, message, null, null);
                        Toast.makeText(getApplicationContext(), "SMS Sent!",
                                Toast.LENGTH_LONG).show();
                    }

                }
            }
        }*/

    }
    public void stopLocationUpdates(View v){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(getApplicationContext(), "Location updates stopped!",
                        Toast.LENGTH_SHORT).show();
                View root = view.getRootView();
                root.setBackgroundColor(Color.WHITE);
                usage.setText("Shake your phone to share your location");
                ArrayList<String> data = db.getAllNumber();

                for(int i = 0; i < data.size(); i++){
                    phoneNo = data.get(i);
                    Toast.makeText(getApplicationContext(), phoneNo,
                            Toast.LENGTH_SHORT).show();
                    try{
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("smsto: "));
                        intent.setType("vnd.android-dir/mms-sms");
                        intent.putExtra("address", phoneNo);
                        intent.putExtra("sms_body", "I arrived safely");
                        startActivity(Intent.createChooser(intent, "Send sms via:"));
                    }catch(Exception e){

                    }

                    //smsManager.sendTextMessage(phoneNo, null, "I arrived safely", null, null);

                }

            }
        });
    }

}