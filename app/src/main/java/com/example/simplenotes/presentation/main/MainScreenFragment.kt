package com.example.simplenotes.presentation.main

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.simplenotes.R
import com.example.simplenotes.databinding.AddCategoryDialogBinding
import com.example.simplenotes.databinding.FragmentMainScreenBinding
import com.example.simplenotes.databinding.SwitchThemeBinding
import com.example.simplenotes.domain.entities.Category
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@ExperimentalStdlibApi
class MainScreenFragment : Fragment(R.layout.fragment_main_screen) {

    private lateinit var myCategoriesAdapter: MyCategoriesAdapter
    private lateinit var latestTasksAdapter: LatestTasksAdapter

    private val theme: SaveTheme by lazy {
        SaveTheme(requireContext())
    }

    private val binding: FragmentMainScreenBinding by lazy {
        FragmentMainScreenBinding.inflate(layoutInflater)
    }

    private val dialogBinding: AddCategoryDialogBinding by lazy {
        AddCategoryDialogBinding.inflate(layoutInflater)
    }

    private val mainScreenMenuBinding: SwitchThemeBinding by lazy {
        SwitchThemeBinding.inflate(layoutInflater)
    }

    private val viewModel by viewModels<MainViewModel>()


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (theme.loadDarkModeState()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        myCategoriesAdapter = MyCategoriesAdapter()
        binding.recyclerViewMyCategories.adapter = myCategoriesAdapter

        latestTasksAdapter = LatestTasksAdapter()
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

        //использование кнопки для тестов
        binding.fabAddTask.setOnClickListener {
            findNavController().navigate(R.id.action_mainScreenFragment_to_taskFragment)
        }

        binding.btnSwitchTheme.setOnClickListener {
            chooseThemeDialog()
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
                myCategoriesAdapter.notifyDataSetChanged()
                binding.recyclerViewMyCategories.adapter = myCategoriesAdapter
                dialog.dismiss()
            })
            setNegativeButton("Отмена") { dialog, _ ->
                dialog.cancel()
            }
        }
        dialog.show()
    }

    private fun chooseThemeDialog() {

//        val builder = AlertDialog.Builder(activity)
//        builder.setTitle("Выберите тему")
//        val styles = arrayOf("Light", "Dark", "System default")
//        val checkedItem = 0
//
//
//        builder.setSingleChoiceItems(styles, checkedItem) { dialog, which ->
//            when (which) {
//                0 -> {
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//                    themeSwitched = true
//                    dialog.dismiss()
//                }
//                1 -> {
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//                    themeSwitched = true
//                    dialog.dismiss()
//                }
//                2 -> {
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
//                    themeSwitched = true
//                    dialog.dismiss()
//                }
//            }
//        }
//        val dialog = builder.create()
//        dialog.show()

        when(resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                theme.setDarkModeState(false)
            }

            Configuration.UI_MODE_NIGHT_NO ->{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                theme.setDarkModeState(true)
            }

        }
    }

    private fun loadTheme() {
        if (theme.loadDarkModeState()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

}