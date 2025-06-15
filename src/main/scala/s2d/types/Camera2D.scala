package s2d.types

case class Camera2D(
    offset: Vector2,
    target: Vector2,
    rotation: Float,
    zoom: Float
)

object Camera2D:
  def apply(target: Vector2): Camera2D =
    Camera2D(Vector2.Zero, target, 0.0f, 1.0f)

  def apply(targetX: Float, targetY: Float): Camera2D =
    Camera2D(Vector2.Zero, Vector2(targetX, targetY), 0.0f, 1.0f)

  val default: Camera2D = Camera2D(Vector2.Zero, Vector2.Zero, 0.0f, 1.0f)
end Camera2D
