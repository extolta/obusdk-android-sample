package com.example.obusdk.sampleapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.obusdk.sampleapp.ui.theme.ObusdkandroidsampleTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: MainViewModel by viewModels()
        setContent {
            ObusdkandroidsampleTheme {
                val state = viewModel.state
                MainScreen(
                    state = state,
                    onAction = viewModel::onAction,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
        viewModel.onAction(ApiAction.InitialiseSDK(this))
    }

}

@Preview
@Composable
fun MainScreenPreview() {
    ObusdkandroidsampleTheme {
        MainScreen(
            state = "Initial State",
            onAction = {}
        )
    }
}
