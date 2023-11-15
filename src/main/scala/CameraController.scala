import scalafx.scene.Camera
import scalafx.scene.transform.Rotate
import scalafx.animation.AnimationTimer

case class CameraPosition(x: Double, y: Double, z: Double, rotationX: Double, rotationY: Double)

class CameraController(camera: Camera, gridSize: Int, boxSize: Int) :

  val gridCenter = (gridSize * boxSize) / 2.0

  camera.translateX = gridCenter
  camera.translateY = gridCenter
  // Adjust camera.translateZ to bring the grid into view
  camera.translateZ = -4 * gridCenter

  private val cameraRotationX = new Rotate :
    // Decrease the negative angle to tilt the camera more downwards
    angle = -1 // Adjust this value as needed
    axis = Rotate.XAxis


  private val cameraRotationY = new Rotate :
    // Adjust this angle as needed to center the grid
    angle = 0
    axis = Rotate.YAxis


  camera.transforms.addAll(cameraRotationX, cameraRotationY)

  // Function to update the camera position based on a CameraPosition object
  def setCameraPosition(position: CameraPosition): Unit =
    camera.translateX = position.x
    camera.translateY = position.y
    camera.translateZ = position.z
    cameraRotationX.angle = position.rotationX
    cameraRotationY.angle = position.rotationY

  // Zoom in function
  def zoomIn(): Unit =
    camera.translateZ = camera.translateZ() + boxSize

  // Zoom out function
  def zoomOut(): Unit =
    camera.translateZ = camera.translateZ() - boxSize

  // Function to rotate the camera
  def rotateCamera(rotationAngle: Double): Unit =
    cameraRotationY.angle = cameraRotationY.angle() + rotationAngle


  // Function to rotate the camera based on the snake's direction
  def rotateCameraBasedOnSnakeDirection(direction: (Int, Int, Int)): Unit = {
    direction match {
      case (1, 0, 0)  => cameraRotationY.angle = 0    // Snake moving right
      case (-1, 0, 0) => cameraRotationY.angle = 180  // Snake moving left
      case (0, 0, 1)  => cameraRotationY.angle = 90   // Snake moving forward
      case (0, 0, -1) => cameraRotationY.angle = -90  // Snake moving backward
      case _          => // Optionally handle other cases or do nothing
    }
  }



