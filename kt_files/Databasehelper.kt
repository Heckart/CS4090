package com.example.sprint1


//necessary import statements for working with android for SQLITE
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


//
class delivery_info(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "delivery.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "delivery_info"
        const val COLUMN_ADDRESS = "address"
        const val COLUMN_CITY = "city"
        const val COLUMN_STATE = "state"
        const val COLUMN_ZIP = "zip"
    }

    //called when database is created
    override fun onCreate(db: SQLiteDatabase?) {
        // Create table SQL query
        val CREATE_TABLE = ("CREATE TABLE $TABLE_NAME ("
                + "$COLUMN_ADDRESS TEXT,"
                + "$COLUMN_CITY TEXT,"
                + "$COLUMN_STATE TEXT,"
                + "$COLUMN_ZIP TEXT" + ")")
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Method to insert data
    fun insertDeliveryInfo(address: String, city: String, state: String, zip: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ADDRESS, address)
            put(COLUMN_CITY, city)
            put(COLUMN_STATE, state)
            put(COLUMN_ZIP, zip)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }
}