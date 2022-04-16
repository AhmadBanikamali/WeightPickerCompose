package com.plcoding.weightpickercompose

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.lang.Math.PI
import kotlin.math.*

@Composable
fun BasicCanvas(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {

        val path = Path().apply {
            moveTo(500f, 100f)
            lineTo(100f, 100f)
            lineTo(100f, 500f)
            lineTo(500f, 500f)
            quadraticBezierTo(x1 = 1000f, y1 = 300f, x2 = 500f, y2 = 100f)
        }

        val cubicPath = Path().apply {
            moveTo(500f, 600f)
            lineTo(100f, 600f)
            lineTo(100f, 1000f)
            lineTo(500f, 1000f)
            cubicTo(
                x1 = 1000f, y1 = 1000f,
                x2 = 1000f, y2 = 600f,
                x3 = 500f, y3 = 600f,
            )
        }

        Path().apply {
            addRect(
                Rect(
                    center = Offset(300f, 1300f),
                    radius = 200f
                )
            )

            drawCircle(color = Color.Yellow, style = Stroke(10f),
                center = Offset(300f, 1200f),
                radius = 100f)
        }.also {
            drawPath(it, color = Color.Magenta, style = Stroke(10f))
        }


        with(Path().apply {
            addOval(Rect(
                topLeft = Offset(
                    x = 100f,
                    y = 1600f,
                ),
                bottomRight = Offset(
                    x = 600f,
                    1900f
                )
            ))

        }) {
            drawPath(this, color = Color.LightGray)
        }


        drawCircle(color = Color.Yellow, radius = 10f, center = Offset(1000f, 1000f))

        drawPath(path = path, color = Color.White, style = Stroke(width = 20f,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round,
            miter = 0f))

        drawPath(cubicPath, color = Color.Green, style = Stroke(width = 20f,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round,
            miter = 0f))

    }
}

@Composable
fun TransformCompose(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        translate(left = size.width / 2 - 300f, top = 0f) {
            drawRect(color = Color.White, topLeft = Offset(0f, 200f), size = Size(600f, 300f))
            scale(scale = 0.5f, pivot = Offset(400f, 350f)) {
                drawRect(color = Color.Yellow,
                    topLeft = Offset(100f, 200f),
                    size = Size(600f, 300f))
            }
        }
    }
}


@ExperimentalAnimationApi
@Composable
fun AnimatedPathCompose(modifier: Modifier = Modifier) {

    val pathPortion = remember {
        Animatable(initialValue = 0f)
    }



    LaunchedEffect(key1 = true) {
        pathPortion.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 2500
            )
        )
    }



    Canvas(modifier = Modifier.fillMaxSize()) {
        val path = Path().apply {
            moveTo(100f, 100f)
            quadraticBezierTo(300f, 300f, 500f, 100f)
            close()
        }

        val outPath = Path()

        PathMeasure().apply {
            setPath(path, forceClosed = false)
            getSegment(0f, pathPortion.value * length, destination = outPath, true)
        }

        drawPath(
            path = outPath,
            color = Color.Red,
            style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}

@ExperimentalAnimationApi
@Composable
fun AnimVisibility() {
    var isVisible by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(
                // customize with tween AnimationSpec
                animationSpec = tween(
                    durationMillis = 1000,
                    delayMillis = 100,
                    easing = LinearOutSlowInEasing
                )
            ),
            // you can also add animationSpec in fadeOut if need be.
            exit = fadeOut() + shrinkHorizontally(),
        ) {
            Text(text = "Animating Text")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            isVisible = !isVisible
        }) {
            Text(text = "Animate")
        }
    }
}


@ExperimentalAnimationApi
@ExperimentalMaterialApi // For material components such as Card.
@Composable
fun AnimContent() {
    var itemExpanded by remember { mutableStateOf(false) }
    val contentTransition = updateTransition(itemExpanded, label = "Expand")

    Card(
        modifier = Modifier.padding(6.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = 4.dp,
        onClick = { itemExpanded = !itemExpanded }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Hi, Compose!")

            // Add Animated visibility
            contentTransition.AnimatedVisibility(
                visible = { isVisible -> isVisible }
            ) {
                Text(text = "What a beautiful animation!")
            }

            val animateInt by contentTransition.animateInt(label = "") {
                if (it) 2 else 4
            }

            // Add Animated content
            contentTransition.AnimatedContent { targetState ->
                if (targetState) {
                    Text(text = "Expanded")
                } else {
                    Text(text = "Click to expand")
                }
            }
        }
    }
}

