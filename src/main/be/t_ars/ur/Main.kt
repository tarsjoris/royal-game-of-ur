package be.t_ars.ur

import be.t_ars.ur.player.DummyPlayer
import be.t_ars.ur.player.RandomPlayer
import be.t_ars.ur.ui.MainFrame

const val DELAY = 1000L

private fun playGameOnBoard(playerA: IPlayer, playerB: IPlayer) {
    val board = Board()
    val mainFrame = MainFrame(board)
    mainFrame.isVisible = true
    val players = arrayOf(playerA, playerB)
    var currentPlayer = EPlayer.A
    val throws = Array(2) {
        Array(5) {
            0
        }
    }
    while (board.winner == null) {
        Thread.sleep(DELAY)
        val stepCount = nextRoll()
        mainFrame.setDiceRoll(stepCount, currentPlayer)
        ++throws[currentPlayer.index][stepCount]
        Thread.sleep(DELAY)
        mainFrame.setDiceRoll(null, currentPlayer)
        val landedOnStar: Boolean
        if (stepCount != 0) {
            val loc = players[currentPlayer.index].getNextMove(board, stepCount)
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

    arrayOf(EPlayer.A, EPlayer.B)
        .forEach { player ->
            println("Dice rolls of player ${player.name}")
            throws[player.index]
                .forEachIndexed { index, throws ->
                    println("$index: $throws")
                }
        }
}

fun main() {
    //playGameOnBoard(RandomPlayer(EPlayer.A), DummyPlayer(EPlayer.B))
    testBestPlayer(RandomPlayer(EPlayer.A), DummyPlayer(EPlayer.B))
}