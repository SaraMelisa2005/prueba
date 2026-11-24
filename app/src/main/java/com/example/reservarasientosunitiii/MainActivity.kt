package com.example.reservarasientosunitiii

import android.content.ContentValues
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.reservarasientosunitiii.DB.Const
import com.example.reservarasientosunitiii.DB.DBHelper
import com.example.reservarasientosunitiii.Model.Reservation

class MainActivity : AppCompatActivity() {
    //OBJECTS LAYOUT
    private lateinit var txtDocument: EditText
    private lateinit var txtSeats: EditText
    private lateinit var btnReserve: Button
    private lateinit var listReservations: ListView

    //DATABASE
    private lateinit var dbHelper: DBHelper

    //LIST
    private lateinit var reservationList: ArrayList<String>
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //LINK OBJECTS
        txtDocument = findViewById(R.id.txtDocument)
        txtSeats = findViewById(R.id.txtSeats)
        btnReserve = findViewById(R.id.btnReserve)
        listReservations = findViewById(R.id.listReservations)

        //DATABASE INSTANCE
        dbHelper = DBHelper(this)
        //LIST CONFIGURATION
        reservationList = ArrayList()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, reservationList)
        listReservations.adapter = adapter
        //LOAD DATA
        loadReservations()
        //BUTTON EVENT
        btnReserve.setOnClickListener {
            reserveSeats()
        }
    }


    //RESERVE METHOD
    private fun reserveSeats() {
        val document = txtDocument.text.toString().trim()
        val seatsText = txtSeats.text.toString().trim()

        //VALIDATIONS
        if (document.isEmpty()) {
            txtDocument.error = "Ingrese el documento"
            txtDocument.requestFocus()
            return
        }

        if (seatsText.isEmpty()) {
           txtSeats.error = "Ingrese la cantidad de asientos"
            txtSeats.requestFocus()
            return
        }

        val seats = seatsText.toIntOrNull()
        if (seats == null) { txtSeats.error = "Valor inválido"
            txtSeats.requestFocus()
            return
        }

        if (seats <= 0) { txtSeats.error = "La cantidad debe ser mayor a cero"
            txtSeats.requestFocus()
            return
        }

        //CREATE OBJECT
        val reservation = Reservation()
        reservation.document = document
        reservation.seats = seats

        //INSERT DATABASE
        val result = insertReservation(reservation)
        if (result) { Toast.makeText( this, "Reserva registrada correctamente", Toast.LENGTH_SHORT ).show()
            clearFields()
            loadReservations()
        }
        else
        {
            Toast.makeText( this, "Error al registrar reserva", Toast.LENGTH_SHORT ).show()
        }
    }

    //LOAD LISTVIEW
    private fun loadReservations()
    {
        reservationList.clear()
        val reservations = getReservations()
        for (reservation in reservations) {
            reservationList.add( "Documento: ${reservation.document}\n" + "Asientos: ${reservation.seats}" )
        }

        adapter.notifyDataSetChanged()
    }

    //CLEAR FIELDS
    private fun clearFields()
    {
        txtDocument.setText("")
        txtSeats.setText("")
        txtDocument.requestFocus()
    }



    //INSERT
    fun insertReservation(reservation: Reservation): Boolean {

        val dbHelper = DBHelper(this)
        var db = dbHelper.writableDatabase

        val values = ContentValues()

        values.put(Const.FIELD_DOCUMENT, reservation.document)
        values.put(Const.FIELD_SEATS, reservation.seats)

        val result =
            db.insert(Const.TABLE_RESERVATION, null, values)

        db.close()

        return result != -1L
    }

    //GET ALL RESERVATIONS
    fun getReservations(): ArrayList<Reservation> {

        val listReservations = ArrayList<Reservation>()

        val dbHelper = DBHelper(this)
        var db = dbHelper.readableDatabase

        val query =
            "SELECT * FROM ${Const.TABLE_RESERVATION}"

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {

            do {

                val reservation = Reservation()

                reservation.id =
                    cursor.getInt(
                        cursor.getColumnIndexOrThrow(Const.FIELD_ID)
                    )

                reservation.document =
                    cursor.getString(
                        cursor.getColumnIndexOrThrow(Const.FIELD_DOCUMENT)
                    )

                reservation.seats =
                    cursor.getInt(
                        cursor.getColumnIndexOrThrow(Const.FIELD_SEATS)
                    )

                listReservations.add(reservation)

            } while (cursor.moveToNext())

        }

        cursor.close()
        db.close()

        return listReservations
    }
}