package s2d.types

opaque type Color = Int

object Color:
  inline def apply(r: Int, g: Int, b: Int, a: Int = 255): Color =
    val rc = if r < 0 then 0 else if r > 255 then 255 else r
    val gc = if g < 0 then 0 else if g > 255 then 255 else g
    val bc = if b < 0 then 0 else if b > 255 then 255 else b
    val ac = if a < 0 then 0 else if a > 255 then 255 else a
    (rc << 24) | (gc << 16) | (bc << 8) | ac

  val White          = Color(255, 255, 255)
  val Black          = Color(0, 0, 0)
  val Red            = Color(255, 0, 0)
  val Green          = Color(0, 255, 0)
  val Blue           = Color(0, 0, 255)
  val Yellow         = Color(255, 255, 0)
  val Magenta        = Color(255, 0, 255)
  val Cyan           = Color(0, 255, 255)
  val Transparent    = Color(0, 0, 0, 0)
  val Orange         = Color(255, 165, 0)
  val Pink           = Color(255, 192, 203)
  val Purple         = Color(128, 0, 128)
  val Brown          = Color(139, 69, 19)
  val Gray           = Color(128, 128, 128)
  val LightGray      = Color(211, 211, 211)
  val DarkGray       = Color(64, 64, 64)
  val Lime           = Color(0, 255, 0)
  val Olive          = Color(128, 128, 0)
  val Teal           = Color(0, 128, 128)
  val Navy           = Color(0, 0, 128)
  val Coral          = Color(255, 127, 80)
  val Gold           = Color(255, 215, 0)
  val Silver         = Color(192, 192, 192)
  val Maroon         = Color(128, 0, 0)
  val Indigo         = Color(75, 0, 130)
  val Violet         = Color(238, 130, 238)
  val Salmon         = Color(250, 128, 114)
  val Turquoise      = Color(64, 224, 208)
  val Beige          = Color(245, 245, 220)
  val Mint           = Color(189, 252, 201)
  val SkyBlue        = Color(135, 206, 235)
  val Lavender       = Color(230, 230, 250)
  val TransparentWhite = Color(255, 255, 255, 128)
  val TransparentBlack = Color(0, 0, 0, 128)
  val DarkBlue       = Color(11, 77, 107)

  def fromNormalized(r: Float, g: Float, b: Float, a: Float = 1.0f): Color =
    Color((r * 255).toInt, (g * 255).toInt, (b * 255).toInt, (a * 255).toInt)

  def fromHex(hex: String): Option[Color] =
    val clean = hex.stripPrefix("#")
    try
      clean.length match
        case 6 =>
          Some(Color(
            Integer.parseInt(clean.substring(0, 2), 16),
            Integer.parseInt(clean.substring(2, 4), 16),
            Integer.parseInt(clean.substring(4, 6), 16)
          ))
        case 8 =>
          Some(Color(
            Integer.parseInt(clean.substring(0, 2), 16),
            Integer.parseInt(clean.substring(2, 4), 16),
            Integer.parseInt(clean.substring(4, 6), 16),
            Integer.parseInt(clean.substring(6, 8), 16)
          ))
        case _ => None
    catch case _: NumberFormatException => None

  def fromHSV(hsv: HSV): Color =
    val c = hsv.v * hsv.s
    val x = c * (1.0f - math.abs((hsv.h / 60.0f) % 2.0f - 1.0f).toFloat)
    val m = hsv.v - c
    val (rf, gf, bf) = (hsv.h / 60.0f).toInt match
      case 0 => (c, x, 0.0f)
      case 1 => (x, c, 0.0f)
      case 2 => (0.0f, c, x)
      case 3 => (0.0f, x, c)
      case 4 => (x, 0.0f, c)
      case _ => (c, 0.0f, x)
    Color(((rf + m) * 255).toInt, ((gf + m) * 255).toInt, ((bf + m) * 255).toInt)

  extension (c: Color)
    inline def r: Int = (c >> 24) & 0xFF
    inline def g: Int = (c >> 16) & 0xFF
    inline def b: Int = (c >>  8) & 0xFF
    inline def a: Int =  c        & 0xFF

    inline def rNorm: Float = c.r / 255.0f
    inline def gNorm: Float = c.g / 255.0f
    inline def bNorm: Float = c.b / 255.0f
    inline def aNorm: Float = c.a / 255.0f

    inline def withAlpha(newAlpha: Int): Color   = Color(c.r, c.g, c.b, newAlpha)
    inline def withAlpha(newAlpha: Float): Color = Color(c.r, c.g, c.b, (newAlpha * 255).toInt)

    def lerp(other: Color, t: Float): Color =
      val ct = if t < 0.0f then 0.0f else if t > 1.0f then 1.0f else t
      Color(
        (c.r + (other.r - c.r) * ct).toInt,
        (c.g + (other.g - c.g) * ct).toInt,
        (c.b + (other.b - c.b) * ct).toInt,
        (c.a + (other.a - c.a) * ct).toInt
      )

    def brighten(factor: Float): Color =
      Color((c.r * factor).toInt, (c.g * factor).toInt, (c.b * factor).toInt, c.a)

    def darken(factor: Float): Color = c.brighten(1.0f - factor)

    def toHex: String          = f"#${c.r}%02x${c.g}%02x${c.b}%02x"
    def toHexWithAlpha: String = f"#${c.r}%02x${c.g}%02x${c.b}%02x${c.a}%02x"

    def invert: Color = Color(255 - c.r, 255 - c.g, 255 - c.b, c.a)

    def toGrayscale: Color =
      val lum = (0.299f * c.r + 0.587f * c.g + 0.114f * c.b).toInt
      Color(lum, lum, lum, c.a)

    def tint(other: Color): Color =
      Color(
        (c.r * other.r / 255.0f).toInt,
        (c.g * other.g / 255.0f).toInt,
        (c.b * other.b / 255.0f).toInt,
        (c.a * other.a / 255.0f).toInt
      )

    def toHSV: HSV =
      val rf = c.r / 255.0f; val gf = c.g / 255.0f; val bf = c.b / 255.0f
      val max = rf.max(gf).max(bf); val min = rf.min(gf).min(bf)
      val delta = max - min
      val h =
        if delta == 0.0f then 0.0f
        else if max == rf then 60.0f * (((gf - bf) / delta) % 6.0f)
        else if max == gf then 60.0f * (((bf - rf) / delta) + 2.0f)
        else                   60.0f * (((rf - gf) / delta) + 4.0f)
      HSV(if h < 0.0f then h + 360.0f else h, if max == 0.0f then 0.0f else delta / max, max)

end Color