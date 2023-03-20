import scalafx.scene.layout.Pane
import scalafx.scene.paint.{Color, PhongMaterial}
import scalafx.scene.shape.Box
import scalafx.scene.input.KeyEvent
import scalafx.scene.input.KeyCode
import scala.annotation.targetName
import scalafx.scene.text.Text
import scalafx.scene.text.Font
import scalafx.scene.transform.Translate

class Snake(startingX: Int, startingY: Int, startingZ: Int, gridSize: Int, boxSize: Int):
  var x = startingX
  var y = startingY
  var z = startingZ

  // Initialize the snake with a length of 3 boxes
  val length = 3
  var body = Seq(
    new Box(boxSize, boxSize, boxSize) {
      translateX = startingX * boxSize
      translateY = startingY * boxSize
      translateZ = startingZ * boxSize
      material = new PhongMaterial(Color.Green)
    },
    new Box(boxSize, boxSize, boxSize) {
      translateX = (startingX - 1) * boxSize
      translateY = startingY * boxSize
      translateZ = startingZ * boxSize
      material = new PhongMaterial(Color.Green)
    },
    new Box(boxSize, boxSize, boxSize) {
      translateX = (startingX - 2) * boxSize
      translateY = startingY * boxSize
      translateZ = startingZ * boxSize
      material = new PhongMaterial(Color.Green)
    }
  )

  val food = new Food(gridSize, boxSize)
  val pane = new Pane()

  // Add the snake's body parts to the scene as children of the pane node
  def addToScene(pane: Pane, food: Food): Unit =
    body.foreach(pane.children.add(_))

    // Add score text to the scene
    val scoreText = new Text("Score: 0") {
      font = Font("Arial", 20)
      fill = Color.Black
      translateX = 20
      translateY = 40
    }
    pane.children.add(scoreText)



  // Move the snake by one step
  @targetName("moveBody")
  def move(): Unit =
    // Save the current position of the last body part
    val lastPart = body.last
    val lastX = lastPart.translateX.value
    val lastY = lastPart.translateY.value
    val lastZ = lastPart.translateZ.value

    // Move the body parts one step forward
    body.tail.zip(body.init).foreach { case (part, prevPart) =>
      part.translateX.value = prevPart.translateX.value
      part.translateY.value = prevPart.translateY.value
      part.translateZ.value = prevPart.translateZ.value
    }

    // Move the head to the next position
    val head = body.head
    head.translateX.value = x * boxSize
    head.translateY.value = y * boxSize
    head.translateZ.value = z * boxSize

    // Update the position of the last body part to the previous position of the head
    lastPart.translateX.value = lastX
    lastPart.translateY.value = lastY
    lastPart.translateZ.value = lastZ

// Helper method to update the position of the snake's body parts
  private def updateBody(): Unit =
    body.zipWithIndex.foreach { case (part, index) =>
      val prevPart = if index == 0 then null else body(index - 1)
      part.translateX.value = prevPart.translateX.value
      part.translateY.value = prevPart.translateY.value
      part.translateZ.value = prevPart.translateZ.value
    }
    val head = body.head
    head.translateX.value = x * boxSize
    head.translateY.value = y * boxSize
    head.translateZ.value = z * boxSize


  // Move the snake by one step
  @targetName("move1")
  def move1(dx: Int, dy: Int, dz: Int): Unit =
    // Save the current position of the last body part
    val lastPart = body.last
    val lastX = lastPart.translateX.value
    val lastY = lastPart.translateY.value
    val lastZ = lastPart.translateZ.value

    // Update head position based on given direction
    x += dx
    y += dy
    z += dz

    // Move the body parts one step forward
    body.tail.zip(body.init).foreach { case (part, prevPart) =>
      part.translateX.value = prevPart.translateX.value
      part.translateY.value = prevPart.translateY.value
      part.translateZ.value = prevPart.translateZ.value
    }

    // Move the head to the next position
    x = math.max(0, math.min(gridSize - 1, x))
    y = math.max(0, math.min(gridSize - 1, y))
    z = math.max(0, math.min(gridSize - 1, z))
    val head = body.head
    head.translateX.value = x * boxSize
    head.translateY.value = y * boxSize
    head.translateZ.value = z * boxSize

    // Check for collisions with the food
    if head.translateX.value == food.translateX.value && head.translateY.value == food.translateY.value && head.translateZ.value == food.translateZ.value then

      // Update the position of the food
      food.updatePosition(this)

      // Add a new box to the end of the snake's body
      val newBox = new Box(boxSize, boxSize, boxSize) {
        translateX = lastX
        translateY = lastY
        translateZ = lastZ
        material = new PhongMaterial(Color.Green)
      }
      body = body :+ newBox
      // Add the new box to the scene
      pane.children.add(newBox)

    // Update the position of the last body part to the previous position of the head
    lastPart.translateX.value = lastX
    lastPart.translateY.value = lastY
    lastPart.translateZ.value = lastZ

  // Check if the snake collides with its own body
  def collidesWithBody: Boolean =
    body.tail.exists(box => box.translateX.value == x * boxSize && box.translateY.value == y * boxSize && box.translateZ.value == z * boxSize)

  // Check if the snake is out of bounds
  def isOutOfBounds: Boolean =
    x < 0 || x >= gridSize || y < 0 || y >= gridSize || z < 0 || z >= gridSize

  // Check if the game is over (snake collided with body or is out of bounds)
  def isGameOver: Boolean = collidesWithBody || isOutOfBounds
