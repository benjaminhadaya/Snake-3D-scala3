import scalafx.scene.paint.Color
import scalafx.scene.shape.Box
import scalafx.scene.paint.PhongMaterial
import scalafx.scene.layout.Pane

// Class to handle the graphical representation of the snake
class SnakeGraphics(snake: Snake, boxSize: Int) :
  // Create a new box with a given color
  def createBox(color: Color): Box = new Box(boxSize, boxSize, boxSize) {
    material = new PhongMaterial(color)
  }

  // Create a box representing a body part of the snake
  def createBodyBox(): Box = createBox(Color.Green)

  // Create a box representing the head of the snake
  def createHeadBox(): Box = createBox(Color.DarkGreen)

  // Create boxes for the body parts of the snake
  var bodyParts: Seq[Box] = snake.body.map(_ => createBodyBox())

  // Update the position of the body parts according to the snake's state
  def update(): Unit =
    bodyParts.zip(snake.body).foreach { case (box, (x, y, z)) =>
      box.translateX = x * boxSize
      box.translateY = y * boxSize
      box.translateZ = z * boxSize
    }
    bodyParts.head.material = new PhongMaterial(Color.DarkGreen)

  // Add the snake's body parts to the scene
  def addToScene(pane: Pane): Unit =
    bodyParts.foreach(pane.children.add(_))
