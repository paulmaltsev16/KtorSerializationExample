package com.example.ktorjsonparser

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.ktorjsonparser.ui.theme.KtorJsonParserTheme
import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KtorJsonParserTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val activity = remember {
                        mutableStateOf<Activity?>(null)
                    }

                    LaunchedEffect(key1 = Unit) {
                        lifecycleScope.launch(Dispatchers.IO) {
                            val activityResult = ktorGateway<Activity>()
                            activity.value = activityResult
                        }
                    }

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                    ) {
                        Greeting("Android ktor Date serialization.")

                        Spacer(modifier = Modifier.height(20.dp))

                        ReceivedActivity(activity.value.toString())
                    }
                }
            }
        }
    }
}

@Composable
private fun Greeting(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        modifier = modifier.fillMaxWidth(),
        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 32.sp),
        textAlign = TextAlign.Center
    )
}

@Composable
private fun ReceivedActivity(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier.fillMaxWidth(),
        style = TextStyle(fontSize = 23.sp),
        textAlign = TextAlign.Center
    )
}

@Preview(showBackground = true)
@Composable
private fun GreetingPreview() {
    KtorJsonParserTheme {
        Greeting("Android")
    }
}

private suspend inline fun <reified T> ktorGateway(): T? {
    val client = HttpClient(CIO)
    var result: T? = null

    try {
        val httpResponse = client.get("http://192.168.27.214:5000/api")
        val jsonString = httpResponse.bodyAsText()
        result = Gson().fromJson(jsonString, T::class.java)
    } catch (e: Exception) {
        // handle exception
        Log.i("tester", "ktorGateway exception: $e}")
    } finally {
        client.close()
    }
    return result
}

private data class Activity(
    val date: Date, val name: String
)