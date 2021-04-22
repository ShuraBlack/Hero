package de.htwg.se.aview

/**
 * @author Ronny Klotz & Alina Göttig
 * @since 16.Dez.2020
 */

import de.htwg.se.util._

import scala.swing._
import scala.swing.event._
import java.awt.Color
import java.nio.file.Paths
import java.util.logging.LogManager

import com.malliina.audio.javasound.FileJavaSoundPlayer
import de.htwg.se.controller.controllerComponent.ControllerInterface
import de.htwg.se.model.boardComponent.CellInterface
import javax.swing.ImageIcon
import javax.swing.border.EmptyBorder

class GUI(controller: ControllerInterface) extends Frame with Observer {

    controller.add(this)
    // ------------------------------------------------- Frame Setup ---------------------------------------------------
    minimumSize = new Dimension(1920,1080)
    preferredSize = new Dimension(1920,1080)
    maximumSize = new Dimension(1920,1080)
    resizable = false

    title = "HERO"
    iconImage = toolkit.getImage("src/main/scala/de/htwg/se/aview/Graphics/UI/Icon.png")

    // -------------------------------------------------- Main Menu ----------------------------------------------------
    def mainmenu(): Unit = {

        //Music
        LogManager.getLogManager.reset()
        val file = Paths get "src/main/scala/de/htwg/se/aview/Audio/Menu.mp3"
        val player: FileJavaSoundPlayer = new FileJavaSoundPlayer(file)
        player.volume = 30
        player.play()

        contents = new ImagePanel() {
            imagePath = "src/main/scala/de/htwg/se/aview/Graphics/UI/Menubackground.png"

            contents += new BoxPanel(Orientation.Vertical) {

                opaque = false
                border = new EmptyBorder(0, 450, 20, 0)

                contents += new Label() {
                    opaque = false
                    icon = new ImageIcon("src/main/scala/de/htwg/se/aview/Graphics/UI/Font.png")
                }

                contents += new BoxPanel(Orientation.Vertical) {

                    opaque = false
                    border = new EmptyBorder(20, 270, 20, 0)

                    contents += new ShortButton("New Game",30,new Color(200, 200, 200))(true)(true) {
                        icon = new ImageIcon("src/main/scala/de/htwg/se/aview/Graphics/UI/Buttonframe.png")
                        reactions += {
                            case ButtonClicked(_) =>
                                player.stop()
                                controller.gamestate = "gamerun"
                                controller.notifyObservers
                        }

                    }

                    contents += new ShortButton("Load Game",30,new Color(200, 200, 200))(true)(true) {
                        icon = new ImageIcon("src/main/scala/de/htwg/se/aview/Graphics/UI/Buttonframe.png")
                        if (!scala.reflect.io.File("HeroSave.json").exists && !scala.reflect.io.File("HeroSave.xml").exists) {
                            foreground = new Color(30,30,30)
                        }
                        reactions += {
                            case ButtonClicked(_) =>
                                if (controller.load()) {
                                    player.stop()
                                    controller.gamestate = "gamerun"
                                    controller.prediction()
                                    controller.notifyObservers
                                } else { foreground = new Color(30,30,30) }
                        }
                    }

                    contents += new ShortButton("Credit",30,new Color(200, 200, 200))(true)(true) {
                        icon = new ImageIcon("src/main/scala/de/htwg/se/aview/Graphics/UI/Buttonframe.png")
                        reactions += {
                            case ButtonClicked(_) =>
                                controller.gamestate = "credit"
                                controller.notifyObservers
                        }
                    }


                    contents += new ShortButton("Exit",30,new Color(200, 200, 200))(true)(true) {
                        icon = new ImageIcon("src/main/scala/de/htwg/se/aview/Graphics/UI/Buttonframe.png")
                        reactions += {
                            case ButtonClicked(_) =>
                                System.exit(1)
                        }
                    }

                    contents += new Label("Version: 2.5 Docker capable") {
                        border = new EmptyBorder(0,155,0,0)
                        foreground = Color.WHITE
                    }

                }
            }

        }
    }

