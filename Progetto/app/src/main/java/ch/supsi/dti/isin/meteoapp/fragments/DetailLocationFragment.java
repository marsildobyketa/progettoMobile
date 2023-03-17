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
    private TextView mHumidityTextView;
    private TextView mWindSpeedTextView;
    private TextView mActualTemperatureTextView;
    private TextView mMinTemperatureTextView;
    private TextView mMaxTemperatureTextView;

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
            mHumidityTextView = v.findViewById(R.id.tvHumidityValue);
            mWindSpeedTextView = v.findViewById(R.id.tvWindSpeedValue);
            mActualTemperatureTextView = v.findViewById(R.id.tvActualTemperature);
            mMinTemperatureTextView = v.findViewById(R.id.tvMinTemperature);
            mMaxTemperatureTextView = v.findViewById(R.id.tvMaxTemperature);

            // Set values in view
            mActualTemperatureTextView.setText(weather.getTemperature() + "° C");
            mMinTemperatureTextView.setText(weather.getMinTemperature() + "° C");
            mMaxTemperatureTextView.setText(weather.getMaxTemperature() + "° C");

            // TODO: Set country icon
            mLocationNameTextView.setText(weather.getCityName().toUpperCase());
            // TODO: Tabulations are temporary
            mDescriptionTextView.setText("\t" + weather.getDescription());
            mPressureTextView.setText("\t\t\t\t\t\t" + weather.getPressure() + " [Pa]");
            mHumidityTextView.setText("\t\t\t\t\t\t" + weather.getHumidity() + " %");
            mWindSpeedTextView.setText("\t\t\t" + weather.getWindSpeed() + " [m/s]");

            // TODO: Use unused information above such as temp, max, min, sunrise, sunset, ...
            // TODO: On rotation put infos on the right instead of underneath?

        } catch (JSONException e) {
            System.err.println(e.getMessage());
        }
        return v;
    }
}

