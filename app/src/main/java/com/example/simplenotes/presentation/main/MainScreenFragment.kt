package com.example.simplenotes.presentation.main

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.simplenotes.R
import com.example.simplenotes.databinding.AddCategoryDialogBinding
import com.example.simplenotes.databinding.FragmentMainScreenBinding
import com.example.simplenotes.presentation.main.alltasks.AllTasksFragmentArgs
import com.example.simplenotes.domain.entities.Category
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainScreenFragment : Fragment(R.layout.fragment_main_screen) {

    private val binding: FragmentMainScreenBinding by lazy {
        FragmentMainScreenBinding.inflate(layoutInflater)
    }

    private val dialogBinding: AddCategoryDialogBinding by lazy {
        AddCategoryDialogBinding.inflate(layoutInflater)
    }

    private val viewModel : MainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = binding.topAppBar

//        val appBarConfiguration = AppBarConfiguration(setOf(
//            R.id.action_mainScreenFragment_to_allTasksFragment,
//            R.id.action_mainScreenFragment_to_categoryFragment,
//            R.id.action_mainScreenFragment_to_taskFragment)
//        )

        val appBarConfiguration = AppBarConfiguration(findNavController().graph)

        val navHostFragment = NavHostFragment.findNavController(this)
        NavigationUI.setupWithNavController(toolbar, navHostFragment,appBarConfiguration)

        toolbar.inflateMenu(R.menu.main_fragment_menu)
        toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.switch_mode -> {
                    //todo: change app theme here
                }
                R.id.logout -> {
                    FirebaseAuth.getInstance().signOut()
                    findNavController().navigate(R.id.action_mainScreenFragment_to_loginActivity3)
                }
            }

            return@setOnMenuItemClickListener false
        }

        val myCategoriesAdapter = MyCategoriesAdapter()
        binding.recyclerViewMyCategories.adapter = myCategoriesAdapter

        val latestTasksAdapter = LatestTasksAdapter()
        binding.recyclerViewLatestTasks.adapter = latestTasksAdapter

        viewModel.getAllCategories()
        viewModel.getLatestTasks()

        viewModel.categoryState.observe(viewLifecycleOwner, {
            myCategoriesAdapter.submitList(it)
        })

        viewModel.latestTaskState.observe(viewLifecycleOwner, {
            latestTasksAdapter.submitList(it)
        })

        binding.btnAddMyCategory.setOnClickListener {
            callAlertDialog()
        }

        binding.fabAddTask.setOnClickListener {
            findNavController().navigate(R.id.action_mainScreenFragment_to_taskFragment)
        }
        binding.test.setOnClickListener {
            findNavController().navigate(R.id.action_mainScreenFragment_to_allTasksFragment, AllTasksFragmentArgs(categoryId = "Все").toBundle())
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