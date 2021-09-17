package be.t_ars.ur

const val COLUMNS = 8
const val ROWS = 3
private const val COUNTER_OFFSET = COLUMNS * ROWS * 2
private const val COUNTER_BITS = 3

const val INITIAL_GAME_STATE: GameState = 63L shl COUNTER_OFFSET // 2 times 3 bits set

typealias GameState = Long

fun isMagicBox(loc: Loc) =
    ((loc.x == 0 || loc.x == 6) && (loc.y == 0 || loc.y == 2)) ||
            (loc.x == 3 && loc.y == 1)

fun isValidBox(loc: Loc) =
    loc.y == 1 || (loc.x != 4 && loc.x != 5)

fun GameState.getBox(loc: Loc): EPlayer? {
    val offset = (loc.y * COLUMNS + loc.x) * 2
    return when {
        isSet(offset) -> EPlayer.A
        isSet(offset + 1) -> EPlayer.B
        else -> null
    }
}

fun GameState.getUnusedCount(player: EPlayer): Int {
    val offset = getUnusedCounterOffset(player)
    return ((this shr offset) and 7L).toInt()
}

fun GameState.getFinishedCount(player: EPlayer): Int {
    val offset = getFinishedCounterOffset(player)
    return ((this shr offset) and 7L).toInt()
}

fun GameState.isValidTarget(player: EPlayer, target: Loc): Boolean {
    val currentToBox = getBox(target)
    return currentToBox != player && (currentToBox == null || !isMagicBox(target))
}

fun GameState.introducePiece(player: EPlayer, loc: Loc): GameState {
    val oldBox = getBox(loc)
    assert(oldBox != player)

    val stateWithBox = setBox(player, loc)
    val stateWithUnusedCounter = stateWithBox.decrement(getUnusedCounterOffset(player))
    return if (oldBox != null) stateWithUnusedCounter.capture(oldBox) else stateWithUnusedCounter
}

fun GameState.movePiece(player: EPlayer, from: Loc, to: Loc): GameState {
    val oldBox = getBox(to)
    assert(oldBox != player)

    val stateWithRemovedBox = clearBox(from)
    val stateWithMovedBox = stateWithRemovedBox.setBox(player, to)
    return if (oldBox != null) stateWithMovedBox.capture(oldBox) else stateWithMovedBox
}


fun GameState.finishPiece(player: EPlayer, loc: Loc): GameState {
    val stateWithBox = clearBox(loc)
    return stateWithBox.increment(getFinishedCounterOffset(player))
}

fun GameState.printGameState() {
    println()
    println(this)
    for (i in 0 until COUNTER_OFFSET) {
        print(if (isSet(i)) "1" else "0")
    }
    print("|")
    for (i in COUNTER_OFFSET until COUNTER_OFFSET + 4 * COUNTER_BITS) {
        print(if (isSet(i)) "1" else "0")
    }
    println()
    println("Unused count A: ${getUnusedCount(EPlayer.A)}")
    println("Finished count A: ${getFinishedCount(EPlayer.A)}")
    println("Unused count B: ${getUnusedCount(EPlayer.B)}")
    println("Finished count B: ${getFinishedCount(EPlayer.B)}")
    for (y in 0 until ROWS) {
        for (x in 0 until COLUMNS) {
            val loc = Loc(x, y)
            print(
                when (getBox(loc)) {
                    EPlayer.A -> "A"
                    EPlayer.B -> "B"
                    else -> if (isMagicBox(loc)) "*" else if (isValidBox(loc)) "." else " "
                }
            )
        }
        println()
    }
}

private fun GameState.isSet(index: Int): Boolean =
    this and (1L shl index) != 0L

private fun GameState.setBox(player: EPlayer, loc: Loc) =
    clearBox(loc) or ((if (player == EPlayer.A) 1L else 2L) shl ((loc.y * COLUMNS + loc.x) * 2))

private fun GameState.clearBox(loc: Loc) =
    this and (3L shl ((loc.y * COLUMNS + loc.x) * 2)).inv()

private fun getUnusedCounterOffset(player: EPlayer) =
    COUNTER_OFFSET + (if (player == EPlayer.A) 0 else COUNTER_BITS)

private fun getFinishedCounterOffset(player: EPlayer) =
    COUNTER_OFFSET + 2 * COUNTER_BITS + (if (player == EPlayer.A) 0 else COUNTER_BITS)

private fun GameState.increment(offset: Int) =
    this xor when {
        !isSet(offset) -> (1L shl offset)
        !isSet(offset + 1) -> (3L shl offset)
        else -> (7L shl offset)
    }


private fun GameState.decrement(offset: Int) =
    this xor when {
        isSet(offset) -> (1L shl offset)
        isSet(offset + 1) -> (3L shl offset)
        else -> (7L shl offset)
    }

private fun GameState.capture(player: EPlayer) =
    increment(getUnusedCounterOffset(player))
