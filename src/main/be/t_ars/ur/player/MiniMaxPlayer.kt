package be.t_ars.ur.player

import be.t_ars.ur.Board
import be.t_ars.ur.EPlayer
import be.t_ars.ur.IPlayer
import be.t_ars.ur.Loc

private const val DEBUG = false
private const val DEPTH = 2


class MiniMaxPlayer(private val player: EPlayer) : IPlayer {
    override fun getName() =
        "MiniMax"

    override fun getNextMove(board: Board, stepCount: Int) =
        getBestMoveAndScore(createGameState(board), player, stepCount, DEPTH).first
}

private val CHANCES = listOf(
    Pair(0, 1F / 16F),
    Pair(1, 4F / 16F),
    Pair(2, 6F / 16F),
    Pair(3, 4F / 16F),
    Pair(4, 1F / 16F)
)

private const val WEIGHT_UNUSED_COUNT = -1
private const val WEIGHT_PIECES_IN_START_LANE_COUNT = 1
private const val WEIGHT_PIECES_IN_CENTER_LANE_COUNT = 0
private const val WEIGHT_CENTER_STAR_OWNED = 6
private const val WEIGHT_PIECES_IN_END_LANE_COUNT = 3
private const val WEIGHT_FINISHED_COUNT = 5

private fun getBestMoveAndScore(gameState: GameState, player: EPlayer, stepCount: Int, depth: Int): Pair<Loc?, Double> {
    if (DEBUG) gameState.printGameState()

    if (stepCount == 0) {
        return Pair(null, getScoreAfterMove(gameState, player, depth))
    }

    val path = Board.PATHS[player.index]
    var bestMove: Loc? = null
    var bestScore: Double? = null

    // introduce piece
    val newPieceLoc = path[stepCount - 1]
    if (gameState.isValidTarget(player, newPieceLoc)) {
        val newPieceGameState = gameState.introducePiece(player, newPieceLoc)
        val newPieceScore = if (isMagicBox(newPieceLoc)) {
            // roll again
            getWeightedScoreForTurn(newPieceGameState, player, depth)
        } else {
            getScoreAfterMove(newPieceGameState, player, depth)
        }
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
                    val movePieceScore = if (isMagicBox(movePieceTo)) {
                        // roll again
                        getWeightedScoreForTurn(movePieceGameState, player, depth)
                    } else {
                        getScoreAfterMove(movePieceGameState, player, depth)
                    }
                    if (isScoreBetter(player, movePieceScore, bestScore)) {
                        bestScore = movePieceScore
                        bestMove = movePieceFrom
                    }
                }
            } else {
                // finish piece
                val finishPieceGameState = gameState.finishPiece(player, movePieceFrom)
                val finishPieceScore = getScoreAfterMove(finishPieceGameState, player, depth)
                if (isScoreBetter(player, finishPieceScore, bestScore)) {
                    bestScore = finishPieceScore
                    bestMove = movePieceFrom
                }
            }
        }
    }
    return Pair(bestMove, bestScore ?: getScoreAfterMove(gameState, player, depth))
}

private fun isScoreBetter(player: EPlayer, newScore: Double, bestScore: Double?) =
    bestScore == null ||
            (if (player == EPlayer.A) newScore > bestScore else newScore < bestScore)

private fun getScoreAfterMove(gameState: GameState, player: EPlayer, depth: Int): Double {
    if (DEBUG) gameState.printGameState()
    if (depth == 0) {
        return heuristic(gameState)
    }
    return getWeightedScoreForTurn(gameState, player.otherPlayer(), depth - 1)
}

private fun getWeightedScoreForTurn(gameState: GameState, player: EPlayer, depth: Int) =
    CHANCES.sumOf { (stepCount, chance) ->
        getBestMoveAndScore(gameState, player, stepCount, depth).second * chance
    }

private fun heuristic(gameState: GameState): Double {
    val heuristicA = heuristicOfPlayer(gameState, EPlayer.A)
    val heuristicB = heuristicOfPlayer(gameState, EPlayer.B)
    if (DEBUG) println("Heuristic A=$heuristicA B=$heuristicB -> ${heuristicA - heuristicB}")
    return (heuristicA - heuristicB).toDouble()
}

private fun heuristicOfPlayer(gameState: GameState, player: EPlayer) =
    WEIGHT_UNUSED_COUNT * gameState.getUnusedCount(player) +
            WEIGHT_PIECES_IN_START_LANE_COUNT * getPiecesInStartLaneCount(gameState, player) +
            WEIGHT_PIECES_IN_CENTER_LANE_COUNT * getPiecesInCenterLaneCount(gameState, player) +
            WEIGHT_CENTER_STAR_OWNED * getCenterStarsOwnedCount(gameState, player) +
            WEIGHT_PIECES_IN_END_LANE_COUNT * getPiecesInEndLaneCount(gameState, player) +
            WEIGHT_FINISHED_COUNT * gameState.getFinishedCount(player)

private fun getPiecesInStartLaneCount(gameState: GameState, player: EPlayer): Int {
    val path = Board.PATHS[player.index]
    return (0..3).count { gameState.getBox(path[it]) == player }
}

private fun getPiecesInCenterLaneCount(gameState: GameState, player: EPlayer): Int {
    val path = Board.PATHS[player.index]
    return (4..11).count { gameState.getBox(path[it]) == player }
}

private fun getCenterStarsOwnedCount(gameState: GameState, player: EPlayer) =
    if (gameState.getBox(Loc(3, 1)) == player) 1 else 0

private fun getPiecesInEndLaneCount(gameState: GameState, player: EPlayer): Int {
    val path = Board.PATHS[player.index]
    return (12..13).count { gameState.getBox(path[it]) == player }
}