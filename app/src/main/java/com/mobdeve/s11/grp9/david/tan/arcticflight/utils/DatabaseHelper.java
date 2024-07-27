package com.mobdeve.s11.grp9.david.tan.arcticflight.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database name and version
    private static final String DATABASE_NAME = "game.db";
    private static final int DATABASE_VERSION = 1;

    // Table name
    private static final String TABLE_NAME = "game_stats";

    // Column names
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SCORE = "score";
    private static final String COLUMN_COINS = "coins";
    private static final String COLUMN_BEST_SCORE = "best_score";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database is created for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create the table
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SCORE + " INTEGER, " +
                COLUMN_COINS + " INTEGER, " +
                COLUMN_BEST_SCORE + " INTEGER)";
        db.execSQL(createTable); // Execute the SQL statement
    }

    // Called when the database needs to be upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME); // Drop the old table if it exists
        onCreate(db); // Create the table again
    }

    // Method to insert game stats into the database
    public void insertStats(int score, int coins, int bestScore) {
        SQLiteDatabase db = this.getWritableDatabase(); // Get writable database
        ContentValues values = new ContentValues();
        values.put(COLUMN_SCORE, score);
        values.put(COLUMN_COINS, coins);
        values.put(COLUMN_BEST_SCORE, bestScore);

        db.insert(TABLE_NAME, null, values); // Insert the values into the table
    }

    // Method to get the latest game stats from the database
    public Cursor getStats() {
        SQLiteDatabase db = this.getReadableDatabase(); // Get readable database
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " DESC LIMIT 1", null);
    }

    // Method to get the total number of coins collected across all sessions
    public int getTotalCoins() {
        SQLiteDatabase db = this.getReadableDatabase(); // Get readable database
        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_COINS + ") FROM " + TABLE_NAME, null);
        int totalCoins = 0;
        if (cursor.moveToFirst()) {
            totalCoins = cursor.getInt(0); // Get the sum of coins from the first column of the result
        }
        cursor.close(); // Close the cursor
        return totalCoins;
    }

    // Method to update the coin count after a purchase
    public void purchaseUpdate(int coinsToSubtract) {
        SQLiteDatabase db = this.getWritableDatabase(); // Get writable database

        // Get the latest session's ID
        Cursor cursor = db.rawQuery("SELECT id, coins FROM " + TABLE_NAME + " ORDER BY id DESC LIMIT 1", null);
        if (cursor.moveToFirst()) {
            int lastSessionId = cursor.getInt(0);
            int currentCoins = cursor.getInt(1);

            // Calculate new coin count
            int newCoinCount = currentCoins - coinsToSubtract;

            // Update the coin count in the database
            ContentValues values = new ContentValues();
            values.put(COLUMN_COINS, newCoinCount);
            db.update(TABLE_NAME, values, "id = ?", new String[] {String.valueOf(lastSessionId)});
        }
        cursor.close(); // Close the cursor
    }
}