    // ------------------------------------------------ Menu->Credit ---------------------------------------------------

    def credit(): Unit = {
        contents = new ImagePanel() {
            imagePath = "src/main/scala/de/htwg/se/aview/Graphics/UI/Menubackground.png"
            contents += new BorderPanel {
                opaque = false

                val top: Label = new ShortLabel("Credit",30,new Color(200, 200, 200))(true) {
                    icon = new ImageIcon("src/main/scala/de/htwg/se/aview/Graphics/UI/Buttonframe.png")
                }
                add(top,BorderPanel.Position.North)

                val middle: BoxPanel = new BoxPanel(Orientation.Vertical) {

                    border = new EmptyBorder(50,300,0,300)
                    opaque = false

                    contents += new ShortLabel("<html><center>Code written</center></html>"
                        ,30,Color.WHITE)(false)

                    contents += new ShortLabel("<html><center>Alina Göttig and Ronny Klotz</center></html>"
                        ,20,Color.WHITE)(false) {
                        border = new EmptyBorder(0,0,100,0)
                    }

                    contents += new ShortLabel("<html><center>Graphic design</center></html>"
                        ,30,Color.WHITE)(false)

                    contents += new ShortLabel("<html><center>Ronny Klotz</center></html>"
                        ,20,Color.WHITE)(false) {
                        border = new EmptyBorder(0,0,100,0)
                    }

                    contents += new ShortLabel("<html><center>Programs and Language</center></html>"
                        ,30,Color.WHITE)(false)

                    contents += new Label() {
                        foreground = Color.WHITE
                        text = "<html><center>IDE: IntelliJ IDEA Community Edition<br>Scala: 2.13.3" +
                            "<br>Java JDK: 1.8.0<br>Sbt: 1.4.5<br><br>Adobe Photoshop CC 2019<br>Marmoset Hexels" +
                            "<br><br>Music:<br>Rob King & Paul Romero<br>Originial Heroes of Might & Magic III Album" +
                            "<br><br><br>Project for Software Engineering at HTWG Konstanz AIN</center></html>"
                        font = new Font("Arial", 1, 20)
                    }

                }
                add(middle,BorderPanel.Position.Center)

                val bottom: Button = new ShortButton("Back",30,new Color(200, 200, 200))(true)(true) {
                    icon = new ImageIcon("src/main/scala/de/htwg/se/aview/Graphics/UI/Buttonframe.png")

                    reactions += {
                        case ButtonClicked(_) =>
                            controller.gamestate = "mainmenu"
                            controller.notifyObservers
                    }
                }
                add(bottom,BorderPanel.Position.South)
            }

        }

    }

    // ------------------------------------------------- Menu->Game ----------------------------------------------------

