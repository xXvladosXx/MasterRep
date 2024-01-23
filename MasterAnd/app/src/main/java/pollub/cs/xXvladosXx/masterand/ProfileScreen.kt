package pollub.cs.xXvladosXx.masterand

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage

@Composable
fun ProfileImage(profileImageUri: Uri?,) {
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
}
@Composable
fun ProfileScreenInitial(
    navController: NavController
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            navController.navigate(route = Screen.Game.route)
        }) { Text(text = "Return to game") }
    }

}



