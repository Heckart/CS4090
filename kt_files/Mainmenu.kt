package com.example.sprint1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity





//Goal of this file is to have the main menu a user sees and the buttons lead into subsequent screens
//locationorder is started when start_order is pressed as the first part of making an order
 class Mainmenu  : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_mainmenu)
        val startorderbutton=findViewById<Button>(R.id.start_order)
        startorderbutton.setOnClickListener{
            val intent= Intent(this,Locationorder::class.java)
            startActivity(intent)
        }
    }

}


