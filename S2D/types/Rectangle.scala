package S2D.types

case class Rectangle(x: Float, y: Float, width: Float, height: Float):
  require(width >= 0, s"Width must be non-negative, got $width")
  require(height >= 0, s"Height must be non-negative, got $height")

  def left: Float = x
  def right: Float = x + width
  def top: Float = y
  def bottom: Float = y + height
  def centerX: Float = x + width / 2
  def centerY: Float = y + height / 2
  def center: Vector2 = Vector2(centerX, centerY)
  def topLeft: Vector2 = Vector2(x, y)
  def topRight: Vector2 = Vector2(right, y)
  def bottomLeft: Vector2 = Vector2(x, bottom)
  def bottomRight: Vector2 = Vector2(right, bottom)
  def size: Vector2 = Vector2(width, height)
  def area: Float = width * height
  def perimeter: Float = 2 * (width + height)
  def contains(point: Vector2): Boolean =
    point.x >= x && point.x <= right && point.y >= y && point.y <= bottom
  def contains(px: Float, py: Float): Boolean =
    px >= x && px <= right && py >= y && py <= bottom
  def intersects(other: Rectangle): Boolean =
    !(right <= other.x || x >= other.right || bottom <= other.y || y >= other.bottom)
  def intersection(other: Rectangle): Option[Rectangle] =
    val newLeft = math.max(x, other.x)
    val newTop = math.max(y, other.y)
    val newRight = math.min(right, other.right)
    val newBottom = math.min(bottom, other.bottom)

    if newLeft < newRight && newTop < newBottom then
      Some(Rectangle(newLeft, newTop, newRight - newLeft, newBottom - newTop))
    else
      None
  def union(other: Rectangle): Rectangle =
    val newLeft = math.min(x, other.x)
    val newTop = math.min(y, other.y)
    val newRight = math.max(right, other.right)
    val newBottom = math.max(bottom, other.bottom)
    Rectangle(newLeft, newTop, newRight - newLeft, newBottom - newTop)
  def expand(amount: Float): Rectangle =
    Rectangle(x - amount, y - amount, width + 2 * amount, height + 2 * amount)
  def expand(horizontal: Float, vertical: Float): Rectangle =
    Rectangle(x - horizontal, y - vertical, width + 2 * horizontal, height + 2 * vertical)
  def moveTo(newX: Float, newY: Float): Rectangle =
    copy(x = newX, y = newY)
  def moveTo(position: Vector2): Rectangle =
    copy(x = position.x, y = position.y)
  def moveBy(dx: Float, dy: Float): Rectangle =
    copy(x = x + dx, y = y + dy)
  def moveBy(offset: Vector2): Rectangle =
    copy(x = x + offset.x, y = y + offset.y)
  def scale(factor: Float): Rectangle =
    Rectangle(x, y, width * factor, height * factor)
  def scale(widthFactor: Float, heightFactor: Float): Rectangle =
    Rectangle(x, y, width * widthFactor, height * heightFactor)

object Rectangle:
  val Empty = Rectangle(0.0f, 0.0f, 0.0f, 0.0f)

  def fromPoints(point1: Vector2, point2: Vector2): Rectangle =
    val left = math.min(point1.x, point2.x)
    val top = math.min(point1.y, point2.y)
    val right = math.max(point1.x, point2.x)
    val bottom = math.max(point1.y, point2.y)
    Rectangle(left, top, right - left, bottom - top)
  def fromCenter(center: Vector2, size: Vector2): Rectangle =
    Rectangle(center.x - size.x / 2, center.y - size.y / 2, size.x, size.y)
  def fromCenter(centerX: Float, centerY: Float, width: Float, height: Float): Rectangle =
    Rectangle(centerX - width / 2, centerY - height / 2, width, height)