package com.firstapp.loginapp.Database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import java.net.URI

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "CODECONNECTM"
        private const val DATABASE_VERSION = 1
    }


    override fun onCreate(db: SQLiteDatabase) {
        // Create your tables here
        createProfileTable(db)
        createCodingProfileTable(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Upgrade the database if needed
    }
    private fun createProfileTable(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE IF NOT EXISTS USERPROFILE " +
                "(Uid TEXT PRIMARY KEY , " +
                "Name TEXT, " +
                "ImageUri TEXT)"
        db.execSQL(createTableQuery)
    }

    private fun createCodingProfileTable(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE IF NOT EXISTS CODINGPROFILE " +
                "(Id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Uid TEXT, " +
                "Pname TEXT, " +
                "Pid TEXT, " +
                "FOREIGN KEY (Uid) REFERENCES USERPROFILE(Uid))"
        db.execSQL(createTableQuery)
    }
    object UserProfilesTable {
        const val TABLE_NAME = "USERPROFILE"
        const val COLUMN_NAME = "Name"
        const val COLUMN_UID = "Uid"
        const val COLUMN_IMAGE_URI = "ImageUri"
    }
    fun insertUserProfile(uid: String, name: String) {
        // Open the database in write mode
        val db = writableDatabase

        // Create a ContentValues object to store the values to be inserted or updated
        val values = ContentValues().apply {
            put(UserProfilesTable.COLUMN_NAME, name)
        }

        // Update the values in the table for the given UID
        db.update(UserProfilesTable.TABLE_NAME, values, "${UserProfilesTable.COLUMN_UID} = ?", arrayOf(uid))

        // Close the database
        db.close()
    }

    fun insertCodingProfile(uid: String, pname: String, pid: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("Uid", uid)
            put("Pname", pname)
            put("Pid", pid)
        }
        return db.insert("CODINGPROFILE", null, values)
    }


    fun readUserProfile(): Cursor {
        val db = readableDatabase
        val query = "SELECT * FROM USERPROFILE"
        return db.rawQuery(query, null)
    }
    fun readCustomUserProfile(uid: String): Cursor{
        val db=readableDatabase
        val query="SELECT Uid FROM USERPROFILE WHERE Uid=?"
        val selectionArgs = arrayOf(uid)
        return db.rawQuery(query, selectionArgs)
    }

    fun readCodingProfile(): Cursor {
        val db = readableDatabase
        val query = "SELECT * FROM CODINGPROFILE"
        return db.rawQuery(query, null)
    }

    fun updateImageUri(uid: String, newImageUri: Uri?): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("ImageUri", newImageUri?.toString())
        }

        // Specify the WHERE clause to identify the row to update based on the UID
        val whereClause = "uid = ?"
        val whereArgs = arrayOf(uid)

        // Perform the update
        return db.update("USERPROFILE", values, whereClause, whereArgs)
    }

    fun getIdCodingProfile(Uid: String, name: String, codingProfile: String): Int? {
        val db = readableDatabase
        val query = "SELECT Id FROM CODINGPROFILE WHERE Uid = ? AND Pname = ? AND Pid = ?"
        val selectionArgs = arrayOf(Uid, name, codingProfile)

        val cursor = db.rawQuery(query, selectionArgs)

        var resultUid: Int? = null

        if (cursor.moveToFirst()) {
            // Move to the first row (if exists) and get the Uid
            val uidIndex = cursor.getColumnIndex("Id")
            resultUid = cursor.getInt(uidIndex)
        }

        cursor.close()
        return resultUid
    }

    fun updateCodingProfile(gettingId: Int, uid: String, name: String, codingProfile: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("Uid", uid)
            put("Pname", name)
            put("Pid", codingProfile)
        }

        // Specify the WHERE clause to identify the row to update based on the Id
        val whereClause = "Id = ?"
        val whereArgs = arrayOf(gettingId.toString())

        // Perform the update
        return db.update("CODINGPROFILE", values, whereClause, whereArgs)
    }
    object CodingProfilesTable {
        const val TABLE_NAME = "USERPROFILE"
        const val COLUMN_UID = "Uid"
        const val COLUMN_NAME = "Name"
        const val COLUMN_IMAGE_URI = "ImageUri"
    }
    fun insertImageCodingProfile(uid: String, imageUri: Uri?) {
        // Open the database in write mode
        val db = writableDatabase

        // Create a ContentValues object to store the values to be inserted
        val values = ContentValues().apply {
            put(CodingProfilesTable.COLUMN_UID, uid)
            put(CodingProfilesTable.COLUMN_IMAGE_URI, imageUri?.toString())
            // If you want to leave the name field blank, you can omit putting a value for it

        }

        // Insert the values into the table
        db.insert(CodingProfilesTable.TABLE_NAME, null, values)

        // Close the database
        db.close()
    }



}

