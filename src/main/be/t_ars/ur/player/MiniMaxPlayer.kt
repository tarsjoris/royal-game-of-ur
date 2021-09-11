package be.t_ars.ur.player

import be.t_ars.ur.Board
import be.t_ars.ur.EPlayer
import be.t_ars.ur.IPlayer
import be.t_ars.ur.Loc


class MiniMaxPlayer(private val player: EPlayer) : IPlayer {
    override fun getName() =
        "MiniMax"

    override fun getNextMove(board: Board, stepCount: Int) =
        getBestMove(createGameState(board), player, stepCount, 5)
}

private val CHANCES = mapOf(
    Pair(0, 1.toFloat() / 16.toFloat()),
    Pair(1, 4.toFloat() / 16.toFloat()),
    Pair(2, 6.toFloat() / 16.toFloat()),
    Pair(3, 4.toFloat() / 16.toFloat()),
    Pair(4, 1.toFloat() / 16.toFloat())
)

private fun heuristic(gameState: GameState) =
    gameState.getFinishedCount(EPlayer.A) - gameState.getFinishedCount(EPlayer.B)

private fun getScore(gameState: GameState, player: EPlayer, depth: Int): Int {
    if (depth == 0) {
        return heuristic(gameState)
    }

    val otherPlayer = if (player == EPlayer.A) EPlayer.B else EPlayer.A
    return CHANCES.map { chance ->
        getBestScore(gameState, otherPlayer, chance.key, depth - 1) * chance.value
    }
        .sum()
        .toInt()
}

private fun isScoreBetter(player: EPlayer, newScore: Int, bestScore: Int) =
    if (player == EPlayer.A) newScore > bestScore else newScore < bestScore

private fun getBestScore(gameState: GameState, player: EPlayer, stepCount: Int, depth: Int): Int {
    if (stepCount == 0) {
        return getScore(gameState, player, depth)
    }

    val path = Board.PATHS[player.index]
    var bestScore = if (player == EPlayer.A) Int.MIN_VALUE else Int.MAX_VALUE

    val newPieceLoc = path[stepCount - 1]
    if (gameState.isValidTarget(player, newPieceLoc)) {
        val newPieceGameState = gameState.introducePiece(player, newPieceLoc)
        val newPieceScore = getScore(newPieceGameState, player, depth)
        if (isScoreBetter(player, newPieceScore, bestScore)) {
            bestScore = newPieceScore
        }
    }
    for (i in 0..(path.size - stepCount)) {
        val movePieceFrom = path[i]
        if (gameState.getBox(movePieceFrom) == player) {
            if (i + stepCount < path.size) {
                val movePieceTo = path[i + stepCount]
                if (gameState.isValidTarget(player, movePieceTo)) {
                    val movePieceGameState = gameState.movePiece(player, movePieceFrom, movePieceTo)
                    val movePieceScore = getScore(movePieceGameState, player, depth)
                    if (isScoreBetter(player, movePieceScore, bestScore)) {
                        bestScore = movePieceScore
                    }
                }
            } else {
                val finishPieceGameState = gameState.finishPiece(player, movePieceFrom)
                val finishPieceScore = getScore(finishPieceGameState, player, depth)
                if (isScoreBetter(player, finishPieceScore, bestScore)) {
                    bestScore = finishPieceScore
                }
            }
        }
    }
    return bestScore
}

private fun getBestMove(gameState: GameState, player: EPlayer, stepCount: Int, depth: Int): Loc? {
    val path = Board.PATHS[player.index]
    var bestMove: Loc? = null
    var bestScore = if (player == EPlayer.A) Int.MIN_VALUE else Int.MAX_VALUE

    // introduce piece
    val newPieceLoc = path[stepCount - 1]
    if (gameState.isValidTarget(player, newPieceLoc)) {
        val newPieceGameState = gameState.introducePiece(player, newPieceLoc)
        val newPieceScore = getScore(newPieceGameState, player, depth)
        if (isScoreBetter(player, newPieceScore, bestScore)) {
            bestScore = newPieceScore
            bestMove = Board.START_LOC
        }
    }

    for (i in 0..(path.size - stepCount)) {
        val movePieceFrom = path[i]
        if (gameState.getBox(movePieceFrom) == player) {
            if (i + stepCount < path.size) {
                // move piece
                val movePieceTo = path[i + stepCount]
                if (gameState.isValidTarget(player, movePieceTo)) {
                    val movePieceGameState = gameState.movePiece(player, movePieceFrom, movePieceTo)
                    val movePieceScore = getScore(movePieceGameState, player, depth)
                    if (isScoreBetter(player, movePieceScore, bestScore)) {
                        bestScore = movePieceScore
                        bestMove = movePieceFrom
                    }
                }
            } else {
                // finish piece
                val finishPieceGameState = gameState.finishPiece(player, movePieceFrom)
                val finishPieceScore = getScore(finishPieceGameState, player, depth)
                if (isScoreBetter(player, finishPieceScore, bestScore)) {
                    bestScore = finishPieceScore
                    bestMove = movePieceFrom
                }
            }
        }
    }
    return bestMove
}