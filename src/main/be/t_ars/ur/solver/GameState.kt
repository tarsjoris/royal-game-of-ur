package be.t_ars.ur.solver

import be.t_ars.ur.EPlayer
import be.t_ars.ur.Loc

private const val COLUMNS = 8
private const val ROWS = 3
private const val COUNTER_OFFSET = COLUMNS * ROWS * 2
private const val COUNTER_BITS = 3

const val INITIAL_GAMESTATE = 63L shl COUNTER_OFFSET // 2 times 3 bits set

fun isMagicBox(loc: Loc) =
    (loc.x == 0 || loc.x == 6) && (loc.y == 0 || loc.y == 2) ||
            loc.x == 3 && loc.y == 1

fun getBox(gameState: Long, loc: Loc): EPlayer {
    val offset = (loc.y * COLUMNS + loc.x) * 2
    return if (isSet(gameState, offset))
        if (isSet(gameState, offset + 1)) EPlayer.B else EPlayer.A
    else
        EPlayer.E
}

fun getUnusedCount(gameState: Long, player: EPlayer): Int {
    val offset = getUnusedCounterOffset(player)
    return (if (isSet(gameState, offset)) 4 else 0) +
            (if (isSet(gameState, offset + 1)) 2 else 0) +
            (if (isSet(gameState, offset + 2)) 1 else 0)
}

fun getFinishedCount(gameState: Long, player: EPlayer): Int {
    val offset = getFinishedCounterOffset(player)
    return (if (isSet(gameState, offset)) 4 else 0) +
            (if (isSet(gameState, offset + 1)) 2 else 0) +
            (if (isSet(gameState, offset + 2)) 1 else 0)
}

fun introducePiece(gameState: Long, player: EPlayer, loc: Loc): Long {
    val oldBox = getBox(gameState, loc)
    assert(oldBox != player)

    val stateWithBox = setBox(gameState, player, loc)
    val stateWithUnusedCounter = decrement(stateWithBox, getUnusedCounterOffset(player))
    return if (oldBox != EPlayer.E) capture(stateWithUnusedCounter, oldBox) else stateWithUnusedCounter
}

fun movePiece(gameState: Long, player: EPlayer, from: Loc, to: Loc): Long {
    val oldBox = getBox(gameState, to)
    assert(oldBox != player)

    val stateWithRemovedBox = clearBox(gameState, from)
    val stateWithMovedBox = setBox(stateWithRemovedBox, player, to)
    return if (oldBox != EPlayer.E) capture(stateWithMovedBox, oldBox) else stateWithMovedBox
}


fun finishPiece(gameState: Long, player: EPlayer, loc: Loc): Long {
    val stateWithBox = clearBox(gameState, loc)
    return increment(stateWithBox, getFinishedCounterOffset(player))
}

fun printGameState(gameState: Long) {
    println()
    println(gameState)
    for (i in 0 until COUNTER_OFFSET) {
        print(if (isSet(gameState, i)) "1" else "0")
    }
    print("|")
    for (i in COUNTER_OFFSET until COUNTER_OFFSET + 4 * COUNTER_BITS) {
        print(if (isSet(gameState, i)) "1" else "0")
    }
    println()
    println("Unused count A: ${getUnusedCount(gameState, EPlayer.A)}")
    println("Finished count A: ${getFinishedCount(gameState, EPlayer.A)}")
    println("Unused count B: ${getUnusedCount(gameState, EPlayer.B)}")
    println("Finished count B: ${getFinishedCount(gameState, EPlayer.B)}")
    for (y in 0 until ROWS) {
        for (x in 0 until COLUMNS) {
            val loc = Loc(x, y)
            print(
                when (getBox(gameState, loc)) {
                    EPlayer.A -> "A"
                    EPlayer.B -> "B"
                    else -> if (isMagicBox(loc)) "*" else if (isValidBox(loc)) "." else " "
                }
            )
        }
        println()
    }
}

private fun isValidBox(loc: Loc) =
    (loc.x != 4 && loc.x != 5) || loc.y == 1


private fun isSet(gameState: Long, index: Int): Boolean =
    gameState and (1L shl index) != 0L

private fun setBox(gameState: Long, player: EPlayer, loc: Loc) =
    gameState or ((if (player == EPlayer.A) 1L else 3L) shl ((loc.y * COLUMNS + loc.x) * 2))

private fun clearBox(gameState: Long, loc: Loc) =
    gameState and (3L shl ((loc.y * COLUMNS + loc.x) * 2)).inv()

private fun getUnusedCounterOffset(player: EPlayer) =
    COUNTER_OFFSET + (if (player == EPlayer.A) 0 else COUNTER_BITS)

private fun getFinishedCounterOffset(player: EPlayer) =
    COUNTER_OFFSET + 2 * COUNTER_BITS + (if (player == EPlayer.A) 0 else COUNTER_BITS)

private fun increment(gameState: Long, offset: Int) =
    gameState xor when {
        !isSet(gameState, offset + 2) -> (1L shl (offset + 2))
        !isSet(gameState, offset + 1) -> (3L shl (offset + 1))
        else -> (7L shl offset)
    }


private fun decrement(gameState: Long, offset: Int) =
    gameState xor when {
        isSet(gameState, offset + 2) -> (1L shl (offset + 2))
        isSet(gameState, offset + 1) -> (3L shl (offset + 1))
        else -> (7L shl offset)
    }

private fun capture(gameState: Long, player: EPlayer) =
    increment(gameState, getUnusedCounterOffset(player))
