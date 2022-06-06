package com.codeyuba.volumebalok


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object{
        private const val STATE_RESULT = "state_result"
    }

   private lateinit var inputPanjang :EditText
   private lateinit var inputLebar :EditText
   private lateinit var inputTinggi :EditText
   private lateinit var btnHitung :Button
   private lateinit var tvHasil :TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputPanjang =findViewById(R.id.edtLength)
        inputLebar = findViewById(R.id.edtWidth)
        inputTinggi = findViewById(R.id.edtHeight)
        btnHitung = findViewById(R.id.btnCalculate)
        tvHasil = findViewById(R.id.tvResult)

        btnHitung.setOnClickListener (this)

        if (savedInstanceState != null){
            val result = savedInstanceState.getString("state_result")
            tvHasil.text = result
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_RESULT,tvHasil.text.toString())
    }


    override fun onClick(view: View) {
        when(view){
            btnHitung -> {
                val panjang = inputPanjang.text.toString().trim()
                val lebar = inputLebar.text.toString().trim()
                val tinggi = inputTinggi.text.toString().trim()
                var isInputEmpty = false

                if (panjang.isEmpty()){
                    isInputEmpty =  true
                    edtLength.error = "Field tidak boleh kosong"
                }
                else if(lebar.isEmpty()){
                    isInputEmpty = true
                    edtLength.error = "Field tidak boleh kosong"
                }
                else if (tinggi.isEmpty()){
                    isInputEmpty =true
                    edtHeight.error = "Field todal boleh kosong"
                }
                else if(!isInputEmpty){
                    val resultVolume = panjang.toDouble() * lebar.toDouble() * tinggi.toDouble()
                    tvHasil.text = resultVolume.toString()
                }

            }
            else -> Toast.makeText(this,"Wrong input",Toast.LENGTH_LONG).show()
        }
    }
}