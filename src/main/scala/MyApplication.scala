import scalafx.application.JFXApp3
import scalafx.scene.{PerspectiveCamera,Scene}
import scalafx.scene.input.KeyCode
import scalafx.scene.input.KeyEvent
import scalafx.scene.layout.Pane
import scalafx.scene.paint.Color
import scalafx.scene.paint.PhongMaterial
import scalafx.scene.shape.Box
import scalafx.scene.transform.Rotate
import scalafx.scene.Camera
import scalafx.scene.PointLight
import scalafx.animation.AnimationTimer
import scalafx.Includes.eventClosureWrapperWithParam
import scalafx.Includes.jfxKeyEvent2sfx

object MyApplication extends JFXApp3:

  // Box, grid, camera...
  val boxSize = 50
  val gridSize = 10
  val camera = new PerspectiveCamera(false) {
    fieldOfView = 45
  }
  val cameraDistance = 800
  var cameraX = 0.0
  var cameraY = 0.0

  def start(): Unit =

    stage = new JFXApp3.PrimaryStage:
      title = "Snake3D"
      width = 1400
      height = 800

    val boxes = for
      x <- 0 until gridSize
      y <- 0 until gridSize
      z <- 0 until gridSize
    yield
      new Box(boxSize, boxSize, boxSize) {
        translateX = x * boxSize
        translateY = y * boxSize
        translateZ = z * boxSize
        material = new PhongMaterial{
          diffuseColor = Color.Grey
        }
      }

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

    // Adding the food
    val food = new Food(gridSize, boxSize)
    food.addToScene(group)

    // Adding the snake
    val snake = new Snake(5, 5, 5, gridSize, boxSize)
    snake.addToScene(group, food)

    val initialRotationX = 30.0
    val initialRotationY = 45.0

    // Initialize camera position and rotation
    camera.translateZ = -cameraDistance
    cameraX = group.translateX() + (boxSize * gridSize / 2)
    cameraY = group.translateY() + (boxSize * gridSize * 0.7)
    camera.translateX = cameraX
    camera.translateY = cameraY

    val cameraRotationX = new Rotate(initialRotationX, Rotate.XAxis)
    val cameraRotationY = new Rotate(initialRotationY, Rotate.YAxis)

    camera.transforms.addAll(cameraRotationX, cameraRotationY)

     // Move the camera to look at the center of the scene
    val centerX = boxSize * gridSize / 2
    val centerY = boxSize * gridSize / 2
    val centerZ = boxSize * gridSize / 2

    val dx = centerX - camera.translateX.value
    val dy = centerY - camera.translateY.value
    val dz = centerZ - camera.translateZ.value

    val distance = math.sqrt(dx * dx + dy * dy + dz * dz)
    val angleX = math.atan2(dy, dz) * (180 / math.Pi)
    val angleY = math.atan2(dx, dz) * (180 / math.Pi)

    camera.rotationAxis = Rotate.XAxis
    camera.rotate = angleX
    camera.rotationAxis = Rotate.YAxis
    camera.rotate = camera.rotate() + angleY // set the rotate property explicitly by adding the angle

    // Translate the group into the middle of the screen
    // Note that the point being moved to the middle is the upper left corner of the object
    // Therefore we have to move it back by half the width of the group.
    group.translateXProperty().bind(stage.width.divide(2).subtract(boxSize * gridSize / 2))
    group.translateYProperty().bind(stage.height.divide(2).subtract(boxSize * gridSize / 2))

      // Set the camera of the scene to a perspective camera object
      myScene.setCamera(camera)

      // Define the direction variable
      var direction: (Int, Int, Int) = (0, 0, 0)

      // Handle key events
      myScene.onKeyPressed = (event: KeyEvent) => {
        event.code match {
          case KeyCode.Up => direction = (0, 0, -1)
          case KeyCode.Down => direction = (0, 0, 1)
          case KeyCode.Left => direction = (-1, 0, 0)
          case KeyCode.Right => direction = (1, 0, 0)
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
          val (dx, dy, dz) = direction
          if dx != 0 || dy != 0 || dz != 0 then
          snake.move1(dx, dy, dz)

          // Update previousTime
          previousTime = now

          // Check if the game is over
          if snake.isGameOver then
            gameLoop.stop()
            println("Game Over")

        // game logic, update and rendering goes here

      })

      // Start the game loop
      gameLoop.start()