@Composable
fun CrossFadeTarget() {
    var myTarget by remember {
        mutableStateOf(MyTarget.First)
    }



    Crossfade(targetState = myTarget, animationSpec = tween(durationMillis = 1000)) {
        when (it) {
            MyTarget.First -> {
                Text(text = "A", modifier = Modifier.clickable {
                    myTarget = when (myTarget) {
                        MyTarget.First -> MyTarget.Second
                        else -> MyTarget.First
                    }
                })
            }
            MyTarget.Second -> {
                Button(onClick = {
                    myTarget = when (myTarget) {
                        MyTarget.First -> MyTarget.Second
                        else -> {
                            MyTarget.First
                        }
                    }
                }) {
                    Text(text = "B")
                }
            }
        }
    }
}

@Composable
fun AnimatedTriAngle() {
    val pathPortion = remember {
        Animatable(initialValue = 0f)
    }
    LaunchedEffect(key1 = true) {
        pathPortion.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 10000
            )
        )
    }

    val path = Path().apply {
        moveTo(100f, 100f)
        lineTo(500f, 100f)
        lineTo(500f, 500f)
        lineTo(100f, 500f)
        close()
        // quadraticBezierTo(300f, 300f, 500f, 100f)
    }

    val outPath = android.graphics.Path()
    val pos = FloatArray(2)
    val tan = FloatArray(2)
    android.graphics.PathMeasure().apply {
        setPath(path.asAndroidPath(), false)
        getSegment(0f, pathPortion.value * length, outPath, true)
        getPosTan(pathPortion.value * length, pos, tan)
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawPath(
            path = outPath.asComposePath(),
            color = Color.Red,
            style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
        )

        val x = pos[0]
        val y = pos[1]
        val degrees = -kotlin.math.atan2(tan[0], tan[1]) * (180f / PI.toFloat()) - 180f

        rotate(degrees = degrees, pivot = Offset(x, y)) {
            drawPath(
                path = Path().apply {
                    moveTo(x, y - 30f)
                    lineTo(x - 30f, y + 60f)
                    lineTo(x + 30f, y + 60f)
                    close()
                },
                color = Color.Red
            )
        }
    }
}

enum class MyTarget {
    First,
    Second
}

