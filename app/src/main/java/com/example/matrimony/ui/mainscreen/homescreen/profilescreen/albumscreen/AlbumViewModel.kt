package com.example.matrimony.ui.mainscreen.homescreen.profilescreen.albumscreen

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matrimony.db.entities.Album
import com.example.matrimony.db.repository.AlbumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AlbumViewModel @Inject constructor(private val albumRepository: AlbumRepository) :
    ViewModel() {

    var loaded=false

    var albumList = mutableListOf<Album>()


    var imageUri: Uri? = null

    suspend fun getProfilePic(userId: Int):LiveData<Album?>{
        return albumRepository.getProfilePic(userId)
    }

    fun addAlbum(album: Album) {
        albumList.add(album)
        viewModelScope.launch {
            albumRepository.addAlbum(album)
        }
    }


    fun removePicture(userId: Int, imageId: Int) {
        viewModelScope.launch {
            albumRepository.removePicture(userId, imageId)
        }
    }


    suspend fun getImage(userId: Int, imageId: Int): LiveData<Album> {
        return albumRepository.getImage(userId, imageId)
    }

    suspend fun getUserAlbum(userId: Int): LiveData<List<Album?>?> {
        return albumRepository.getUserAlbum(userId)
    }


    suspend fun getUserAlbumCount(userId: Int):LiveData<Int>{
        return albumRepository.getUserAlbumCount(userId)
    }
}