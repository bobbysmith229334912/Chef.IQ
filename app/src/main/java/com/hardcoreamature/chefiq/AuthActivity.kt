package com.hardcoreamature.chefiq

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class AuthActivity : AppCompatActivity() {

    private lateinit var emailLoginEditText: EditText
    private lateinit var passwordLoginEditText: EditText
    private lateinit var emailRegisterEditText: EditText
    private lateinit var passwordRegisterEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var switchToRegisterTextView: TextView
    private lateinit var switchToLoginTextView: TextView
    private lateinit var loginLayout: LinearLayout
    private lateinit var registerLayout: LinearLayout

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        // Initialize UI components for Login
        emailLoginEditText = findViewById(R.id.emailLoginEditText)
        passwordLoginEditText = findViewById(R.id.passwordLoginEditText)
        loginButton = findViewById(R.id.loginButton)
        switchToRegisterTextView = findViewById(R.id.switchToRegisterTextView)
        loginLayout = findViewById(R.id.loginLayout)

        // Initialize UI components for Registration
        emailRegisterEditText = findViewById(R.id.emailRegisterEditText)
        passwordRegisterEditText = findViewById(R.id.passwordRegisterEditText)
        registerButton = findViewById(R.id.registerButton)
        switchToLoginTextView = findViewById(R.id.switchToLoginTextView)
        registerLayout = findViewById(R.id.registerLayout)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Set onClickListeners for buttons
        setupButtonClickListeners()

        // Switch views between Login and Register
        switchToRegisterTextView.setOnClickListener {
            loginLayout.visibility = LinearLayout.GONE
            registerLayout.visibility = LinearLayout.VISIBLE
        }

        switchToLoginTextView.setOnClickListener {
            loginLayout.visibility = LinearLayout.VISIBLE
            registerLayout.visibility = LinearLayout.GONE
        }
    }

    private fun setupButtonClickListeners() {
        loginButton.setOnClickListener {
            val email = emailLoginEditText.text.toString().trim()
            val password = passwordLoginEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        registerButton.setOnClickListener {
            val email = emailRegisterEditText.text.toString().trim()
            val password = passwordRegisterEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                registerUser(email, password)
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                navigateToDashboardActivity()
            } else {
                Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Successfully Registered.", Toast.LENGTH_SHORT).show()
                navigateToDashboardActivity()
            } else {
                Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun navigateToDashboardActivity() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }
}
