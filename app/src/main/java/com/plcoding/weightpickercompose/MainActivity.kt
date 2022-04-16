package com.plcoding.weightpickercompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    @ExperimentalMaterialApi
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            /*MessageBox()*/

            LazyColumn(content = {
                this.items(200, itemContent = {
                    val text1 =
                        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum"

                    val endIndex = Random.nextInt(text1.length)
                    MessageBox(text = text1.substring(0, endIndex), isSender = endIndex%2==0)
                    Spacer(modifier = Modifier.height(20.dp))
                })
            })
/*
            TicTacToe2()

            XSign()


                CircleSign(Modifier,100f)



             BezierCurves()

                var a by remember {
                    mutableStateOf(1)
                }

                var b by remember {
                    mutableStateOf(0)
                }


                LaunchedEffect(key1 = a)
                {
                   b = a
                }

                Button(onClick = { a++ }) {
                    Text(text = a.toString())
                    Text(text = b.toString())
                }



                AnimatedTriAngle()
                AnimContent()


                AnimatedPathCompose(Modifier
                    .fillMaxSize()
                    .background(Color.Blue))

                Clock()

                Scale(
                    style = ScaleStyle(
                        scaleWidth = 150.dp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    weight = it
                }
                */

        }
    }
}

