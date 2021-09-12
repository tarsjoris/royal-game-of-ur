package be.t_ars.ur.player

import be.t_ars.ur.*

class DummyPlayer(private val player: EPlayer) : IPlayer {
    private val path = Board.PATHS[player.index]

    override fun getName() =
        "Dummy"

    override fun getNextMove(board: Board, stepCount: Int): Loc? {
        for (fromPathIndex in path.size - stepCount downTo 0) {
            val fromLoc = path[fromPathIndex]
            if (board.state[fromLoc.y][fromLoc.x] == player) {
                val toPathIndex = fromPathIndex + stepCount
                if (toPathIndex == path.size) {
                    // finish
                    return fromLoc
                } else {
                    val toLoc = path[toPathIndex]
                    val toBox = board.state[toLoc.y][toLoc.x]
                    if (toBox == null || (toBox != player && Board.BOXES[toLoc.y][toLoc.x] != EBox.S)) {
                        return fromLoc
                    }
                }
            }
        }
        if (board.available[player.index] > 0) {
            val toPathIndex = stepCount - 1
            val toLoc = path[toPathIndex]
            board.state[toLoc.y][toLoc.x] ?: return Board.START_LOC
        }
        return null
    }
}