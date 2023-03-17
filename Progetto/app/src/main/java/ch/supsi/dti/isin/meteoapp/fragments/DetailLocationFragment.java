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

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
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
    private TextView mSunriseTextView;
    private TextView mSunsetTextView;
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
            mSunriseTextView = v.findViewById(R.id.tvSunriseValue);
            mSunsetTextView = v.findViewById(R.id.tvSunsetValue);
            mActualTemperatureTextView = v.findViewById(R.id.tvActualTemperature);
            mMinTemperatureTextView = v.findViewById(R.id.tvMinTemperature);
            mMaxTemperatureTextView = v.findViewById(R.id.tvMaxTemperature);

            // Set values in view
            mActualTemperatureTextView.setText(Math.round(weather.getTemperature()) + "° C");
            mMinTemperatureTextView.setText("Min: " + Math.round(weather.getMinTemperature()) + "°");
            mMaxTemperatureTextView.setText("Max: " + Math.round(weather.getMaxTemperature()) + "°");

            // TODO: Set country icon
            mLocationNameTextView.setText(weather.getCityName());
            // TODO: Tabulations are temporary
            mDescriptionTextView.setText(getFormattedString(weather.getDescription()));
            mPressureTextView.setText(weather.getPressure() + " [Pa]");
            mHumidityTextView.setText(weather.getHumidity() + " %");
            mSunriseTextView.setText(getFormattedDate(weather.getSunrise()));
            mSunsetTextView.setText(getFormattedDate(weather.getSunset()));

            // TODO: Use unused information above such sunrise, sunset, ...
            // TODO: On rotation put infos on the right instead of underneath?

        } catch (JSONException e) {
            System.err.println(e.getMessage());
        }
        return v;
    }

    private String getFormattedDate(long timestamp){
        // Given timestamp is in UNIX time, therefore must multiply by 1000.
        Date d = new Date(timestamp * 1000);
        return String.format("%02d : %02d : %02d", d.getHours(), d.getMinutes(), d.getSeconds());
    }

    private String getFormattedString(String str){
        String[] words = str.split(" ");
        StringBuilder builder = new StringBuilder();
        for(String word : words){
            builder.append(word.substring(0, 1).toUpperCase()).append(word.substring(1)).append(" ");
        }
        return builder.toString().trim();
    }
}

