package dev.shushant.knit_together.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.shushant.knit_together.utils.GradientIcon
import dev.shushant.utils.navigation.AppState
import dev.shushant.utils.navigation.Screens
import dev.shushant.utils.navigation.TopLevelDestination
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun OnBoardingScreen(navigator: AppState, loggedIn: Boolean) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center).wrapContentHeight().verticalScroll(
                rememberScrollState()
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            Image(
                painter = painterResource("onboarding_illustration.png"), contentDescription = null,
                modifier = Modifier.heightIn(max = 300.dp),
                contentScale = ContentScale.Inside
            )
            Text(
                "Share your yarn \uD83D\uDC96", modifier = Modifier,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )
            Text(
                "It's an app that connects the world's knitters and yarn community, allowing users to share ideas, find inspiration, participate in discussions, and connect with like-minded individuals.",
                modifier = Modifier.padding(horizontal = 20.dp),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

        }

        GradientIcon(
            icon = Icons.Filled.ArrowForward,
            modifier = Modifier.align(
                Alignment.TopEnd
            ).padding(20.dp).background(color = Color.White, CircleShape).size(36.dp), onClick = {
                navigator.navigate(
                    if (loggedIn) {
                        TopLevelDestination.HOME.name
                    } else
                        Screens.AuthenticateScreen.route
                )
            }
        )

        Text(
            "\"Unravel your creativity with our knitting community.\"",
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)
                .align(Alignment.BottomCenter),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
    }
}