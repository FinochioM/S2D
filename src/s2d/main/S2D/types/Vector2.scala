package S2D.types

case class Vector2(x: Float, y: Float):
  def +(other: Vector2): Vector2 = Vector2(x + other.x, y + other.y)
  def -(other: Vector2): Vector2 = Vector2(x - other.x, y - other.y)
  def *(scalar: Float): Vector2 = Vector2(x * scalar, y * scalar)
  def /(scalar: Float): Vector2 = Vector2(x / scalar, y / scalar)
  def unary_- : Vector2 = Vector2(-x, -y)

  def dot(other: Vector2): Float = x * other.x + y * other.y

  def length: Float = math.sqrt(x * x + y * y).toFloat
  def lengthSquared: Float = x * x + y * y
  def distanceTo(other: Vector2): Float = (this - other).length
  def distanceSquaredTo(other: Vector2): Float = (this - other).lengthSquared

  def normalize: Vector2 =
    val len = length
    if len == 0.0f then Vector2.Zero else this / len

  def angle: Float = math.atan2(y, x).toFloat
  def angleTo(other: Vector2): Float = math.atan2(other.y - y, other.x - x).toFloat

  def lerp(other: Vector2, t: Float): Vector2 =
    this + (other - this) * t

  def rotate(angle: Float): Vector2 =
    val cos = math.cos(angle).toFloat
    val sin = math.sin(angle).toFloat
    Vector2(x * cos - y * sin, x * sin + y * cos)

object Vector2:
  val Zero = Vector2(0.0f, 0.0f)
  val One = Vector2(1.0f, 1.0f)
  val UnitX = Vector2(1.0f, 0.0f)
  val UnitY = Vector2(0.0f, 1.0f)

  def fromAngle(angle: Float, length: Float = 1.0f): Vector2 =
    Vector2(math.cos(angle).toFloat * length, math.sin(angle).toFloat * length)