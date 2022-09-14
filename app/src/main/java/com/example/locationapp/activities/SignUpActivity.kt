package com.example.locationapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.example.locationapp.databinding.ActivitySignUpBinding
import com.example.locationapp.viewmodels.SignUpViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    private var binding: ActivitySignUpBinding? = null
    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnRegister?.setOnClickListener {
            registerUser()
        }

        binding?.fabRegister?.setOnClickListener {
            onBackPressed()
        }

        binding?.btnGoToSignIn?.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun registerUser() {
        if (binding?.getName?.text?.isNotEmpty() == true
            && binding?.getEmail?.text?.isNotEmpty() == true
            && binding?.getPassword?.text?.isNotEmpty() == true) {
            val email = binding?.getEmail?.text.toString()
            val password = binding?.getPassword?.text.toString()
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    task: Task<AuthResult> ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Registered new account", Toast.LENGTH_SHORT).show()

                            //send POST request here in order to store user credentials on the server
                            val name: String = binding?.getName?.text.toString()
                            viewModel.postUserToServer(email, name, FirebaseAuth.getInstance().currentUser!!.uid)
                            FirebaseMessaging.getInstance().token.addOnCompleteListener(
                                OnCompleteListener { task ->
                                if (!task.isSuccessful) {
                                    Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                                    return@OnCompleteListener
                                }

                                // Get new FCM registration token
                                val token = task.result

                                // Send info to server
                                 viewModel.sendDeviceTokenToServer(token, FirebaseAuth.getInstance().currentUser!!.uid)
                            })
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