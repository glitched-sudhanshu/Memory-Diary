package com.example.memodiary.viewmodels

import android.widget.ViewSwitcher
import androidx.lifecycle.*
import com.example.memodiary.models.database.MemoDiaryRepository
import com.example.memodiary.models.entities.MemoDiary
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MemoDiaryViewModel(private val repository: MemoDiaryRepository) : ViewModel() {

    fun insert(memoDiary: MemoDiary) = viewModelScope.launch {
        repository.insertMemoDiaryData(memoDiary)
    }

    /**
     * using live data and caching with all returns has several benefits, we can put an observer on the data instead of calling for changes and only update the UI when data actually changes
     */
    val allMemoryList : LiveData<List<MemoDiary>> = repository.allMemoDiaryList.asLiveData()

    fun update(memory : MemoDiary) = viewModelScope.launch {
        repository.updateFavMemoryData(memory)
    }

    val allFavouriteMemory : LiveData<List<MemoDiary>> = repository.allFavouriteMemory.asLiveData()

    fun delete(memory: MemoDiary) = viewModelScope.launch {
        repository.deleteMemoryData(memory)
    }

    fun getFilteredList(value:String) :LiveData<List<MemoDiary>> = repository.filteredListMemories(value).asLiveData()
}

//it will take care of the life cycle of viewModel. So, it will survive configuration changes
//takes in all the arguments as of ViewModel class
class MemoDiaryViewModelFactory(private val repository: MemoDiaryRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>) : T {
        if(modelClass.isAssignableFrom(MemoDiaryViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return MemoDiaryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}


//wonderful documentation for viewModel
//https://developer.android.com/codelabs/android-room-with-a-view-kotlin#9

/**
 * ViewModel call -> insert -> launch in a coroutine -> repository call -> insert method of repository [insertMemoDiaryData()] -> DAO call -> DAO insert function call hoga [insertFavMemoDiaryDetails()]
 */