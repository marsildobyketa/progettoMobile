package ch.supsi.dti.isin.meteoapp.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ch.supsi.dti.isin.meteoapp.database.DataBaseHelper;

public class LocationsHolder {
    private static LocationsHolder sLocationsHolder;
    private static List<Location> mLocations ;

    DataBaseHelper db;

    public static LocationsHolder get(Context context){
        if (sLocationsHolder == null)
            sLocationsHolder = new LocationsHolder(context);

        return sLocationsHolder;
    }

    private LocationsHolder(Context context){

        mLocations = new ArrayList<>();
        db = new DataBaseHelper(context);

        // Current location gets added in MainActivity
        mLocations.add(new Location("-"));
        mLocations = db.readData(mLocations);




    }

    public List<Location> getLocations() {
        return mLocations;
    }

    public Location getLocation(UUID id) {
        for (Location location : mLocations) {
            if (location.getId().equals(id))
                return location;
        }
        return null;
    }

    public void updateFirstLocation(String locationName){
        mLocations.get(0).setName(locationName);
    }
}
