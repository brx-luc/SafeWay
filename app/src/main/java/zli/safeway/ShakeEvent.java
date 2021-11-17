package zli.safeway;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ShakeEvent extends Service {

    LocationManager locationManager;

    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder{
        ShakeEvent getService(){return ShakeEvent.this;}
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){return START_STICKY;}

    @Override
    public IBinder onBind(Intent intent){return binder;}

    public void getLocation(){

    }

}
