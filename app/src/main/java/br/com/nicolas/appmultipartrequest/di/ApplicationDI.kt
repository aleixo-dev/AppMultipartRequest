package br.com.nicolas.appmultipartrequest.di

import br.com.nicolas.appmultipartrequest.data.repository.TurismRepository
import br.com.nicolas.appmultipartrequest.data.repository.TurismRepositoryImpl
import br.com.nicolas.appmultipartrequest.data.service.TurismService
import br.com.nicolas.appmultipartrequest.home.MainActivityViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val applicationModule = module {

    single { provideRetrofitInstance() }


    single<TurismRepository> {
        TurismRepositoryImpl(get())
    }

    viewModel {
        MainActivityViewModel(get())
    }
}


private fun provideRetrofitInstance(): TurismService {
    return Retrofit.Builder()
        .baseUrl("http://localhost:3333/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(provideOkHttpClient())
        .build()
        .create(TurismService::class.java)
}

fun provideOkHttpClient(): OkHttpClient {

    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

    return OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(httpLoggingInterceptor)
        .build()
}