//https://stackoverflow.com/questions/29664993/how-to-convert-dp-px-sp-among-each-other-especially-dp-and-sp
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TicTacToe() {
    println("tic tok")
    val boxSize = 900f
    val strokeWidth = 20f
    var rowCount by remember {
        mutableStateOf(3)
    }
    val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)

    val firstShape = X_SHAPE

    val segmentLength = boxSize / rowCount

    val totalItemCount = rowCount.toDouble().pow(2).toInt()

    val shapeList = remember {
        mutableStateListOf<ShapeSpec>()
    }

    val selectedShapes = remember {
        mutableStateListOf<Pair<Int, Offset>>()
    }

    var winner by remember {
        mutableStateOf(-1)
    }

    var isPlaying by remember {
        mutableStateOf(true)
    }

    repeat(totalItemCount) {

        shapeList.add(
            ShapeSpec(
                type = X_SHAPE,
                isShown = false,
                topLeft = Offset.Zero
            )
        )

        shapeList.add(
            ShapeSpec(
                type = CIRCLE_SHAPE,
                isShown = false,
                topLeft = Offset.Zero
            )
        )

    }


    Column(Modifier.fillMaxSize(), Arrangement.SpaceAround, Alignment.CenterHorizontally) {
        println("column")
        Box {
            println("box")
            Canvas(modifier = Modifier
                .size(((boxSize + strokeWidth) / LocalContext.current.resources.displayMetrics.density).dp)
                .pointerInput(true) {
                    detectTapGestures {
                        if (!isPlaying) return@detectTapGestures

                        val absoluteOffset = it - topLeft
                        val clickedRow = floor(absoluteOffset.x / segmentLength)
                        val clickedColumn = floor(absoluteOffset.y / segmentLength)
                        val clickedTile = Offset(clickedRow, clickedColumn)


                        val selectedShape = ShapeSpec(
                            if (selectedShapes.lastOrNull()?.first ?: firstShape == CIRCLE_SHAPE) X_SHAPE else CIRCLE_SHAPE,
                            true,
                            topLeft + clickedTile * segmentLength
                        )

                        shapeList
                            .map { itMap ->
                                itMap.topLeft
                            }
                            .forEach { itForEach ->
                                if (itForEach == topLeft + clickedTile * segmentLength) return@detectTapGestures
                            }

                        shapeList.add(selectedShape)

                        selectedShapes.add(Pair(selectedShape.type, clickedTile))

                        val a =
                            selectedShapes.filter { filteredItem -> filteredItem.first == selectedShape.type }


                        val xItems = a.map { item ->
                            item.second.x
                        }

                        val yItems = a.map { item ->
                            item.second.y
                        }

                        var winnerBool = true

                        if (a.size < rowCount) return@detectTapGestures

                        if (isLine(a.map { map -> map.second }, rowCount.toFloat())) {
                            winner = selectedShape.type
                            isPlaying = false
                        }

                    }


                }) {
                println("canvas")
                val boundaryOffsets = mutableListOf<Pair<IntOffset, IntOffset>>()

                for (i in 1 until rowCount) {
                    boundaryOffsets.add(Pair(IntOffset(0, i), IntOffset(rowCount, i)))
                    boundaryOffsets.add(Pair(IntOffset(i, 0), IntOffset(i, rowCount)))
                }

                drawRect(color = DarkGray,
                    topLeft = topLeft,
                    size = Size(boxSize, boxSize),
                    style = Stroke(
                        width = strokeWidth,
                        pathEffect = PathEffect.cornerPathEffect(30f)
                    ))

                boundaryOffsets.forEach {
                    val startFactor = it.first
                    val endFactor = it.second

                    drawLine(color = Color.DarkGray,
                        strokeWidth = strokeWidth,
                        start = Offset(startFactor.x * segmentLength,
                            startFactor.y * segmentLength) + topLeft,
                        end = Offset(endFactor.x * segmentLength,
                            endFactor.y * segmentLength) + topLeft
                    )

                }


            }

            shapeList.forEach {
                if (it.isShown.not()) return@forEach

                if (it.type == X_SHAPE)
                    XSign(modifier = Modifier.offset(it.topLeft.x.pxToDp().dp,
                        it.topLeft.y.pxToDp().dp), segmentLength)
                else
                    CircleSign(Modifier.offset(it.topLeft.x.pxToDp().dp,
                        it.topLeft.y.pxToDp().dp), segmentLength)
            }
        }

        AnimatedVisibility(visible = !isPlaying) {
            Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.Start) {
                Text(text = "winner is",
                    color = Color.Black,
                    fontSize = 50.sp)
                if (winner == CIRCLE_SHAPE) CircleSign(segmentLength = segmentLength)
                else XSign(segmentLength = segmentLength)
            }

        }

        Button(
            onClick = {
                shapeList.clear()
                selectedShapes.clear()
                winner = -1
                isPlaying = true
            },
        ) {
            Text(text = "restart")
        }

    }

}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TicTacToe2() {
    println("tic tok")
    val boxSize = 900f
    val strokeWidth = 20f
    val rowCount by remember {
        mutableStateOf(10)
    }
    val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)

    val firstShape = X_SHAPE

    val segmentLength = boxSize / rowCount

    val totalItemCount = rowCount.toDouble().pow(2).toInt()

    val shapeList = remember {
        mutableStateListOf<ShapeSpec>()
    }

    val selectedShapes = remember {
        mutableStateListOf<Pair<Int, Offset>>()
    }

    var winner by remember {
        mutableStateOf(-1)
    }

    var isPlaying by remember {
        mutableStateOf(true)
    }



    Column(Modifier.fillMaxSize(), Arrangement.SpaceAround, Alignment.CenterHorizontally) {
        println("column")
        Box {
            println("box")
            Canvas(modifier = Modifier
                .size(((boxSize + strokeWidth) / LocalContext.current.resources.displayMetrics.density).dp)
                .pointerInput(true) {
                    detectTapGestures {
                        if (!isPlaying) return@detectTapGestures

                        val absoluteOffset = it - topLeft
                        val clickedRow = floor(absoluteOffset.x / segmentLength)
                        val clickedColumn = floor(absoluteOffset.y / segmentLength)
                        val clickedTile = Offset(clickedRow, clickedColumn)


                        val selectedShape = ShapeSpec(
                            if (selectedShapes.lastOrNull()?.first ?: firstShape == CIRCLE_SHAPE) X_SHAPE else CIRCLE_SHAPE,
                            true,
                            topLeft + clickedTile * segmentLength
                        )


                        shapeList
                            .map { itMap ->
                                itMap.topLeft
                            }
                            .forEach { itForEach ->
                                if (itForEach == topLeft + clickedTile * segmentLength) return@detectTapGestures
                            }

                        shapeList.add(selectedShape)

                        selectedShapes.add(Pair(selectedShape.type, clickedTile))

                        val a =
                            selectedShapes.filter { filteredItem -> filteredItem.first == selectedShape.type }


                        val xItems = a.map { item ->
                            item.second.x
                        }

                        val yItems = a.map { item ->
                            item.second.y
                        }

                        var winnerBool = true

                        if (a.size < rowCount) return@detectTapGestures

                        if (isLine(a.map { map -> map.second }, rowCount.toFloat())) {
                            winner = selectedShape.type
                            isPlaying = false
                        }

                    }


                }) {
                println("canvas")
                val boundaryOffsets = mutableListOf<Pair<IntOffset, IntOffset>>()

                for (i in 1 until rowCount) {
                    boundaryOffsets.add(Pair(IntOffset(0, i), IntOffset(rowCount, i)))
                    boundaryOffsets.add(Pair(IntOffset(i, 0), IntOffset(i, rowCount)))
                }

                drawRect(color = DarkGray,
                    topLeft = topLeft,
                    size = Size(boxSize, boxSize),
                    style = Stroke(
                        width = strokeWidth,
                        pathEffect = PathEffect.cornerPathEffect(30f)
                    ))

                boundaryOffsets.forEach {
                    val startFactor = it.first
                    val endFactor = it.second

                    drawLine(color = Color.DarkGray,
                        strokeWidth = strokeWidth,
                        start = Offset(startFactor.x * segmentLength,
                            startFactor.y * segmentLength) + topLeft,
                        end = Offset(endFactor.x * segmentLength,
                            endFactor.y * segmentLength) + topLeft
                    )

                }


            }

            shapeList.forEach {
                if (it.isShown.not()) return@forEach

                if (it.type == X_SHAPE)
                    XSign(modifier = Modifier.offset(it.topLeft.x.pxToDp().dp,
                        it.topLeft.y.pxToDp().dp), segmentLength)
                else
                    CircleSign(Modifier.offset(it.topLeft.x.pxToDp().dp,
                        it.topLeft.y.pxToDp().dp), segmentLength)
            }
        }

        AnimatedVisibility(visible = !isPlaying) {
            Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.Start) {
                Text(text = "winner is",
                    color = Color.Black,
                    fontSize = 50.sp)
                if (winner == CIRCLE_SHAPE) CircleSign(segmentLength = segmentLength)
                else XSign(segmentLength = segmentLength)
            }

        }

        Button(
            onClick = {
                shapeList.clear()
                selectedShapes.clear()
                winner = -1
                isPlaying = true
            },
        ) {
            Text(text = "restart")
        }

    }

}

