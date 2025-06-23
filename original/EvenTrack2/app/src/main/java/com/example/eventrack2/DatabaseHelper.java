package com.example.eventrack2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database and table names
    private static final String DATABASE_NAME = "EventsDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_EVENTS = "events";

    // Table columns
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "event_name";
    private static final String COLUMN_DATE = "event_date";
    private static final String COLUMN_LOCATION = "event_location";

    // SQL query to create the table
    private static final String CREATE_TABLE_EVENTS = "CREATE TABLE " + TABLE_EVENTS + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NAME + " TEXT, "
            + COLUMN_DATE + " TEXT, "
            + COLUMN_LOCATION + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_EVENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(db);
    }

    // Method to insert an event
    public boolean insertEvent(String name, String date, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_LOCATION, location);

        long result = db.insert(TABLE_EVENTS, null, values);
        db.close();
        return result != -1; // Return true if insertion is successful
    }

    // Method to retrieve all events
    public Cursor getAllEvents() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_EVENTS, null);
    }

    // Method to get an event by name (to use when updating or deleting)
    public Cursor getEventByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_EVENTS + " WHERE " + COLUMN_NAME + " = ?", new String[]{name});
    }

    // Method to delete an event by ID
    public boolean deleteEvent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_EVENTS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0; // Return true if deletion was successful
    }

    // Method to update an event by ID
    public boolean updateEvent(int id, String name, String date, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_LOCATION, location);

        int result = db.update(TABLE_EVENTS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0; // Return true if update was successful
    }
}
