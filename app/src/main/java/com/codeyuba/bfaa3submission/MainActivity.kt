package com.codeyuba.bfaa3submission

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import com.codeyuba.bfaa3submission.databinding.ActivityMainBinding
import com.codeyuba.bfaa3submission.viewmodel.UserViewModel
import com.codeyuba.bfaa3submission.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this@MainActivity)
        val vm: UserViewModel by viewModels {
            factory
        }
        this.viewModel = vm

        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)

        viewModel.getThemeSettings().observe(this) {
            val mode: Int
            if (it){
                mode = AppCompatDelegate.MODE_NIGHT_YES
                menu?.findItem(R.id.mode_menu)?.setIcon(R.drawable.ic_baseline_light_mode_20)
            }else{
                mode = AppCompatDelegate.MODE_NIGHT_NO
                menu?.findItem(R.id.mode_menu)?.setIcon(R.drawable.ic_baseline_nightlight_20)
            }
            AppCompatDelegate.setDefaultNightMode(mode)

        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.search_menu -> {
                val action = FamousUserFragmentDirections.actionFamousUserFragmentToSearchUserFragment()
                binding.fragmentContainerView.findNavController().navigate(action)
            }

            R.id.mode_menu -> {
                val mode: Int
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
                    viewModel.setThemeSettings(false)
                }else{
                    viewModel.setThemeSettings(true)
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

}