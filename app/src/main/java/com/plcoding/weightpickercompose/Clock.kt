package com.plcoding.weightpickercompose

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun Clock(
    modifier: Modifier = Modifier
        .clip(RoundedCornerShape(16.dp))
        .background(Color.Green)
        .size(200.dp),
    clockStyle: ClockStyle = ClockStyle(),
) {

    var hourAngle by remember {
        mutableStateOf(
            (90 - 360 / 12 * Calendar.getInstance().get(Calendar.HOUR).toFloat()).toRad())
    }
    var minuteAngle by remember {
        mutableStateOf(
            (90 - 360 / 60 * Calendar.getInstance().get(Calendar.MINUTE).toFloat()).toRad()
        )
    }
    var secondAngle by remember {
        mutableStateOf(
            (90 - 360 / 60 * Calendar.getInstance().get(Calendar.SECOND)).toFloat().toRad()
        )
    }


    val oneHourAngle = (360 / 12).toFloat().toRad()

    BoxWithConstraints {
        var boxCenter by remember {
            mutableStateOf(Offset(-100f, -100f))
        }

        val secondAnimated = remember {
            Animatable(0f)
        }

        var currentSecondIndicatorPosition by remember {
            mutableStateOf(Offset(
                (boxCenter.x + clockStyle.secondIndicator * cos(secondAngle)),
                (boxCenter.y - clockStyle.secondIndicator * sin(secondAngle))
            ))
        }


        var currentMinuteIndicatorPosition by remember {
            mutableStateOf(Offset(
                (boxCenter.x + clockStyle.minuteIndicator * cos(minuteAngle)),
                (boxCenter.y - clockStyle.minuteIndicator * sin(minuteAngle))
            ))
        }


        var currentHourIndicatorPosition by remember {
            mutableStateOf(Offset(
                (boxCenter.x + clockStyle.hourIndicator * cos(hourAngle)),
                (boxCenter.y - clockStyle.hourIndicator * sin(hourAngle))
            ))
        }

        rememberCoroutineScope().launch() {
            while (true) {
                delay(100)

                hourAngle =
                    (90 - 360 / 12 * Calendar.getInstance().get(Calendar.HOUR).toFloat()).toRad()

                minuteAngle =
                    (90 - 360 / 60 * Calendar.getInstance().get(Calendar.MINUTE).toFloat()).toRad()
                secondAngle =
                    (90 - 360 / 60 * Calendar.getInstance().get(Calendar.SECOND)).toFloat().toRad()

                secondAnimated.animateTo(
                    (90 - 360 / 60 * Calendar.getInstance()
                        .get(Calendar.SECOND)).toFloat().toRad(),
                )

                currentSecondIndicatorPosition = Offset(
                    (boxCenter.x + clockStyle.secondIndicator * cos(secondAnimated.value)),
                    (boxCenter.y - clockStyle.secondIndicator * sin(secondAnimated.value))
                )
                currentMinuteIndicatorPosition = Offset(
                    (boxCenter.x + clockStyle.minuteIndicator * cos(minuteAngle)),
                    (boxCenter.y - clockStyle.minuteIndicator * sin(minuteAngle))
                )

                val hourOffset = minuteAngle / (2 * PI) * oneHourAngle

                currentHourIndicatorPosition = Offset(
                    ((boxCenter.x + clockStyle.hourIndicator * cos(hourAngle + hourOffset)).toFloat()),
                    ((boxCenter.y - clockStyle.hourIndicator * sin(hourAngle + hourOffset)).toFloat())
                )

            }
        }

        Canvas(modifier = modifier) {
            val clockRadius = size.width / 2 - 30
            boxCenter = center

            val timeLength = 60



            for (i in 0..timeLength) {

                val lineType = if (i % 5 == 0) ClockLineType.Minute else ClockLineType.Second

                val lineLength =
                    if (lineType is ClockLineType.Minute) clockStyle.minuteIndicatorLength else clockStyle.secondIndicatorLength

                val lineAngle = (90 + (-2 * 180).toFloat() / 60 * i).toRad()

                drawLine(
                    cap = StrokeCap.Square,
                    color = Color.Red,
                    strokeWidth = 5f,
                    start = Offset(
                        x = center.x + ((clockRadius - lineLength) * cos(lineAngle)),
                        y = center.y - ((clockRadius - lineLength) * sin(lineAngle))),
                    end = Offset(x = center.x + (clockRadius * cos(lineAngle)),
                        y = center.y - (clockRadius * sin(lineAngle))),
                )



                if (lineType == ClockLineType.Minute) {
                    var hourValue = 3 - lineAngle.toDeg() / 30
                    var hourInt = hourValue.roundToInt()

                    val region: CircleRegion = when (hourInt) {
                        0 -> CircleRegion.Top
                        3 -> CircleRegion.Right
                        6 -> CircleRegion.Bottom
                        9 -> CircleRegion.Left
                        in 1..2 -> CircleRegion.TopRight
                        in 4..5 -> CircleRegion.BottomRight
                        in 7..8 -> CircleRegion.BottomLeft
                        in 10..11 -> CircleRegion.TopLeft
                        else -> CircleRegion.Top
                    }


                    drawContext.canvas.nativeCanvas.apply {
                        val offsetValue = 50
                        val horizontalOffset = when (region) {
                            is CircleRegion.Top -> 0
                            is CircleRegion.Bottom -> 100
                            is CircleRegion.Right -> -offsetValue * 0.5
                            is CircleRegion.TopRight -> -offsetValue
                            is CircleRegion.BottomRight -> -offsetValue
                            is CircleRegion.Left -> 0
                            is CircleRegion.TopLeft -> 0
                            is CircleRegion.BottomLeft -> 0
                        }.toFloat()

                        val verticalOffset = when (region) {
                            is CircleRegion.Top -> offsetValue
                            is CircleRegion.Bottom -> offsetValue * 0.5
                            is CircleRegion.Right -> -offsetValue
                            is CircleRegion.TopRight -> offsetValue
                            is CircleRegion.BottomRight -> 0
                            is CircleRegion.Left -> offsetValue * -2
                            is CircleRegion.TopLeft -> offsetValue
                            is CircleRegion.BottomLeft -> 0
                        }.toFloat()

                        if (hourInt == 0) hourInt = 12

                        drawText(
                            hourInt.toString(),
                            center.x + ((clockRadius - lineLength + horizontalOffset) * cos(
                                lineAngle)),
                            center.y - ((clockRadius - lineLength - verticalOffset) * sin(lineAngle)),
                            android.graphics.Paint().apply {
                                textSize = clockStyle.numberSize.value
                                color = android.graphics.Color.BLUE
                            },
                        )
                    }
                }

            }

            /* drawCircle(color = Color.Red,
                 style = Stroke(width = 5f),
                 radius = clockRadius - clockStyle.minuteIndicatorLength - clockStyle.numberSize.value)
 */


            drawLine(
                color = Color.White,
                start = center,
                end = currentSecondIndicatorPosition,
                strokeWidth = 5f)

            drawLine(
                color = Color.White,
                start = center,
                end = currentMinuteIndicatorPosition,
                strokeWidth = 5f)

            drawLine(
                color = Color.White,
                start = center,
                end = currentHourIndicatorPosition,
                strokeWidth = 5f)

            drawCircle(color = Color.White,
                style = Stroke(width = 10f),
                center = center,
                radius = clockRadius
            )
        }

    }
}

fun Float.toRad() = (this * PI / 180).toFloat()

fun Float.toDeg() = (this * 180 / PI)


sealed class CircleRegion {
    object TopLeft : CircleRegion()
    object BottomLeft : CircleRegion()
    object TopRight : CircleRegion()
    object BottomRight : CircleRegion()
    object Bottom : CircleRegion()
    object Right : CircleRegion()
    object Top : CircleRegion()
    object Left : CircleRegion()
}

data class ClockStyle(
    val numberSize: TextUnit = 50.sp,
    val timeSpeed: Int = 2,
    val minuteIndicatorLength: Float = 40f,
    val secondIndicatorLength: Float = 20f,
    val secondNeedleLength: Float = 35f,
    val secondIndicator: Float = 220f,
    val minuteIndicator: Float = 180f,
    val hourIndicator: Float = 110f,
)

sealed class ClockLineType {
    object Minute : ClockLineType()
    object Second : ClockLineType()
}

@Composable
@Preview
fun ClockPreview() {
    Surface {
        Clock()
    }
}