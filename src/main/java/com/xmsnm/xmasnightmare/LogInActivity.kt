package com.xmsnm.xmasnightmare

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LogInActivity : Fragment() {

    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val register = view.findViewById<Button>(R.id.register)
        register.setOnClickListener {
            createAccount()
        }

        val login = view.findViewById<Button>(R.id.login)
        login.setOnClickListener {
            logInAccount()
        }
    }

    private fun createAccount() {
        val emailBeforeString = view?.findViewById<EditText>(R.id.username)
        val passwordBeforeString = view?.findViewById<EditText>(R.id.password)
        val email = emailBeforeString?.text.toString()
        val password = passwordBeforeString?.text.toString()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                val db = Firebase.firestore
                val email = auth.currentUser?.email.toString()
                val uniqueId = auth.currentUser?.uid.toString()
                var userObj = hashMapOf(
                    "email" to email,
                    "clear" to 0,
                    "fail" to 0
                )
                db.collection("users").document(uniqueId).set(userObj)
                Toast.makeText(
                    context, "User Successfully Created!",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigate(R.id.action_logInActivity_to_homeFragment)
            } else {
                // If sign in fails, display a message to the user.
                Toast.makeText(
                    context, "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun logInAccount() {
        val emailBeforeString = view?.findViewById<EditText>(R.id.username)
        val passwordBeforeString = view?.findViewById<EditText>(R.id.password)
        val email = emailBeforeString?.text.toString()
        val password = passwordBeforeString?.text.toString()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = auth.currentUser?.email
                Toast.makeText(
                    context, "Logged in as: $user.",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigate(R.id.action_logInActivity_to_homeFragment)
            } else {
                // If sign in fails, display a message to the user.
                Toast.makeText(
                    context, "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}