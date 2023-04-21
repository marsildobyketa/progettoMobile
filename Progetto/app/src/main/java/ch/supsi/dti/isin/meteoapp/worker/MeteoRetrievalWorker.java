package ch.supsi.dti.isin.meteoapp.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONException;

import ch.supsi.dti.isin.meteoapp.model.Location;
import ch.supsi.dti.isin.meteoapp.model.LocationsHolder;
import ch.supsi.dti.isin.meteoapp.model.WeatherModel;

public class MeteoRetrievalWorker extends Worker{

    private Context mContext;

    public MeteoRetrievalWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
    }

    @Override
    public Result doWork(){
        // Controlla temperatura della posizione corrente
        Location currentLocation = LocationsHolder.getMyLocation();
        WeatherModel wm = new WeatherModel();

        if(currentLocation != null){
            // Retrieve temperature
            double temperature;
            try {
                temperature = wm.getTemperatureInLocation(currentLocation.getName());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            // Check if temperature is above or below bounds
            if(temperature < 0 || temperature > 5){
                // Crea e manda notifica
                this.sendNotification(temperature);
            }
        }
        return Result.success(); // con failure() lo scheduling viene interrotto!
    }

    private void sendNotification(double currentTemp){
        NotificationCompat.Builder mBuilder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mBuilder = new NotificationCompat.Builder(mContext, "weatherChannelID")
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentTitle("The temperature has" + ((currentTemp > 0) ? "risen" : "fallen")
                            + " a lot.")
                    .setContentText("Current temperature: " + currentTemp + " °C")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }

        NotificationManagerCompat manager = NotificationManagerCompat.from(mContext);
        manager.notify(0, mBuilder.build());
    }
}
