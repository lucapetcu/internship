package com.example.locationapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.example.locationapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private var binding: ActivityLoginBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnSignIn?.setOnClickListener {
            signInUser()
        }

        binding?.fabLogin?.setOnClickListener {
            onBackPressed()
        }

        binding?.btnGoToRegister?.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun signInUser() {
        val email = binding?.getEmailSignIn?.text.toString()
        val password = binding?.getPasswordSignIn?.text.toString()

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, MenuActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Log.e("error sign in", "Error signing user in" + task.exception)
                    }
            }
        }
    }
}