package com.gomoku.common

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.ViewModel

class MainActivity : AppCompatActivity() {
    val v: V by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                App(state = v.state)
            }
        }
    }

    class V : ViewModel() {
        val state = BoardState(15, 15)
    }
}
