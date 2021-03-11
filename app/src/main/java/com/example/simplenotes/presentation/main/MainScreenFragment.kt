package com.example.simplenotes.presentation.main

import android.app.AlertDialog
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.simplenotes.R
import com.example.simplenotes.databinding.AddCategoryDialogBinding
import com.example.simplenotes.databinding.FragmentMainScreenBinding
import com.example.simplenotes.databinding.SwitchThemeBinding
import com.example.simplenotes.domain.entities.Category
import com.example.simplenotes.presentation.main.alltasks.AllTasksFragmentArgs
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.ext.android.viewModel


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

    private val viewModel: MainViewModel by viewModel()

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

        myCategoriesAdapter = MyCategoriesAdapter(this)
        val toolbar = binding.topAppBar

//        val appBarConfiguration = AppBarConfiguration(setOf(
//            R.id.action_mainScreenFragment_to_allTasksFragment,
//            R.id.action_mainScreenFragment_to_categoryFragment,
//            R.id.action_mainScreenFragment_to_taskFragment)
//        )

        val appBarConfiguration = AppBarConfiguration(findNavController().graph)

        val navHostFragment = NavHostFragment.findNavController(this)
        NavigationUI.setupWithNavController(toolbar, navHostFragment,appBarConfiguration)

        setHasOptionsMenu(true)
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

        latestTasksAdapter = LatestTasksAdapter()
        binding.recyclerViewLatestTasks.adapter = latestTasksAdapter


        updateCategories()
        updateLatestTasks()


        binding.btnAddMyCategory.setOnClickListener {
            callAlertDialog()
        }

        //использование кнопки для тестов
        binding.fabAddTask.setOnClickListener {
            findNavController().navigate(R.id.action_mainScreenFragment_to_taskFragment)
        }

        binding.btnSwitchTheme.setOnClickListener {
            chooseTheme()
        }

        binding.llFolderAll.setOnClickListener {
            findNavController().navigate(
                R.id.action_mainScreenFragment_to_allTasksFragment,
                AllTasksFragmentArgs(categoryId = "Все").toBundle()
            )
        }

        binding.llFolderToday.setOnClickListener {
            findNavController().navigate(
                R.id.action_mainScreenFragment_to_allTasksFragment,
                AllTasksFragmentArgs(categoryId = "Сегодня").toBundle()
            )
        }

        binding.llFolderImportant.setOnClickListener {
            findNavController().navigate(
                R.id.action_mainScreenFragment_to_allTasksFragment,
                AllTasksFragmentArgs(categoryId = "Важные").toBundle()
            )
        }

        binding.llFolderCompleted.setOnClickListener {
            findNavController().navigate(
                R.id.action_mainScreenFragment_to_allTasksFragment,
                AllTasksFragmentArgs(categoryId = "Выполнено").toBundle()
            )
        }
    }


    private fun callAlertDialog() {
        val et = EditText(activity)
        val dialog = AlertDialog.Builder(activity).apply {
            setTitle("Добавить категорию")
            setView(et)
            setPositiveButton("Добавить") { dialog, _ ->
                val categoryName = et.editableText.toString()
                val category = Category(
                    id = "", // нужно будет добавить автоинкремент
                    name = categoryName
                )
                Firebase.auth.uid?.let {
                    viewModel.addCategory(category)
                }
                updateCategories()
                dialog.dismiss()
            }
            setNegativeButton("Отмена") { dialog, _ ->
                dialog.cancel()
            }
        }
        dialog.show()
    }

    private fun chooseTheme() {

        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                theme.setDarkModeState(false)
            }

            Configuration.UI_MODE_NIGHT_NO -> {
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

    private fun updateCategories() {
        viewModel.getAllCategories()
        viewModel.categoryState.observe(viewLifecycleOwner, {
            myCategoriesAdapter.submitList(it.sortedBy { category -> category.name })
        })
    }

    private fun updateLatestTasks() {
        viewModel.getLatestTasks()
        viewModel.latestTaskState.observe(viewLifecycleOwner, {
            latestTasksAdapter.submitList(it)
        })
    }
}