package com.bigOne.stayfittrainer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bigOne.stayfittrainer.databinding.ActivityMainBinding
import com.bigOne.stayfittrainer.ui.Login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    lateinit var mainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        val user = FirebaseAuth.getInstance().currentUser
        if(user==null){
                finish()
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            }else{
            FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        }
        init()
        observe()
    }

    private fun observe() {

    }

    private fun init() {
        val navController = findNavController(R.id.nav_host_fragment)
        mainBinding.navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment, R.id.CoursesFragment, R.id.accountFragment,R.id.myCourseFragment,R.id.recipeLogFragment -> {
                    mainBinding.navView.visibility = View.VISIBLE
                }
                else -> {
                    mainBinding.navView.visibility = View.GONE
                }
            }
        }
    }
}