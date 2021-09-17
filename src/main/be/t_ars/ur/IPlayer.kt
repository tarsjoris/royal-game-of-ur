package be.t_ars.ur

interface IPlayer {
    fun getName(): String
    fun getNextMove(gameState: GameState, stepCount: Int): Loc?
}