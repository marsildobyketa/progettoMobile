package ch.supsi.dti.isin.meteoapp.worker;

import android.app.NotificationManager;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONException;

import ch.supsi.dti.isin.meteoapp.model.Location;
import ch.supsi.dti.isin.meteoapp.model.LocationsHolder;
import ch.supsi.dti.isin.meteoapp.model.WeatherModel;

public class MeteoRetrievalWorker extends Worker{

    public MeteoRetrievalWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
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
            if(temperature < 0 || temperature > 30){
                // Crea e manda notifica
                // TODO
            }
        }
        return Result.success(); // con failure() lo scheduling viene interrotto!
    }

    private void sendNotification(int i) {
        NotificationManager mNotificationManager = (NotificationManager)
        getSystemService(Context.NOTIFICATION_SERVICE);

        // creo il contenuto della notifica
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "default")
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle("Title")
                .setContentText("i: " + i)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());
    }
}
