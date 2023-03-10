package ch.supsi.dti.isin.meteoapp.model;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherModel {
    private JSONObject requestWeather(String location){
        JSONObject response = null;
        try{
            // Implementare logica

            // Set main.temp to Metric, otherwise temnperature will be in Kelvin!!

            response = new JSONObject("{\"coord\":{\"lon\":-0.13,\"lat\":51.51},\"weather\":[{\"id\":300,\"main\":\"Drizzle\",\"description\":\"light intensity drizzle\",\"icon\":\"09d\"}],\"base\":\"stations\",\"main\":{\"temp\":280.32,\"pressure\":1012,\"humidity\":81,\"temp_min\":279.15,\"temp_max\":281.15},\"visibility\":10000,\"wind\":{\"speed\":4.1,\"deg\":80},\"clouds\":{\"all\":90},\"dt\":1485789600,\"sys\":{\"type\":1,\"id\":5091,\"message\":0.0103,\"country\":\"GB\",\"sunrise\":1485762037,\"sunset\":1485794875},\"id\":2643743,\"name\":\"London\",\"cod\":200}\n");
        }catch(JSONException jsone){
            System.err.println("Error while requesting JSON.\n" + jsone.getMessage());
        }
        return response;
    }

    public WeatherCondition getWeatherInLocation(String location) throws JSONException {
        JSONObject weather = this.requestWeather(location);
        if(weather == null) return null;

        return new WeatherCondition(
                (int) weather.getJSONObject("weather").get("id"),
                weather.getJSONObject("weather").get("main").toString(),
                weather.getJSONObject("weather").get("description").toString(),
                weather.getJSONObject("weather").get("icon").toString(),
                (float) weather.getJSONObject("main").get("temp")
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
         "description": "light intensity drizzle",
         "icon": "09d"
       }
     ],
     "base": "stations",
     "main": {
       "temp": 280.32,
       "pressure": 1012,
       "humidity": 81,
       "temp_min": 279.15,
       "temp_max": 281.15
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
