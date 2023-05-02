import scalafx.scene.Camera
import scalafx.scene.transform.Rotate
import scalafx.animation.AnimationTimer

case class CameraPosition(x: Double, y: Double, z: Double, rotationX: Double, rotationY: Double)

class CameraController(camera: Camera, gridSize: Int, boxSize: Int) :

  // Calculate the center of the grid
  val gridCenter = (gridSize * boxSize) / 2.0
  // Define the camera offset from the grid
  val cameraOffset = 3.0 * gridCenter

  // Set the initial camera position
  camera.translateX = gridCenter
  camera.translateY = 2.5 * gridCenter
  camera.translateZ = -2.0 * cameraOffset

  // Create rotation transforms for the camera
  private val cameraRotationX = new Rotate(30, Rotate.XAxis)
  private val cameraRotationY = new Rotate(45, Rotate.YAxis)
  camera.transforms.addAll(cameraRotationX, cameraRotationY)


  // Function to set the camera position based on a CameraPosition object
  private def setCameraPosition(position: CameraPosition): Unit = {
    camera.translateX = position.x
    camera.translateY = position.y
    camera.translateZ = position.z - cameraOffset
    cameraRotationX.angle = position.rotationX
    cameraRotationY.angle = position.rotationY
  }

  // Zoom in
  def zoomIn(): Unit =
    camera.translateZ.set(camera.translateZ.value + boxSize)

  // Zoom out
  def zoomOut(): Unit =
    camera.translateZ.set(camera.translateZ.value - boxSize)

  def rotateCamera(rotationY: Double): Unit = {
    cameraRotationY.angle.value += rotationY
}

