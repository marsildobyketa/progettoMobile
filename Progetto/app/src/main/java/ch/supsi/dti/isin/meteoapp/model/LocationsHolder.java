package ch.supsi.dti.isin.meteoapp.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LocationsHolder {
    private static LocationsHolder sLocationsHolder;
    private static List<Location> mLocations;

    public static LocationsHolder get(Context context){
        if (sLocationsHolder == null)
            sLocationsHolder = new LocationsHolder(context);

        return sLocationsHolder;
    }

    private LocationsHolder(Context context){
        mLocations = new ArrayList<>();

        // Current location gets added in MainActivity
        mLocations.add(new Location("-"));

        // Load saved locations
        mLocations.add(new Location("London"));
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
