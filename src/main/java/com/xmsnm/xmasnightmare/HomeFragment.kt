package com.xmsnm.xmasnightmare

import android.content.ActivityNotFoundException
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.xmsnm.xmasnightmare.R

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // access database
        val email = auth.currentUser?.email.toString()
        val uniqueId = auth.currentUser?.uid.toString()
        db.collection("users").document(uniqueId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val clearLong: Long? = document.get("clear") as Long?
                    val clear = clearLong.toString()
                    val failLong: Long? = document.get("fail") as Long?
                    val fail = failLong.toString()
                    view.findViewById<TextView>(R.id.clear_count).text = "Completed: $clear"
                    view.findViewById<TextView>(R.id.failed_count).text = "Failed: $fail"
                }
            }
        view.findViewById<TextView>(R.id.user_email).text = "User Name: $email"

        // nav
        view.findViewById<Button>(R.id.log_out_button).setOnClickListener {
            try {
                Firebase.auth.signOut()
                Toast.makeText(
                    context, "Successfully Logged Out",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigate(R.id.action_homeFragment_to_logInActivity)
            } catch (e: ActivityNotFoundException) {
                // display error state to the user
                val myToast = Toast.makeText(context, e.message, Toast.LENGTH_SHORT)
                myToast.show()
            }
        }

        // nav
        view.findViewById<Button>(R.id.start_button).setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_FirstFragment)
        }
    }

    public fun signOut() {
        Firebase.auth.signOut()
    }
}