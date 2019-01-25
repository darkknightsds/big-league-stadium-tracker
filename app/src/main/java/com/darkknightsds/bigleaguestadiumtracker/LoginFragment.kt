package com.darkknightsds.bigleaguestadiumtracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.darkknightsds.bigleaguestadiumtracker.profile.ProfileFragment
import com.darkknightsds.bigleaguestadiumtracker.stadiums.StadiumsFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import com.google.firebase.database.DataSnapshot

class LoginFragment : Fragment(), View.OnClickListener {
    //Values
    private val TAG: String = "Login Fragment"
    private val RC_SIGN_IN = 1007
    //Variables
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var id: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(activity!!, gso)

        google_sign_in_button.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v == google_sign_in_button) {
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Log.d(TAG, "signInResult:failed code=" + e.statusCode)
            }
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account!!)
        } catch (e: ApiException) {
            Log.d(TAG, "signInResult:failed code=" + e.statusCode)
        }

    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this.activity!!) { task ->
                if (task.isSuccessful) {
                    completeLogInProcess()
                } else {
                    Log.d(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

    private fun completeLogInProcess() {
        id = auth.currentUser!!.uid
        database = FirebaseDatabase.getInstance().reference

        database.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                println(error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(id).exists()){
                    val fragment = StadiumsFragment()
                    loadFragment(fragment)
                } else {
                    createNewDatabaseUser()
                }
            }
        })
    }

    private fun loadFragment(fragment: Fragment) {
        activity!!.supportFragmentManager.beginTransaction().remove(this).commit()
        activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit()
    }

    private fun createNewDatabaseUser() {
        val email = auth.currentUser!!.email
        database.child("users").child(id).child("user_id").setValue(id)
        database.child("users").child(id).child("user_email").setValue(email)
        val fragment = ProfileFragment()
        loadFragment(fragment)
    }
}
