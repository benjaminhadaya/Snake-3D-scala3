import scalafx.application.JFXApp3
import scalafx.beans.property.ObjectProperty
import scalafx.scene.Scene
import scalafx.scene.layout.Pane
import scalafx.scene.paint.Color
import scalafx.scene.paint.PhongMaterial
import scalafx.scene.shape.Box
import scalafx.scene.transform.Rotate

object MyApplication extends JFXApp3:

  val boxSize = 50
  val gridSize = 10

  def start(): Unit =

    val boxes = for {
      x <- 0 until gridSize
      y <- 0 until gridSize
      z <- 0 until gridSize
    } yield {
      new Box(boxSize, boxSize, boxSize) {
        translateX = x * boxSize
        translateY = y * boxSize
        translateZ = z * boxSize
        material = new PhongMaterial(Color.Gray)
      }
    }

    val group = new Pane {
      children = boxes
    }

    group.transforms = Seq(new Rotate(60, Rotate.YAxis))

    stage = new JFXApp3.PrimaryStage:
      title = "Snake3D"
      width = boxSize * gridSize
      height = boxSize * gridSize
      val myScene = new Scene(
        group,
        width(),
        height(),
        Color.rgb(38, 38, 38)
      )
      val sceneProperty: ObjectProperty[Scene] = new ObjectProperty[Scene](this, "scene", myScene)
      scene = sceneProperty.value

