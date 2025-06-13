package S2D.types

case class Color(r: Int, g: Int, b: Int, a: Int = 255):
  require(r >= 0 && r <= 255, s"Red component must be 0-255, got $r")
  require(g >= 0 && g <= 255, s"Green component must be 0-255, got $g")
  require(b >= 0 && b <= 255, s"Blue component must be 0-255, got $b")
  require(a >= 0 && a <= 255, s"Alpha component must be 0-255, got $a")

  def rNorm: Float = r / 255.0f
  def gNorm: Float = g / 255.0f
  def bNorm: Float = b / 255.0f
  def aNorm: Float = a / 255.0f

  def withAlpha(newAlpha: Int): Color = copy(a = newAlpha.max(0).min(255))
  def withAlpha(newAlpha: Float): Color = copy(a = (newAlpha * 255).toInt.max(0).min(255))

  def lerp(other: Color, t: Float): Color =
    val clampedT = t.max(0.0f).min(1.0f)
    Color(
      (r + (other.r - r) * clampedT).toInt,
      (g + (other.g - g) * clampedT).toInt,
      (b + (other.b - b) * clampedT).toInt,
      (a + (other.a - a) * clampedT).toInt,
    )

  def brighten(factor: Float): Color =
    Color(
      (r * factor).toInt.min(255),
      (g * factor).toInt.min(255),
      (b * factor).toInt.min(255),
      a
    )

  def darken(factor: Float): Color = brighten(1.0f - factor)

  def toHex: String = f"#$r%02x$g%02x$b%02x" // Fixed typo: was $g%02x$g%02x
  def toHexWithAlpha: String = f"#$r%02x$g%02x$b%02x$a%02x"

object Color:
  val White = Color(255, 255, 255)
  val Black = Color(0, 0, 0)
  val Red = Color(255, 0, 0)
  val Green = Color(0, 255, 0)
  val Blue = Color(0, 0, 255)
  val Yellow = Color(255, 255, 0)
  val Magenta = Color(255, 0, 255)
  val Cyan = Color(0, 255, 255)
  val Transparent = Color(0, 0, 0, 0)

  def fromNormalized(r: Float, g: Float, b: Float, a: Float = 1.0f): Color =
    Color(
      (r * 255).toInt.max(0).min(255),
      (g * 255).toInt.max(0).min(255),
      (b * 255).toInt.max(0).min(255),
      (a * 255).toInt.max(0).min(255)
    )

  def fromHex(hex: String): Option[Color] =
    val cleanHex = hex.stripPrefix("#")
    try
      cleanHex.length match
        case 6 => // RGB
          val r = Integer.parseInt(cleanHex.substring(0, 2), 16)
          val g = Integer.parseInt(cleanHex.substring(2, 4), 16)
          val b = Integer.parseInt(cleanHex.substring(4, 6), 16)
          Some(Color(r, g, b))
        case 8 => // RGBA
          val r = Integer.parseInt(cleanHex.substring(0, 2), 16)
          val g = Integer.parseInt(cleanHex.substring(2, 4), 16)
          val b = Integer.parseInt(cleanHex.substring(4, 6), 16)
          val a = Integer.parseInt(cleanHex.substring(6, 8), 16)
          Some(Color(r, g, b, a))
        case _ => None
    catch
      case _: NumberFormatException => None