package com.example.sprint1


//necessary import statements for working with android for SQLITE
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper



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

    override fun onCreate(db: SQLiteDatabase?) {
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
        getAllDeliveryInfo()
    }

    // Method to retrieve and print all delivery information
    fun getAllDeliveryInfo() {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        if (cursor.moveToFirst()) {
            do {
                val addressIndex = cursor.getColumnIndex(COLUMN_ADDRESS)
                val cityIndex= cursor.getColumnIndex(COLUMN_CITY)
                val stateIndex = cursor.getColumnIndex(COLUMN_STATE)
                val zipIndex = cursor.getColumnIndex(COLUMN_ZIP)

                if (addressIndex >= 0 && cityIndex >= 0 && stateIndex >= 0 && zipIndex >= 0) {
                    val address = cursor.getString(addressIndex)
                    val city = cursor.getString(cityIndex)
                    val state = cursor.getString(stateIndex)
                    val zip = cursor.getString(zipIndex)

                    // Print the delivery information
                    println("Address: $address, City: $city, State: $state, Zip: $zip")
                }
                else{
                    println("ERROR one or more indices are invalid")
                }
                } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
    }
}
