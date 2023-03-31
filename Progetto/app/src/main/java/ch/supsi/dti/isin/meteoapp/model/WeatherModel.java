package ch.supsi.dti.isin.meteoapp.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class WeatherModel {

    private final URLManager urlManager;

    public WeatherModel(){
        this.urlManager = new URLManager();
    }

    private JSONObject requestWeather(String location){
        try{
            // Geocoding
            JSONObject geocodingJSON = new JSONObject(
                this.urlManager.getUrlString(
                    "https://api.openweathermap.org/geo/1.0/direct?q="
                    + location + "&limit=1&appid=" + URLManager.OPEN_WEATHERMAP_API_KEY
                ).replace("[", "").replace("]", "")
            );

            if(!(geocodingJSON.has("lat") && geocodingJSON.has("lon"))) return null;

            // Get weather
            return  new JSONObject(
                this.urlManager.getUrlString(
                    "https://api.openweathermap.org/data/2.5/weather?lat="
                    + geocodingJSON.getDouble("lat") + "&lon="
                    + geocodingJSON.getDouble("lon") + "&units=metric&appid=" + URLManager.OPEN_WEATHERMAP_API_KEY
                ).replace("[", "").replace("]", "")
            );

        }catch(JSONException jsone){
            System.err.println("Error while requesting JSON.\n" + jsone.getMessage());
        } catch (IOException e) {
            System.out.println("IOException.\n" + e.getMessage());
        }
        return null;
    }

    public String getCountryName(String countryCode) throws IOException, JSONException {
        // Make request and keep only country name.
        // Not the prettiest solution but it works, JSONObject keeps
        // throwing exceptions, I suppose because of special characters.
        String response = this.urlManager.getUrlString(
                "https://restcountries.com/v3.1/alpha/" + countryCode
        ).replace("[{\"name\":{\"common\":\"", "");

        return response.substring(0, response.indexOf("\""));
    }

    public Bitmap getWeatherIcon(String iconName) throws IOException {
        return this.getImageFromUrl(
                "https://openweathermap.org/img/wn/" +
                        iconName
                        + "@4x.png"
        );
    }

    public Bitmap getCountryFlagIcon(String countryCode) throws IOException {
        return this.getImageFromUrl(
                "https://flagsapi.com/" +
                        countryCode.toUpperCase()
                        + "/flat/64.png"
        );
    }

    private Bitmap getImageFromUrl(String url) throws IOException {
        byte[] responseData = this.urlManager.getUrlBytes(url);
        // Translates bytes into bitmap image
        return BitmapFactory.decodeByteArray(responseData, 0, responseData.length);
    }

    public WeatherCondition getWeatherInLocation(String location) throws JSONException {
        JSONObject weather = this.requestWeather(location);
        return new WeatherCondition(
                weather.getJSONObject("weather").getInt("id"),
                weather.getJSONObject("weather").getString("main"),
                weather.getJSONObject("weather").getString("description"),
                weather.getJSONObject("weather").getString("icon"),
                weather.getJSONObject("main").getDouble("temp"),
                weather.getJSONObject("main").getInt("pressure"),
                weather.getJSONObject("main").getInt("humidity"),
                weather.getJSONObject("main").getDouble("temp_min"),
                weather.getJSONObject("main").getDouble("temp_max"),
                weather.getString("name"),
                weather.getJSONObject("sys").getString("country"),
                weather.getJSONObject("sys").getLong("sunrise"),
                weather.getJSONObject("sys").getLong("sunset"),
                weather.getJSONObject("wind").getDouble("speed")
        );
    }
}
