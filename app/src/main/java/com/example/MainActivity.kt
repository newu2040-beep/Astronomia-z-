package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.AppDatabase
import com.example.data.AstronomyRepository
import com.example.ui.AstronomyApp
import com.example.ui.AstronomyViewModel
import com.example.ui.AstronomyViewModelFactory
import com.example.ui.theme.AstronomyZTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val database = AppDatabase.getDatabase(this)
        val repository = AstronomyRepository(database.articleDao())
        
        setContent {
            AstronomyZTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: AstronomyViewModel = viewModel(
                        factory = AstronomyViewModelFactory(repository)
                    )
                    AstronomyApp(viewModel = viewModel)
                }
            }
        }
    }
}
