package be.t_ars.ur.player

import be.t_ars.ur.EPlayer
import be.t_ars.ur.Loc
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class IntroduceTest {
    @Test
    fun testIntroduceOnePiece() {
        doTest(16607023625928705L, INITIAL_GAME_STATE, EPlayer.A, 0, 0)
        doTest(16607023625928708L, INITIAL_GAME_STATE, EPlayer.A, 1, 0)
        doTest(16607023625928720L, INITIAL_GAME_STATE, EPlayer.A, 2, 0)
        doTest(16607023625928768L, INITIAL_GAME_STATE, EPlayer.A, 3, 0)
        doTest(16607023625932800L, INITIAL_GAME_STATE, EPlayer.A, 6, 0)
        doTest(16607023625945088L, INITIAL_GAME_STATE, EPlayer.A, 7, 0)
        doTest(16607023625994240L, INITIAL_GAME_STATE, EPlayer.A, 0, 1)
        doTest(16607023626190848L, INITIAL_GAME_STATE, EPlayer.A, 1, 1)
        doTest(16607023626977280L, INITIAL_GAME_STATE, EPlayer.A, 2, 1)
        doTest(16607023630123008L, INITIAL_GAME_STATE, EPlayer.A, 3, 1)
        doTest(16607023642705920L, INITIAL_GAME_STATE, EPlayer.A, 4, 1)
        doTest(16607023693037568L, INITIAL_GAME_STATE, EPlayer.A, 5, 1)
        doTest(16607023894364160L, INITIAL_GAME_STATE, EPlayer.A, 6, 1)
        doTest(16607024699670528L, INITIAL_GAME_STATE, EPlayer.A, 7, 1)

        doTest(8725737162932224L, INITIAL_GAME_STATE, EPlayer.B, 0, 2)
        doTest(8725775817637888L, INITIAL_GAME_STATE, EPlayer.B, 1, 2)
        doTest(8725930436460544L, INITIAL_GAME_STATE, EPlayer.B, 2, 2)
        doTest(8726548911751168L, INITIAL_GAME_STATE, EPlayer.B, 3, 2)
        doTest(8778500836163584L, INITIAL_GAME_STATE, EPlayer.B, 6, 2)
        doTest(8936830510563328L, INITIAL_GAME_STATE, EPlayer.B, 7, 2)
        doTest(8725724278226944L, INITIAL_GAME_STATE, EPlayer.B, 0, 1)
        doTest(8725724278816768L, INITIAL_GAME_STATE, EPlayer.B, 1, 1)
        doTest(8725724281176064L, INITIAL_GAME_STATE, EPlayer.B, 2, 1)
        doTest(8725724290613248L, INITIAL_GAME_STATE, EPlayer.B, 3, 1)
        doTest(8725724328361984L, INITIAL_GAME_STATE, EPlayer.B, 4, 1)
        doTest(8725724479356928L, INITIAL_GAME_STATE, EPlayer.B, 5, 1)
        doTest(8725725083336704L, INITIAL_GAME_STATE, EPlayer.B, 6, 1)
        doTest(8725727499255808L, INITIAL_GAME_STATE, EPlayer.B, 7, 1)
    }

    @Test
    fun testIntroduceSevenPieces() {
        doTest(16607023625928705L, INITIAL_GAME_STATE, EPlayer.A, 0, 0)
        doTest(17169973579350021L, 16607023625928705L, EPlayer.A, 1, 0)
        doTest(16044073672507413L, 17169973579350021L, EPlayer.A, 2, 0)
        doTest(17451448556060757L, 16044073672507413L, EPlayer.A, 3, 0)
        doTest(16325548649222229L, 17451448556060757L, EPlayer.A, 6, 0)
        doTest(16888498602659925L, 16325548649222229L, EPlayer.A, 7, 0)
        doTest(15762598695882837L, 16888498602659925L, EPlayer.A, 0, 1)

        doTest(8725737162932224L, INITIAL_GAME_STATE, EPlayer.B, 0, 2)
        doTest(13229388329910272L, 8725737162932224L, EPlayer.B, 1, 2)
        doTest(4222395233599488L, 13229388329910272L, EPlayer.B, 2, 2)
        doTest(15482218935746560L, 4222395233599488L, EPlayer.B, 3, 2)
        doTest(6527796239138816L, 15482218935746560L, EPlayer.B, 6, 2)
        doTest(11242502099042304L, 6527796239138816L, EPlayer.B, 7, 2)
        doTest(2235302844497920L, 11242502099042304L, EPlayer.B, 0, 1)
    }

    private fun doTest(expectedGameState: Long, initialGameState: Long, player: EPlayer, x: Int, y: Int) {
        val actualGameState = initialGameState.introducePiece(player, Loc(x, y))
        actualGameState.printGameState()
        Assertions.assertEquals(expectedGameState, actualGameState)
    }
}