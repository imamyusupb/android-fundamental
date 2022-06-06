package com.codeyuba.myintentapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MoveWithObjectActivity : AppCompatActivity() {
    companion object{
        const val EXTRA_DATA = "extra_data"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_move_with_object)

        val tvReceived :TextView = findViewById(R.id.tv_object_received)
        val data = intent.getParcelableExtra<Person>(EXTRA_DATA) as Person
        tvReceived.text = "Nama saya ${data.name} \nberumur ${data.age} \ndan email saya ${data.email} \ntinggal di ${data.city}"
    }
}