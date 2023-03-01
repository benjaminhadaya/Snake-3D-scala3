import scala.util.Random
import scalafx.scene.layout.Pane
import scalafx.scene.paint.Color
import scalafx.scene.paint.PhongMaterial
import scalafx.scene.shape.Box
import scalafx.Includes.jfxKeyEvent2sfx

class Food(gridSize: Int, boxSize: Int) extends Box(boxSize, boxSize, boxSize):
  // Set a random position for the food
  var (foodX, foodY, foodZ) = (0, 0, 0)
  while
    foodX = Random.nextInt(gridSize)
    foodY = Random.nextInt(gridSize)
    foodZ = Random.nextInt(gridSize)
    false
  do ()

  // Set the position of the food box
  translateX = foodX * boxSize
  translateY = foodY * boxSize
  translateZ = foodZ * boxSize

  // Set the material of the food box
  material = new PhongMaterial(Color.Red)

  // Add the food box to the scene
  def addToScene(pane: Pane): Unit = pane.children.add(this)

  // Update the position of the food box
  def updatePosition(snake: Snake): Unit =
    var (newFoodX, newFoodY, newFoodZ) = (0, 0, 0)
    while
      newFoodX = Random.nextInt(gridSize)
      newFoodY = Random.nextInt(gridSize)
      newFoodZ = Random.nextInt(gridSize)
      snake.body.exists(box => box.translateX.value == newFoodX * boxSize && box.translateY.value == newFoodY * boxSize && box.translateZ.value == newFoodZ * boxSize)
    do ()
    foodX = newFoodX
    foodY = newFoodY
    foodZ = newFoodZ
    translateX = foodX * boxSize
    translateY = foodY * boxSize
    translateZ = foodZ * boxSize