private fun checkHasSameValues(
    xItems: List<Float>,
    maxCount: Int,
): Boolean {
    xItems.forEach main@{ mainItem ->
        var itemCount = 0
        xItems.forEach sub@{ subItem ->
            if (
                mainItem == subItem
            )
                itemCount++
            if (itemCount == maxCount) {
                return true
            }
        }
    }
    return false
}

private fun checkHasSameValuesDiagonal(
    offsets: List<Offset>,
    maxCount: Int,
): Boolean {
    var itemCount = 0
    offsets.forEach { mainItem ->
        if (mainItem.x == mainItem.y) itemCount++
        if (itemCount == maxCount) return true
    }

    itemCount = 0
    offsets.forEach { mainItem ->
        if (mainItem.y == maxCount - 1 - mainItem.x) itemCount++
        if (itemCount == maxCount) return true
    }
    return false
}

private fun isLine(items: List<Offset>, lineLength: Float): Boolean {

    val isHorizontal = checkHasSameValues(items.map { it.x }, lineLength.toInt())
    val isVertical = checkHasSameValues(items.map { it.y }, lineLength.toInt())
    val isDiagonal = checkHasSameValuesDiagonal(items, lineLength.toInt())

    return isHorizontal || isVertical || isDiagonal
}

@Composable
fun XSign(modifier: Modifier = Modifier, segmentLength: Float) {

    val startPoint = Offset(0f, 0f)

    val lineLength: Float = (segmentLength / sin(PI / 4)).toFloat()

    val scalePortion = remember {
        Animatable(initialValue = 0f)
    }

    LaunchedEffect(key1 = true, block = {
        scalePortion.animateTo(0.5f,
            animationSpec = spring(stiffness = Spring.StiffnessMedium))
    })

    val endPoint = startPoint + Offset((lineLength * cos(PI / 4)).toFloat(),
        (lineLength * sin(PI / 4)).toFloat())

    Canvas(modifier = modifier, onDraw = {

        scale(scale = scalePortion.value, pivot = (endPoint - startPoint) / 2f) {
            drawLine(color = Red,
                start = startPoint,
                end = endPoint,
                strokeWidth = 30f,
                cap = StrokeCap.Round)

            drawLine(color = Red,
                start = Offset(startPoint.x, (startPoint.y + lineLength * sin(PI / 4)).toFloat()),
                end = Offset(startPoint.x + lineLength * cos(PI / 4).toFloat(), startPoint.y),
                strokeWidth = 30f,
                cap = StrokeCap.Round)
        }

    }

    )

}

