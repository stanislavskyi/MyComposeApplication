package com.hfad.mycomposeapplication.ui.screens.caster

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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

private fun generatePalette(bitmap: Bitmap): Palette {
    return Palette.from(bitmap).generate()
}

@Composable
fun CasterScreen(
    modifier: Modifier = Modifier.clickable(
        onClick = {},
        interactionSource = null,
        indication = null
    ),
    viewModel: TopChartViewModel,
    backLambda: () -> Unit
) {
    val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()

    // Анимированное состояние для изменения цвета
    var dominantColor by remember { mutableStateOf(Color.Gray) }
    var darkMutedColor by remember { mutableStateOf(Color.Gray) }

    val animatedDominantColor by animateColorAsState(
        targetValue = dominantColor,
        animationSpec = tween(durationMillis = 200)
    )

    val animatedDarkMutedColor by animateColorAsState(
        targetValue = darkMutedColor,
        animationSpec = tween(durationMillis = 200)
    )

    // Запуск асинхронного извлечения цвета
    LaunchedEffect(isPlaying.obj.md5_image) {
        when(isPlaying.isContent){
            true -> {
                isPlaying.audio?.let {
                    val palette = generatePalette(it)
                    val dominant = palette.getDominantColor(Color.Gray.toArgb())
                    val darkMuted = palette.getDarkMutedColor(dominant)

                    // Обновляем цвета с плавной анимацией
                    dominantColor = Color(dominant)
                    darkMutedColor = Color(darkMuted)
                }
            }
            false -> {
                val imageUrl = "https://e-cdns-images.dzcdn.net/images/cover/${isPlaying.obj.md5_image}/1000x1000.jpg"
                val bitmap = loadImageBitmap(imageUrl) // Функция загрузки Bitmap
                bitmap?.let {
                    val palette = generatePalette(it)
                    val dominant = palette.getDominantColor(Color.Gray.toArgb())
                    val darkMuted = palette.getDarkMutedColor(dominant)

                    // Обновляем цвета с плавной анимацией
                    dominantColor = Color(dominant)
                    darkMutedColor = Color(darkMuted)
                }
            }
        }
    }



    Column(
        modifier = modifier
            //.background(MaterialTheme.colorScheme.primary)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(animatedDominantColor, animatedDarkMutedColor)
                )
            )
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        //verticalArrangement = Arrangement.Center
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { backLambda() }) {
                Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "Back")
            }
            IconButton(onClick = {}) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Back")
            }
        }


        if (isPlaying.isContent){
            isPlaying.audio?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Обложка",
                    modifier = Modifier
                        .size(350.dp)
                        .padding(top = 32.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } ?: Icon(
                painter = painterResource(id = R.drawable.ic_launcher_foreground), // Ваш ресурс заглушки
                contentDescription = "Заглушка",
                modifier = Modifier
                    .size(350.dp)
                    .padding(top = 32.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }else{
            AsyncImage(
                model = "https://e-cdns-images.dzcdn.net/images/cover/${isPlaying.obj.md5_image}/1000x1000.jpg",
                placeholder = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier
                    .size(350.dp)
                    .padding(top = 32.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }


        Spacer(modifier = Modifier.height(32.dp))


        Row(modifier = Modifier.padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = isPlaying.obj.title,
                    style = MaterialTheme.typography.bodyLarge,
                    //textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = isPlaying.obj.artist,
                    style = MaterialTheme.typography.bodyMedium,
                    //textAlign = TextAlign.Center
                )
            }
            IconButton(onClick = { }) {
                Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
            }
        }

        Slider(
            value = if (isPlaying.duration > 0) isPlaying.currentPosition.toFloat() / isPlaying.duration.toFloat() else 0f,
            onValueChange = {

            },
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp).padding(horizontal = 8.dp),
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.onPrimary,
                activeTrackColor = MaterialTheme.colorScheme.onPrimary
            )
        )

        Row(modifier = Modifier.padding(top = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            IconButton(onClick = { }) {
                Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "Pause",
                    modifier = Modifier.size(64.dp))
            }
            if (isPlaying.isPlaying) {
                IconButton(onClick = { viewModel.pause() }) {
                    Icon(imageVector = Icons.Default.Close , contentDescription = "Pause",
                        modifier = Modifier.size(64.dp))
                }
            } else {
                IconButton(onClick = {viewModel.playCaster() }){//viewModel.play(post.preview, post.cover_medium) }) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play",
                        modifier = Modifier.size(64.dp))
                }
            }
            IconButton(onClick = { }) {
                Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "Pause",
                    modifier = Modifier.size(64.dp))
            }
        }
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