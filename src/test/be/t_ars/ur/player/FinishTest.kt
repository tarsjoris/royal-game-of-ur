package be.t_ars.ur.player

import be.t_ars.ur.Board
import be.t_ars.ur.EPlayer
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class FinishTest {
    @Test
    fun testFinishOnePiece() {
        EPlayer.values().forEach { player ->
            // given
            val path = Board.PATHS[player.index]
            var gameState = INITIAL_GAME_STATE
            for (i in path.indices) {
                gameState = gameState.introducePiece(player, path[i])
            }
            gameState.printGameState()
            for (i in path.indices) {
                // when
                val newGameState = gameState.finishPiece(player, path[i])
                gameState.printGameState()
                // then
                assertNull(newGameState.getBox(path[i]))
                assertEquals(1, newGameState.getFinishedCount(player))
            }
        }
    }

    @Test
    fun testFinishSevenPieces() {
        EPlayer.values().forEach { player ->
            // given
            val path = Board.PATHS[player.index]
            var gameState = INITIAL_GAME_STATE
            for (i in 0..6) {
                gameState = gameState.introducePiece(player, path[i])
            }
            gameState.printGameState()
            assertEquals(0, gameState.getFinishedCount(player))
            for (i in 0..6) {
                // when
                gameState = gameState.finishPiece(player, path[i])
                gameState.printGameState()
                // then
                assertNull(gameState.getBox(path[i]))
                assertEquals(i + 1, gameState.getFinishedCount(player))
            }
        }
    }
}