@Composable
fun CircleSign(modifier: Modifier = Modifier, segmentLength: Float) {

    val circlePortion = remember {
        Animatable(initialValue = 0f)
    }

    LaunchedEffect(key1 = true, block = {
        circlePortion.animateTo(1f, tween(durationMillis = 500))
    })

    val path = Path().apply {
        this.addOval(
            Rect(
                radius = segmentLength / 2,
                center = Offset(segmentLength / 2, segmentLength / 2),
            ))
    }

    val destination = Path()
    PathMeasure().apply {
        this.setPath(path = path, false)
        getSegment(0f,
            stopDistance = circlePortion.value * length,
            destination = destination,
            startWithMoveTo = true)
    }


    Canvas(modifier = modifier, onDraw = {

        scale(0.5f, pivot = Offset(
            segmentLength / 2,
            segmentLength / 2
        )) {

            drawPath(path = destination, color = Green, style = Stroke(30f))
        }

    })
}

const val X_SHAPE = 0
const val CIRCLE_SHAPE = 1

data class ShapeSpec(val type: Int, val isShown: Boolean, val topLeft: Offset)

@Composable
fun Float.pxToDp() = this / LocalContext.current.resources.displayMetrics.density

@Composable
fun BezierCurves() {
    var canvasCenter by remember { mutableStateOf(Offset(0f, 0f)) }

    var radius by remember { mutableStateOf(0f) }

    var firstHandleCenter by remember {
        mutableStateOf(Offset(canvasCenter.x + 50f, canvasCenter.y + 50f))
    }
    var secondHandleCenter by remember {
        mutableStateOf(Offset(canvasCenter.x + 400f, canvasCenter.y + 400f))
    }
    var firstCurveCenter by remember {
        mutableStateOf(Offset(canvasCenter.x + 400f, canvasCenter.y + 200f))
    }
    var secondCurveCenter by remember {
        mutableStateOf(Offset(canvasCenter.x + 200f, canvasCenter.y + 400f))
    }


    LaunchedEffect(key1 = canvasCenter) {
        firstHandleCenter = Offset(canvasCenter.x + 50f, canvasCenter.y + 50f)
        secondHandleCenter = Offset(canvasCenter.x + 400f, canvasCenter.y + 400f)
        firstCurveCenter = Offset(canvasCenter.x + 400f, canvasCenter.y + 200f)
        secondCurveCenter = Offset(canvasCenter.x + 200f, canvasCenter.y + 400f)
    }


    var path by remember { mutableStateOf(Path()) }

    Canvas(modifier = Modifier
        .fillMaxSize()

        .pointerInput(true) {
            detectDragGestures { change, dragAmount ->
                when {
                    firstHandleCenter.isTouched(change.previousPosition, radius) -> {
                        firstHandleCenter = change.position
                    }
                    secondHandleCenter.isTouched(change.previousPosition, radius) -> {
                        secondHandleCenter = change.position
                    }
                    firstCurveCenter.isTouched(change.previousPosition, radius) -> {
                        firstCurveCenter = change.position
                    }
                    secondCurveCenter.isTouched(change.previousPosition, radius) -> {
                        secondCurveCenter = change.position
                    }
                }
            }
        }) {


        canvasCenter = center

        radius = 20.dp.toPx()

        drawCircle(color = Blue, radius = radius, center = firstCurveCenter)
        drawCircle(color = Blue, radius = radius, center = secondCurveCenter)

        drawCircle(color = Green, radius = radius, center = firstHandleCenter)
        drawCircle(color = Red, radius = radius, center = secondHandleCenter)

        path = Path().apply {
            moveTo(firstHandleCenter.x, firstHandleCenter.y)
            cubicTo(
                firstCurveCenter.x,
                firstCurveCenter.y,
                secondCurveCenter.x,
                secondCurveCenter.y,
                secondHandleCenter.x,
                secondHandleCenter.y
            )
            close()
        }
        drawPath(path, color = Gray)

        val lineCount = 20
        val lineAngle = 60f
        val segmentHorizontalLength = size.width / lineCount
        val segmentVerticalLength = size.height / lineCount


        val squareWidth =
            maxOf(
                firstHandleCenter.x,
                firstCurveCenter.x,
                secondCurveCenter.x,
                secondHandleCenter.x,
            ) -
                    minOf(
                        firstHandleCenter.x,
                        firstCurveCenter.x,
                        secondCurveCenter.x,
                        secondHandleCenter.x,
                    )


        val squareHeight =
            maxOf(
                firstHandleCenter.y,
                firstCurveCenter.y,
                secondCurveCenter.y,
                secondHandleCenter.y,
            ) - minOf(
                firstHandleCenter.y,
                firstCurveCenter.y,
                secondCurveCenter.y,
                secondHandleCenter.y,
            )


        val squareTopLeft = Offset(
            minOf(
                firstHandleCenter.x,
                firstCurveCenter.x,
                secondCurveCenter.x,
                secondHandleCenter.x,
            ),
            minOf(
                firstHandleCenter.y,
                firstCurveCenter.y,
                secondCurveCenter.y,
                secondHandleCenter.y,
            )
        )

        Path().apply {
            addRect(Rect(
                offset = squareTopLeft,
                size = Size(squareWidth, squareHeight)
            ))
        }.also {
            drawPath(it, style = Stroke(width = 10f), color = Black)
        }


        val angle = atan2(
            (secondHandleCenter.y - firstHandleCenter.y),
            (secondHandleCenter.x - firstHandleCenter.x)
        )

        val length = secondHandleCenter.distanceFrom(firstHandleCenter)
        val pointCount = 10

        for (i in 1 until pointCount) {
            val circleCenter = Offset(
                firstHandleCenter.x + length / pointCount * (i) * cos(angle),
                firstHandleCenter.y + length / pointCount * (i) * sin(angle)
            )

            drawCircle(
                color = Yellow,
                center = circleCenter,
                radius = 20f
            )

            android.graphics.PathMeasure().apply {
                val pos = FloatArray(2)
                val tan = FloatArray(2)

                setPath(Path().apply {
                    moveTo(firstHandleCenter.x, firstHandleCenter.y)
                    lineTo(secondHandleCenter.x, secondHandleCenter.y)
                }.asAndroidPath(), false)

                getPosTan(i / pointCount * length, pos, tan)

                val theta = atan2(tan[1], tan[0])
                println(theta)

                drawLine(color = Yellow,
                    strokeWidth = 20f,
                    start = circleCenter,
                    end = Offset(pos[0] + 100 * sin(theta), pos[1] + 100 * cos(theta)))
            }

        }

    }
}

