package com.codeyuba.submisionbfaaone.ui

import android.content.Intent
import android.content.res.Configuration
import android.content.res.TypedArray
import android.graphics.Insets.add
import android.net.LinkAddress
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codeyuba.submisionbfaaone.R
import com.codeyuba.submisionbfaaone.adapter.UserListAdapter
import com.codeyuba.submisionbfaaone.data.GithubUser
import com.codeyuba.submisionbfaaone.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var rvUser: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Github User"
        showRecyclerView()

    }

    private fun getArrayListUser(): ArrayList<GithubUser> {
        val dataName = resources.getStringArray(R.array.name)
        val dataUsername = resources.getStringArray(R.array.name)
        val dataAvatar = resources.obtainTypedArray(R.array.avatar)
        val dataRepo = resources.getStringArray(R.array.repository)
        val dataFollower = resources.getStringArray(R.array.followers)
        val dataFollowing = resources.getStringArray(R.array.following)
        val dataCompany = resources.getStringArray(R.array.company)
        val dataLocation = resources.getStringArray(R.array.location)

        val arrayListUser: ArrayList<GithubUser> = arrayListOf()
        for (position in dataName.indices) {
            arrayListUser.add(
                GithubUser(
                    dataAvatar.getResourceId(position, -1),
                    dataUsername[position],
                    dataName[position],
                    dataFollower[position],
                    dataFollowing[position],
                    dataCompany[position],
                    dataRepo[position],
                    dataLocation[position]
                )
            )
        }
        return arrayListUser
    }


    private fun showSelectedHero(user: GithubUser) {
        val intent = Intent(this,DetailUserActivity::class.java)
        intent.putExtra(DetailUserActivity.EXTRA_DATA,user)
        startActivity(intent)
        Toast.makeText(this, "Kamu memilih " + user.name, Toast.LENGTH_SHORT).show()
    }

    private fun showRecyclerView() {
        val userAdapter= UserListAdapter(getArrayListUser(),object :UserListAdapter.OnItemClickCallback{
            override fun onItemClicked(data: GithubUser) {
                showSelectedHero(data)
            }
        })

        with(binding) {
            rvUser.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUser.adapter = userAdapter
        }

    }
}