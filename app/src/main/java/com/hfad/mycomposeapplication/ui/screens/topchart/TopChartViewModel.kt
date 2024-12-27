package com.hfad.mycomposeapplication.ui.screens.topchart


import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.hfad.mycomposeapplication.data.database.DatabaseRepository
import com.hfad.mycomposeapplication.data.network.DeezerApiService
import com.hfad.mycomposeapplication.data.network.dto.TrackDto
import com.hfad.mycomposeapplication.data.paging.TrackPagingSource
import com.hfad.mycomposeapplication.domain.entity.Audio
import com.hfad.mycomposeapplication.domain.entity.Track
import com.hfad.mycomposeapplication.services.MusicCasterService
import com.hfad.mycomposeapplication.ui.screens.library.displayAudioInfo
import com.hfad.mycomposeapplication.ui.state.StateCaster
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class TopChartViewModel @Inject constructor(
    private val apiService: DeezerApiService,
    private val context: Application,
    private val repository: DatabaseRepository
) : ViewModel() {


    private val _musicList = MutableStateFlow<List<Audio>>(emptyList())
    val musicList: StateFlow<List<Audio>> = _musicList

    val tracks: Flow<PagingData<Track>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { TrackPagingSource(apiService) }
    ).flow.map { pagingData ->
        pagingData.map {
            mapDtoToEntity(it)
        }
    }.cachedIn(viewModelScope) // Кэширование данных в рамках жизненного цикла ViewModel

    private fun mapDtoToEntity(dto: TrackDto) = Track(
        id = dto.id,
        title = dto.title,
        duration = dto.duration.toLong(),
        link = dto.link,
        preview = dto.preview,
        md5_image = dto.md5_image,
        cover_medium = dto.album.cover_xl,
        artist = dto.artist.name
    )

    private val _isPlaying = MutableStateFlow(StateCaster(isPlaying = false))
    val isPlaying: StateFlow<StateCaster> = _isPlaying


    private val controllerFuture: ListenableFuture<MediaController>

    init {
        viewModelScope.launch {
            _musicList.value = repository.getAll()  // Загрузка данных при запуске
        }
        val sessionToken = SessionToken(
            context.applicationContext,
            ComponentName(context.applicationContext, MusicCasterService::class.java)
        )
        controllerFuture =
            MediaController.Builder(context.applicationContext, sessionToken).buildAsync()


        viewModelScope.launch {
            while (true) {
                if (_isPlaying.value.isPlaying) {
                    val currentPosition = controllerFuture.get().currentPosition
                    val duration = controllerFuture.get().duration

                    _isPlaying.value = _isPlaying.value.copy(
                        currentPosition = currentPosition,
                        duration = duration
                    )
                }
                delay(100L) // Обновление каждую 0.3 секунды
            }
        }


        controllerFuture.addListener({
            val controller = controllerFuture.get()

            controller.addListener(object : Player.Listener {
//                override fun onPlaybackStateChanged(playbackState: Int) {
//                    val state = _isPlaying.value
//                    state.isPlaying = playbackState == Player.STATE_READY || playbackState == Player.STATE_BUFFERING
//                }
//

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    _isPlaying.value = _isPlaying.value.copy(isPlaying = isPlaying)
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    mediaItem?.mediaMetadata?.let {
                        _isPlaying.value = _isPlaying.value.copy(
                            obj = Track(
                                title = it.title.toString(),
                                artist = it.artist.toString(),
                                md5_image = "",
                                cover_medium = it.artworkUri.toString(),
                                duration = controller.duration // Задать продолжительность трека
                            )
                        )
                    }
                }

            })
        }, MoreExecutors.directExecutor())
    }

    //fun play(uri: String, coverMedium: String) {
    fun play(post: Track){
        val mediaItem = MediaItem.Builder()
                // Set a unique media ID for the media item
                //.setMediaId("media-1")
                .setUri(Uri.parse(post.preview))
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setArtist(post.artist)
                        .setTitle(post.title)
                        .setArtworkUri(Uri.parse(post.cover_medium))
                        .build()
                ).build()

        controllerFuture.get().setMediaItem(mediaItem)
        controllerFuture.get().play()
        Log.d("MY_TAG", "${post.md5_image}")
        _isPlaying.value = _isPlaying.value.copy(preview = post.preview, obj = post, isContent = false)
    }

    fun playCaster(){
        controllerFuture.get().play()
    }

    fun pause() {
        controllerFuture.get().pause()
    }

    fun playForContent(post: Audio){
        val artworkData = post.imageLong?.let { bitmapToByteArray(it) }
        val mediaItem = MediaItem.Builder()
            // Set a unique media ID for the media item
            //.setMediaId("media-1")
            .setUri(post.uri)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setArtist(post.title)
                    .setTitle(post.title)
                    .setArtworkData(artworkData, MediaMetadata.PICTURE_TYPE_FRONT_COVER)
                    .build()
            ).build()

        controllerFuture.get().setMediaItem(mediaItem)
        controllerFuture.get().play()

        _isPlaying.value = _isPlaying.value.copy(audio = post.imageLong, isContent = true)
        //_isPlaying.value = _isPlaying.value.copy(preview = post.preview, obj = post)
    }

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    var isBottomSheetVisible by mutableStateOf(false)
        private set

    fun showBottomSheet() {
        isBottomSheetVisible = true
    }

    fun hideBottomSheet() {
        isBottomSheetVisible = false
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

