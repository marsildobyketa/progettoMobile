package ch.supsi.dti.isin.meteoapp.activities;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import ch.supsi.dti.isin.meteoapp.R;
import ch.supsi.dti.isin.meteoapp.database.CursorWrapper;
import ch.supsi.dti.isin.meteoapp.database.DataBaseHelper;
import ch.supsi.dti.isin.meteoapp.database.DataBaseSchema;
import ch.supsi.dti.isin.meteoapp.database.LocationContentValues;
import ch.supsi.dti.isin.meteoapp.fragments.ListFragment;
import ch.supsi.dti.isin.meteoapp.model.Location;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;

public class MainActivity extends AppCompatActivity {

    private final static int LOCATION_REQUEST_PERMISSION_CODE = 1;

    private SQLiteDatabase mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Context context = getApplicationContext();
        mDatabase = new DataBaseHelper(context).getWritableDatabase();

        insertData();

        readData();

        setContentView(R.layout.fragment_single_fragment);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new ListFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }

        requestPermission();

        mDatabase.close();
    }
    private void insertData() {

        // istanzio un oggetto TestEntry
        Location entry = new Location("London");
        // istanzio un ContentValues per la entry appena istanziata
        ContentValues values = LocationContentValues.getContentValues(entry);
        // chiamo il metodo insert sul db che ho in memoria
        mDatabase.insert(DataBaseSchema.TestTable.NAME, null, values);

    }

    private void readData() {
        StringBuilder res = new StringBuilder("Data:");

        // istanzio un oggetto CursorWrapper
        CursorWrapper cursor = queryData();

        // itero, tramite il cursor, tutti i risultati rirornati
        try {
            // mi sposto al primo elemento
            cursor.moveToFirst();

            // fintanto che ci sono elementi
            while (!cursor.isAfterLast()) {
                // mi faccio dare l'oggetto TestEntry dal cursor
                Location entry = cursor.getEntry();
                res.append("\n").append(entry.getName());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        // mostro i risultati
        Toast.makeText(this, res.toString(), Toast.LENGTH_SHORT).show();
        Log.i("Locationn", res.toString());
    }

    private CursorWrapper queryData() {
        Cursor cursor = mDatabase.query(
                DataBaseSchema.TestTable.NAME,
                null, // columns - null selects all columns
                null, // where clause
                null, // where args
                null, // groupBy
                null,  // having
                null  // orderBy
        );
        return new CursorWrapper(cursor);
    }


    private void requestPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Non ho i permessi
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_PERMISSION_CODE);

        } else {
            // Ho già i permessi
            startLocationListener();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
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
                .setInterval(5000); // 5 sec

        SmartLocation.with(this)
                .location().continuous().config(builder.build())
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(android.location.Location location) {
                        // Do something
                        Log.i("MY_TAG", "Location" + location.toString());
                        System.out.println("Location: " + location.toString());
                    }
                });
    }
}
