package be.t_ars.ur.player

import be.t_ars.ur.*

class DummyPlayer(private val player: EPlayer) : IPlayer {
    private val path = Board.PATHS[player.index]

    override fun getName() =
        "Dummy"

    override fun getNextMove(gameState: GameState, stepCount: Int): Loc? {
        for (fromPathIndex in path.size - stepCount downTo 0) {
            val fromLoc = path[fromPathIndex]
            if (gameState.getBox(fromLoc) == player) {
                val toPathIndex = fromPathIndex + stepCount
                if (toPathIndex == path.size) {
                    // finish
                    return fromLoc
                } else {
                    val toLoc = path[toPathIndex]
                    val toBox = gameState.getBox(toLoc)
                    if (toBox == null || (toBox != player && Board.BOXES[toLoc.y][toLoc.x] != EBox.S)) {
                        return fromLoc
                    }
                }
            }
        }
        if (gameState.getUnusedCount(player) > 0) {
            val toPathIndex = stepCount - 1
            val toLoc = path[toPathIndex]
            if (gameState.getBox(toLoc) == null) {
                return Board.START_LOC
            }
        }
        return null
    }
}