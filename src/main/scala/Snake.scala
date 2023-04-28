import scalafx.scene.layout.Pane
import scalafx.scene.paint.{Color, PhongMaterial}
import scalafx.scene.shape.Box
import scalafx.scene.input.KeyEvent
import scalafx.scene.input.KeyCode
import scala.annotation.targetName
import scalafx.scene.text.Text
import scalafx.scene.text.Font
import scalafx.scene.transform.Translate

class Snake(startingX: Int, startingY: Int, startingZ: Int, gridSize: Int) :
  var x = startingX
  var y = startingY
  var z = startingZ

  // Initialize the snake with a length of 3 boxes
  val length = 3
  var body = Seq(
    (startingX, startingY, startingZ),
    (startingX - 1, startingY, startingZ),
    (startingX - 2, startingY, startingZ)
  )

  def move(dx: Int, dy: Int, dz: Int): Unit =
    // Update head position based on given direction
    x = (x + dx + gridSize) % gridSize
    y = (y + dy + gridSize) % gridSize
    z = (z + dz + gridSize) % gridSize

    // Move the body parts one step forward
    body = (x, y, z) +: body.init

  // Checks if the snake's head collides with its body
  def collidesWithBody: Boolean =
    body.tail.exists { case (bx, by, bz) => bx == x && by == y && bz == z }

  // Checks if the snake's head is out of bounds
  def isOutOfBounds: Boolean =
    x < 0 || x >= gridSize || y < 0 || y >= gridSize || z < 0 || z >= gridSize

  // Returns true if the game is over (head collides with body or out of bounds)
  def isGameOver: Boolean = collidesWithBody || isOutOfBounds

  // Checks if the snake's head is at the edge of the grid
  def isAtEdge: Boolean =
    x == 0 || x == gridSize - 1 || y == 0 || y == gridSize - 1 || z == 0 || z == gridSize - 1

  // Grows the snake by adding a new body part at the tail
  def grow(): Unit =
    val (lastX, lastY, lastZ) = body.last
    val (prevX, prevY, prevZ) = body(body.length - 2)

    val dx = lastX - prevX
    val dy = lastY - prevY
    val dz = lastZ - prevZ

    val newTailX = lastX + dx
    val newTailY = lastY + dy
    val newTailZ = lastZ + dz

    body = body :+ (newTailX, newTailY, newTailZ)
    (newTailX, newTailY, newTailZ)

  // Gets the snake's head position
  def head: (Int, Int, Int) = body.head

  def eatFood(foodX: Int, foodY: Int, foodZ: Int): Boolean = {
  head._1 == foodX && head._2 == foodY && head._3 == foodZ
}
