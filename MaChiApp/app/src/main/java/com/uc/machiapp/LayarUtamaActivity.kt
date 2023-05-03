package com.uc.machiapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.uc.machiapp.databinding.ActivityLayarUtamaBinding
import com.uc.machiapp.fragment.HomeFragment
import com.uc.machiapp.fragment.PredictionFragment
import com.uc.machiapp.fragment.ProfileFragment

class LayarUtamaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLayarUtamaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLayarUtamaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId){

                R.id.home -> replaceFragment(HomeFragment())
                R.id.prediction -> replaceFragment(PredictionFragment())
                R.id.profile -> replaceFragment(ProfileFragment())

                else ->{
                }
            }
            true
        }

    }

    private fun replaceFragment(fragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
    }
}