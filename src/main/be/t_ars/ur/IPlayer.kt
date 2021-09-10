package be.t_ars.ur

interface IPlayer {
    fun getNextMove(board: Board, stepCount: Int): Loc?
}