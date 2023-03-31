package ch.supsi.dti.isin.meteoapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ch.supsi.dti.isin.meteoapp.R;
import ch.supsi.dti.isin.meteoapp.activities.DetailActivity;
import ch.supsi.dti.isin.meteoapp.activities.MainActivity;
import ch.supsi.dti.isin.meteoapp.database.DataBaseHelper;
import ch.supsi.dti.isin.meteoapp.model.LocationsHolder;
import ch.supsi.dti.isin.meteoapp.model.Location;
import ch.supsi.dti.isin.meteoapp.model.WeatherCondition;
import ch.supsi.dti.isin.meteoapp.model.WeatherModel;

public class ListFragment extends Fragment {
    private RecyclerView mLocationRecyclerView;
    private LocationAdapter mAdapter;
    private DataBaseHelper mDatabase;
    private Button mButtonDelete;

    public void addFirstLocation(Context context, String locationString){
        LocationsHolder.get(context).updateFirstLocation(
                locationString
        );
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mLocationRecyclerView = view.findViewById(R.id.recycler_view);
       // button = view.findViewById(R.id.menu_add);
        mLocationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<Location> locations = LocationsHolder.get(getActivity()).getLocations();
        mAdapter = new LocationAdapter(locations);
        mLocationRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                Context context= getContext();
                showDialog(context);
                Log.d("MENU","click menu");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDialog(Context context) {

        mDatabase = new DataBaseHelper(context);
        // Crea un nuovo oggetto EditText da aggiungere alla finestra di dialogo
        final EditText input = new EditText(context);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Insert Location");
       // builder.setMessage("");
        builder.setView(input);

        // Aggiungi pulsante OK
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String inputText = input.getText().toString();
                Log.d("ADD",inputText);
                Location location = new Location(inputText);
                mDatabase.insertData(location);

               LocationsHolder.get(context).appendLocations(location);
                // Azioni da eseguire quando si fa clic sul pulsante OK
                mAdapter.notifyDataSetChanged();

                mDatabase.close();
            }
        });

        // Aggiungi pulsante Annulla
        builder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Azioni da eseguire quando si fa clic sul pulsante Annulla
            }
        });

        // Crea e visualizza la finestra di dialogo
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    // Holder

    private class LocationHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mNameTextView;
        private Location mLocation;
        private ImageView mImageViewListItemWeatherIcon;
        private TextView mCountry;


        public LocationHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item, parent, false));
            itemView.setOnClickListener(this);
            mNameTextView = itemView.findViewById(R.id.name);
            mImageViewListItemWeatherIcon = itemView.findViewById(R.id.ivListItemWeatherIcon);
            mButtonDelete = itemView.findViewById(R.id.delete);
            mCountry = itemView.findViewById(R.id.tvCountry);

            mButtonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Location location = null;

                    LocationsHolder.get(getContext()).removeItem(getContext(),mLocation.getId());
                    mAdapter.notifyDataSetChanged();

                    // Richiama il metodo removeItem per eliminare l'elemento dalla lista di dati dell'adapter
                    // removeItem(location);
                }
            });
        }

        private void removeItem(int location) {
            // TODO: rimuovi location
        }

        @Override
        public void onClick(View view) {
            Intent intent = DetailActivity.newIntent(getActivity(), mLocation.getId());
            startActivity(intent);
        }

        public void bind(Location location){
            mLocation = location;
            mNameTextView.setText(mLocation.getName());

            // Get weather icon
            //Make api calls
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(() -> {
                WeatherCondition weather;
                Bitmap weatherIcon;
                String countryName;
                try {
                    WeatherModel wm = new WeatherModel();
                    weather = wm.getWeatherInLocation(
                            location.getName()
                    );
                    weatherIcon = wm.getWeatherIcon(
                            weather.getIcon()
                    );
                    countryName = wm.getCountryName(
                            weather.getCountryCode()
                    );
                } catch (JSONException e) {
                    System.out.println("JSONException: " + e.getLocalizedMessage());
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    System.out.println("IOException: " + e.getLocalizedMessage());
                    throw new RuntimeException(e);
                }
                getActivity().runOnUiThread(() -> updateLocationData(weatherIcon, countryName));
            });
            executor.shutdown();
        }

        private void updateLocationData(Bitmap weatherIcon, String countryName){
            mImageViewListItemWeatherIcon.setImageBitmap(weatherIcon);
            mCountry.setText(countryName);
        }
    }

    // Adapter

    private class LocationAdapter extends RecyclerView.Adapter<LocationHolder> {
        private List<Location> mLocations;

        public LocationAdapter(List<Location> locations) {
            mLocations = locations;
        }

        @Override
        public LocationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new LocationHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(LocationHolder holder, int position) {
            Location location = mLocations.get(position);
            holder.bind(location);
        }

        @Override
        public int getItemCount() {
            return mLocations.size();
        }
    }
}
