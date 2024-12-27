package com.hfad.mycomposeapplication.ui.common.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import com.hfad.mycomposeapplication.R
import com.hfad.mycomposeapplication.ui.screens.topchart.TopChartViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun CasterBar(
    modifier: Modifier = Modifier,
    viewModel: TopChartViewModel,
    lambdaToCaster: () -> Unit
){

    val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()

    // Анимированное состояние для изменения цвета
    var targetColor by remember { mutableStateOf(Color.Gray) }
    // Хранение цвета в состоянии
    //var backgroundColor by remember { mutableStateOf(Color.Gray) }
    val backgroundColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(durationMillis = 1000) // Плавный переход цвета
    )


    // Запуск асинхронного извлечения цвета
    LaunchedEffect(isPlaying.obj.md5_image) {
        when(isPlaying.isContent){
            true -> {
                isPlaying.audio?.let {
                    val palette = Palette.from(it).generate()
                    //backgroundColor = palette.getDominantColor(Color.Gray.toArgb()).toComposeColor()
                    val dominant = palette.getDominantColor(Color.Gray.toArgb())
                    val darkMuted = palette.getDarkMutedColor(Color.Gray.toArgb())
                    targetColor = Color(if (darkMuted != Color.Gray.toArgb()) darkMuted else dominant)
                }
            }
            false -> {
                val imageUrl = "https://e-cdns-images.dzcdn.net/images/cover/${isPlaying.obj.md5_image}/1000x1000.jpg"
                val bitmap = loadImageBitmap(imageUrl) // Функция загрузки Bitmap
                bitmap?.let {
                    val palette = Palette.from(it).generate()
                    //backgroundColor = palette.getDominantColor(Color.Gray.toArgb()).toComposeColor()
                    val dominant = palette.getDominantColor(Color.Gray.toArgb())
                    val darkMuted = palette.getDarkMutedColor(Color.Gray.toArgb())
                    targetColor = Color(if (darkMuted != Color.Gray.toArgb()) darkMuted else dominant)
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
            .clickable {
                lambdaToCaster()
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.fillMaxWidth()
            //.clip(MaterialTheme.shapes.small)
        ) {

            if(isPlaying.isContent){
                isPlaying.audio?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Обложка",
                        modifier = Modifier
                            .padding(10.dp)
                            .size(38.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        contentScale = ContentScale.Crop
                    )
                } ?: Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground), // Ваш ресурс заглушки
                    contentDescription = "Заглушка",
                    modifier = Modifier
                        .padding(10.dp)
                        .size(38.dp)
                        .clip(RoundedCornerShape(4.dp))
                )
            }else{
                AsyncImage(
                    model = "https://e-cdns-images.dzcdn.net/images/cover/${isPlaying.obj.md5_image}/250x250.jpg",
                    placeholder = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(38.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Column(
                //modifier = Modifier.padding(horizontal = 16.dp)
                modifier = Modifier.weight(1f) //Поэтому button play находится в углу справа
            ) {
                Text(
                    text = isPlaying.obj.title,  //stringResource(R.string.app_name)
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                    //fontWeight = FontWeight.Bold
                )
                Text(
                    text = isPlaying.obj.artist,//stringResource(R.string.app_name),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline
                    //style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButton(onClick = { }) {
                Icon(imageVector = Icons.Default.FavoriteBorder , contentDescription = "Pause")
            }
            IconButton(onClick = { }) {
                Icon(imageVector = Icons.Default.MailOutline , contentDescription = "Pause")
            }

            if (isPlaying.isPlaying) {
                IconButton(onClick = { viewModel.pause() }) {
                    Icon(imageVector = Icons.Default.Close , contentDescription = "Pause")
                }
            } else {
                IconButton(onClick = {viewModel.playCaster() }){//viewModel.play(post.preview, post.cover_medium) }) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play")
                }
            }
        }
        LinearProgressIndicator(
            progress = {if (isPlaying.duration > 0) {
                isPlaying.currentPosition / isPlaying.duration.toFloat()
            } else {
                0f
            }},
            modifier = modifier
                .fillMaxWidth()
                .height(2.dp)
                .padding(horizontal = 10.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

// Функция загрузки Bitmap
private suspend fun loadImageBitmap(url: String): Bitmap? = withContext(Dispatchers.IO) {
    try {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()
        val inputStream = connection.inputStream
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        Log.e("CasterBar", "Error loading image: $e")
        null
    }
}