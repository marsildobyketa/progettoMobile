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

    @Override
    public String toString() {
        return "WeatherCondition{" +
                "id=" + id +
                ", weatherType='" + weatherType + '\'' +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                ", temperature=" + temperature +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ", minTemperature=" + minTemperature +
                ", maxTemperature=" + maxTemperature +
                ", cityName='" + cityName + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", sunrise=" + sunrise +
                ", sunset=" + sunset +
                ", windSpeed=" + windSpeed +
                '}';
    }
}
