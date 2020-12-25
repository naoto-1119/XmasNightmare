package com.xmsnm.xmasnightmare

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.xmsnm.xmasnightmare.R

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class SecondPart4Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second_part4, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.next_round_button_2).setOnClickListener {
            findNavController().navigate(R.id.action_SecondPart4Fragment_to_ThirdFragment)
        }
    }
}