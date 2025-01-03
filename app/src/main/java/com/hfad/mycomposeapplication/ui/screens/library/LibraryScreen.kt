package com.hfad.mycomposeapplication.ui.screens.library

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hfad.mycomposeapplication.R
import com.hfad.mycomposeapplication.domain.entity.Music
import com.hfad.mycomposeapplication.ui.screens.library.swipe.ActionIcon
import com.hfad.mycomposeapplication.ui.screens.topchart.TopChartViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(modifier: Modifier = Modifier, viewModel: TopChartViewModel){

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val musicListState by viewModel.musicList.collectAsStateWithLifecycle()

//    val listMusicState by viewModel.isPlaying.collectAsState()

    val context = LocalContext.current
    val openFilePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
                viewModel.addAudio(uri)
            }
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            itemsIndexed(musicListState){ index, contact ->
                SwipeableItemWithActions(
                    isRevealed = contact.isOptionsRevealed,
                    onExpanded = {
                        viewModel.updateItem(index, contact.copy(isOptionsRevealed = true))
                    },
                    onCollapsed = {
                        viewModel.updateItem(index, contact.copy(isOptionsRevealed = false))
                    },
                    actions = {
                        ActionIcon(
                            onClick = {
                                Toast.makeText(
                                    context,
                                    "Contact ${contact.title} was deleted.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                viewModel.removeContact(contact)
                            },
                            backgroundColor = Color.Red,
                            icon = Icons.Default.Delete,
                            modifier = Modifier.fillMaxHeight()
                        )
                    },
                ) {
                    AudioItem(contact, viewModel)
                }
            }
//            items(musicListState) { audio ->
//                AudioItem(audio, viewModel)
//            }
        }
        FloatingActionButton(
            onClick = {  viewModel.showBottomSheet()  },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(id = R.string.email)
            )
        }

        if (viewModel.isBottomSheetVisible){
            ModalBottomSheet(onDismissRequest = {
                viewModel.hideBottomSheet()
            },
                sheetState = sheetState
            ) {
                Button(
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 16.dp),
                    onClick = {
                    openFilePicker.launch(arrayOf("audio/*"))
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            viewModel.hideBottomSheet()
                        }
                    }

                }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Pick audio file",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add track in library")
                }
            }
        }
    }
}

//@Composable
//fun DraggableListItem(text: String) {
//    var offsetX by remember { mutableStateOf(0f) } // Хранит смещение по оси X
//    val density = LocalDensity.current // Для конвертации dp в пиксели
//    val maxDragDistance = with(density) { 150.dp.toPx() } // Максимальное смещение
//
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(64.dp)
//    ) {
//        // Задний блок (красный фон)
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color.Red)
//        )
//
//        // Передний блок (текст)
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .offset { IntOffset(offsetX.toInt(), 0) } // Смещение по X
//                .background(Color.White)
//                .pointerInput(Unit) {
//                    detectDragGestures(
//                        onDrag = { change, dragAmount ->
//                            change.consume() // Указываем, что жест обработан
//                            offsetX = (offsetX + dragAmount).coerceIn(0f, maxDragDistance) // Ограничиваем смещение
//                        },
//                        onDragEnd = {
//                            // Возвращаем в исходное состояние после завершения перетаскивания
//                            offsetX = 0f
//                        }
//                    )
//                }
//                .clip(RoundedCornerShape(8.dp))
//                .border(1.dp, Color.Gray),
//            contentAlignment = Alignment.Center
//        ) {
//            Text(text = text, color = Color.Black, style = MaterialTheme.typography.bodyMedium)
//        }
//    }
//}


fun displayAudioInfo(context: Context, uri: Uri): Music? {
    val retriever = MediaMetadataRetriever()
    return try {
        retriever.setDataSource(context, uri)
        val title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
        val albumArt = retriever.embeddedPicture

        val bitmap = if (albumArt != null) {
            BitmapFactory.decodeByteArray(albumArt, 0, albumArt.size)
        } else {
            BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher_foreground)
        }
        retriever.release()
        Music(title, bitmap, uri)
    } catch (e: Exception) {
        retriever.release()
        e.printStackTrace()
        null
    }
}



@Composable
fun AudioItem(music: Music, viewModel: TopChartViewModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        music.imageLong?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Обложка",
                modifier = Modifier
                    .size(84.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
        } ?: Icon(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Заглушка",
            modifier = Modifier
                .size(84.dp)
                .clip(RoundedCornerShape(4.dp)),
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f) //Поэтому button play находится в углу справа
        ) {
            Text(
                text = music.title!!,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis

            )
            Text(
                text =  music.title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline

            )
        }

        IconButton(onClick = {
            Log.d("MY_TAG26", "${music.uri}")
            viewModel.playForContent(music)
        }) { Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "") }
    }
}
