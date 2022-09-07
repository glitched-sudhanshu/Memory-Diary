package com.example.memodiary.utils

object Constants {
    const val ALL_ITEMS: String = "All"
    const val FILTER_SELECTION : String = "FilterSelection"
    const val MEMORY_TYPE : String = "MemoryType"
    const val PEOPLE_INVOLVED : String = "PeopleInvolvedInMemory"

    const val MEMO_IMAGE_SOURCE_LOCAL : String = "Local"
    const val MEMO_IMAGE_SOURCE_ONLINE : String = "Online"

    const val EXTRA_MEMORY_DETAILS : String = "MemoryDetails"

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