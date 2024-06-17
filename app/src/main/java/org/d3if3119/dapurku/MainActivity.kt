package org.d3if3119.dapurku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.d3if3119.dapurku.ui.screen.HomeScreen
import org.d3if3119.dapurku.ui.theme.DapurKuTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DapurKuTheme {
                HomeScreen()
            }
        }
    }
}
