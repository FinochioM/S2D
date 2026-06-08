package s2d.font

import s2d.types.{FontType, Color, Vector2}

object Text:
  def draw(text: String, x: Float, y: Float, font: FontType, color: Color): Unit =
    FontRenderer.drawText(text, x, y, font, color)

  def draw(text: String, position: Vector2, font: FontType, color: Color): Unit =
    FontRenderer.drawText(text, position.x, position.y, font, color)

  def measure(text: String, font: FontType): Vector2 =
    FontRenderer.measureText(text, font)
