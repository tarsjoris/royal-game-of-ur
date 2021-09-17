package be.t_ars.ur.player

import be.t_ars.ur.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MiniMaxTest {
    @Test
    fun testPreferFinish() {
        //given
        var gameState = INITIAL_GAME_STATE
        gameState = gameState.introducePiece(EPlayer.A, Loc(0,0))
        gameState = gameState.movePiece(EPlayer.A, Loc(0, 0), Loc(3, 1))
        gameState = gameState.movePiece(EPlayer.A, Loc(3, 1), Loc(7, 1))
        // when
        val nextMove = ExpectiMiniMaxPlayer(EPlayer.A, 2).getNextMove(gameState, 3)
        // then
        assertEquals(Loc(7, 1), nextMove)
    }

    @Test
    fun testBugSkipNotAllowed1() {
        // given
        var gameState = INITIAL_GAME_STATE
        gameState = gameState.introducePiece(EPlayer.A, Loc(2, 0))
        gameState = gameState.introducePiece(EPlayer.A, Loc(1, 0))
        gameState = gameState.introducePiece(EPlayer.B, Loc(2, 0))
        gameState = gameState.introducePiece(EPlayer.B, Loc(6, 2))
        // when
        val nextMove = ExpectiMiniMaxPlayer(EPlayer.A, 2).getNextMove(gameState, 2)
        // then
        assertEquals(Loc(2, 0), nextMove)
    }

    @Test
    fun testBugSkipNotAllowed2() {
        // given
        var gameState = INITIAL_GAME_STATE
        gameState = gameState.introducePiece(EPlayer.A, Loc(3, 0))
        gameState = gameState.introducePiece(EPlayer.B, Loc(0, 1))
        // when
        val nextMove = ExpectiMiniMaxPlayer(EPlayer.A, 2).getNextMove(gameState, 1)
        // then
        assertEquals(Loc(3, 0), nextMove)
    }

    @Test
    fun testBestMove1() {
        // given
        var gameState = INITIAL_GAME_STATE
        gameState = gameState.introducePiece(EPlayer.A, Loc(2, 0))
        gameState = gameState.introducePiece(EPlayer.B, Loc(1, 0))
        gameState = gameState.introducePiece(EPlayer.A, Loc(0, 0))
        // when
        val nextMove = ExpectiMiniMaxPlayer(EPlayer.A, 2).getNextMove(gameState, 3)
        // then
        assertEquals(Board.START_LOC, nextMove)
        /*
        board.movePiece(EPlayer.A, Loc(0, 0), 3)
        board.movePiece(EPlayer.B, Loc(1, 2), 2)
        board.introducePiece(EPlayer.A, 4)
        board.movePiece(EPlayer.A, Loc(0, 0), 2)
        board.movePiece(EPlayer.B, Loc(0, 1), 4)
        board.movePiece(EPlayer.A, Loc(1, 1), 2)
        board.movePiece(EPlayer.B, Loc(0, 1), 4)
        */
    }
}