package com.example.locationapp


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.locationapp.databinding.ActivityMenuBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging

class MenuActivity : AppCompatActivity() {

    private var binding: ActivityMenuBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.i("FCM token", token)
            Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
        })

        binding?.btnshowCurrentLocation?.setOnClickListener {
            val intent = Intent(this, ShowLocationActivity::class.java)
            startActivity(intent)
        }

        binding?.btnSettings?.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        binding?.btnLogout?.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            finish()
        }

        binding?.btnListUsers?.setOnClickListener {
            val intent = Intent(this, UserListActivity::class.java)
            startActivity(intent)
        }
    }
}