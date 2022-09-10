package com.example.memodiary.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.memodiary.models.entities.RandomMeme
import com.example.memodiary.models.network.RandomMemeApiService
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver


class RandomMemeViewModel : ViewModel(){

    private val randomMemeApiService = RandomMemeApiService()

    //A disposable, previously called SUBSCRIPTION, is a tool that can be used to control the lifecycle of an observable
    private val compositeDisposable = CompositeDisposable()

    val loadRandomMeme = MutableLiveData<Boolean>()
    val randomMemeResponse = MutableLiveData<RandomMeme.Memes>()
    val randomMemeLoadingError = MutableLiveData<Boolean>()


    fun getRandomMemeFromAPI() {
        loadRandomMeme.value = true
        //this will make sure that the application will not going to run endlessly but will only run for a certain time and then it will stop. And when it stops or anything goes wrong, then it will dispose of anything that tried to access it.
        compositeDisposable.add(
            randomMemeApiService.getRandomMeme()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<RandomMeme.Memes>() {
                    override fun onSuccess(value: RandomMeme.Memes) {
                        loadRandomMeme.value = false
                        randomMemeResponse.value = value
                        randomMemeLoadingError.value = false
                    }

                    override fun onError(e: Throwable) {
                        loadRandomMeme.value = false
                        randomMemeLoadingError.value = true
                        e!!.printStackTrace()
                    }
                })
        )
    }

}