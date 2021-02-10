package com.example.simplenotes.presentation.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.simplenotes.R

class SplashFragment : Fragment(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //переход с фрагмента Splash на LoginActivity
        findNavController().navigate(R.id.action_splashFragment_to_loginActivity)

    }
}