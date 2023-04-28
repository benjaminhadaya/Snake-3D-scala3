import scalafx.application.JFXApp3
import scalafx.scene.{PerspectiveCamera,Scene}
import scalafx.scene.input.KeyCode
import scalafx.scene.input.KeyEvent
import scalafx.scene.layout.Pane
import scalafx.scene.paint.Color
import scalafx.scene.paint.PhongMaterial
import scalafx.scene.shape.Box
import scalafx.scene.transform.Rotate
import scalafx.scene.PointLight
import scalafx.animation.AnimationTimer
import scalafx.Includes.eventClosureWrapperWithParam
import scalafx.Includes.jfxKeyEvent2sfx
object MyApplication extends JFXApp3:

  // Define box size and grid size and perspective camera
  val boxSize = 50
  val gridSize = 10
  val camera = new PerspectiveCamera(false) {
    fieldOfView = 45
    translateX = gridSize * boxSize / 2
    translateY = gridSize * boxSize / 2
    translateZ = -3 * gridSize * boxSize
  }

  // Function to create outer surface boxes for the grid
  def createOuterSurfaceBoxes(): Seq[Box] =
    for {
      x <- 0 until gridSize
      y <- 0 until gridSize
      z <- 0 until gridSize
      // Include only boxes on the outer surface of the grid
      if x == 0 || x == gridSize - 1 || y == 0 || y == gridSize - 1 || z == 0 || z == gridSize - 1
    } yield new Box(boxSize, boxSize, boxSize) {
      translateX = x * boxSize
      translateY = y * boxSize
      translateZ = z * boxSize
      material = new PhongMaterial {
        diffuseColor = Color.rgb(192, 192, 192, 0.7)
        specularColor = Color.rgb(192, 192, 192, 0.7)
      }
    }
  def start(): Unit =

    stage = new JFXApp3.PrimaryStage:
      title = "Snake3D"
      width = 1400
      height = 800

    // Create outer surface boxes
    val boxes = createOuterSurfaceBoxes()

     // Create a group to hold the boxes
    val group = new Pane {
      children = boxes
    }

    // Add the PointLight to the group
    val light = new PointLight {
      color = Color.White
      translateX = boxSize * gridSize / 2
      translateY = boxSize * gridSize / 2
      translateZ = -boxSize * gridSize / 2
}
    group.children.add(light)

    val myScene = new Scene{
      content = group
}

    stage.scene = myScene

    // Adding the snake
    val snake = new Snake(5, 5, 5, gridSize)
    val snakeGraphics = new SnakeGraphics(snake, boxSize)
    snakeGraphics.addToScene(group)

    // Adding the food
    val food = new Food(gridSize, boxSize, snake)
    food.addToScene(group)

    // Translate the group into the middle of the screen
    group.translateXProperty().bind(stage.width.divide(2).subtract(boxSize * gridSize / 2))
    group.translateYProperty().bind(stage.height.divide(2).subtract(boxSize * gridSize / 2))

    // Set the camera of the scene to a perspective camera object
    myScene.setCamera(camera)

    // Define the direction variable
    var direction: (Int, Int, Int) = (1, 0, 0)

    // Handle key events
    myScene.onKeyPressed = (event: KeyEvent) => {
      event.code match {
        case KeyCode.Up if direction != (0, 1, 0) => direction = (0, -1, 0)
        case KeyCode.Down if direction != (0, -1, 0) => direction = (0, 1, 0)
        case KeyCode.Left if direction != (1, 0, 0) => direction = (-1, 0, 0)
        case KeyCode.Right if direction != (-1, 0, 0) => direction = (1, 0, 0)
        case KeyCode.W if direction != (0, 0, 1) => direction = (0, 0, -1)
        case KeyCode.S if direction != (0, 0, -1) => direction = (0, 0, 1)
        case KeyCode.A if direction != (1, 0, 0) => direction = (-1, 0, 0)
        case KeyCode.D if direction != (-1, 0, 0) => direction = (1, 0, 0)
        case _ => ()
      }
    }

    // Create the game loop AnimationTimer
    val frameDelay = 180000000L // 180 ms delay
    var previousTime = 0L

    // Create the game loop AnimationTimer
    lazy val gameLoop: AnimationTimer = AnimationTimer((now) => {
      if now - previousTime >= frameDelay then

        // Move the snake in the current direction
        snake.move(direction._1, direction._2, direction._3)
        snakeGraphics.update() // Update the snake graphics

        // Check if the snake has eaten the food
        if snake.eatFood(food.foodX, food.foodY, food.foodZ) then
          snake.grow() // Increase the length of the snake
          food.updatePosition(snake) // Move the food to a new position
          // Add the new body part to the SnakeGraphics
          val newBodySegment = snake.body.last
          val newBodyBox = snakeGraphics.createBodyBox()
          newBodyBox.translateX = newBodySegment._1 * boxSize
          newBodyBox.translateY = newBodySegment._2 * boxSize
          newBodyBox.translateZ = newBodySegment._3 * boxSize
          snakeGraphics.bodyParts = snakeGraphics.bodyParts :+ newBodyBox
        // Update previousTime
        previousTime = now

        // Check if the game is over
        if snake.isGameOver then
          gameLoop.stop()
          println("Game Over")

    })

    // Start the game loop
    gameLoop.start()
