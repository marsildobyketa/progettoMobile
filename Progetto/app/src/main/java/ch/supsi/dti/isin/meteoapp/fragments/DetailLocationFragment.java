package ch.supsi.dti.isin.meteoapp.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;

import java.util.UUID;

import ch.supsi.dti.isin.meteoapp.R;
import ch.supsi.dti.isin.meteoapp.model.LocationsHolder;
import ch.supsi.dti.isin.meteoapp.model.Location;
import ch.supsi.dti.isin.meteoapp.model.WeatherCondition;
import ch.supsi.dti.isin.meteoapp.model.WeatherModel;

public class DetailLocationFragment extends Fragment {
    private static final String ARG_LOCATION_ID = "location_id";

    private Location mLocation;
    private TextView mLocationNameTextView;
    private TextView mDescriptionTextView;
    private TextView mPressureTextView;

    public static DetailLocationFragment newInstance(UUID locationId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_LOCATION_ID, locationId);

        DetailLocationFragment fragment = new DetailLocationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID locationId = (UUID) getArguments().getSerializable(ARG_LOCATION_ID);
        mLocation = LocationsHolder.get(getActivity()).getLocation(locationId);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail_location, container, false);

        WeatherModel wm = new WeatherModel();
        try {
            WeatherCondition weather = wm.getWeatherInLocation(mLocation.getName());
            // Bind components
            mLocationNameTextView = v.findViewById(R.id.tvLocationName);
            mDescriptionTextView = v.findViewById(R.id.tvDescriptionValue);
            mPressureTextView = v.findViewById(R.id.tvPressureValue);

            // Set values in view
            mLocationNameTextView.setText(weather.getCityName());
            mDescriptionTextView.setText(weather.getDescription());
            mPressureTextView.setText(weather.getPressure() + " [Pa]");

        } catch (JSONException e) {
            System.err.println(e.getMessage());
            //throw new RuntimeException(e);
        }
        return v;
    }
}

