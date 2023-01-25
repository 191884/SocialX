package com.snappy.socialx

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.snappy.socialx.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.activity_main_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        setupWithNavController(binding.NavigationBar,navController)

        val bottomNavigationView = binding.NavigationBar

        bottomNavigationView.setOnItemSelectedListener { when(it.itemId){
            R.id.signUpNav -> {
                navController.navigate(R.id.action_loginFragment_to_signUpFragment)
                binding.loginButton.text = "REGISTER"
                true
            };
            R.id.loginNav -> {
                navController.navigate(R.id.action_signUpFragment_to_loginFragment)
                binding.loginButton.text = "LOGIN"
                true
            };
            else -> {null
                false}
        } }

        bottomNavigationView.setOnItemReselectedListener {  }
    }
}