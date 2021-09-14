package be.t_ars.ur.player

import be.t_ars.ur.Board
import be.t_ars.ur.EPlayer
import be.t_ars.ur.Loc
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MiniMaxTest {
    @Test
    fun testPreferFinish() {
        //given
        val board = Board()
        board.introducePiece(EPlayer.A, 4)
        board.movePiece(EPlayer.A, Loc(0, 0), 4)
        board.movePiece(EPlayer.A, Loc(3, 1), 4)
        // when
        val nextMove = MiniMaxPlayer(EPlayer.A).getNextMove(board, 3)
        // then
        assertEquals(Loc(7, 1), nextMove)
    }

    @Test
    fun testBugSkipNotAllowed1() {
        // given
        val board = Board()
        board.introducePiece(EPlayer.A, 2)
        board.introducePiece(EPlayer.A, 3)
        board.introducePiece(EPlayer.B, 2)
        board.introducePiece(EPlayer.B, 14)
        // when
        val nextMove = MiniMaxPlayer(EPlayer.A).getNextMove(board, 2)
        // then
        assertEquals(Loc(2, 0), nextMove)
    }

    @Test
    fun testBugSkipNotAllowed2() {
        // given
        val board = Board()
        board.introducePiece(EPlayer.A, 1)
        board.introducePiece(EPlayer.B, 5)
        // when
        val nextMove = MiniMaxPlayer(EPlayer.A).getNextMove(board, 1)
        // then
        assertEquals(Loc(3, 0), nextMove)
    }

    @Test
    fun testBestMove1() {
        // given
        val board = Board()
        board.introducePiece(EPlayer.A, 2)
        board.introducePiece(EPlayer.B, 3)
        board.introducePiece(EPlayer.A, 4)
        // when
        val nextMove = MiniMaxPlayer(EPlayer.A).getNextMove(board, 3)
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