import scala.util.Random
import scalafx.scene.layout.Pane
import scalafx.scene.paint.Color
import scalafx.scene.paint.PhongMaterial
import scalafx.scene.shape.Box
import scalafx.Includes.jfxKeyEvent2sfx

class Food(gridSize: Int, boxSize: Int) extends Box(boxSize, boxSize, boxSize) {
  // Set a random position for the food
  var foodX, foodY, foodZ = 0
  var newPosition = true
  while (newPosition) {
    foodX = Random.nextInt(gridSize)
    foodY = Random.nextInt(gridSize)
    foodZ = Random.nextInt(gridSize)
    newPosition = false
  }

  // Set the position of the food box
  translateX = foodX * boxSize
  translateY = foodY * boxSize
  translateZ = foodZ * boxSize

  // Set the material of the food box
  material = new PhongMaterial(Color.Red)

  // Add the food box to the scene
  def addToScene(pane: Pane): Unit = pane.children.add(this)

  // Update the position of the food box
  def updatePosition(snake: Snake): Unit = {
    var newFoodX, newFoodY, newFoodZ = 0
    var occupiedPosition = true
    while (occupiedPosition) {
      newFoodX = Random.nextInt(gridSize)
      newFoodY = Random.nextInt(gridSize)
      newFoodZ = Random.nextInt(gridSize)
      occupiedPosition = snake.body.exists { case (bx, by, bz) => bx == newFoodX && by == newFoodY && bz == newFoodZ }
    }

    foodX = newFoodX
    foodY = newFoodY
    foodZ = newFoodZ
    translateX = foodX * boxSize
    translateY = foodY * boxSize
    translateZ = foodZ * boxSize
  }
}
