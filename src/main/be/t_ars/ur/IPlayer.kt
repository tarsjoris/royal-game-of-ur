package be.t_ars.ur

interface IPlayer {
    fun getName(): String
    fun getNextMove(board: Board, stepCount: Int): Loc?
}