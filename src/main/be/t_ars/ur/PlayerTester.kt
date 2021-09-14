package be.t_ars.ur

private const val NUMBER_OF_GAMES = 10000

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
            val loc = players[currentPlayer.index].getNextMove(board, stepCount)
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