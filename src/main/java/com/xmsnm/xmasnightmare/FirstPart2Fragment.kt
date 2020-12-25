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
class FirstPart2Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first_part2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.next_round_button).setOnClickListener {
            findNavController().navigate(R.id.action_FirstPart2Fragment_to_SecondFragment)
        }
    }
}