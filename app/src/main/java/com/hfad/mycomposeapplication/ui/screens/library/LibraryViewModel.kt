package com.hfad.mycomposeapplication.ui.screens.library

import android.app.Application
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hfad.mycomposeapplication.data.repository.AccountRepositoryImpl
import com.hfad.mycomposeapplication.domain.entity.Music
import com.hfad.mycomposeapplication.domain.repository.DatabaseRepository
import com.hfad.mycomposeapplication.domain.usecase.FriendsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val repository: DatabaseRepository,
    private val context: Application,
): ViewModel() {

    private val _musicList = MutableStateFlow<List<Music>>(emptyList())
    val musicList: StateFlow<List<Music>> = _musicList

    init {
        viewModelScope.launch {
            _musicList.value = repository.getAll()
        }
    }

    fun updateItem(index: Int, updatedMusic: Music) {
        _musicList.update { currentList ->
            currentList.toMutableList().also {
                it[index] = updatedMusic
            }
        }
    }

    fun removeContact(contact: Music) {
        viewModelScope.launch {
            repository.deleteItem(contact)
            _musicList.value = repository.getAll()
        }
    }

    fun addAudio(uri: Uri) {
        val audioItem = displayAudioInfo(context, uri)
        if (audioItem != null) {
            viewModelScope.launch {
                repository.insert(audioItem)
                _musicList.value = repository.getAll()  // Обновление списка после добавления
            }
        }
    }
}

