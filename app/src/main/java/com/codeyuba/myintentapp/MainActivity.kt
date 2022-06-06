package com.codeyuba.myintentapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnMoveActivity: Button = findViewById(R.id.btn_move_activitiy)
        btnMoveActivity.setOnClickListener(this)

        val btnMoveWithDataActivity :Button = findViewById(R.id.btn_move_activity_data)
        btnMoveWithDataActivity.setOnClickListener(this)

        val btnMoveWithObjectActivity :Button = findViewById(R.id.btn_move_activity_object)
        btnMoveWithObjectActivity.setOnClickListener(this)

        val btnDialNumber :Button = findViewById(R.id.btnDialNumber)
        btnDialNumber.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when(view.id) {
            R.id.btn_move_activitiy -> {
                val intent = Intent(this,MoveActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_move_activity_data -> {
                val intent = Intent(this,MoveWithDataActivity::class.java)
                intent.putExtra(MoveWithDataActivity.EXTRA_NAME,"imam yusufb")
                intent.putExtra(MoveWithDataActivity.EXTRA_AGE,"222")
                startActivity(intent)
            }
           R.id.btn_move_activity_object-> {
               val person = Person(
                   "imam yusup bachtiar",
                   22,
                   "imam123@gmail.com",
                   "Jakarta"
               )
               val intent = Intent(this,MoveWithObjectActivity::class.java)
               intent.putExtra(MoveWithObjectActivity.EXTRA_DATA,person)
               startActivity(intent)
           }
            R.id.btnDialNumber->{
                val phoneNumber = "081380922598"
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel: $phoneNumber"))
                startActivity(intent)
            }
        }
    }
}