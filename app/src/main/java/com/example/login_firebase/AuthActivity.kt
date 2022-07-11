package com.example.login_firebase

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
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
        accionesMenuBajo()

    }

    private fun accionesMenuBajo() {
        views.navigation.setOnItemSelectedListener { itemBajo ->
            when (itemBajo.itemId) {
                R.id.opciones -> {
                    val intent = Intent(this@AuthActivity, Options::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.principal_auth, menu)
        views.principal.setOnMenuItemClickListener { item ->
            when (item.itemId){
                R.id.preferencias_auth -> {
                    Toast.makeText(this@AuthActivity, "Aqui van las preferencias", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
        return super.onCreateOptionsMenu(menu)
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

        views.socialOpt.setOnClickListener {
            showOptions()
        }

        views.registerMsg.setOnClickListener {
            showRegister()
        }


    }

    private fun showOptions() {
        val intent: Intent = Intent(this, Options::class.java)
        startActivity(intent)
    }

    private fun showRegister() {
        val intent: Intent = Intent(this, Register::class.java)
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





