package com.example.greenish



import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import app.akexorcist.bluetotohspp.library.BluetoothSPP
import app.akexorcist.bluetotohspp.library.BluetoothState
import app.akexorcist.bluetotohspp.library.DeviceList
import com.example.greenish.databinding.ActivityMainBinding
import com.example.greenish.retrofit.PlantResult
import com.example.greenish.retrofit.Retrofit
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.shape.ShapeAppearanceModel.Builder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var bt: BluetoothSPP

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)


        binding.bottomNav.setOnItemSelectedListener { item ->
            changeFragment(
                when (item.itemId) {
                    R.id.home -> {
                        HomeFragment()
                    }
                    R.id.search -> {
                        SearchFragment()
                    }
                    else -> {
                        BluetoothFragment()
                    }
                } as Fragment
            )
            true
        }
        binding.bottomNav.selectedItemId = R.id.home


    }

    private lateinit var binding: ActivityMainBinding
    private val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_nav_host) as NavHostFragment?
    private val navController = navHostFragment?.navController


    override fun onStart() {
        super.onStart()




    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_nav_host, fragment)
            .commit()
    }


    fun hideBottomNavigation(state: Boolean){
        if(state) binding.bottomNav.visibility = View.GONE else binding.bottomNav.visibility = View.VISIBLE
    }


    companion object {
        const val TAG = "MainActivityTag"
    }
}