private fun Offset.distanceFrom(point: Offset): Float =
    sqrt((point.x - x).pow(2) + (point.y - y).pow(2))

private fun Offset.isTouched(touchPoint: Offset, touchAreaRadius: Float) =
    distanceFrom(touchPoint) <= touchAreaRadius


fun hasSameValue(array: List<Float>): Boolean {
    for (i in 1 until array.size) {
        if (array[0] != array[i]) return false
    }
    return true
}

@Composable
fun MessageBox(text: String,isSender: Boolean) {

    BoxWithConstraints() {
        Box(
            Modifier
                .height(IntrinsicSize.Min).fillMaxWidth(1f),
            contentAlignment = if(isSender) Alignment.TopEnd else Alignment.TopStart
        ) {

            var textBoxWidthPx by remember {
                mutableStateOf(0)
            }
            var textBoxHeightPx by remember {
                mutableStateOf(0)
            }


            val roundness = 80f
            val text1 =
                "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum"
            val text2 =
                "Lorem Ipsum is simp"

            Canvas(
                modifier = Modifier
                    .background(Yellow),
                onDraw = {
                    translate(left = if (isSender) size.width - textBoxWidthPx - 2 * roundness else 0f) {
                        drawPath(
                            path = messageCloud(textBoxWidthPx.toFloat(), textBoxHeightPx.toFloat(),
                                roundness = roundness, isSender = isSender),
                            color = Red,
                        )
                    }
                }
            )

            Text(text = text,

                color = White,
                modifier = Modifier
                    .requiredWidthIn(max=this@BoxWithConstraints.maxWidth*0.9f)
                    .padding(
                        top = (roundness / 2).toDp().dp,
                        bottom = (roundness / 2).toDp().dp,
                        start = (roundness / 2 * if (isSender) 1 else 3).toDp().dp,
                        end = (roundness / 2 * if (isSender) 3 else 1).toDp().dp
                    )
                    .onGloballyPositioned {
                        textBoxWidthPx = it.size.width
                        textBoxHeightPx = it.size.height
                    })

        }

    }

}


