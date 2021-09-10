package be.t_ars.ur

import be.t_ars.ur.solver.DummySolver
import be.t_ars.ur.ui.MainFrame

const val DELAY = 1000L

fun main() {
    val board = Board()
    val mainFrame = MainFrame(board)
    mainFrame.isVisible = true
    val solvers = arrayOf(DummySolver(EPlayer.A), DummySolver(EPlayer.B))
    var currentPlayer = EPlayer.A
    while (board.winner == null) {
        Thread.sleep(DELAY)
        val stepCount = nextRoll()
        mainFrame.setDiceRoll(stepCount, currentPlayer)
        Thread.sleep(DELAY)
        mainFrame.setDiceRoll(null, currentPlayer)
        val landedOnStar: Boolean
        if (stepCount != 0) {
            val loc = solvers[currentPlayer.index].getNextMove(board, stepCount)
            landedOnStar = when (loc) {
                Board.START_LOC -> board.introducePiece(currentPlayer, stepCount)
                null -> false
                else -> board.move(currentPlayer, loc, stepCount)
            }
            mainFrame.update()
        } else {
            landedOnStar = false
        }
        if (!landedOnStar) {
            currentPlayer = if (currentPlayer == EPlayer.A) EPlayer.B else EPlayer.A
        }
    }
}