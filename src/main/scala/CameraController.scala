import scalafx.scene.Camera
import scalafx.scene.transform.Rotate
import scalafx.animation.AnimationTimer

case class CameraPosition(x: Double, y: Double, z: Double, rotationX: Double, rotationY: Double)

class CameraController(camera: Camera, gridSize: Int, boxSize: Int) {

  // Calculate the center of the grid
  val gridCenter = (gridSize * boxSize) / 2.0
  // Define the camera offset from the grid
  val cameraOffset = 2.0 * gridCenter
  // Set the angle of the camera
  val cameraAngle = 60.0

  // Define fixed camera positions for each side
  val fixedCameraPositions = Seq(
    CameraPosition(gridCenter, gridCenter, -cameraOffset, cameraAngle, 0),
    CameraPosition(gridCenter, gridCenter, cameraOffset, cameraAngle, 180),
    CameraPosition(gridCenter, -cameraOffset, gridCenter, 180 - cameraAngle, 90),
    CameraPosition(gridCenter, cameraOffset, gridCenter, 180 - cameraAngle, -90),
    CameraPosition(-cameraOffset, gridCenter, gridCenter, cameraAngle, 90),
    CameraPosition(cameraOffset, gridCenter, gridCenter, cameraAngle, -90)
  )

  // Store the initial camera position
  private var initialCameraPosition = fixedCameraPositions.head

  // Create rotation transforms for the camera
  private val cameraRotationX = new Rotate(0, Rotate.XAxis)
  private val cameraRotationY = new Rotate(0, Rotate.YAxis)
  camera.transforms.addAll(cameraRotationX, cameraRotationY)

  // Set the camera position with animation
  def setCameraPosition(targetPosition: CameraPosition, duration: Double = 1.0): Unit = {
    val startTime = System.nanoTime()

    // Get the current camera position and rotation
    val initialPosition = CameraPosition(camera.translateX(), camera.translateY(), camera.translateZ(), cameraRotationX.angle(), cameraRotationY.angle())

    // Create an animation timer for camera transition
    lazy val cameraTransition: AnimationTimer = AnimationTimer((now) => {
        val elapsedTime = (now - startTime) / 1e9
        val progress = math.min(elapsedTime / duration, 1.0)

        // Linear interpolation between the initial and target positions
        val x = initialPosition.x + (targetPosition.x - initialPosition.x) * progress
        val y = initialPosition.y + (targetPosition.y - initialPosition.y) * progress
        val z = initialPosition.z + (targetPosition.z - initialPosition.z) * progress
        val rotX = initialPosition.rotationX + (targetPosition.rotationX - initialPosition.rotationX) * progress
        val rotY = initialPosition.rotationY + (targetPosition.rotationY - initialPosition.rotationY) * progress

        // Update camera position and rotation
        camera.translateX = x
        camera.translateY = y
        camera.translateZ = z
        cameraRotationX.angle = rotX
        cameraRotationY.angle = rotY

        // Stop the transition when the target position is reached
        if progress >= 1.0 then
            cameraTransition.stop()
            initialCameraPosition = targetPosition
        })

    cameraTransition.start()
  }

  // Set the camera position based on the snake's position
  def setCameraPositionBySnake(snake: Snake): Unit = {
    val side = determineSide(snake)
    val targetPosition = side match {
        case 1 => fixedCameraPositions(4)
        case 2 => fixedCameraPositions(5)
        case 3 => fixedCameraPositions(2)
        case 4 => fixedCameraPositions(3)
        case 5 => fixedCameraPositions.head
        case 6 => fixedCameraPositions(1)
        case _ => initialCameraPosition
      }

      // Update the camera position if it's different from the current position
      if targetPosition != initialCameraPosition then
        setCameraPosition(targetPosition)
    }

    // Determine which side of the grid the snake is on
  def determineSide(snake: Snake): Int = {
    if snake.head._1 == 0 then 1
    else if snake.head._1 == gridSize - 1 then 2
    else if snake.head._2 == 0 then 3
    else if snake.head._2 == gridSize - 1 then 4
    else if snake.head._3 == 0 then 5
    else if snake.head._3 == gridSize - 1 then 6
    else 0
  }


}
