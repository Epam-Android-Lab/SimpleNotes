package com.example.simplenotes.presentation.main

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.simplenotes.R
import com.example.simplenotes.databinding.AddCategoryDialogBinding
import com.example.simplenotes.databinding.FragmentLoginBinding
import com.example.simplenotes.databinding.FragmentMainScreenBinding
import com.example.simplenotes.domain.entities.Category
import com.example.simplenotes.presentation.MainViewModel
import com.example.simplenotes.presentation.login.AuthViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainScreenFragment : Fragment(R.layout.fragment_main_screen) {

    private val binding: FragmentMainScreenBinding by lazy {
        FragmentMainScreenBinding.inflate(layoutInflater)
    }

    private val dialogBinding: AddCategoryDialogBinding by lazy {
        AddCategoryDialogBinding.inflate(layoutInflater)
    }

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddMyCategory.setOnClickListener {
            callAlertDialog()
        }

        //использование кнопки для тестов
        binding.fabAddTask.setOnClickListener {
            findNavController().navigate(R.id.action_mainScreenFragment_to_taskFragment)
        }
    }

    private fun callAlertDialog() {
        val et = EditText(activity)
        val dialog = AlertDialog.Builder(activity).apply {
            setTitle("Добавить категорию")
            setView(et)
            setPositiveButton("Добавить", DialogInterface.OnClickListener { dialog, _ ->
                val categoryName = et.editableText.toString()
                val category = Category(
                        id = "", // нужно будет добавить автоинкремент
                        name = categoryName
                )
                Firebase.auth.uid?.let {
                    viewModel.addCategory(category)
                }
                dialog.dismiss()
            })
            setNegativeButton("Отмена") { dialog, _ ->
                dialog.cancel()
            }
        }
        dialog.show()
    }

}