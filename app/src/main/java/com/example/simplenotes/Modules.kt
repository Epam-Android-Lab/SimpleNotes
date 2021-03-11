package com.example.simplenotes

import com.example.simplenotes.data.repositories.FirestoreRepository
import com.example.simplenotes.data.repositories.RemoteRepository
import com.example.simplenotes.domain.repositories.IRepository
import com.example.simplenotes.domain.usecases.*
import com.example.simplenotes.presentation.login.AuthViewModel
import com.example.simplenotes.presentation.main.Contract
import com.example.simplenotes.presentation.main.MainViewModel
import com.example.simplenotes.presentation.main.TaskViewModel
import com.example.simplenotes.presentation.main.alltasks.AllTasksViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    single { FirestoreRepository() } bind IRepository.FirestoreRepository::class
    single { RemoteRepository() } bind IRepository.AuthRepository::class
}

val domainModule  = module {
    factory { AddNewTaskUseCase(get()) }
    factory { CreateCategoryUseCase(get()) }
    factory { GetAllCategoriesByUser(get()) }
    factory { GetAllTasksByUserUseCase(get()) }
    factory { GetTaskByIdUseCase(get()) }
    factory { GetTasksByCategoryUseCase(get()) }
    factory { SignInUserUseCase(get()) }
    factory { SignUpUserUseCase(get()) }
    factory { UpdateTaskStatusUseCase(get()) }
    factory { UpdateTaskUseCase(get()) }
}

val viewModelModule = module {
    viewModel { TaskViewModel(get(), get(), get(), get(), get()) } bind Contract.ITaskViewModel::class
    viewModel { AuthViewModel(get(), get()) } bind Contract.IAuthViewModel::class
    viewModel { MainViewModel(get(), get(), get()) } bind Contract.IMainViewModel::class
    viewModel { AllTasksViewModel(get(), get()) } bind Contract.IAllTasksViewModel::class
}

val modules = listOf(
        dataModule,
        domainModule,
        viewModelModule
)