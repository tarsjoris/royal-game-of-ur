package be.t_ars.ur

import be.t_ars.ur.player.DummyPlayer
import be.t_ars.ur.player.ExpectiMiniMaxPlayer

private const val NUMBER_OF_GAMES = 100

fun testBestPlayer(playerA: IPlayer, playerB: IPlayer): EPlayer {
    val wins = Array(2) { 0 }
    for (i in 1..NUMBER_OF_GAMES) {
        val winner = runGame(playerA, playerB)
        ++wins[winner.index]
        print(winner.label)
        if (i.mod(100) == 0) println()
    }
    println("Player ${playerA.getName()} won ${wins[EPlayer.A.index]} games.")
    println("Player ${playerB.getName()} won ${wins[EPlayer.B.index]} games.")
    return if (wins[EPlayer.B.index] > wins[EPlayer.A.index]) EPlayer.B else EPlayer.A
}

private fun runGame(playerA: IPlayer, playerB: IPlayer): EPlayer {
    val board = Board()
    val players = arrayOf(playerA, playerB)
    var currentPlayer = EPlayer.A
    var winner: EPlayer? = null
    while (winner == null) {
        val stepCount = nextRoll()
        val landedOnStar: Boolean
        if (stepCount != 0) {
            val loc = players[currentPlayer.index].getNextMove(board.gameState, stepCount)
            landedOnStar = when (loc) {
                Board.START_LOC -> board.introducePiece(currentPlayer, stepCount)
                null -> false
                else -> board.movePiece(currentPlayer, loc, stepCount)
            }
        } else {
            landedOnStar = false
        }
        if (!landedOnStar) {
            currentPlayer = if (currentPlayer == EPlayer.A) EPlayer.B else EPlayer.A
        }
        winner = board.winner
    }
    return winner
}

fun main() {
    //testBestPlayer(RandomPlayer(EPlayer.A), DummyPlayer(EPlayer.B))
    testBestPlayer(ExpectiMiniMaxPlayer(EPlayer.A, 3), DummyPlayer(EPlayer.B))
    //testBestPlayer(ExpectiMiniMaxPlayerWeights(EPlayer.A, 2), ExpectiMiniMaxPlayer(EPlayer.B, 2))
}