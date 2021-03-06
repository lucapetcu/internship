package com.example.locationapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
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
                        Toast.makeText(this, "Signed in", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, MenuActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Log.e("error sign in", "Error signing user in")
                    }
            }
        }
    }
}