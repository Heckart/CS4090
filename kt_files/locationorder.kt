package com.example.sprint1

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity



//this kt file is the screen the user sees when they input their location based information for their order
class Locationorder: AppCompatActivity(){
    lateinit var addressinput: EditText
    lateinit var cityinput: EditText
    lateinit var stateinput: EditText
    lateinit var zipinput: EditText
    private lateinit var delivery_info:delivery_info

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        //points to activity_locationorder.xml format for what page looks like
        setContentView(R.layout.activity_locationorder)
        //corresponds values to their place in layout
        addressinput=findViewById(R.id.addressinput)
        cityinput=findViewById(R.id.cityinput)
        stateinput=findViewById(R.id.stateinput)
        zipinput=findViewById(R.id.zipinput)
        //creates new instance of delivery_info class and passes in the currently existing activity(this)
        delivery_info=delivery_info(this)

    }

    //input information into the database
    fun buttonClick(view: View?){
        val address=addressinput.text.toString()
        val city = cityinput.text.toString()
        val state=stateinput.text.toString()
        val zip = zipinput.text.toString()
        if (address.isNotEmpty() && city.isNotEmpty() && state.isNotEmpty() && zip.isNotEmpty()) {
            delivery_info.insertDeliveryInfo(address, city, state, zip)
            Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
    }
}