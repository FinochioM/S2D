package s2d.types

private[s2d] class FontData(
    val textureId: Int,
    val charData: Array[Float],
    val atlasWidth: Int,
    val atlasHeight: Int,
    val size: Float,
    val firstChar: Int,
    val numChars: Int,
)

opaque type FontType = FontData

object FontType:
    private[s2d] inline def apply(data: FontData): FontType = data
    private[s2d] inline def data(f: FontType): FontData = f