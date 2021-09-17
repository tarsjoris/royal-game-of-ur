package be.t_ars.ur.player

import be.t_ars.ur.*
import kotlin.random.Random

private val playerRandom = Random(System.currentTimeMillis())

class RandomPlayer(private val player: EPlayer) : IPlayer {
    private val path = Board.PATHS[player.index]

    override fun getName() =
        "Random"

    override fun getNextMove(gameState: GameState, stepCount: Int): Loc? {
        val moves = mutableListOf<Loc>()
        for (fromPathIndex in path.size - stepCount downTo 0) {
            val fromLoc = path[fromPathIndex]
            if (gameState.getBox(fromLoc) == player) {
                val toPathIndex = fromPathIndex + stepCount
                if (toPathIndex == path.size) {
                    // finish
                    moves.add(fromLoc)
                } else {
                    val toLoc = path[toPathIndex]
                    val toBox = gameState.getBox(toLoc)
                    if (toBox == null || (toBox != player && Board.BOXES[toLoc.y][toLoc.x] != EBox.S)) {
                        moves.add(fromLoc)
                    }
                }
            }
        }
        if (gameState.getUnusedCount(player) > 0) {
            val toPathIndex = stepCount - 1
            val toLoc = path[toPathIndex]
            val toBox = gameState.getBox(toLoc)
            if (toBox == null) {
                moves.add(Board.START_LOC)
            }
        }
        return if (moves.isEmpty()) null else moves[playerRandom.nextInt(moves.size)]
    }
}