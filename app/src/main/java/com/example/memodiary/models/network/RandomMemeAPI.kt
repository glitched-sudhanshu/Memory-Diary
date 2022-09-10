package com.example.memodiary.models.network

import com.example.memodiary.models.entities.RandomMeme
import com.example.memodiary.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomMemeAPI {

    //@GET annotation is from retrofit
    @GET(Constants.API_ENDPOINT)
    //creating the weblink via these constants
    //the parameters required by the API are passed here
    fun getRandomMeme(
//        @Query(Constants.API_ENDPOINT) apiKey: String,
        @Query(Constants.NUMBER) number : Int
    ) : Single<RandomMeme.Memes>
    //Single is from rxJAVA
    //Runs multiple SingleSources and signals the events of the first one that signals (disposing the rest).

}