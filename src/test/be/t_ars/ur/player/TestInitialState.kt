package be.t_ars.ur.player

import be.t_ars.ur.Board
import be.t_ars.ur.EPlayer
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TestInitialState {
    @Test
    fun testInitialState() {
        EPlayer.values().forEach { player ->
            val path = Board.PATHS[player.index]
            for (i in path.indices) {
                assertNull(INITIAL_GAME_STATE.getBox(path[i]))
            }
            assertEquals(7, INITIAL_GAME_STATE.getUnusedCount(player))
            assertEquals(0, INITIAL_GAME_STATE.getFinishedCount(player))
        }
    }
}