package com.example.nytarticles.di
import android.net.ConnectivityManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nytarticles.networking.NetworkStatusChecker
import com.example.nytarticles.repository.ArticleRepo
import com.example.nytarticles.repository.UserRepository
import com.example.nytarticles.viewmodel.ArticlesViewModel
import com.example.nytarticles.viewmodel.UserViewModel
import org.koin.dsl.module
import splitties.init.appCtx

val presentationModule = module {


    fun provideConnectivityManager(): ConnectivityManager {
        return appCtx.getSystemService(ConnectivityManager::class.java)
    }

    fun provideNetworkStatusChecker(): NetworkStatusChecker {
        return NetworkStatusChecker()
    }
    single { provideConnectivityManager() }
    single { ArticleRepo() }
    single { UserRepository() }
    single {
        ArticlesViewModel(get())
    }
    single { provideNetworkStatusChecker() }
    fun provideLayoutManager(): LinearLayoutManager {
        return LinearLayoutManager(appCtx, RecyclerView.VERTICAL, false)
    }
    factory { provideLayoutManager() }
    single { UserViewModel() }

}