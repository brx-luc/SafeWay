package zli.safeway;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.media.audiofx.Equalizer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.net.URI;
import java.util.ArrayList;

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
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 100000;
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
                Dexter.withActivity(MainActivity.this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        requestinLocationUpdates = true;
                        startLocationUpdates();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        if(permissionDeniedResponse.isPermanentlyDenied()){
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                    }
                }).check();

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

    @SuppressLint("IntentReset")
    private void sendSMS(){

        DBHelper db = new DBHelper(this);
        ArrayList<String> data = db.getAllNumber();

        if(currentLocation != null){
            latitude = currentLocation.getLatitude();
            longitude = currentLocation.getLongitude();

        }
        message = "lat: "+ latitude +", long:" + longitude;
        SmsManager smsManager = SmsManager.getDefault();

            for(int i = 0; i < data.size(); i++){
                phoneNo = data.get(i);
                smsManager.sendTextMessage(phoneNo, null, message, null, null);
                Toast.makeText(getApplicationContext(), message,
                        Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
    public void stopLocationUpdates(View v){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @SuppressLint("IntentReset")
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(getApplicationContext(), "Location updates stopped!",
                        Toast.LENGTH_SHORT).show();
                View root = view.getRootView();
                root.setBackgroundColor(Color.WHITE);
                usage.setText("Shake your phone to share your location");
                ArrayList<String> data = db.getAllNumber();

                for(int i = 0; i < data.size(); i++) {
                    phoneNo = data.get(i);
                    smsManager.sendTextMessage(phoneNo, null, "I arrived safely", null, null);
                }

            }
        });
    }

    public void openSettings(){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



}