package com.example.prm.data.remote

import com.example.prm.data.remote.api.OrderApi
import com.example.prm.data.session.SessionManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import android.util.Log
import com.example.prm.data.remote.api.PaymentApi

object RetrofitClient {
    private const val TAG = "RetrofitClient"
    // Change this to your backend URL
    // For localhost: https://10.0.2.2:5001/api/ (Android emulator - HTTPS)
    // For real device: https://YOUR_MACHINE_IP:5001/api/
    private const val BASE_URL = "https://10.0.2.2:5001/api/"

    // Will be initialized from the app layer so that we can attach tokens
    @Volatile
    var sessionManager: SessionManager? = null

    fun initSessionManager(manager: SessionManager) {
        Log.d(TAG, "initSessionManager called with token: ${manager.getAccessToken()}")
        sessionManager = manager
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Interceptor to automatically add Authorization header if token exists
    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val token = sessionManager?.getAccessToken()

        Log.d(TAG, "authInterceptor: token = $token")
        Log.d(TAG, "authInterceptor: sessionManager = ${sessionManager != null}")

        val newRequestBuilder = originalRequest.newBuilder()
        if (!token.isNullOrBlank()) {
            Log.d(TAG, "Adding Authorization header with token: Bearer $token")
            newRequestBuilder.addHeader("Authorization", "Bearer $token")
        } else {
            Log.w(TAG, "Warning: No token found, request will be sent without Authorization header")
        }

        chain.proceed(newRequestBuilder.build())
    }

    // Create a trust manager that accepts all certificates (for development only)
    private val trustAllCerts = arrayOf<TrustManager>(
        object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        }
    )

    // Install the all-trusting trust manager
    private val sslContext = SSLContext.getInstance("SSL").apply {
        init(null, trustAllCerts, java.security.SecureRandom())
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
        .hostnameVerifier { _, _ -> true }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }

    val orderApi: OrderApi by lazy {
        retrofit.create(OrderApi::class.java)
    }

    val paymentApi: PaymentApi by lazy {
        retrofit.create(PaymentApi::class.java)
    }
}

