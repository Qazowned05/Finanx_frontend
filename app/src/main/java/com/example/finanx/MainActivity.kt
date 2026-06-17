package com.example.finanx

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finanx.ui.MainScreen
import com.example.finanx.ui.screens.HomeScreen
import com.example.finanx.ui.screens.budget.BudgetListScreen
import com.example.finanx.ui.screens.budget.BudgetScreen
import com.example.finanx.ui.screens.category.CategoryListScreen
import com.example.finanx.ui.theme.FinanxTheme
import com.example.finanx.ui.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinanxTheme {
                MainScreen()


            }
        }
    }
}
