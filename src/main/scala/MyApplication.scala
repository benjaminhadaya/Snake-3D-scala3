import scalafx.application.JFXApp3
import scalafx.beans.property.ObjectProperty
import scalafx.scene.{PerspectiveCamera,Scene}
import scalafx.scene.input.KeyCode
import scalafx.scene.layout.Pane
import scalafx.scene.paint.Color
import scalafx.scene.paint.PhongMaterial
import scalafx.scene.shape.Box
import scalafx.scene.transform.Rotate
import scalafx.scene.Camera
import scalafx.animation.AnimationTimer
import scalafx.Includes.jfxKeyEvent2sfx

object MyApplication extends JFXApp3:

  // Box, grid, camera...
  val boxSize = 50
  val gridSize = 10
  val camera = new PerspectiveCamera(false)
  val cameraDistance = 1000
  var cameraX = 0.0
  var cameraY = 0.0

  def start(): Unit =

    val boxes = for
      x <- 0 until gridSize
      y <- 0 until gridSize
      z <- 0 until gridSize
    yield
      new Box(boxSize, boxSize, boxSize) {
        translateX = x * boxSize
        translateY = y * boxSize
        translateZ = z * boxSize
        material = new PhongMaterial(Color.Gray)
      }

    val group = new Pane {
      children = boxes
    }

    
    // Lines 45-51 are giving me problems. 
    
    // Adding the snake
    val snake = new Snake(5, 5, 5, gridSize, boxSize)
    snake.addToScene(group, food)

    // Adding the food
    val food = new Food(gridSize, boxSize)
    food.addToScene(group)

    // Initialize camera position and rotation
    camera.translateZ = -cameraDistance
    cameraX = group.translateX() + (boxSize * gridSize / 2)
    cameraY = group.translateY() + (boxSize * gridSize / 2)
    camera.translateX = cameraX
    camera.translateY = cameraY

    // Rotate the camera to look at the center of the scene
    val lookAt = group.localToScene(boxSize * gridSize / 2, boxSize * gridSize / 2, boxSize * gridSize / 2)
    val dx = lookAt.getX - cameraX
    val dy = lookAt.getY - cameraY
    val dz = lookAt.getZ - camera.translateZ.value
    val angleX = math.atan2(dy, dz)
    val angleY = math.atan2(dx, dz)
    camera.setRotationAxis(Rotate.XAxis)
    camera.rotate = angleX.toDegrees
    camera.setRotationAxis(Rotate.YAxis)
    camera.rotate = angleY.toDegrees

    // Translate the group into the middle of the screen
    // Note that the point being moved to the middle is the upper left corner of the object
    // Therefore we have to move it back by half the width of the group.
    group.translateXProperty().set(1400 / 2 - boxSize * gridSize / 2)
    group.translateYProperty().set(800 / 2 - boxSize * gridSize / 2)

    stage = new JFXApp3.PrimaryStage:
      title = "Snake3D"
      width = 1400
      height = 800
      val myScene = new Scene(
        group,
        width(),
        height(),
        Color.rgb(255, 255, 255),
      )

      // Set the camera of the scene to a perspective camera object
      myScene.setCamera(camera)

      // Define a setOnKeyPressed event handler that listens for arrow key events and updates the snake's movement direction and the camera position
      //
      // I don't know what is worng with lines "91-104" since it's not an error but a warning. VSCode says it's alright but Intelij does not.
      // "Scala compiler will replace this argument list with tuple" I guess there is a syntax error between scala 3 and scala 2 I can't figure out what is the problem?
      myScene.setOnKeyPressed { e =>
        snake.handleKeyEvent1(e)
        if e.getCode == KeyCode.Left then
          cameraX -= 10
        else if e.getCode == KeyCode.Right then
          cameraX += 10
        else if e.getCode == KeyCode.Up then
          cameraY -= 10
        else if e.getCode == KeyCode.Down then
          cameraY += 10

        camera.translateX = cameraX
        camera.translateY = cameraY
      }

