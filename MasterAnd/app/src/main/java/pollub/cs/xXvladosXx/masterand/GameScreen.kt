package pollub.cs.xXvladosXx.masterand

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import okhttp3.internal.toImmutableList

@Composable
fun CircularButton(onClick: () -> Unit, color: Color) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .size(50.dp)
    ) { Text("") }
}

@Composable
fun SelectableColorsRow(colors: List<Color>, onClick: (Int) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        colors.forEachIndexed { index, color ->
            CircularButton(
                onClick = { onClick(index) },
                color = color
            )
        }
    }
}

@Composable
fun SmallCircle(color: Color) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(color = color)
            .border(2.dp, MaterialTheme.colorScheme.onBackground, CircleShape)
            .size(22.dp)
    )
}

@Composable
fun FeedbackCircles(colors: List<Color>) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(end = 5.dp)
            .height(50.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(end = 5.dp)
                .width(50.dp)
        ) {
            colors.take(2).forEach { color ->
                SmallCircle(color = color)
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(end = 5.dp)
                .width(50.dp)
        ) {
            colors.drop(2).take(2).forEach { color ->
                SmallCircle(color = color)
            }
        }
    }
}

@Composable
fun GameRow(
    selectedColors: List<Color>,
    feedbackColors: List<Color>,
    clickable: Boolean,
    onSelectColorClick: (Int) -> Unit,
    onCheckClick: () -> Unit
) {

    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        SelectableColorsRow(
            colors = selectedColors,
            onClick = onSelectColorClick
        )

        IconButton(
            onClick = { if (clickable) onCheckClick() },
            enabled = clickable,
            modifier = Modifier
                .clip(CircleShape)
                .background(if (clickable) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background)
                .size(50.dp)
        ) {
            Icon(
                Icons.Filled.Done,
                contentDescription = "Check",
                tint = if (clickable) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground,
            )
        }
        FeedbackCircles(colors = feedbackColors)
    }
}

private val AVAILABLE_COLORS = listOf(
    Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Magenta,
    Color.Cyan, Color.Gray, Color.DarkGray, Color.Black, Color.LightGray
)

fun selectRandomColors(availableColors: List<Color>, numToSelect: Int): List<Color> {
    return availableColors.shuffled().take(numToSelect)
}

fun selectNextAvailableColor(
    availableColors: List<Color>,
    selectedColors: List<Color>,
    buttonNumber: Int
): Color {
    val availableButNotSelected = availableColors.filterNot { selectedColors.contains(it) }
    return availableButNotSelected.shuffled().first()
}

fun checkColors(
    selectedColors: List<Color>,
    correctColors: List<Color>,
    notFoundColor: Color
): List<Color> {
    val feedbackColors = mutableListOf<Color>()
    for (i in selectedColors.indices) {
        if (selectedColors[i] == correctColors[i]) {
            feedbackColors.add(Color.Red)
        } else if (correctColors.contains(selectedColors[i])) {
            feedbackColors.add(Color.Yellow)
        } else {
            feedbackColors.add(notFoundColor)
        }
    }
    return feedbackColors
}

@Composable
fun GameScreenInitial(
    navController: NavController,
    colors: Int = 6
) {
    var usedColors by remember { mutableStateOf(selectRandomColors(AVAILABLE_COLORS, colors)) }
    var correctAnswer by remember { mutableStateOf(selectRandomColors(usedColors, 4)) }
    var selectedColors = remember {
        mutableStateListOf<Color>(
            Color.Transparent,
            Color.Transparent,
            Color.Transparent,
            Color.Transparent
        )
    }
    var feedbackColors = remember {
        mutableStateListOf<Color>(
            Color.Transparent,
            Color.Transparent,
            Color.Transparent,
            Color.Transparent
        )
    }
    var history = remember { mutableStateListOf<Pair<List<Color>, List<Color>>>() }
    var attempts by remember { mutableIntStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "Your score: $attempts",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 48.dp)
            )
        }

        items(history.size) { i ->
            GameRow(
                selectedColors = history[i].first,
                feedbackColors = history[i].second,
                clickable = false,
                onSelectColorClick = { },
                onCheckClick = { }
            )
        }

        item {
            GameRow(
                selectedColors = selectedColors,
                feedbackColors = feedbackColors,
                clickable = !selectedColors.contains(Color.Transparent),
                onSelectColorClick = { index ->
                    val nextColor = selectNextAvailableColor(
                        usedColors,
                        selectedColors,
                        index
                    )
                    selectedColors[index] = nextColor
                },
                onCheckClick = {
                    if (selectedColors.size == 4 && !isGameOver) {
                        val selectedColorsList: List<Color> = selectedColors.toList()
                        val feedbackColorsList: List<Color> =
                            checkColors(selectedColorsList, correctAnswer, Color.Gray)

                        feedbackColors.clear()
                        feedbackColors.addAll(feedbackColorsList)
                        attempts++

                        if (feedbackColors.all { it == Color.Red }) {
                            isGameOver = true
                        } else {
                            history.add(
                                Pair(
                                    selectedColors.toImmutableList(),
                                    feedbackColors.toImmutableList()
                                )
                            )
                            selectedColors.clear()
                            feedbackColors.clear()
                            selectedColors.addAll(List(4) { Color.Transparent })
                            feedbackColors.addAll(List(4) { Color.Transparent })
                        }
                    }
                }
            )
        }

        item {
            if (isGameOver) {
                Button(onClick = {
                    usedColors = selectRandomColors(AVAILABLE_COLORS, 6)
                    correctAnswer = selectRandomColors(usedColors, 4)
                    selectedColors.clear()
                    feedbackColors.clear()
                    selectedColors.addAll(List(4) { Color.Transparent })
                    feedbackColors.addAll(List(4) { Color.Transparent })
                    history.clear()
                    attempts = 0
                    isGameOver = false
                }) { Text("Play Again") }
            }
        }

        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.padding(top = 48.dp)
            ) {
                Button(onClick = {
                    navController.navigate(route = Screen.Profile.route)
                }) { Text(text = "See your profile") }
                Button(onClick = {
                    navController.navigate(route = Screen.Login.route)
                }) { Text(text = "Logout") }
            }
        }
    }

}


