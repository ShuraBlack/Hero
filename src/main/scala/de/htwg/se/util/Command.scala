package de.htwg.se.util

import de.htwg.se.controller.controllerComponent.ControllerInterface
import de.htwg.se.model.boardComponent.BoardInterface

/**
 * @author Ronny Klotz & Alina Göttig
 * @since 06.Dez.2020
 */

trait Command {
    def doStep:Unit
    def changeStep(board: BoardInterface):Unit
}

class SetCommand(Command: Vector[String], controller: ControllerInterface) extends Command {
    override def doStep: Unit = {
        val current = controller.intpos(controller.board.currentcreature.name)

        if (Command.head.equals("m")) {
            controller.move(current(0), current(1), Command(2).toInt, Command(1).toInt)
        } else if (Command.head.equals("a")) {
            controller.attack(current(0), current(1), Command(2).toInt, Command(1).toInt)
        }

    }
    override def changeStep(board: BoardInterface): Unit = controller.board = board
}

//noinspection ScalaStyle
class UndoManager {

    private var boarddo: List[(BoardInterface,Command)] = List()
    private var boardre: List[(BoardInterface,Command)] = List()

    def doStep(command: Command, board : BoardInterface): Unit = {
        boarddo = boarddo :+ (board,command)
        command.doStep
    }

    def undoStep(controller: ControllerInterface): Unit = {
        boarddo match {
            case  Nil =>
            case head:: _ =>
                val board = boarddo.last
                boardre = boardre :+ (controller.board,head._2)
                boarddo = boarddo.dropRight(1)
                controller.board = board._1
                head._2.changeStep(board._1)
        }

    }

    def redoStep(controller: ControllerInterface): Unit = {
        boardre match {
            case  Nil =>
            case head:: _ =>
                val board = boardre.last
                boarddo = boarddo :+ (controller.board,head._2)
                boardre = boardre.dropRight(1)
                controller.board = board._1
                head._2.changeStep(board._1)
        }

    }
}



