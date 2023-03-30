package ch.supsi.dti.isin.meteoapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ch.supsi.dti.isin.meteoapp.fragments.ListFragment;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "myLocation.db";
    private static final int DATABASE_VERSION = 1;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DataBaseSchema.TestTable.NAME + "("
                + " _id integer primary key autoincrement, " + DataBaseSchema.TestTable.Cols.UUID + ", "
                + DataBaseSchema.TestTable.Cols.NAME
                + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS locations");
        onCreate(db);
    }


    public void insertData(ch.supsi.dti.isin.meteoapp.model.Location location) {

        SQLiteDatabase mDatabase= getWritableDatabase();

        // istanzio un oggetto TestEntry
        // istanzio un ContentValues per la entry appena istanziata
        ContentValues values = LocationContentValues.getContentValues(location);
        // chiamo il metodo insert sul db che ho in memoria
        mDatabase.insert(DataBaseSchema.TestTable.NAME, null, values);

    }

    public void deleteData(ch.supsi.dti.isin.meteoapp.model.Location location) {

        SQLiteDatabase mDatabase= getWritableDatabase();

        // Get the location's ID from the model object
        UUID locationId = location.getId();

        // Define the WHERE clause to delete only the row with the matching ID
        String whereClause = DataBaseSchema.TestTable.Cols.UUID + " = ?";
        String[] whereArgs = { String.valueOf(locationId) };

        // Call the delete method on the database to delete the row
        mDatabase.delete(DataBaseSchema.TestTable.NAME, whereClause, whereArgs);
    }


    public List<ch.supsi.dti.isin.meteoapp.model.Location> readData(List<ch.supsi.dti.isin.meteoapp.model.Location> list) {


        // istanzio un oggetto CursorWrapper
        CursorWrapper cursor = queryData();

        // itero, tramite il cursor, tutti i risultati rirornati
        try {
            // mi sposto al primo elemento
            cursor.moveToFirst();

            // fintanto che ci sono elementi
            while (!cursor.isAfterLast()) {
                // mi faccio dare l'oggetto TestEntry dal cursor
                ch.supsi.dti.isin.meteoapp.model.Location entry = cursor.getEntry();
                list.add(entry);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        // mostro i risultati
        for (ch.supsi.dti.isin.meteoapp.model.Location location: list){


            Log.d("LOCATION",location.getName());

        }

        return list;

    }



    public CursorWrapper queryData() {

        SQLiteDatabase mDatabase= getWritableDatabase();

        Cursor cursor = mDatabase.query(
                DataBaseSchema.TestTable.NAME,
                null, // columns - null selects all columns
                null, // where clause
                null, // where args
                null, // groupBy
                null,  // having
                null  // orderBy
        );
        return new CursorWrapper(cursor);
    }



}
