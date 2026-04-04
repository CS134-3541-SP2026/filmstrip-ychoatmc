package edu.cs134.scrapbook

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import edu.cs134.scrapbook.ui.theme.ScrapbookTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.map


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ScrapbookTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val scrapbookVM: ScrapbookViewModel = viewModel()
                    //scrapbookVM.refreshPhoto(totalPhotos, LocalContext.current)
                    ScrapbookScreen(
                        scrapbookVM,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}