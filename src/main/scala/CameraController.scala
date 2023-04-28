import scalafx.scene.Camera
import scalafx.scene.transform.Rotate

class CameraController(camera: Camera, gridSize: Int, boxSize: Int) :
  // Calculate the center of the grid
  val gridCenter = (gridSize * boxSize) / 2.0
  // Define the camera offset from the grid
  val cameraOffset = 2.0 * gridCenter

  // Set the initial camera position
  camera.translateX = gridCenter
  camera.translateY = gridCenter
  camera.translateZ = -cameraOffset

  // Create rotation transforms for the camera
  private val cameraRotationX = new Rotate(60, Rotate.XAxis)
  private val cameraRotationY = new Rotate(0, Rotate.YAxis)
  camera.transforms.addAll(cameraRotationX, cameraRotationY)
