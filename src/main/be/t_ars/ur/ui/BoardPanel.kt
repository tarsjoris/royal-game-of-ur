package be.t_ars.ur.ui

import be.t_ars.ur.*
import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.MouseEvent
import javax.swing.JPanel
import kotlin.math.roundToInt

private val PLAYER_COLOR = arrayOf(Color.BLUE, Color.RED)

class BoardPanel(private val board: Board) : JPanel() {
    var diceRoll: Int? = null
    var currentPlayer = EPlayer.A

    override fun paint(g: Graphics) {
        val xStep = width.toFloat() / Board.WIDTH.toFloat()
        val yStep = height.toFloat() / Board.HEIGHT.toFloat()

        g.color = Color.WHITE
        g.fillRect(0, 0, width, height)
        for (y in Board.BOXES.indices) {
            val row = Board.BOXES[y]
            for (x in row.indices) {
                when (row[x]) {
                    EBox.S -> {
                        g.color = Color.ORANGE
                        g.drawOval(
                            (xStep * (x.toFloat() + 0.1F)).roundToInt(),
                            (yStep * (y.toFloat() + 0.1F)).roundToInt(),
                            (xStep * 0.8F).roundToInt(),
                            (yStep * 0.8F).roundToInt()
                        )
                    }
                    EBox.V -> {
                        g.color = Color.LIGHT_GRAY
                        val x1 = (xStep * x.toFloat()).roundToInt()
                        val y1 = (yStep * y.toFloat()).roundToInt()
                        g.fillRect(x1, y1, xStep.roundToInt(), yStep.roundToInt())
                    }
                    else -> {
                    }
                }
            }
        }
        g.color = Color.BLACK
        for (i in 1 until Board.WIDTH) {
            val x = (xStep * i.toFloat()).roundToInt()
            g.drawLine(x, 0, x, height)
        }
        for (i in 1 until Board.HEIGHT) {
            val y = (yStep * i.toFloat()).roundToInt()
            g.drawLine(0, y, width, y)
        }

        g.font = Font("Arial", Font.BOLD, 20)
        val fontRenderContext = (g as Graphics2D).fontRenderContext
        val bounds = g.font.getStringBounds("A", fontRenderContext)
        val xOffset = bounds.width.toFloat() * 0.5F
        val yOffset = bounds.height.toFloat() * 0.5F
        for (y in 0 until Board.HEIGHT) {
            for (x in 0 until Board.WIDTH) {
                val state = board.gameState.getBox(Loc(x, y))
                if (state != null) {
                    g.color = PLAYER_COLOR[state.index]
                    g.drawString(
                        state.label,
                        (xStep * (x.toFloat() + 0.5F) - xOffset).roundToInt(),
                        (yStep * (y.toFloat() + 0.5F) + yOffset).roundToInt()
                    )
                }
            }
        }

        for (player in EPlayer.values()) {
            val available = board.gameState.getUnusedCount(player)
            if (available > 0) {
                g.color = PLAYER_COLOR[player.index]
                val x = 4
                val y = if (player == EPlayer.A) 0 else 2
                g.drawString(
                    available.toString(),
                    (xStep * (x.toFloat() + 0.5F) - xOffset).roundToInt(),
                    (yStep * (y.toFloat() + 0.5F) + yOffset).roundToInt()
                )
            }
        }
        for (player in EPlayer.values()) {
            val finished = board.gameState.getFinishedCount(player)
            if (finished > 0) {
                g.color = PLAYER_COLOR[player.index]
                val x = 5
                val y = if (player == EPlayer.A) 0 else 2
                g.drawString(
                    finished.toString(),
                    (xStep * (x.toFloat() + 0.5F) - xOffset).roundToInt(),
                    (yStep * (y.toFloat() + 0.5F) + yOffset).roundToInt()
                )
            }
        }
        diceRoll?.let {
            g.color = Color.BLACK
            g.drawString(
                it.toString(),
                10,
                if (currentPlayer == EPlayer.A) (10F + yOffset).roundToInt() else height - 10
            )
        }

        board.winner?.let {
            g.color = Color.BLACK
            g.drawString(
                "Winner is ${it.label}",
                10,
                height - 10
            )
        }
    }

    override fun processMouseEvent(e: MouseEvent) {

    }
}