package id.antasari.p8_multimedia_230104040212.ui

import android.net.Uri
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import id.antasari.p8_multimedia_230104040212.ui.gallery.CameraGalleryScreen
import id.antasari.p8_multimedia_230104040212.ui.home.HomeScreen
import id.antasari.p8_multimedia_230104040212.ui.player.AudioPlayerScreen
import id.antasari.p8_multimedia_230104040212.ui.recorder.AudioRecorderScreen
import id.antasari.p8_multimedia_230104040212.ui.video.VideoPlayerScreen

@Composable
fun AppNavHost(startDestination: String = Screen.Home.route) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {

        // 1. HOME
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        // 2. AUDIO RECORDER
        composable(Screen.AudioRecorder.route) {
            AudioRecorderScreen(
                onBack = { navController.popBackStack() },
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        // 3. AUDIO PLAYER
        composable(
            route = Screen.AudioPlayer.route,
            arguments = listOf(
                navArgument("audioPath") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val audioPath = backStackEntry.arguments?.getString("audioPath") ?: ""
            val decodedPath = Uri.decode(audioPath)

            AudioPlayerScreen(
                onBack = { navController.popBackStack() },
                audioPath = decodedPath
            )
        }

        // 4. VIDEO PLAYER (SUDAH DIHUBUNGKAN)
        composable(
            route = Screen.VideoPlayer.route,
            arguments = listOf(
                navArgument("videoPath") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val videoPath = backStackEntry.arguments?.getString("videoPath") ?: ""
            val decodedPath = Uri.decode(videoPath)

            VideoPlayerScreen(
                onBack = { navController.popBackStack() },
                videoPath = decodedPath
            )
        }

        // 5. CAMERA & GALLERY (FINAL)
        composable(Screen.CameraGallery.route) {
            CameraGalleryScreen(
                onBack = { navController.popBackStack() },
                onNavigate = { route -> navController.navigate(route) }
            )
        }
    }
}