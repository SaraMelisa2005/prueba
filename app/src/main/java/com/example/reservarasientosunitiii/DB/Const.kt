package com.example.reservarasientosunitiii.DB

class Const {

    companion object {

        //DATABASE
        const val DATABASE_NAME = "DB_RESERVATIONS"
        const val DATABASE_VERSION = 1

        //TABLE
        const val TABLE_RESERVATION = "RESERVATION"

        //FIELDS
        const val FIELD_ID = "ID"
        const val FIELD_DOCUMENT = "DOCUMENT"
        const val FIELD_SEATS = "SEATS"

        //CREATE TABLE
        const val TABLE_RESERVATION_CREATE =
            "CREATE TABLE $TABLE_RESERVATION (" +
                    "$FIELD_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$FIELD_DOCUMENT TEXT NOT NULL, " +
                    "$FIELD_SEATS INTEGER NOT NULL)"

        //DROP TABLE
        const val TABLE_RESERVATION_DROP =
            "DROP TABLE IF EXISTS $TABLE_RESERVATION"

    }
}

