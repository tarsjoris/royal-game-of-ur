package be.t_ars.ur.player

import be.t_ars.ur.Board
import be.t_ars.ur.EPlayer
import be.t_ars.ur.Loc
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class IntroduceTest {
    @Test
    fun testIntroduceOnePiece() {
        EPlayer.values().forEach { player ->
            // given
            val path = Board.PATHS[player.index]
            for (i in path.indices) {
                // when
                val newGameState = INITIAL_GAME_STATE.introducePiece(player, path[i])
                newGameState.printGameState()
                // then
                assertEquals(player, newGameState.getBox(path[i]))
                assertEquals(6, newGameState.getUnusedCount(player))
            }
        }
    }

    @Test
    fun testIntroduceSevenPieces() {
        EPlayer.values().forEach { player ->
            // given
            val path = Board.PATHS[player.index]
            var gameState = INITIAL_GAME_STATE
            for (i in 0..6) {
                // when
                gameState = gameState.introducePiece(player, path[i])
                gameState.printGameState()
                // then
                assertEquals(player, gameState.getBox(path[i]))
                assertEquals(6 - i, gameState.getUnusedCount(player))
            }
        }
    }
}