package io.pemassi.heartbeat.util

import com.telcuon.appcard.restful.extension.getLogger
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.reflect.KClass

object RestfulClient
{
    private val logger by getLogger()

    private val retrofit: Retrofit

    init
    {
        val interceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger
        {
            override fun log(message: String) {
                logger.debug(message)
            }
        })

        interceptor.level = HttpLoggingInterceptor.Level.BODY


        val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

        retrofit = Retrofit.Builder()
                .baseUrl("http://localhost/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
    }

    fun <T : Any> create(service: KClass<T>): T
    {
        return retrofit.create(service.java)
    }
}