    def gamerun(): Unit = {


        //Music
        val file = Paths get "src/main/scala/de/htwg/se/aview/Audio/Combat.mp3"
        val player = new FileJavaSoundPlayer(file)
        player.volume = 25
        player.play()

        contents = new ImagePanel {
            imagePath = "src/main/scala/de/htwg/se/aview/Graphics/UI/Background.png"
            contents += new BoxPanel(Orientation.Vertical) {
                opaque = false
                contents += new BoxPanel(Orientation.Vertical) {
                    border = new EmptyBorder(46, 82, 0, 40)
                    opaque = false

                    contents += new BoxPanel(Orientation.Vertical) {
                        opaque = false
                        for {i <- 0 to 10} {
                            contents += new BoxPanel(Orientation.Horizontal) {
                                opaque = false
                                for {j <- 0 to 14} {
                                    contents += new CoorButton(j, i, controller.getCreature(controller.board.field, i, j), controller) {

                                        opaque = false
                                        contentAreaFilled = false
                                        borderPainted = false
                                        focusPainted = false
                                        foreground = Color.WHITE

                                        minimumSize = new Dimension(119, 85)
                                        maximumSize = new Dimension(119, 85)
                                        preferredSize = new Dimension(119, 85)

                                        val cell: CellInterface = controller.getCreature(controller.board.field, i, j)

                                        if (!cell.player.name.equals("none")) {
                                            if (cell.name.equals(controller.board.currentcreature.name)) {
                                                icon = loadcellimage(cellname)(2)
                                            } else if (controller.checkattack(Vector("m", j.toString, i.toString))) {
                                                icon = loadcellimage(cellname)(3)
                                            } else {
                                                icon = loadcellimage(cellname)(1)
                                            }
                                        } else {
                                            if (cell.name.equals(" _ ")) {
                                                icon = new ImageIcon("src/main/scala/de/htwg/se/aview/Graphics/UI/Marker.png")
                                            }
                                        }

                                    }
                                }
                            }
                        }
                    }

                    contents += new BorderPanel {
                        opaque = false

                        val info = controller.board.currentplayer.name + ": " +
                            controller.board.realname(controller.board.currentcreature.name.replace("_",""))
                        add(new ShortLabel(info,30,Color.WHITE)(false) {
                            border = new EmptyBorder(10,70,0,0)
                        }, BorderPanel.Position.West)

                        val log: TextField = new TextField() {
                            opaque = false
                            foreground = Color.WHITE
                            font = new Font("Arial", 1, 20)
                            border = new EmptyBorder(10,300,0,180)
                            columns = 25
                            reactions += {
                                case EditDone(_) =>
                                    if (text.contains("CHEAT") && (text.contains("coconuts") || text.contains("godunit")
                                        || text.contains("feedcreature") || text.contains("handofjustice"))) {
                                        val split = text.split(" ")
                                        if (split.length == 2) {
                                            val code = Vector(split(0),split(1))
                                            controller.cheatCode(code)
                                            controller.notifyObservers
                                        }
                                    }
                            }
                        }

                        val boardlog: List[String] = controller.board.log
                        log.text = if (boardlog.isEmpty) "" else boardlog.last.replace("_","")
                        add(log,BorderPanel.Position.Center)
                        listenTo(log)


                        val passpanel: FlowPanel = new FlowPanel() {
                            preferredSize = new Dimension(300,100)
                            opaque = false
                            border = new EmptyBorder(15,15,0,30)
                            contents += new ShortButton("Pass",30,Color.WHITE)(true)(false) {
                                reactions += {
                                    case ButtonClicked(_) =>
                                        if (controller.winner().isDefined){
                                            controller.gamestate = "finished"
                                        } else {
                                            controller.next()
                                            controller.prediction()
                                        }
                                        controller.notifyObservers
                                }
                            }
                            contents += new ShortButton("Save",30,Color.WHITE)(true)(false) {
                                reactions += {
                                    case ButtonClicked(_) =>
                                        controller.save()
                                        log.text = "Currently running game got saved!"
                                }
                            }
                        }

                        add(passpanel, BorderPanel.Position.East)

                    }

                }
            }
        }
    }

    // ---------------------------------------------- Game->Scoreboard -------------------------------------------------

