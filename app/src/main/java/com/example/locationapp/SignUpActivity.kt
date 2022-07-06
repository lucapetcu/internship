package com.example.locationapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.locationapp.databinding.ActivitySignUpBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignUpActivity : AppCompatActivity() {

    private var binding: ActivitySignUpBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnRegister?.setOnClickListener {
            registerUser()
        }

    }

    private fun registerUser() {
        if (binding?.getName?.text?.isNotEmpty() == true
            && binding?.getEmail?.text?.isNotEmpty() == true
            && binding?.getPassword?.text?.isNotEmpty() == true) {
            val email = binding?.getEmail?.text.toString()
            val password = binding?.getPassword?.text.toString()
            //Toast.makeText(this, "$email and $password", Toast.LENGTH_SHORT).show()
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    task: Task<AuthResult> ->
                        if (task.isSuccessful) {
                            val user: FirebaseUser = task.result!!.user!!
                            Toast.makeText(this, "Registered new account", Toast.LENGTH_SHORT).show()
                            //FirebaseAuth.getInstance().signOut()
                            val intent = Intent(this, MenuActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Log.e("register User", "Error registering user")
                        }

                }
        }
    }

}