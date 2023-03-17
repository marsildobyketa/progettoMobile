package ch.supsi.dti.isin.meteoapp.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherModel {

    private JSONObject requestWeather(String location){
        JSONObject response = null;
        try{
            // Implementare logica

            // Set main.temp to Metric, otherwise temnperature will be in Kelvin!!

            response = new JSONObject("{\"coord\":{\"lon\":-0.13,\"lat\":51.51},\"weather\":[{\"id\":300,\"main\":\"Drizzle\",\"description\":\"thunderstorm with heavy drizzle\",\"icon\":\"09d\"}],\"base\":\"stations\",\"main\":{\"temp\":17,\"pressure\":1012,\"humidity\":81,\"temp_min\":-2,\"temp_max\":21},\"visibility\":10000,\"wind\":{\"speed\":4.1,\"deg\":80},\"clouds\":{\"all\":90},\"dt\":1485789600,\"sys\":{\"type\":1,\"id\":5091,\"message\":0.0103,\"country\":\"GB\",\"sunrise\":1485762037,\"sunset\":1485794875},\"id\":2643743,\"name\":\"London\",\"cod\":200}\n");
        }catch(JSONException jsone){
            System.err.println("Error while requesting JSON.\n" + jsone.getMessage());
        }
        return response;
    }

    public WeatherCondition getWeatherInLocation(String location) throws JSONException {
        JSONObject weather = this.requestWeather(location);
        if(weather == null) return null;

        // Needed to only get the first weather element, otherwise because of the JSON format
        // the JSONObject class won't know how to handle multiple elements in it.
        //
        // Example:
        //
        //  This works:
        //  "coord": {
        //      "lon": -0.13,
        //      "lat": 51.51
        //  }
        //
        //  This does not work (without the following change):
        //  "weather": [
        //      {
        //          "id": 300,
        //          "main": "Drizzle",
        //          "description": "light intensity drizzle",
        //          "icon": "09d"
        //      }
        //  ]
        //
        JSONObject subWeather = new JSONObject(weather.getJSONArray("weather").get(0).toString());

        return new WeatherCondition(
                subWeather.getInt("id"),
                subWeather.getString("main"),
                subWeather.getString("description"),
                subWeather.getString("icon"),
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

    /*
    RESPONSE EXAMPLE

    {
     "coord": {
       "lon": -0.13,
       "lat": 51.51
     },
     "weather": [
       {
         "id": 300,
         "main": "Drizzle",
         "description": "thunderstorm with heavy drizzle",
         "icon": "09d"
       }
     ],
     "base": "stations",
     "main": {
       "temp": 17,
       "pressure": 1012,
       "humidity": 81,
       "temp_min": -2,
       "temp_max": 21
     },
     "visibility": 10000,
     "wind": {
       "speed": 4.1,
       "deg": 80
     },
     "clouds": {
       "all": 90
     },
     "dt": 1485789600,
     "sys": {
       "type": 1,
       "id": 5091,
       "message": 0.0103,
       "country": "GB",
       "sunrise": 1485762037,
       "sunset": 1485794875
     },
     "id": 2643743,
     "name": "London",
     "cod": 200
     }
    * */
}