    def scoreboard(): Unit = {
        contents = new ImagePanel() {
            imagePath = "src/main/scala/de/htwg/se/aview/Graphics/UI/Menubackground.png"

            contents += new BorderPanel() {
                opaque = false
                border = new EmptyBorder(100, 750, 100, 0)

                layout(new BoxPanel(Orientation.Vertical) {
                    opaque = false
                    contents += new ShortLabel("WINNER:",30,Color.WHITE)(true) {
                        border = new EmptyBorder(0, 150, 0, 0)
                    }

                    contents += new ShortLabel("",30,new Color(230, 200, 130))(true) {
                        controller.winner() match {
                            case Some(value) =>
                                if (value == 1) text = "Castle" else text = "Inferno"
                            case None => text = "Winner not found"
                        }
                        icon = new ImageIcon("src/main/scala/de/htwg/se/aview/Graphics/UI/Buttonframe.png")
                    }
                }) = BorderPanel.Position.North

                add(new BoxPanel(Orientation.Horizontal) {
                    opaque = false
                    border = new EmptyBorder(20, 0, 0, 0)

                    contents += new BoxPanel(Orientation.Vertical) {
                        opaque = false
                        border = new EmptyBorder(20, 0, 0, 20)

                        contents += new ShortLabel("",30,Color.WHITE)(false) {
                            controller.winner() match {
                                case Some(_) => text = "Name:"
                                case None => text = "Error: Winner not found"
                            }
                        }

                        for (cell <- controller.winnercreatures) {
                            val name = controller.board.realname(cell.name)
                            contents += new ShortLabel(name,30,Color.WHITE)(false)
                        }

                    }

                    contents += new BoxPanel(Orientation.Vertical) {
                        opaque = false
                        border = new EmptyBorder(20, 0, 0, 20)

                        contents += new ShortLabel("",30,Color.WHITE)(true) {
                            controller.winner() match {
                                case Some(_) => text = "Multiplier:"
                                case None => text = "Error: Winner not found"
                            }
                            horizontalAlignment = Alignment.Center
                        }

                        for (cell <- controller.winnercreatures) {
                            val multi = cell.multiplier
                            contents += new ShortLabel(multi.toString,30,Color.WHITE)(false)
                        }

                    }

                    contents += new BoxPanel(Orientation.Vertical) {
                        opaque = false
                        border = new EmptyBorder(20, 0, 0, 20)

                        contents += new ShortLabel("",30,Color.WHITE)(true) {
                            controller.winner() match {
                                case Some(_) => text = "Health:"
                                case None => text = "Error: Winner not found"
                            }
                            horizontalAlignment = Alignment.Center
                            horizontalTextPosition = Alignment.Center
                        }

                        for (cell <- controller.winnercreatures) {
                            val hp = cell.hp
                            contents += new ShortLabel(hp.toString,30,Color.WHITE)(false)
                        }

                    }
                }, BorderPanel.Position.Center)

                add(new BoxPanel(Orientation.Vertical) {
                    opaque = false
                    border = new EmptyBorder(40, 0, 0, 0)

                    contents += new ShortButton("Menu",30,new Color(200, 200, 200))(true)(true) {
                        icon = new ImageIcon("src/main/scala/de/htwg/se/aview/Graphics/UI/Buttonframe.png")
                        reactions += {
                            case ButtonClicked(_) =>
                                controller.gamestate = "mainmenu"
                                controller.notifyObservers
                        }
                    }

                    contents += new ShortButton("Exit",30,new Color(200, 200, 200))(true)(true) {
                        icon = new ImageIcon("src/main/scala/de/htwg/se/aview/Graphics/UI/Buttonframe.png")
                        reactions += {
                            case ButtonClicked(_) => System.exit(0)
                        }
                    }

                    contents += new ShortLabel("Thanks for playing!",15,Color.WHITE)(true) {
                        border = new EmptyBorder(10, 160, 0, 0)
                    }

                }, BorderPanel.Position.South)
            }
        }
    }

    // ----------------------------------------------- Frame Function --------------------------------------------------

    def loadcellimage(cellname: String)(mode: Int): ImageIcon = {
        val name = cellname.replaceAll("_","")
        if (mode == 1) {
            new ImageIcon("src/main/scala/de/htwg/se/aview/Graphics/Model/" + name + ".png")
        } else if (mode == 2) {
            new ImageIcon("src/main/scala/de/htwg/se/aview/Graphics/Model/" + name + "activ.png")
        }else {
            new ImageIcon("src/main/scala/de/htwg/se/aview/Graphics/Model/" + name + "attack.png")
        }
    }

    override def update: Unit = {
        controller.gamestate match {
            case "mainmenu" => mainmenu()
            case "credit" => credit()
            case "gamerun" => gamerun()
            case "finished" => scoreboard()
        }
    }

}