import scala.util.Random
import scalafx.scene.paint.Color
import scalafx.scene.paint.PhongMaterial
import scalafx.scene.shape.Box
import scalafx.scene.layout.Pane

class Food(gridSize: Int, boxSize: Int, snake: Snake) :
  private val rand = new Random()
  private val foodBox = new Box(boxSize, boxSize, boxSize) {
    material = new PhongMaterial {
      diffuseColor = Color.Red
      specularColor = Color.Red
    }
  }

  //  Getters for the food position
  def foodX: Int = foodBox.translateX.toInt / boxSize
  def foodY: Int = foodBox.translateY.toInt / boxSize
  def foodZ: Int = foodBox.translateZ.toInt / boxSize

  // Add the food to the scene
  def addToScene(scene: Pane): Unit = 
    scene.children.add(foodBox)
    updatePosition(snake)
  

  // Update the position of the food
  def updatePosition(snake: Snake): Unit = 
    // Helper function to check if the food position is valid
    def isValidPosition: Boolean =
      !snake.body.exists { case (x, y, z) => x == foodX && y == foodY && z == foodZ }

    // Update the position until a valid position is found
    var validPosition = false
    while (!validPosition) {
      foodBox.translateX = rand.nextInt(gridSize) * boxSize
      foodBox.translateY = rand.nextInt(gridSize) * boxSize
      foodBox.translateZ = rand.nextInt(gridSize) * boxSize
      validPosition = isValidPosition
    }
  
