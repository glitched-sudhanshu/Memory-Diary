package com.example.memodiary.models.network

import com.example.memodiary.models.entities.RandomMeme
import com.example.memodiary.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RandomMemeApiService {
    private val api = Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        //this will call an adapter which uses RxJava3 for creating observables.
        // Adding this class to  retrofit allows us to return observables, flowables, singles etc. (single in our case)
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create(RandomMemeAPI::class.java)

    fun getRandomMeme() : Single<RandomMeme.Memes>{
        //pass in the parameters here as in the URL
        return api.getRandomMeme(Constants.NUMBER_VALUE)
    }
}