package com.hardcoreamature.chefiq

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.hardcoreamature.chefiq.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        binding.userEmailTextView.text = currentUser?.email ?: "User"

        binding.viewIngredientsButton.setOnClickListener {
            val intent = Intent(this, IngredientsActivity::class.java)
            startActivity(intent)
        }

        binding.logoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
