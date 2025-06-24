/* --------------------------------------------------------------------------------
S2D - Shapes Package Examples - Basic Shapes.

This example was created with S2D 0.1.7. (Updated 24/06/2025)

You can run this example with any scala build tool but I'd recommend either SBT or ScalaCLI.

--------------------------------------------------------------------------------- */

package sandbox

import s2d.core.*
import s2d.shapes.*
import s2d.textures.*
import s2d.types.*
import scalanative.unsafe.*
import scalanative.unsigned.*

@main
def main(): Unit =
  val screenWidth = 800
  val screenHeight = 450
  
  Window.create(screenWidth, screenHeight, "S2D Framework - Basic Shapes Drawing")
  Input.setExitKey(Key.Escape)
  
  var rotation = 0.0f

  while !Window.shouldCloseWindow() do
    rotation += 0.2f
    
    Drawing.beginFrame()
    Drawing.clear(Color.White)
    
    Basics.circle(screenWidth/5, 120, 35.0f, Color.DarkBlue)
    Basics.circle(screenWidth/5, 220, 60.0f, Color.Green)
    Basics.circleOutline(Vector2(screenWidth/5, 340), 80.0f, Color.DarkBlue)
    
    Basics.ellipse(screenWidth/5, 120, 25.0f, 20.0f, Color.Yellow)
    Basics.ellipseOutlines(screenWidth/5, 120, 30.0f, 25.0f, Color.Yellow)
    
    Basics.rectangle(screenWidth/4*2 - 60, 100, 120, 60, Color.Red)
    Basics.rectangle(screenWidth/4*2 - 90, 170, 180, 130, Color.Maroon)
    Basics.rectangleOutline(screenWidth/4*2 - 40, 320, 80, 60, Color.Orange)
    
    val trianglePoint1 = Vector2(screenWidth/4.0f * 3.0f, 80.0f)
    val trianglePoint2 = Vector2(screenWidth/4.0f * 3.0f - 60.0f, 150.0f)
    val trianglePoint3 = Vector2(screenWidth/4.0f * 3.0f + 60.0f, 150.0f)
    Basics.triangle(trianglePoint1, trianglePoint2, trianglePoint3, Color.Violet)
    
    val trianglePoint4 = Vector2(screenWidth/4.0f * 3.0f, 160.0f)
    val trianglePoint5 = Vector2(screenWidth/4.0f * 3.0f - 20.0f, 230.0f)
    val trianglePoint6 = Vector2(screenWidth/4.0f * 3.0f + 20.0f, 230.0f)
    Basics.triangleOutline(trianglePoint4, trianglePoint5, trianglePoint6, Color.DarkBlue)
    
    val polygonCenter = Vector2(screenWidth/4.0f * 3.0f, 330.0f)
    Basics.polygon(polygonCenter, 6, 80.0f, rotation, Color.Brown)
    Basics.polygonOutline(polygonCenter, 6, 90.0f, rotation, Color.Brown)
    Basics.polygonOutline(polygonCenter, 6, 85.0f, rotation, Color.Beige)
    
    Basics.line(Vector2(18.0f, 42.0f), Vector2(screenWidth - 18.0f, 42.0f), Color.Black)
    
    Drawing.endFrame()
  
  Window.close()