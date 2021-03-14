package com.example.simplenotes.presentation.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.simplenotes.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashFragment : Fragment(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if(Firebase.auth.currentUser != null){
            findNavController().navigate(R.id.action_splashFragment_to_mainActivity2)
            activity?.finish()
        } else {
            //переход с фрагмента Splash на LoginActivity
            findNavController().navigate(R.id.action_splashFragment_to_loginActivity)
            activity?.finish()
        }
    }
}