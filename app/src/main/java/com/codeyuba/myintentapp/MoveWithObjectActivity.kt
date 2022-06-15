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

        val tvObject :TextView = findViewById(R.id.tv_object_received)
        val person = intent.getParcelableExtra<Person>(EXTRA_DATA) as Person
        val text = "Name : ${person.name},\nEmail : ${person.email},\nAge : ${person.age},\nLocation : ${person.city}"
        tvObject.text = text

    }
}