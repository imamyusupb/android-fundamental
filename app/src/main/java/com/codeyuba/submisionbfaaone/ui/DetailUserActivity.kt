package com.codeyuba.submisionbfaaone.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.codeyuba.submisionbfaaone.R
import com.codeyuba.submisionbfaaone.data.GithubUser
import com.codeyuba.submisionbfaaone.databinding.ActivityDetailUserBinding

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        val view = binding.root
        val actionBar = supportActionBar
        actionBar?.title = "Detail User"
        actionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(view)


        val dataUser = intent.getParcelableExtra<GithubUser>(EXTRA_DATA) as GithubUser

        Glide.with(this@DetailUserActivity.baseContext)
            .load(dataUser.avatar)
            .apply(RequestOptions.overrideOf(130,130))
            .circleCrop()
            .into(binding.imgUserDetail)

        binding.tvDetailName.text = dataUser.name
        binding.tvDetailUserName.text = "@"+dataUser.username
        binding.tvCompany.text = dataUser.company
        binding.tvFollower.text = dataUser.follower
        binding.tvFollowing.text = dataUser.folowing
        binding.tvRepository.text = dataUser.repository
        binding.tvLocation.text = dataUser.location

    }



    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object{
        const val EXTRA_DATA = "extra_data"
    }
}