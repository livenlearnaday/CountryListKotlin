package io.github.livenlearnaday.countrylistkotlin.data.remote


import io.github.livenlearnaday.countrylistkotlin.utils.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitBuilder {


    /*
        Configure Timeout Settings

We can set timeouts settings on the underlying HTTP client. If we don’t specify a client, Retrofit will create one with default connect and read timeouts. By default, Retrofit uses the following timeouts:

    Connection timeout: 10 seconds
    Read timeout: 10 seconds
    Write timeout: 10 seconds

To customize the timeouts settings you need to configure OkHttp, Retrofit’s network layer. See the code below.

OkHttpClient okHttpClient = new OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build();
         */

        var client = OkHttpClient().newBuilder()
        .readTimeout(5, TimeUnit.MINUTES)
        .writeTimeout(3, TimeUnit.MINUTES)
        .connectTimeout(3, TimeUnit.MINUTES).build()



    private fun getRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val countryService:CountryService = getRetrofit().create(CountryService::class.java)

}
