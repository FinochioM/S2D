package s2d.types

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
  def withAlpha(newAlpha: Float): Color =
    copy(a = (newAlpha * 255).toInt.max(0).min(255))

  def lerp(other: Color, t: Float): Color =
    val clampedT = t.max(0.0f).min(1.0f)
    Color(
      (r + (other.r - r) * clampedT).toInt,
      (g + (other.g - g) * clampedT).toInt,
      (b + (other.b - b) * clampedT).toInt,
      (a + (other.a - a) * clampedT).toInt
    )
  end lerp

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
end Color

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
  val Orange = Color(255, 165, 0)
  val Pink = Color(255, 192, 203)
  val Purple = Color(128, 0, 128)
  val Brown = Color(139, 69, 19)
  val Gray = Color(128, 128, 128)
  val LightGray = Color(211, 211, 211)
  val DarkGray = Color(64, 64, 64)
  val Lime = Color(0, 255, 0)
  val Olive = Color(128, 128, 0)
  val Teal = Color(0, 128, 128)
  val Navy = Color(0, 0, 128)
  val Coral = Color(255, 127, 80)
  val Gold = Color(255, 215, 0)
  val Silver = Color(192, 192, 192)
  val Maroon = Color(128, 0, 0)
  val Indigo = Color(75, 0, 130)
  val Violet = Color(238, 130, 238)
  val Salmon = Color(250, 128, 114)
  val Turquoise = Color(64, 224, 208)
  val Beige = Color(245, 245, 220)
  val Mint = Color(189, 252, 201)
  val SkyBlue = Color(135, 206, 235)
  val Lavender = Color(230, 230, 250)
  val TransparentWhite = Color(255, 255, 255, 128)
  val TransparentBlack = Color(0, 0, 0, 128)

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
    catch case _: NumberFormatException => None
    end try
  end fromHex
end Color
