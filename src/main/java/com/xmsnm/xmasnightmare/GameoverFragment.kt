package com.xmsnm.xmasnightmare

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.xmsnm.xmasnightmare.GameoverFragmentArgs
import com.xmsnm.xmasnightmare.R

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class GameoverFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_gameover, container, false)
    }

    val args: GameoverFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//      creating accuracy text for gameover version
        val accuracy = args.myArg
        val accuracyText = getString(R.string.gameover_accuracy, accuracy)
        view.findViewById<TextView>(R.id.gameover_accuracy_text).text = accuracyText

//      get
        val uniqueId = auth.currentUser?.uid.toString()
        db.collection("users").document(uniqueId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val failLong: Long? = document.get("fail") as Long?
                    val newFail = failLong?.plus(1)
                    val data = hashMapOf("fail" to newFail)
                    db.collection("users")
                        .document(uniqueId)
                        .set(data, SetOptions.merge())
                }
                Toast.makeText(
                    context, "Fail Count Increased! Try Again!",
                    Toast.LENGTH_SHORT
                ).show()
            }

//      navgate back to first fragment
        view.findViewById<Button>(R.id.return_button).setOnClickListener {
            findNavController().navigate(R.id.action_GameoverFragment_to_homeFragment)
        }
    }
}