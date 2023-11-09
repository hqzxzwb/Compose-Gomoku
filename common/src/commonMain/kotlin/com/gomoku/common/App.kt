package com.gomoku.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset

@Composable
fun App() {
    val state = BoardState(19, 19)
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
                .padding(it)
                .background(color = Color.Green.copy(alpha = 0.6F)),
        ) {
            GomokuBoard(
                state = state,
                lineColor = Color.Black,
                modifier = Modifier.weight(1F),
            )

            Column {
                Text("重来", modifier = Modifier.clickable { state.reset() })
                Text("悔棋", modifier = Modifier.clickable { state.revert() })
                Text(
                    text = if (state.blackTurn) {
                        "黑方走棋"
                    } else {
                        "白方走棋"
                    }
                )
            }
        }
    }

}

@Composable
fun GomokuBoard(
    state: BoardState,
    lineColor: Color,
    modifier: Modifier = Modifier,
) {
    state.operationCount
    PadIn(
        modifier = modifier
            .layout { measurable, constraints ->
                val size = minOf(constraints.maxHeight, constraints.maxWidth)
                val newConstraints = Constraints(size, size, size, size)
                val placeable = measurable.measure(newConstraints)
                layout(size, size) {
                    placeable.place(0, 0)
                }
            }
    ) {
        Row(
            Modifier.fillMaxSize()
        ) {
            repeat(state.width) { x ->
                Column(Modifier.fillMaxHeight().weight(1F)) {
                    repeat(state.height) { y ->
                        GridUi(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1F)
                                .clickable { state.emitClick(x, y) },
                            state = state,
                            x = x,
                            y = y,
                            lineColor = lineColor,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GridUi(modifier: Modifier, state: BoardState, x: Int, y: Int, lineColor: Color) {
    val gridState = state[x, y]
    Canvas(
        modifier
    ) {
        val size = size
        drawLine(
            lineColor,
            start = Offset(if (x == 0) size.width / 2 else 0F, size.height / 2),
            end = Offset(if (x == state.width - 1) size.width / 2 else size.width, size.height / 2),
        )
        drawLine(
            lineColor,
            start = Offset(size.width / 2, if (y == 0) size.height / 2 else 0F),
            end = Offset(
                size.width / 2,
                if (y == state.height - 1) size.height / 2 else size.height
            ),
        )
        when (gridState) {
            GridState.Black -> drawCircle(
                color = Color.Black,
                radius = size.minDimension / 3F,
            )

            GridState.White -> drawCircle(
                color = Color.White,
                radius = size.minDimension / 3F,
            )
        }
    }
}

@Composable
inline fun PadIn(modifier: Modifier, content: @Composable BoxScope.() -> Unit) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.weight(0.05F))
        Row(modifier = Modifier.weight(0.9F)) {
            Spacer(modifier = Modifier.weight(0.05F))
            Box(Modifier.weight(0.9F)) {
                content()
            }
            Spacer(modifier = Modifier.weight(0.05F))
        }
        Spacer(modifier = Modifier.weight(0.05F))
    }
}

@Stable
class BoardState(
    val width: Int,
    val height: Int,
) {
    var blackTurn by mutableStateOf(true)
    var operationCount by mutableStateOf(0)
    val operationStack = ArrayDeque<IntOffset>()

    private val grid = Array(width) {
        IntArray(height) {
            GridState.Empty.value
        }
    }

    private operator fun Array<IntArray>.set(x: Int, y: Int, value: GridState) {
        this[x][y] = value.value
    }

    private operator fun Array<IntArray>.get(x: Int, y: Int): GridState {
        return GridState.fromValue(this[x][y])
    }

    fun emitClick(x: Int, y: Int) {
        val gridState = grid[x, y]
        if (gridState == GridState.Empty) {
            grid[x, y] = if (blackTurn) {
                GridState.Black
            } else {
                GridState.White
            }
            blackTurn = !blackTurn
            operationStack.add(IntOffset(x, y))
            operationCount++
        }
    }

    val revertable get() = operationStack.isNotEmpty()

    fun revert() {
        if (operationStack.isNotEmpty()) {
            val (x, y) = operationStack.removeLast()
            grid[x, y] = GridState.Empty
            blackTurn = !blackTurn
            operationCount++
        }
    }

    operator fun get(x: Int, y: Int): GridState {
        return grid[x, y]
    }

    fun reset() {
        grid.forEach { it.fill(GridState.Empty.value) }
        blackTurn = true
        operationCount++
    }
}

@JvmInline
value class GridState private constructor(val value: Int) {
    companion object {
        val Empty = GridState(1)
        val White = GridState(2)
        val Black = GridState(3)

        fun fromValue(value: Int): GridState {
            check(value in 1..3)
            return GridState(value)
        }
    }
}
