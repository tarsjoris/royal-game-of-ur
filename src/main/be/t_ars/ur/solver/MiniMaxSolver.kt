package be.t_ars.ur.solver

import be.t_ars.ur.EPlayer
import be.t_ars.ur.Loc


private val PATH_A = arrayOf(
    Loc(3, 0),
    Loc(2, 0),
    Loc(1, 0),
    Loc(0, 0),
    Loc(0, 1),
    Loc(1, 1),
    Loc(2, 1),
    Loc(3, 1),
    Loc(4, 1),
    Loc(5, 1),
    Loc(6, 1),
    Loc(7, 1),
    Loc(7, 0),
    Loc(6, 0)
)
private val PATH_B = arrayOf(
    Loc(3, 2),
    Loc(2, 2),
    Loc(1, 2),
    Loc(0, 2),
    Loc(0, 1),
    Loc(1, 1),
    Loc(2, 1),
    Loc(3, 1),
    Loc(4, 1),
    Loc(5, 1),
    Loc(6, 1),
    Loc(7, 1),
    Loc(7, 2),
    Loc(6, 2)
)

private val CHANCES = mapOf(
    Pair(0, 1.toFloat() / 16.toFloat()),
    Pair(1, 4.toFloat() / 16.toFloat()),
    Pair(2, 6.toFloat() / 16.toFloat()),
    Pair(3, 4.toFloat() / 16.toFloat()),
    Pair(4, 1.toFloat() / 16.toFloat())
)

private fun getPath(player: EPlayer) =
    if (player == EPlayer.A) PATH_A else PATH_B

private fun heuristic(gameState: Long) =
    getFinishedCount(gameState, EPlayer.A) - getFinishedCount(gameState, EPlayer.B)

private fun getScore(gameState: Long, player: EPlayer, depth: Int): Int {
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

private fun isValidTarget(gameState: Long, player: EPlayer, target: Loc): Boolean {
    val currentToBox = getBox(gameState, target)
    return currentToBox != player && (currentToBox == EPlayer.E || !isMagicBox(target))
}

private fun isScoreBetter(player: EPlayer, newScore: Int, bestScore: Int) =
    if (player == EPlayer.A) newScore > bestScore else newScore < bestScore

private fun getBestScore(gameState: Long, player: EPlayer, stepCount: Int, depth: Int): Int {
    if (stepCount == 0) {
        return getScore(gameState, player, depth)
    }

    val path = getPath(player)
    var bestScore = if (player == EPlayer.A) Int.MIN_VALUE else Int.MAX_VALUE

    val newPieceLoc = path[stepCount - 1]
    if (isValidTarget(gameState, player, newPieceLoc)) {
        val newPieceGameState = introducePiece(gameState, player, newPieceLoc)
        val newPieceScore = getScore(newPieceGameState, player, depth)
        if (isScoreBetter(player, newPieceScore, bestScore)) {
            bestScore = newPieceScore
        }
    }
    for (i in 0..(path.size - stepCount)) {
        val movePieceFrom = path[i]
        if (getBox(gameState, movePieceFrom) == player) {
            if (i + stepCount < path.size) {
                val movePieceTo = path[i + stepCount]
                if (isValidTarget(gameState, player, movePieceTo)) {
                    val movePieceGameState = movePiece(gameState, player, movePieceFrom, movePieceTo)
                    val movePieceScore = getScore(movePieceGameState, player, depth)
                    if (isScoreBetter(player, movePieceScore, bestScore)) {
                        bestScore = movePieceScore
                    }
                }
            } else {
                val finishPieceGameState = finishPiece(gameState, player, movePieceFrom)
                val finishPieceScore = getScore(finishPieceGameState, player, depth)
                if (isScoreBetter(player, finishPieceScore, bestScore)) {
                    bestScore = finishPieceScore
                }
            }
        }
    }
    return bestScore
}

fun doBestMove(gameState: Long, player: EPlayer, stepCount: Int, depth: Int): Long? {
    val path = getPath(player)
    var bestGameState: Long? = null
    var bestScore = if (player == EPlayer.A) Int.MIN_VALUE else Int.MAX_VALUE

    val newPieceLoc = path[stepCount - 1]
    if (isValidTarget(gameState, player, newPieceLoc)) {
        val newPieceGameState = introducePiece(gameState, player, newPieceLoc)
        val newPieceScore = getScore(newPieceGameState, player, depth)
        if (isScoreBetter(player, newPieceScore, bestScore)) {
            bestScore = newPieceScore
            bestGameState = newPieceGameState
        }
    }
    for (i in 0..(path.size - stepCount)) {
        val movePieceFrom = path[i]
        if (getBox(gameState, movePieceFrom) == player) {
            if (i + stepCount < path.size) {
                val movePieceTo = path[i + stepCount]
                if (isValidTarget(gameState, player, movePieceTo)) {
                    val movePieceGameState = movePiece(gameState, player, movePieceFrom, movePieceTo)
                    val movePieceScore = getScore(movePieceGameState, player, depth)
                    if (isScoreBetter(player, movePieceScore, bestScore)) {
                        bestScore = movePieceScore
                        bestGameState = movePieceGameState
                    }
                }
            } else {
                val finishPieceGameState = finishPiece(gameState, player, movePieceFrom)
                val finishPieceScore = getScore(finishPieceGameState, player, depth)
                if (isScoreBetter(player, finishPieceScore, bestScore)) {
                    bestScore = finishPieceScore
                    bestGameState = finishPieceGameState
                }
            }
        }
    }
    return bestGameState
}

fun main() {
    val newGameState = doBestMove(INITIAL_GAMESTATE, EPlayer.A, 5, 10)
    if (newGameState != null) {
        printGameState(newGameState)
    } else {
        println("No best move found")
    }
}