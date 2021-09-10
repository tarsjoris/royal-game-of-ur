package be.t_ars.ur

interface ISolver {
    fun getNextMove(board: Board, stepCount: Int): Loc?
}