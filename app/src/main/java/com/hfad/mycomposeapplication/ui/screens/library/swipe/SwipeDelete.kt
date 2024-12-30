package com.hfad.mycomposeapplication.ui.screens.library.swipe

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt




@Composable
fun SwipeDelete() {
    val items = remember { mutableStateListOf("Элемент 1", "Элемент 2", "Элемент 3", "Элемент 4") }
    ContactScreen()
//    LazyColumn(
//        modifier = Modifier.fillMaxSize(),
//        contentPadding = PaddingValues(16.dp)
//    ) {
//        items(items, key = { it }) { item ->
//            DraggableListItem(
//                text = item,
//                onDismissed = { items.remove(item) }
//            )
//        }
//    }
}

@Composable
fun DraggableListItem(text: String, onDismissed: () -> Unit) {
    var offsetX by remember { mutableStateOf(0f) }
    var isDismissed by remember { mutableStateOf(false) }

    // Отслеживаем свайп по горизонтали
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White)
            .clip(RoundedCornerShape(8.dp))
            .offset { IntOffset(offsetX.roundToInt(), 0) }
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    offsetX += dragAmount
                    if (offsetX > 200f) { // Если свайп превышает 200f, элемент удаляется
                        isDismissed = true
                        onDismissed() // Удаление элемента
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        // Красный фон, который будет двигаться за элементом
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(Color.Red)
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .clip(RoundedCornerShape(8.dp))
        )

        // Текст внутри элемента
        Text(
            text = text,
            color = Color.Black,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(20.dp)
        )

        // Когда элемент удаляется, можно скрыть его
        if (isDismissed) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Удалено", color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

/*
@Composable
fun SwipeDelete(){
    // Список элементов
    val items = remember { mutableStateListOf("Элемент 1", "Элемент 2", "Элемент 3", "Элемент 4") }

    LazyColumn(
        //modifier = Modifier.fillMaxSize(),
//        contentPadding = PaddingValues(16.dp),
//        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items.size) { index ->
            DraggableListItem(
                text = items[index],
                onDismissed = { items.removeAt(index) }
            )
        }
    }
}


@Composable
fun DraggableListItem(
    text: String,
    onDismissed: () -> Unit
){
    var offsetX by remember { mutableStateOf(0f) }
    var isDismissed by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .height(80.dp)
            .padding(8.dp)
            .background(Color.Red)
//            .clip(RoundedCornerShape(8.dp))
            .offset { IntOffset(offsetX.roundToInt(), 0) }
//            .draggable(
//                orientation = Orientation.Horizontal,
//                state = rememberDraggableState { delta ->
//                    offsetX += delta
//                }
//            )
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    offsetX += dragAmount
                    if (offsetX > 200f) {
                        // Если свайп больше 200, скрываем элемент
//                        isDismissed = true
                        onDismissed() // Удаляем элемент
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(Color.White)
                .offset { IntOffset(offsetX.roundToInt(), 0) }
//                .clip(RoundedCornerShape(8.dp))
        )
        Text(
            text = text, color = Color.Black,
            style = MaterialTheme.typography.bodyMedium
        )
        // Когда элемент удаляется, можно скрыть его
//        if (isDismissed) {
//            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                Text(text = "Удалено", color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
//            }
//        }
        //var offsetX by remember { mutableStateOf(0f) }
//        Text(
//            text = text, color = Color.Black,
//            style = MaterialTheme.typography.bodyMedium,
//            modifier = Modifier.padding(20.dp)
//                .offset { IntOffset(offsetX.roundToInt(), 0) }
//                .draggable(
//                orientation = Orientation.Horizontal,
//                state = rememberDraggableState { delta ->
//                    offsetX += delta
//                }
//            )
//        )
    }
}

 */