package com.example.covidtrackerapp

import Data.User
import Verification_Validation.Validation
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient:GoogleSignInClient

    companion object {
        val RC_SIGN_IN=100
       val  databaseref =FirebaseDatabase.getInstance().reference
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth= FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web))
            .build()
        googleSignInClient = GoogleSignIn.getClient(this,gso)
        val signInButton = findViewById<SignInButton>(R.id.sign_in_with_gmail)
        signInButton.setOnClickListener({
            signIn()
        })

        val register = findViewById<Button>(R.id.Register)
        register.setOnClickListener {
                register_user()
        }
    }
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            Log.e("s",task.toString())
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }
    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
//        val intent = Intent(this,HomeActivity::class.java)
//        intent.putExtra("user","Abhishek")
//        startActivity(intent)

        if (currentUser != null) {
            updateUI(currentUser)
        }
    }

    private fun updateUI(user:FirebaseUser) {
        val intent = Intent(this,HomeActivity::class.java)
        val user1 = user.displayName?.let { user?.email?.let { it1 -> User(it, it1) } }
        intent.putExtra("user",user1.toString())
        startActivity(intent)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = mAuth.currentUser
                    if (user != null) {
                        updateUI(user)
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this,"Something went wrong!",Toast.LENGTH_LONG).show()
                }
            }
    }
    fun register_user()
    {
        val obj = Validation()
        val name = findViewById<EditText>(R.id.name)
        val password = findViewById<EditText>(R.id.pass)
        val cpassword = findViewById<EditText>(R.id.cPpassword)
        val email = findViewById<EditText>(R.id.email)

        val user_name = name.text.toString()
        val user_pass=password.text.toString()
        val user_email =email.text.toString()

        val isnamecorrect = obj.verify_name(user_name)
        val ispasswordcorrect = obj.verify_password(user_pass)
        val isemailCorrect = obj.verify_email(user_email)

        if(!isnamecorrect)
            name.setError("Name is not valid!!")

        if(!isemailCorrect)
            email.setError("Error must contain @ and one number!")

        if(!ispasswordcorrect)
            password.setError("Password must contain atleast 1 num,special symbol ,small and caps!")

        if(!user_pass.equals(cpassword.text.toString()))
            cpassword.setError("Password does not match!")
        Log.e(user_pass, cpassword.text.toString())

        if(isnamecorrect && isemailCorrect && ispasswordcorrect && user_pass.equals(cpassword.text.toString())) {
            val intent = Intent(this, HomeActivity::class.java)
            val user2 = User(user_name,user_email,user_pass)

            var useremail1 = user_email.replace("@","_")
            useremail1=useremail1.replace('.','_')
            databaseref.child("Users").child(useremail1).setValue(user2)
            intent.putExtra("user",user2.toString())
            startActivity(intent)
        }

    }
}