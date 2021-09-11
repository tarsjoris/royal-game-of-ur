package be.t_ars.ur.player

import be.t_ars.ur.*
import kotlin.random.Random

private val playerRandom = Random(System.currentTimeMillis())

class RandomPlayer(private val player: EPlayer) : IPlayer {
    private val path = Board.PATHS[player.index]

    override fun getName() =
        "Random"

    override fun getNextMove(board: Board, stepCount: Int): Loc? {
        val moves = mutableListOf<Loc>()
        for (fromPos in path.size - stepCount downTo 0) {
            val fromLoc = path[fromPos]
            if (board.state[fromLoc.y][fromLoc.x] == player) {
                val toPos = fromPos + stepCount
                if (toPos == path.size) {
                    // finish
                    moves.add(fromLoc)
                } else {
                    val toLoc = path[toPos]
                    val toBox = board.state[toLoc.y][toLoc.x]
                    if (toBox == EPlayer.E || (toBox != player && Board.BOXES[toLoc.y][toLoc.x] != EBox.S)) {
                        moves.add(fromLoc)
                    }
                }
            }
        }
        if (board.available[player.index] > 0) {
            val toPos = stepCount - 1
            val toLoc = path[toPos]
            val toBox = board.state[toLoc.y][toLoc.x]
            if (toBox == EPlayer.E) {
                moves.add(Board.START_LOC)
            }
        }
        return if (moves.isEmpty()) null else moves[playerRandom.nextInt(moves.size)]
    }
}