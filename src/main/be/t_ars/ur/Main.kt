package be.t_ars.ur

import be.t_ars.ur.player.DummyPlayer
import be.t_ars.ur.player.MiniMaxPlayer
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
        mainFrame.setDiceRoll(null, currentPlayer)
        Thread.sleep(DELAY)
        val stepCount = nextRoll()
        println("Player ${currentPlayer.label} threw $stepCount")
        mainFrame.setDiceRoll(stepCount, currentPlayer)
        ++throws[currentPlayer.index][stepCount]
        Thread.sleep(DELAY)
        val landedOnStar: Boolean
        if (stepCount != 0) {
            val loc = players[currentPlayer.index].getNextMove(board, stepCount)
            println("   and moved $loc")
            landedOnStar = when (loc) {
                Board.START_LOC -> board.introducePiece(currentPlayer, stepCount)
                null -> board.skipTurn(currentPlayer, stepCount)
                else -> board.movePiece(currentPlayer, loc, stepCount)
            }
            mainFrame.update()
        } else {
            landedOnStar = false
        }
        if (!landedOnStar) {
            currentPlayer = if (currentPlayer == EPlayer.A) EPlayer.B else EPlayer.A
        }
    }

    EPlayer.values()
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
    //playGameOnBoard(MiniMaxPlayer(EPlayer.A), DummyPlayer(EPlayer.B))

    //testBestPlayer(RandomPlayer(EPlayer.A), DummyPlayer(EPlayer.B))
    testBestPlayer(MiniMaxPlayer(EPlayer.A), DummyPlayer(EPlayer.B))
}