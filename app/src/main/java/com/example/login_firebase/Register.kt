package com.example.login_firebase

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.login_firebase.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class Register : AppCompatActivity() {

    private lateinit var views: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        views = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(views.root)
        setup()
        session()
        accionesMenuBajo()
    }

    private fun accionesMenuBajo() {
        views.navigation.setOnItemSelectedListener { itemBajo ->
            when (itemBajo.itemId) {
                R.id.home -> {
                    val intent = Intent(this@Register, AuthActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.opciones -> {
                    val intent = Intent(this@Register, Options::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false

            }
        }
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

    private fun setup(){
        views.signupBtn.setOnClickListener {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                views.user.text.toString(),
                views.password.text.toString()
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    if (views.password.text.toString().equals(views.confirmPassword.text.toString())) {
                        showHome(it.result?.user?.email ?: "", ProviderType.BASIC)
                    } else {
                        Toast.makeText(this, "!ERROR! Passwords does not match", Toast.LENGTH_SHORT)
                            .show()
                    }

                } else {
                    Toast.makeText(this, "!ERROR!, User already exists", Toast.LENGTH_SHORT).show()
                }
            }
        }
        views.loginMsg.setOnClickListener {
            showAuth()
        }
    }

    private fun showAuth() {
        val intent:Intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
    }

    private fun showHome(email: String, provider: ProviderType) {
        val intent: Intent = Intent(this, MainActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(intent)
    }
}