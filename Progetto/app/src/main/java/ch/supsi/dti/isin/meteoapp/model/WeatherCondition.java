package ch.supsi.dti.isin.meteoapp.model;

public class WeatherCondition {
    private final int id;
    private final String weatherType;
    private final String description;
    private final String icon;
    private final double temperature;
    private final int pressure;
    private final int humidity;
    private final double minTemperature;
    private final double maxTemperature;
    private final String cityName;
    private final String countryCode;
    private final long sunrise;
    private final long sunset;
    private final double windSpeed;

    public WeatherCondition(final int id, final String weatherType, final String description,
                            final String icon, final double temperature, final int pressure,
                            final int humidity, final double minTemperature, final double maxTemperature,
                            final String cityName, final String countryCode, final long sunrise,
                            final long sunset, final double windSpeed
    ){
        this.id = id;
        this.weatherType = weatherType;
        this.description = description;
        this.icon = icon;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.cityName = cityName;
        this.countryCode = countryCode;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.windSpeed = windSpeed;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getId() {
        return id;
    }

    public int getPressure() {
        return pressure;
    }

    public long getSunrise() {
        return sunrise;
    }

    public long getSunset() {
        return sunset;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    public String getWeatherType() {
        return weatherType;
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
