package com.example.login_firebase

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.login_firebase.databinding.ActivityAuthBinding


import com.google.firebase.auth.FirebaseAuth

class AuthActivity : AppCompatActivity() {

    private lateinit var views: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        views = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(views.root)
        setup()
        session()

    }

    override fun onStart() {
        super.onStart()
        views.root.visibility = View.VISIBLE
    }

    private fun session() {

        val prefs = getSharedPreferences(
            getString(com.example.login_firebase.R.string.prefs_file),
            Context.MODE_PRIVATE
        )
        val email = prefs.getString("email", null)
        val provider = prefs.getString("provider", null)

        //si ya estamos logeados pasamos a la siguiente activity

        if (email != null && provider != null) {
            views.root.visibility = View.INVISIBLE
            showHome(email, ProviderType.valueOf(provider))
        }

    }

    private fun setup() {
        views.loginBtn.setOnClickListener {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(
                views.user.text.toString(),
                views.password.text.toString()
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    showHome(it.result?.user?.email ?: "", ProviderType.BASIC)

                } else {
                    Toast.makeText(this, "Error, incorrect password", Toast.LENGTH_SHORT).show()
                }
            }
        }

        views.registerBtn.setOnClickListener {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                views.user.text.toString(),
                views.password.text.toString()
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    if (views.password.text.toString().equals(views.password.text.toString())) {
                        showHome(it.result?.user?.email ?: "", ProviderType.BASIC)
                    } else {
                        Toast.makeText(this, "Error, password does not match", Toast.LENGTH_SHORT)
                            .show()
                    }

                } else {
                    Toast.makeText(this, "Error, user already exists", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    private fun showHome(email: String, provider: ProviderType) {

        val intent: Intent = Intent(this, MainActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(intent)
    }


}