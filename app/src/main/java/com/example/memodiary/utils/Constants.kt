package com.example.memodiary.utils

object Constants {
    const val ALL_ITEMS: String = "All"
    const val FILTER_SELECTION : String = "FilterSelection"
    const val MEMORY_TYPE : String = "MemoryType"
    const val PEOPLE_INVOLVED : String = "PeopleInvolvedInMemory"

    const val MEMO_IMAGE_SOURCE_LOCAL : String = "Local"
    const val MEMO_IMAGE_SOURCE_ONLINE : String = "Online"

    const val EXTRA_MEMORY_DETAILS : String = "MemoryDetails"

    const val NOTIFICATION_ID = "MemoDiary_notification_id"
    const val NOTIFICATION_NAME = "MemoDiary"
    const val NOTIFICATION_CHANNEL = "MemoDiary_channel_01"

    //creating the weblink via these constants
    //the parameters required by the API are created here
    const val API_ENDPOINT : String = "gimme"
    const val NUMBER : String = "number"
    const val BASE_URL : String = "https://meme-api.herokuapp.com/"

    //giving values to the parameters now

    //API_KEY_VALUE not required in our case
    const val API_KEY_VALUE : String = "gimme"
    const val NUMBER_VALUE : Int = 1



    fun memoryTypes():ArrayList<String>{
        val list = ArrayList<String>()
        list.add("Happiness")
        list.add("Sadness")
        list.add("Disgust")
        list.add("Anger")
        list.add("Surprise")
        return list
    }

    fun peopleInvolvedInMemory():ArrayList<String>{
        val list = ArrayList<String>()
        list.add("Papaji")
        list.add("Mummy")
        list.add("Didi")
        list.add("Babi")
        list.add("Sourav")
        list.add("Muski")
        list.add("Riya")
        return list
    }
}