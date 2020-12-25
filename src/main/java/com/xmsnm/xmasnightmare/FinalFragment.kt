package com.xmsnm.xmasnightmare

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.xmsnm.xmasnightmare.R

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FinalFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_final, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val uniqueId = auth.currentUser?.uid.toString()
        db.collection("users").document(uniqueId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val clearLong: Long? = document.get("clear") as Long?
                    val newClear = clearLong?.plus(1)
                    val data = hashMapOf("clear" to newClear)
                    db.collection("users")
                        .document(uniqueId)
                        .set(data, SetOptions.merge())
                }
                Toast.makeText(
                    context, "Completed Count Increased! Congratulations!",
                    Toast.LENGTH_SHORT
                ).show()
            }

        view.findViewById<Button>(R.id.return_button_2).setOnClickListener {
            findNavController().navigate(R.id.action_finalFragment_to_homeFragment)
        }
    }
}