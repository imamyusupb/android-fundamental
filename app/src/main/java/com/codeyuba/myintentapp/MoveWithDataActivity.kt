package com.codeyuba.myintentapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView


class MoveWithDataActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_AGE = "extra_age"
        const val EXTRA_NAME = "extra_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_move_with_data)

        val name = intent.getStringExtra(EXTRA_NAME)
        val umur = intent.getStringExtra(EXTRA_AGE)

        val result :TextView = findViewById(R.id.tvDataReceived)
        result.text = "Nama saya $name dan berumur $umur"


    }
}