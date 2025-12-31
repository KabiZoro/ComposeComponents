package com.kabi.composecomponents.android_basics.intent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import com.kabi.composecomponents.ui.theme.ComposeComponentsTheme

class SecondActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeComponentsTheme {
                Text(text = "SecondActivity")
            }
        }
    }
}