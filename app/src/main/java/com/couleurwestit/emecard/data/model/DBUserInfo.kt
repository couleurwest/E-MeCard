package com.couleurwestit.emecard.data.model

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DBUserInfo(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    // below is the method for creating a database by a sqlite query
    override fun onCreate(db: SQLiteDatabase) {
        // below is a sqlite query, where column names
        // along with their data types is given
        val query = ("CREATE TABLE userinfo ("
                + UUID_COL + " TEXT PRIMARY KEY, " +
                LASTNAME_COL + " TEXT," + FIRSTNAME_COL + " TEXT," +
                CORP_COL + " TEXT," +
                EMAIL_COL + " TEXT," + WEBSITE_COL + " TEXT," +
                PHONE1_COL + " TEXT," + PHONE2_COL + " TEXT)")

        // we are calling sqlite
        // method for executing our query
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // this method is to check if table already exists
        db.execSQL("DROP TABLE IF EXISTS userinfo")
        onCreate(db)
    }

    // This method is for adding data in our database
    fun record(
        lastname: String,
        firstname: String,
        corp: String,
        email: String,
        website: String,
        phone_1: String,
        phone_2: String
    ): UserInfo {

        // below we are creating
        // a content values variable
        val values = ContentValues()

        // we are inserting our values
        // in the form of key-value pair
        val user = this.findVCard()

        values.put(LASTNAME_COL, lastname)
        values.put(FIRSTNAME_COL, firstname)
        values.put(CORP_COL, corp)
        values.put(EMAIL_COL, email)
        values.put(WEBSITE_COL, website)
        values.put(PHONE1_COL, phone_1)
        values.put(PHONE2_COL, phone_2)

        val db = this.writableDatabase!!

        if (user == null) {
            values.put(UUID_COL, "vcard")
            db.insert(TABLE_NAME, null, values)
        } else {
            val whereArgs = arrayOf("vcard")
            db.update(TABLE_NAME, values, "uuid = ?", whereArgs)
        }


        // all values are inserted into database

        // at last we are
        // closing our database
        db.close()
        return UserInfo(
            firstname = firstname,
            lastname = lastname,
            corp = corp,
            email = email,
            website = website,
            phone_1 = phone_1,
            phone_2 = phone_2
        )
    }

    @SuppressLint("Recycle")
    fun findVCard(): UserInfo? {
        val db = this.readableDatabase

        // below code returns a cursor to
        val result = db.rawQuery("SELECT * FROM userinfo WHERE userinfo.uuid= 'vcard'", null)
        print(result)
        if (result.count == 1) {
            result.moveToFirst()
            val id_firstname = result.getColumnIndex(FIRSTNAME_COL)
            val id_lasttname = result.getColumnIndex(LASTNAME_COL)
            val id_corp = result.getColumnIndex(CORP_COL)
            val id_email = result.getColumnIndex(EMAIL_COL)
            val id_website = result.getColumnIndex(WEBSITE_COL)
            val id_phone_1 = result.getColumnIndex(PHONE1_COL)
            val id_phone_2 = result.getColumnIndex(PHONE2_COL)

            return UserInfo(
                firstname = result.getString(id_firstname),
                lastname = result.getString(id_lasttname),
                corp = result.getString(id_corp),
                email = result.getString(id_email),
                website = result.getString(id_website),
                phone_1 = result.getString(id_phone_1),
                phone_2 = result.getString(id_phone_2)
            )
        }
        return null
    }


    companion object {
        // here we have defined variables for our database

        // below is variable for database name
        private val DATABASE_NAME = "CWQRCODER"

        // below is the variable for database version
        private val DATABASE_VERSION = 2

        // below is the variable for table name
        val TABLE_NAME = "userinfo"

        // below is the variable for id column
        val UUID_COL = "uuid"

        // below is the variable for name column
        val LASTNAME_COL = "lastname"
        val FIRSTNAME_COL = "firstname"
        val CORP_COL = "corp"
        val EMAIL_COL = "email"
        val WEBSITE_COL = "website"
        val PHONE1_COL = "phone1"
        val PHONE2_COL = "phone2"

    }
}