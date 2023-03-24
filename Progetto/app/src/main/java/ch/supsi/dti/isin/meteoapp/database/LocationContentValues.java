package ch.supsi.dti.isin.meteoapp.database;

import android.content.ContentValues;

import ch.supsi.dti.isin.meteoapp.model.Location;

public class LocationContentValues {

    public static ContentValues getContentValues(Location entry) {
        ContentValues values = new ContentValues();
        // mi riferisco a TestDbSchema.TestTable.Cols, in modo da non dover hardcodare qui le stringhe
        values.put(DataBaseSchema.TestTable.Cols.UUID, entry.getId().toString());
        values.put(DataBaseSchema.TestTable.Cols.NAME, entry.getName());
        return values;
    }

}
