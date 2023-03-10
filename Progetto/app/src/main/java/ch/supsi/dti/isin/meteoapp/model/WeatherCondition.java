package ch.supsi.dti.isin.meteoapp.model;

public class WeatherCondition {
    private final int id;
    private final String weatherType;
    private final String description;
    private final String icon;

    private final float temperature;

    public WeatherCondition(final int id, final String weatherType, final String description, final String icon, final float temperature){
        this.id = id;
        this.weatherType = weatherType;
        this.description = description;
        this.icon = icon;
        this.temperature = temperature;
    }
}
