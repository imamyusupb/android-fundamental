package com.codeyuba.myintentapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var tvResult: TextView
    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == MoveForResultActivity.RESULT_CODE && result.data != null) {
            val selectedValue =
                result.data?.getIntExtra(MoveForResultActivity.EXTRA_SELECTED_VALUE, 0)
            tvResult.text = "Hasil : $selectedValue"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnMoveActivity: Button = findViewById(R.id.btn_move_activity)
        btnMoveActivity.setOnClickListener(this)

        val btnMoveWithDataActivity: Button = findViewById(R.id.btn_move_activity_data)
        btnMoveWithDataActivity.setOnClickListener(this)

        val btnMoveWithObject: Button = findViewById(R.id.btn_move_activity_object)
        btnMoveWithObject.setOnClickListener(this)

        val btnDialPhone: Button = findViewById(R.id.btn_dial_number)
        btnDialPhone.setOnClickListener(this)

        val btnMoveForResult:Button = findViewById(R.id.btn_move_for_result)
        btnMoveForResult.setOnClickListener(this)
        tvResult = findViewById(R.id.tv_result)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_move_activity -> {
                val moveIntent = Intent(this, MoveActivity::class.java)
                startActivity(moveIntent)
            }
            R.id.btn_move_activity_data -> {
                val moveWithDataInten = Intent(this, MoveWithDataActivity::class.java)
                moveWithDataInten.putExtra(MoveWithDataActivity.EXTRA_NAME, "Imam Yusup Bachtiar")
                moveWithDataInten.putExtra(MoveWithDataActivity.EXTRA_AGE, 22)
                startActivity(moveWithDataInten)
            }
            R.id.btn_move_activity_object -> {
                val person = Person(
                    "Imam Yusup Bachtiar",
                    5,
                    "imam@gmail.com",
                    "Jakarta"
                )
                val moveWithObjectIntent =
                    Intent(this@MainActivity, MoveWithObjectActivity::class.java)
                moveWithObjectIntent.putExtra(MoveWithObjectActivity.EXTRA_DATA, person)
                startActivity(moveWithObjectIntent)
            }
            R.id.btn_dial_number -> {
                val phoneNumber = "081210841382"
                val dialPhoneIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
                startActivity(dialPhoneIntent)
            }
            R.id.btn_move_for_result -> {
                val moveForResultIntent = Intent(this@MainActivity, MoveForResultActivity::class.java)
                resultLauncher.launch(moveForResultIntent)
            }

        }
    }
}