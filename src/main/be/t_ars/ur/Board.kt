package be.t_ars.ur

import kotlin.random.Random

enum class EBox {
    V, // Void
    N, // Normal
    S, // Star
}

enum class EPlayer(val index: Int, val label: String) {
    E(-1, ""), // Empty
    A(0, "A"),
    B(1, "B"),
}

val EYE_COUNT = arrayOf(
    0,
    1,
    1, 2,
    1, 2, 2, 3,
    1, 2, 2, 3, 2, 3, 3, 4
)

fun nextRoll() =
    EYE_COUNT[(Random.nextFloat() * 16F).toInt()]

class Board {
    val state = Array(3) {
        Array(8) {
            EPlayer.E
        }
    }
    var available = arrayOf(PIECES, PIECES)
    var finished = arrayOf(0, 0)
    var winner: EPlayer? = null

    fun introducePiece(player: EPlayer, steps: Int) =
        move(player, START_LOC, steps)

    /**
     * @return true when landed on a star
     */
    fun move(player: EPlayer, fromLoc: Loc, steps: Int): Boolean {
        val otherPlayer = if (player == EPlayer.A) EPlayer.B else EPlayer.A
        val path = PATHS[player.index]
        if (fromLoc == START_LOC) {
            // introduce piece
            val toPosInPath = steps - 1
            assert(toPosInPath in path.indices)
            val toPos = path[toPosInPath]
            val toBox = BOXES[toPos.y][toPos.x]
            assert(toBox != EBox.V)
            val toState = state[toPos.y][toPos.x]
            assert(toState == EPlayer.E)

            --available[player.index]
            state[toPos.y][toPos.x] = player
            return toBox == EBox.S
        } else {
            assert(fromLoc.x in 0 until WIDTH)
            assert(fromLoc.y in 0 until HEIGHT)
            assert(state[fromLoc.y][fromLoc.x] == player)
            val fromPosInPath = POS_IN_PATH[fromLoc.y][fromLoc.x]
            assert(fromPosInPath != null)
            if (fromPosInPath != null) {
                val fromPos = path[fromPosInPath]
                assert(fromPos.x == fromLoc.x)
                assert(fromPos.y == fromLoc.y)
                val toPosInPath = fromPosInPath + steps
                if (toPosInPath == path.size) {
                    // finish
                    state[fromLoc.y][fromLoc.x] = EPlayer.E
                    if (++finished[player.index] == PIECES) {
                        winner = player
                    }
                } else {
                    // plain move
                    assert(toPosInPath in path.indices)
                    val toPos = path[toPosInPath]
                    val toBox = BOXES[toPos.y][toPos.x]
                    assert(toBox != EBox.V)
                    val toState = state[toPos.y][toPos.x]
                    assert(toState == EPlayer.E || (toState == otherPlayer && toBox != EBox.S))

                    state[fromLoc.y][fromLoc.x] = EPlayer.E
                    if (toState == otherPlayer) {
                        ++available[otherPlayer.index]
                    }
                    state[toPos.y][toPos.x] = player
                    return toBox == EBox.S
                }
            }
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