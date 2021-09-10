package be.t_ars.ur.ui

import be.t_ars.ur.Board
import be.t_ars.ur.EPlayer
import be.t_ars.ur.nextRoll
import be.t_ars.ur.solver.DummySolver
import java.awt.BorderLayout
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JFrame
import javax.swing.SwingUtilities
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.system.exitProcess

class MainFrame(board: Board) : JFrame() {
    private val boardPanel = BoardPanel(board)

    init {
        layout = BorderLayout()
        add(boardPanel, BorderLayout.CENTER)
        setSize(800, 300)
        setLocation(100, 100)
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                exitProcess(0)
            }
        })
    }

    fun setDiceRoll(diceRoll: Int?, currentPlayer: EPlayer) {
        boardPanel.diceRoll = diceRoll
        boardPanel.currentPlayer = currentPlayer
        update()
    }

    fun update() {
        SwingUtilities.invokeLater(boardPanel::repaint)
    }
}