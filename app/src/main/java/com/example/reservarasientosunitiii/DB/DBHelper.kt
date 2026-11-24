package com.example.reservarasientosunitiii.DB


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(
        context,
        Const.DATABASE_NAME,
        null,
        Const.DATABASE_VERSION
    ) {

    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL(Const.TABLE_RESERVATION_CREATE)

    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {

        db?.execSQL(Const.TABLE_RESERVATION_DROP)
        onCreate(db)

    }


}

