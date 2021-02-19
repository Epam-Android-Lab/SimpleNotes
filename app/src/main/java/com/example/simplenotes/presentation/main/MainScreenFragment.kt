package com.example.simplenotes.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.simplenotes.R
import com.example.simplenotes.databinding.FragmentLoginBinding
import com.example.simplenotes.databinding.FragmentMainScreenBinding
import com.example.simplenotes.presentation.login.AuthViewModel

class MainScreenFragment : Fragment(R.layout.fragment_main_screen) {

    private val binding: FragmentMainScreenBinding by lazy {
        FragmentMainScreenBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //использование кнопки для тестов
        binding.fabAddTask.setOnClickListener {
            findNavController().navigate(R.id.action_mainScreenFragment_to_taskFragment)
        }
    }

}