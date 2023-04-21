package ch.supsi.dti.isin.meteoapp.activities;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ch.supsi.dti.isin.meteoapp.R;
import ch.supsi.dti.isin.meteoapp.database.CursorWrapper;
import ch.supsi.dti.isin.meteoapp.database.DataBaseHelper;
import ch.supsi.dti.isin.meteoapp.database.DataBaseSchema;
import ch.supsi.dti.isin.meteoapp.database.LocationContentValues;
import ch.supsi.dti.isin.meteoapp.fragments.ListFragment;
import ch.supsi.dti.isin.meteoapp.model.LocationsHolder;
import ch.supsi.dti.isin.meteoapp.model.WeatherModel;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;

public class MainActivity extends AppCompatActivity {

    private final static int LOCATION_REQUEST_PERMISSION_CODE = 1;
    private static Fragment fragment;
    private List<ch.supsi.dti.isin.meteoapp.model.Location> list = new ArrayList<>();
    private DataBaseHelper mDatabase;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_single_fragment);
        FragmentManager fm = getSupportFragmentManager();
        fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new ListFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
        requestPermission();
    }



    private void requestPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Non ho i permessi
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST_PERMISSION_CODE
            );
        } else {
            // Ho già i permessi
            startLocationListener();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch(requestCode){
            case LOCATION_REQUEST_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationListener();
                }
                return;
            }
        }
    }

    private void startLocationListener() {
        LocationParams.Builder builder = new LocationParams.Builder()
                .setAccuracy(LocationAccuracy.HIGH)
                .setDistance(0)
                .setInterval(15000); // 5 sec

        SmartLocation.with(this)
                .location().continuous().config(builder.build())
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(android.location.Location location) {
                        WeatherModel wm = new WeatherModel();

                        // Needed to avoid exceptions
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);

                        String cityName = "";

                        try {
                            cityName = wm.getLocationNameFromCoordinates(location.getLatitude(), location.getLongitude());
                        } catch (IOException | JSONException e) {
                            throw new RuntimeException(e);
                        }
                        // Add current location
                        if(MainActivity.fragment != null){
                            ((ListFragment)MainActivity.fragment)
                                .addFirstLocation(
                                    MainActivity.this,
                                        cityName
                                    );
                        }
                    }
                });
    }
}
