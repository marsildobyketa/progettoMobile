package ch.supsi.dti.isin.meteoapp.database;

import android.database.Cursor;

import java.util.UUID;

import ch.supsi.dti.isin.meteoapp.model.Location;

public class CursorWrapper  extends android.database.CursorWrapper {

    public CursorWrapper(Cursor cursor) {
        super(cursor);
    }

    /**
     * Questo metodo mi ritorna un oggetto TestEntry, anziché dei valori "singoli".
     * @return
     */
    public Location getEntry() {
        String id = getString(getColumnIndex(DataBaseSchema.TestTable.Cols.UUID));
        String name = getString(getColumnIndex(DataBaseSchema.TestTable.Cols.NAME));

        return new Location(UUID.fromString(id), name);
    }
}