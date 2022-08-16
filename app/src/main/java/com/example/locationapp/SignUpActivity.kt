package com.example.locationapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.locationapp.databinding.ActivitySignUpBinding
import com.example.locationapp.models.DeviceTokenModel
import com.example.locationapp.models.DeviceTokenResponse
import com.example.locationapp.models.UserModel
import com.example.locationapp.models.UserResponse
import com.example.locationapp.network.SettingsService
import com.example.locationapp.network.UserService
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class SignUpActivity : AppCompatActivity() {

    private var binding: ActivitySignUpBinding? = null

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
                            val user: FirebaseUser = task.result!!.user!!
                            Toast.makeText(this, "Registered new account", Toast.LENGTH_SHORT).show()

                            //send POST request here in order to store user credentials on the server
                            val name: String = binding?.getName?.text.toString()
                            postUserToServer(email, name, FirebaseAuth.getInstance().currentUser!!.uid)
                            FirebaseMessaging.getInstance().token.addOnCompleteListener(
                                OnCompleteListener { task ->
                                if (!task.isSuccessful) {
                                    Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                                    return@OnCompleteListener
                                }

                                // Get new FCM registration token
                                val token = task.result

                                // Send info to server
                                sendDeviceTokenToServer(token, FirebaseAuth.getInstance().currentUser!!.uid)
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

    private fun postUserToServer(email: String, name: String, token: String) {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val userService: UserService = retrofit.create(UserService::class.java)

        val newUser = UserModel(token, email, name)

        val call: Call<UserResponse> = userService.postUser(newUser)

        call.enqueue(object : Callback<UserResponse>{
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                val userResponse: UserResponse = response.body()!!
                Log.i("Response result", "$userResponse")
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e("retrofit", "error " + t.message)
            }
        })
    }

    private fun sendDeviceTokenToServer(deviceToken: String, userToken: String) {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val settingsService: SettingsService = retrofit.create(SettingsService::class.java)

        val newDeviceToken = DeviceTokenModel(deviceToken, userToken)

        val call: Call<DeviceTokenResponse> = settingsService.sendDeviceToken(newDeviceToken)

        call.enqueue(object : Callback<DeviceTokenResponse>{
            override fun onResponse(call: Call<DeviceTokenResponse>, response: Response<DeviceTokenResponse>) {
                val settingsResponse: DeviceTokenResponse = response.body()!!
                Log.i("Response result", "$settingsResponse")
            }

            override fun onFailure(call: Call<DeviceTokenResponse>, t: Throwable) {
                Log.e("retrofit", "error " + t.message)
            }
        })
    }
}