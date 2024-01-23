@file:OptIn(ExperimentalMaterial3Api::class)

package pollub.cs.xXvladosXx.masterand

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage

@Composable
fun OutlinedTextFieldWithError(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    hasError: Boolean,
    errorMessage: String
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        isError = value.isNotEmpty() && hasError,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        supportingText = {
            if(value.isNotEmpty() && hasError) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    )
}

@Composable
fun ProfileImageWithPicker(profileImageUri: Uri?, selectImageOnClick: () -> Unit) {
    Box {
        if(profileImageUri != null) {
            AsyncImage(
                model = profileImageUri,
                contentDescription = "Profile image",
                modifier = Modifier
                    .size(128.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(
                    id = R.drawable.baseline_question_mark
                ),
                contentDescription = "Profile photo",
                modifier = Modifier
                    .size(128.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        IconButton(
            onClick = selectImageOnClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_image_change),
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.Center),
                contentScale = ContentScale.Crop
            )
        }

    }
}

fun validateName(name: String): Boolean {
    return !name.isEmpty()
}

fun validateEmail(email: String): Boolean {
    val emailPattern = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$")
    return emailPattern.matches(email)
}

fun validateColorNumber(colorNumber: String): Boolean {
    return colorNumber.toIntOrNull()?.let { it in 5..10 } ?: false
}

@Composable
fun LoginScreenInitial(
    navController: NavController
) {
    val name = rememberSaveable { mutableStateOf("") }
    val email = rememberSaveable { mutableStateOf("")}
    val colorNumber = rememberSaveable { mutableStateOf("")}
    val profileImageUri = rememberSaveable{
        mutableStateOf<Uri?>(null)
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { selectedUri ->
            if (selectedUri != null) {
                profileImageUri.value = selectedUri
            }
        })

    val textVisibility = rememberSaveable { mutableStateOf(true) }
    val infiniteTransition = rememberInfiniteTransition()

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val animatedTextModifier = Modifier
        .padding(bottom = 48.dp)
        .graphicsLayer(
            scaleX = scale,
            scaleY = scale,
            transformOrigin = TransformOrigin.Center
        )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(
            visible = textVisibility.value,
        ) {
            Text(
                text = "MasterAnd",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                ),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = animatedTextModifier
            )
        }
        ProfileImageWithPicker(
            profileImageUri = profileImageUri.value,
            selectImageOnClick = {
                imagePicker.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            })

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextFieldWithError(
            value = name.value,
            onValueChange = { name.value = it },
            label = "Enter Name",
            hasError = !validateName(name.value),
            errorMessage = "Name cannot be empty"
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextFieldWithError(
            value = email.value,
            onValueChange = { email.value = it },
            label = "Enter email",
            hasError = !validateEmail(email.value),
            errorMessage = "Wrong email format"
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextFieldWithError(
            value = colorNumber.value,
            onValueChange = { colorNumber.value = it },
            label = "Enter number of colors",
            hasError = !validateColorNumber(colorNumber.value),
            errorMessage = "Choose between 5 and 10"
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if(validateName(name.value) &&
                    validateEmail(email.value) &&
                    validateColorNumber(colorNumber.value)) {
                    navController.navigate(route = "game_screen?colors=${colorNumber.value}")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Next")
        }
    }
}


