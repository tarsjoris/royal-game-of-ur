package be.t_ars.ur.player

import be.t_ars.ur.EPlayer
import be.t_ars.ur.Loc
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class FinishTest {
    @Test
    fun testFinishOnePiece() {
        doTest(88664617663856640L, 16607023625928705L, EPlayer.A, 0, 0)
        doTest(88664617663856640L, 16607023625928708L, EPlayer.A, 1, 0)
        doTest(88664617663856640L, 16607023625928720L, EPlayer.A, 2, 0)
        doTest(88664617663856640L, 16607023625928768L, EPlayer.A, 3, 0)
        doTest(88664617663856640L, 16607023625932800L, EPlayer.A, 6, 0)
        doTest(88664617663856640L, 16607023625945088L, EPlayer.A, 7, 0)
        doTest(88664617663856640L, 16607023625994240L, EPlayer.A, 0, 1)
        doTest(88664617663856640L, 16607023626190848L, EPlayer.A, 1, 1)
        doTest(88664617663856640L, 16607023626977280L, EPlayer.A, 2, 1)
        doTest(88664617663856640L, 16607023630123008L, EPlayer.A, 3, 1)
        doTest(88664617663856640L, 16607023642705920L, EPlayer.A, 4, 1)
        doTest(88664617663856640L, 16607023693037568L, EPlayer.A, 5, 1)
        doTest(88664617663856640L, 16607023894364160L, EPlayer.A, 6, 1)
        doTest(88664617663856640L, 16607024699670528L, EPlayer.A, 7, 1)

        doTest(585186476581453824L, 8725737162932224L, EPlayer.B, 0, 2)
        doTest(585186476581453824L, 8725775817637888L, EPlayer.B, 1, 2)
        doTest(585186476581453824L, 8725930436460544L, EPlayer.B, 2, 2)
        doTest(585186476581453824L, 8726548911751168L, EPlayer.B, 3, 2)
        doTest(585186476581453824L, 8778500836163584L, EPlayer.B, 6, 2)
        doTest(585186476581453824L, 8936830510563328L, EPlayer.B, 7, 2)
        doTest(585186476581453824L, 8725724278226944L, EPlayer.B, 0, 1)
        doTest(585186476581453824L, 8725724278816768L, EPlayer.B, 1, 1)
        doTest(585186476581453824L, 8725724281176064L, EPlayer.B, 2, 1)
        doTest(585186476581453824L, 8725724290613248L, EPlayer.B, 3, 1)
        doTest(585186476581453824L, 8725724328361984L, EPlayer.B, 4, 1)
        doTest(585186476581453824L, 8725724479356928L, EPlayer.B, 5, 1)
        doTest(585186476581453824L, 8725725083336704L, EPlayer.B, 6, 1)
        doTest(585186476581453824L, 8725727499255808L, EPlayer.B, 7, 1)
    }

    @Test
    fun testFinishSevenPieces() {
        doTest(87820192733810772L, 15762598695882837L, EPlayer.A, 0, 0)
        doTest(51791395714846800L, 87820192733810772L, EPlayer.A, 1, 0)
        doTest(123848989752774720L, 51791395714846800L, EPlayer.A, 2, 0)
        doTest(33776997205364736L, 123848989752774720L, EPlayer.A, 3, 0)
        doTest(105834591243288576L, 33776997205364736L, EPlayer.A, 6, 0)
        doTest(69805794224308224L, 105834591243288576L, EPlayer.A, 7, 0)
        doTest(141863388262170624L, 69805794224308224L, EPlayer.A, 0, 1)

        doTest(578696042263019520L, 2235302844497920L, EPlayer.B, 0, 2)
        doTest(290465614571700224L, 578696042263019520L, EPlayer.B, 1, 2)
        doTest(866926160716693504L, 290465614571700224L, EPlayer.B, 2, 2)
        doTest(146349395703693312L, 866926160716693504L, EPlayer.B, 3, 2)
        doTest(722757371448983552L, 146349395703693312L, EPlayer.B, 6, 2)
        doTest(434315889064738816L, 722757371448983552L, EPlayer.B, 7, 2)
        doTest(1010776641367965696L, 434315889064738816L, EPlayer.B, 0, 1)
    }

    private fun doTest(expectedGameState: Long, initialGameState: Long, player: EPlayer, x: Int, y: Int) {
        val actualGameState = initialGameState.finishPiece(player, Loc(x, y))
        actualGameState.printGameState()
        Assertions.assertEquals(expectedGameState, actualGameState)
    }
}