@Composable
fun Float.toDp() = this / LocalContext.current.resources.displayMetrics.density


@Composable
fun Float.toPX() = this * LocalContext.current.resources.displayMetrics.density

private fun messageCloud(
    widthInPx: Float,
    heightInPx: Float,
    roundness: Float = 20f,
    isSender: Boolean = true,
): Path {

    return Path().apply {

        arcTo(
            rect = Rect(
                Offset(
                    if (isSender) 0f else roundness,
                    0f
                ),
                size = Size(roundness * 2, roundness * 2)
            ),
            startAngleDegrees = -90f,
            sweepAngleDegrees = -90f,
            false
        )

        arcTo(
            Rect(
                offset = Offset(
                    x = roundness * if (isSender) 0 else -1,
                    heightInPx - roundness
                ),
                size = Size(roundness * 2, roundness * 2)
            ),
            startAngleDegrees = if (isSender) 180f else 0f,
            sweepAngleDegrees = 90f * (if (isSender) -1 else 1),
            false
        )

        arcTo(
            Rect(
                offset = Offset(widthInPx + if (isSender) roundness else 0f,
                    heightInPx - roundness),
                size = Size(
                    roundness * 2, roundness * 2
                ),
            ),
            startAngleDegrees = 90f,
            sweepAngleDegrees = 90f * if (isSender) 1 else -1,
            false
        )

        arcTo(
            Rect(
                Offset(
                    x = widthInPx - if (isSender) roundness * 1 else 0f,
                    y = 0f,
                ),
                size = Size(roundness * 2, roundness * 2)
            ),
            startAngleDegrees = 0f,
            sweepAngleDegrees = -90f,
            false
        )

    }
}