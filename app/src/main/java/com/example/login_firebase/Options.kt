package com.example.login_firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import com.example.login_firebase.databinding.ActivityOptionsBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider


class Options : AppCompatActivity() {

    private lateinit var views: ActivityOptionsBinding

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        views = ActivityOptionsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(views.root)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        views.googleBtn.setOnClickListener{
            signIn()
        }
        accionesMenuBajo()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.principal, menu)
        val menuItem = menu?.findItem(R.id.buscar)
        hacerBuscar(menuItem)
        return super.onCreateOptionsMenu(menu)
    }

    private fun hacerBuscar(menuItem: MenuItem?){
        val buscarAlgo = menuItem?.actionView as SearchView
        buscarAlgo.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(this@Options,"typing... " + query, Toast.LENGTH_LONG).show()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Toast.makeText(this@Options, "mandando a buscar... "+newText, Toast.LENGTH_LONG).show()
                return false
            }
        })

    }

    private fun accionesMenuBajo() {
        views.navigation.setOnItemSelectedListener { itemBajo ->
            when (itemBajo.itemId){
                R.id.home -> {
                    val intent = Intent(this@Options, AuthActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }

                else -> false
            }
        }
    }

    // [START onactivityresult]
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 9001) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("succes", "datos:" + account.displayName +" "+ account.email +" "+ account.photoUrl+" "+account.idToken)
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra("full_name", account.displayName)
                    putExtra("email", account.email)
                    putExtra("photoUrl", account.photoUrl.toString())
                }
                this.startActivity(intent)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("error", "Google sign in failed", e)
            }
        }
    }
    // [START signin]
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, 9001)
    }


}





