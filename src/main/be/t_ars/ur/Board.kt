package be.t_ars.ur

import kotlin.random.Random

enum class EBox {
    V, // Void
    N, // Normal
    S, // Star
}

enum class EPlayer(val index: Int, val label: String) {
    A(0, "A"),
    B(1, "B");

    fun otherPlayer() =
        if (this == A) B else A

    override fun toString() =
        label
}

data class Loc(val x: Int, val y: Int) {
    override fun toString() =
        "($x, $y)"
}

val EYE_COUNT = arrayOf(
    0,
    1,
    1, 2,
    1, 2, 2, 3,
    1, 2, 2, 3, 2, 3, 3, 4
)

private val rollRandom = Random(System.currentTimeMillis())

fun nextRoll() =
    EYE_COUNT[rollRandom.nextInt(16)]

class InvalidMoveException(message: String) : Exception(message)

class Board {
    var gameState: GameState = INITIAL_GAME_STATE
    var winner: EPlayer? = null

    fun skipTurn(player: EPlayer, steps: Int): Boolean {
        val otherPlayer = player.otherPlayer()
        val path = PATHS[player.index]
        if (gameState.getUnusedCount(player) > 0) {
            val toIndexInPath = steps - 1
            assert(toIndexInPath in path.indices)
            val toLoc = path[toIndexInPath]
            if (gameState.getBox(toLoc) == null) throw InvalidMoveException("Not allowed to skip because a new piece can be introduced")
        }
        for (fromIndexInPath in path.indices) {
            val fromLoc = path[fromIndexInPath]
            if (gameState.getBox(fromLoc) == player) {
                val toIndexInPath = fromIndexInPath + steps
                if (toIndexInPath in path.indices) {
                    val toLoc = path[toIndexInPath]
                    val toState = gameState.getBox(toLoc)
                    if (toState == null || (toState == otherPlayer && BOXES[toLoc.y][toLoc.x] != EBox.S))
                        throw InvalidMoveException("Not allowed to skip because the piece at $fromLoc can be moved")
                }
            }
        }
        return false
    }

    fun introducePiece(player: EPlayer, steps: Int): Boolean {
        if (gameState.getUnusedCount(player) <= 0) throw InvalidMoveException("Can't introduce piece because player ${player.label} has no pieces left")
        val path = PATHS[player.index]
        val toIndexInPath = steps - 1
        assert(toIndexInPath in path.indices)
        val toLoc = path[toIndexInPath]
        val toBox = BOXES[toLoc.y][toLoc.x]
        assert(toBox != EBox.V)
        if (gameState.getBox(toLoc) != null) throw InvalidMoveException("Can't introduce piece at $toLoc because it is occupied by ${gameState.getBox(toLoc)}")

        gameState = gameState.introducePiece(player, toLoc)
        return toBox == EBox.S
    }

    /**
     * @return true when landed on a star
     */
    fun movePiece(player: EPlayer, fromLoc: Loc, steps: Int): Boolean {
        val otherPlayer = player.otherPlayer()
        val path = PATHS[player.index]
        if (fromLoc.x !in 0 until WIDTH || fromLoc.y !in 0 until HEIGHT) {
            throw InvalidMoveException("Can't move piece at $fromLoc because it is off the board")
        }
        val fromIndexInPath = POS_IN_PATH[fromLoc.y][fromLoc.x]
            ?: throw InvalidMoveException("Can't move piece at $fromLoc because it is off the board")
        if (gameState.getBox(fromLoc) != player) {
            if (gameState.getBox(fromLoc) == null) {
                throw InvalidMoveException("There is no piece to move at $fromLoc")
            } else {
                throw InvalidMoveException("Can't move piece at $fromLoc because it belongs to the other player ($otherPlayer)")
            }
        }

        val fromPos = path[fromIndexInPath]
        assert(fromPos.x == fromLoc.x)
        assert(fromPos.y == fromLoc.y)
        val toIndexInPath = fromIndexInPath + steps
        if (toIndexInPath == path.size) {
            // finish
            gameState = gameState.finishPiece(player, fromLoc)
            if (gameState.getFinishedCount(player) == PIECES) {
                winner = player
            }
        } else {
            // plain move
            if (toIndexInPath !in path.indices) throw InvalidMoveException("Can't move piece at $fromLoc because it can't do $steps steps")
            val toLoc = path[toIndexInPath]
            val toBox = BOXES[toLoc.y][toLoc.x]
            assert(toBox != EBox.V)
            val toState = gameState.getBox(toLoc)
            if (toState == player)
                throw InvalidMoveException("Can't move piece to $toLoc because it is occupied by the same player")
            if (toState == otherPlayer && toBox == EBox.S)
                throw InvalidMoveException("Can't move piece to $toLoc because it is protected")

            gameState = gameState.movePiece(player, fromLoc, toLoc)
            return toBox == EBox.S
        }
        return false
    }

    companion object {
        val BOXES = arrayOf(
            arrayOf(EBox.S, EBox.N, EBox.N, EBox.N, EBox.V, EBox.V, EBox.S, EBox.N),
            arrayOf(EBox.N, EBox.N, EBox.N, EBox.S, EBox.N, EBox.N, EBox.N, EBox.N),
            arrayOf(EBox.S, EBox.N, EBox.N, EBox.N, EBox.V, EBox.V, EBox.S, EBox.N),
        )

        const val WIDTH = 8
        const val HEIGHT = 3
        private const val PIECES = 7

        val START_LOC = Loc(-1, -1)

        private val POS_IN_PATH = arrayOf(
            arrayOf(3, 2, 1, 0, null, null, 13, 12),
            arrayOf(4, 5, 6, 7, 8, 9, 10, 11),
            arrayOf(3, 2, 1, 0, null, null, 13, 12),
        )

        val PATHS = arrayOf(
            arrayOf(
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
            ),
            arrayOf(
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
        )
    }
}