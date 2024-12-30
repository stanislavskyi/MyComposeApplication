package com.hfad.mycomposeapplication.ui.screens.topchart

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.hfad.mycomposeapplication.R
import com.hfad.mycomposeapplication.domain.entity.Track

@Composable
fun ChartScreen(
    modifier: Modifier = Modifier,
    viewModel: TopChartViewModel
) {

    val lazyPagingItems = viewModel.tracks.collectAsLazyPagingItems()

    Box(modifier = Modifier.fillMaxWidth()) {
        LazyColumn {
            when (lazyPagingItems.loadState.refresh) {
                is LoadState.Loading -> {
                    items(6) { LoadingRow() }
                }

                is LoadState.Error -> {
                    item { ErrorItem(lazyPagingItems::refresh) }
                }
                else -> {
                    items(lazyPagingItems.itemCount) { index ->
                        //lazyPagingItems[index]?.let { TopChartItem(it, viewModel) }
                        val post = lazyPagingItems[index]
                        if (post != null) {
                            TopChartItem(post, viewModel)
                        }
                    }
                }
            }

            //

            lazyPagingItems.apply {
                when {
                    loadState.append is LoadState.Loading -> {
                        item {
                            LoadingRow()
                        }
                    }

                    loadState.append is LoadState.Error -> {
                        item {
                            Text(
                                text = "Нет данных для отображения.",
                                modifier = Modifier.fillMaxSize(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun ErrorItem(onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Ошибка загрузки. Проверьте интернет-соединение.",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text("Повторить")
        }
    }
}

@Composable
fun TrackImage(md5Image: String) {
    AsyncImage(
        model = "https://e-cdns-images.dzcdn.net/images/cover/$md5Image/250x250.jpg",
        placeholder = painterResource(R.drawable.ic_launcher_foreground),
        contentDescription = null,
        modifier = Modifier
            .size(84.dp)
            .clip(RoundedCornerShape(4.dp)),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun TopChartItem(
    post: Track,
    viewModel: TopChartViewModel
) {

    val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            //.padding(horizontal = 12.dp, vertical = 4.dp)
            .padding(16.dp)
        //.clip(RoundedCornerShape(8.dp)
    ) {
        TrackImage(post.md5_image)
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            //modifier = Modifier.padding(horizontal = 16.dp)
            modifier = Modifier.weight(1f) //Поэтому button play находится в углу справа
        ) {
            Text(
                text = post.title,  //stringResource(R.string.app_name)
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
                //fontWeight = FontWeight.Bold
            )
            Text(
                text = post.artist,//stringResource(R.string.app_name),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline
                //style = MaterialTheme.typography.bodyMedium
            )
        }

        if (isPlaying.isPlaying && isPlaying.preview == post.preview) {
            IconButton(onClick = { viewModel.pause() }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Pause")
            }
        } else {
            IconButton(onClick = { viewModel.play(post) }) {//viewModel.play(post.preview, post.cover_medium) }) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play")
            }
        }
    }
}

@Composable
private fun LoadingRow() {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1000
                0.7f at 500
                0.9f at 800
            },
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Row(
        modifier = Modifier
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(84.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.LightGray.copy(alpha = alpha))
        )
        Spacer(modifier = Modifier.width(16.dp))


        Column(
            //modifier = Modifier.padding(horizontal = 16.dp)
            modifier = Modifier.weight(1f) //Поэтому button play находится в углу справа
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(20.dp)
                    .background(Color.LightGray.copy(alpha = alpha))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(16.dp)
                    .background(Color.LightGray.copy(alpha = alpha))
            )
        }
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.LightGray.copy(alpha = alpha))
        )
    }
}







