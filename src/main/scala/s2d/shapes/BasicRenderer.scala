package s2d.shapes

import s2d.types.{Color, Vector2, Rectangle}
import s2d.math.Matrix4
import s2d.core.Window
import s2d.backend.gl.GL.*
import s2d.backend.gl.GLExtras.*
import s2d.backend.gl.GLEWHelper
import s2d.core.{Shaders, Drawing}
import scalanative.unsafe.*
import scalanative.unsigned.*
import scala.scalanative.libc.stdlib

object BasicRenderer:
  private val vertexShaderSource = """
    #version 330 core

    layout (location = 0) in vec2 aPos;
    layout (location = 1) in vec4 aColor;

    uniform mat4 uProjection;

    out vec4 vertexColor;

    void main() {
        gl_Position = uProjection * vec4(aPos, 0.0, 1.0);
        vertexColor = aColor;
    }
  """

  private val fragmentShaderSource = """
    #version 330 core

    in vec4 vertexColor;
    out vec4 FragColor;

    void main() {
        FragColor = vertexColor;
    }
  """

  private val BATCH_MAX_VERTS  = 65536
  private val FLOATS_PER_VERT  = 6

  private var defaultShader: Option[s2d.types.Shader] = None
  private var VAO: UInt           = 0.toUInt
  private var VBO: UInt           = 0.toUInt
  private var immediateVAO: UInt  = 0.toUInt
  private var immediateVBO: UInt  = 0.toUInt
  private var projectionLocation: Int = -1
  private var isInitialized: Boolean  = false

  private val batchBuffer: Ptr[GLfloat] =
    stdlib.malloc(sizeof[GLfloat] * (BATCH_MAX_VERTS * FLOATS_PER_VERT).toUInt).asInstanceOf[Ptr[GLfloat]]
  private var batchCount: Int     = 0
  private var batchPrimitive: UInt = GL_TRIANGLES.toUInt

  private val scratchBuffer: Ptr[GLfloat] =
    stdlib.malloc(sizeof[GLfloat] * 2048.toUInt).asInstanceOf[Ptr[GLfloat]]

  private inline def checkBatch(primitive: UInt, vertsNeeded: Int): Unit =
    if batchPrimitive != primitive || batchCount + vertsNeeded > BATCH_MAX_VERTS then
      flush()
      batchPrimitive = primitive

  private inline def bv(x: Float, y: Float, r: Float, g: Float, b: Float, a: Float): Unit =
    val i = batchCount * FLOATS_PER_VERT
    batchBuffer(i)     = x; batchBuffer(i + 1) = y
    batchBuffer(i + 2) = r; batchBuffer(i + 3) = g
    batchBuffer(i + 4) = b; batchBuffer(i + 5) = a
    batchCount += 1

  def flush(): Unit =
    if batchCount == 0 then return
    defaultShader.foreach { shader =>
      Drawing.useProgram(shader.id.toUInt)
      GLEWHelper.glUniformMatrix4fv(projectionLocation, 1.toUInt, GL_FALSE, Drawing.getProjectionPtr)
    }
    GLEWHelper.glBindVertexArray(VAO)
    GLEWHelper.glBindBuffer(GL_ARRAY_BUFFER.toUInt, VBO)
    GLEWHelper.glBufferData(
      GL_ARRAY_BUFFER.toUInt,
      (batchCount * FLOATS_PER_VERT * sizeof[GLfloat].toInt),
      batchBuffer.asInstanceOf[Ptr[Byte]],
      GL_DYNAMIC_DRAW.toUInt
    )
    glDrawArrays(batchPrimitive, 0, batchCount.toUInt)
    GLEWHelper.glBindVertexArray(0.toUInt)
    batchCount = 0

  private inline def immediateSetup(customShader: s2d.types.Shader, color: Color): Unit =
    Drawing.useProgram(customShader.id.toUInt)
    val projLoc  = Drawing.getCustomProjLoc
    val colorLoc = Drawing.getCustomColorLoc
    if projLoc >= 0 then
      GLEWHelper.glUniformMatrix4fv(projLoc, 1.toUInt, GL_FALSE, Drawing.getProjectionPtr)
    if colorLoc >= 0 then
      GLEWHelper.glUniform4f(colorLoc, color.rNorm, color.gNorm, color.bNorm, color.aNorm)

  private inline def immediateDraw(vertCount: Int, floatCount: Int, primitive: UInt): Unit =
    GLEWHelper.glBindVertexArray(immediateVAO)
    GLEWHelper.glBindBuffer(GL_ARRAY_BUFFER.toUInt, immediateVBO)
    GLEWHelper.glBufferData(GL_ARRAY_BUFFER.toUInt, (floatCount * sizeof[GLfloat].toInt), scratchBuffer.asInstanceOf[Ptr[Byte]], GL_DYNAMIC_DRAW.toUInt)
    glDrawArrays(primitive, 0, vertCount.toUInt)
    GLEWHelper.glBindVertexArray(0.toUInt)

  def initialize(): Boolean =
    if isInitialized then return true

    defaultShader = Shaders.loadFromMemory(vertexShaderSource, fragmentShaderSource)

    defaultShader match
      case Some(shader) =>
        Zone {
          val projName = toCString("uProjection")
          projectionLocation = GLEWHelper.glGetUniformLocation(shader.id.toUInt, projName)
        }

        val vaoArr = stackalloc[GLuint](); val vboArr = stackalloc[GLuint]()
        GLEWHelper.glGenVertexArrays(1.toUInt, vaoArr)
        GLEWHelper.glGenBuffers(1.toUInt, vboArr)
        VAO = !vaoArr; VBO = !vboArr

        GLEWHelper.glBindVertexArray(VAO)
        GLEWHelper.glBindBuffer(GL_ARRAY_BUFFER.toUInt, VBO)
        GLEWHelper.glVertexAttribPointer(0.toUInt, 2, GL_FLOAT.toUInt, GL_FALSE, (FLOATS_PER_VERT * sizeof[GLfloat].toInt).toUInt, null)
        GLEWHelper.glEnableVertexAttribArray(0.toUInt)
        GLEWHelper.glVertexAttribPointer(1.toUInt, 4, GL_FLOAT.toUInt, GL_FALSE, (FLOATS_PER_VERT * sizeof[GLfloat].toInt).toUInt, (2 * sizeof[GLfloat].toInt).toPtr.asInstanceOf[Ptr[Byte]])
        GLEWHelper.glEnableVertexAttribArray(1.toUInt)
        GLEWHelper.glBindVertexArray(0.toUInt)

        val immVaoArr = stackalloc[GLuint](); val immVboArr = stackalloc[GLuint]()
        GLEWHelper.glGenVertexArrays(1.toUInt, immVaoArr)
        GLEWHelper.glGenBuffers(1.toUInt, immVboArr)
        immediateVAO = !immVaoArr; immediateVBO = !immVboArr

        GLEWHelper.glBindVertexArray(immediateVAO)
        GLEWHelper.glBindBuffer(GL_ARRAY_BUFFER.toUInt, immediateVBO)
        GLEWHelper.glVertexAttribPointer(0.toUInt, 2, GL_FLOAT.toUInt, GL_FALSE, (2 * sizeof[GLfloat].toInt).toUInt, null)
        GLEWHelper.glEnableVertexAttribArray(0.toUInt)
        GLEWHelper.glBindVertexArray(0.toUInt)

        isInitialized = true
        true

      case None =>
        println("Failed to create default shader for BasicRenderer")
        false
  end initialize

  def cleanup(): Unit =
    if isInitialized then
      val vaoArr = stackalloc[GLuint](); val vboArr = stackalloc[GLuint]()
      !vaoArr = VAO; !vboArr = VBO
      GLEWHelper.glDeleteVertexArrays(1.toUInt, vaoArr)
      GLEWHelper.glDeleteBuffers(1.toUInt, vboArr)

      val immVaoArr = stackalloc[GLuint](); val immVboArr = stackalloc[GLuint]()
      !immVaoArr = immediateVAO; !immVboArr = immediateVBO
      GLEWHelper.glDeleteVertexArrays(1.toUInt, immVaoArr)
      GLEWHelper.glDeleteBuffers(1.toUInt, immVboArr)

      defaultShader.foreach(Shaders.unloadShader)
      defaultShader = None
      isInitialized = false

  def updateProjectionFromDrawing(): Unit = ()  // projection set in flush()

  def renderPixel(x: Float, y: Float, color: Color): Unit =
    renderRectangle(x, y, 1.0f, 1.0f, color)

  def renderLine(startPos: Vector2, endPos: Vector2, color: Color): Unit =
    if !isInitialized then if !initialize() then return

    Drawing.getCurrentShader match
      case Some(cs) =>
        flush()
        immediateSetup(cs, color)
        scratchBuffer(0) = startPos.x; scratchBuffer(1) = startPos.y
        scratchBuffer(2) = endPos.x;   scratchBuffer(3) = endPos.y
        immediateDraw(2, 4, GL_LINES.toUInt)

      case None =>
        checkBatch(GL_LINES.toUInt, 2)
        val r = color.rNorm; val g = color.gNorm; val b = color.bNorm; val a = color.aNorm
        bv(startPos.x, startPos.y, r, g, b, a)
        bv(endPos.x,   endPos.y,   r, g, b, a)

  def renderThickLine(startPos: Vector2, endPos: Vector2, thick: Float, color: Color): Unit =
    if !isInitialized then if !initialize() then return

    val dx = endPos.x - startPos.x
    val dy = endPos.y - startPos.y
    val length = math.sqrt(dx * dx + dy * dy).toFloat
    if length == 0.0f then return

    val perpX = -dy / length * (thick / 2.0f)
    val perpY =  dx / length * (thick / 2.0f)

    val x0 = startPos.x + perpX; val y0 = startPos.y + perpY
    val x1 = startPos.x - perpX; val y1 = startPos.y - perpY
    val x2 = endPos.x   - perpX; val y2 = endPos.y   - perpY
    val x3 = endPos.x   + perpX; val y3 = endPos.y   + perpY

    Drawing.getCurrentShader match
      case Some(cs) =>
        flush()
        immediateSetup(cs, color)
        scratchBuffer(0) = x0; scratchBuffer(1) = y0
        scratchBuffer(2) = x1; scratchBuffer(3) = y1
        scratchBuffer(4) = x2; scratchBuffer(5) = y2
        scratchBuffer(6) = x0; scratchBuffer(7) = y0
        scratchBuffer(8) = x2; scratchBuffer(9) = y2
        scratchBuffer(10) = x3; scratchBuffer(11) = y3
        immediateDraw(6, 12, GL_TRIANGLES.toUInt)

      case None =>
        checkBatch(GL_TRIANGLES.toUInt, 6)
        val r = color.rNorm; val g = color.gNorm; val b = color.bNorm; val a = color.aNorm
        bv(x0, y0, r, g, b, a); bv(x1, y1, r, g, b, a); bv(x2, y2, r, g, b, a)
        bv(x0, y0, r, g, b, a); bv(x2, y2, r, g, b, a); bv(x3, y3, r, g, b, a)

  def renderLineStrip(points: Array[Vector2], color: Color): Unit =
    if !isInitialized then if !initialize() then return
    if points.length < 2 then return

    Drawing.getCurrentShader match
      case Some(cs) =>
        flush()
        immediateSetup(cs, color)
        var n = 0; var i = 0
        while i < points.length - 1 do
          scratchBuffer(n) = points(i).x;   n += 1; scratchBuffer(n) = points(i).y;   n += 1
          scratchBuffer(n) = points(i+1).x; n += 1; scratchBuffer(n) = points(i+1).y; n += 1
          i += 1
        immediateDraw(n / 2, n, GL_LINES.toUInt)

      case None =>
        val r = color.rNorm; val g = color.gNorm; val b = color.bNorm; val a = color.aNorm
        var i = 0
        while i < points.length - 1 do
          checkBatch(GL_LINES.toUInt, 2)
          bv(points(i).x, points(i).y, r, g, b, a)
          bv(points(i+1).x, points(i+1).y, r, g, b, a)
          i += 1

  def renderRectangle(x: Float, y: Float, width: Float, height: Float, color: Color): Unit =
    if !isInitialized then if !initialize() then return

    Drawing.getCurrentShader match
      case Some(cs) =>
        flush()
        immediateSetup(cs, color)
        scratchBuffer(0)  = x;         scratchBuffer(1)  = y
        scratchBuffer(2)  = x + width; scratchBuffer(3)  = y
        scratchBuffer(4)  = x;         scratchBuffer(5)  = y + height
        scratchBuffer(6)  = x + width; scratchBuffer(7)  = y
        scratchBuffer(8)  = x + width; scratchBuffer(9)  = y + height
        scratchBuffer(10) = x;         scratchBuffer(11) = y + height
        immediateDraw(6, 12, GL_TRIANGLES.toUInt)

      case None =>
        checkBatch(GL_TRIANGLES.toUInt, 6)
        val r = color.rNorm; val g = color.gNorm; val b = color.bNorm; val a = color.aNorm
        bv(x,         y,          r, g, b, a); bv(x + width, y,          r, g, b, a); bv(x,         y + height, r, g, b, a)
        bv(x + width, y,          r, g, b, a); bv(x + width, y + height, r, g, b, a); bv(x,         y + height, r, g, b, a)

  def renderRotatedRectangle(rectangle: Rectangle, origin: Vector2, rotation: Float, color: Color): Unit =
    if !isInitialized then if !initialize() then return

    val angleRad = math.toRadians(rotation).toFloat
    val cos = math.cos(angleRad).toFloat
    val sin = math.sin(angleRad).toFloat
    val ox = rectangle.x + origin.x; val oy = rectangle.y + origin.y

    val lx0 = -origin.x;                    val ly0 = -origin.y
    val lx1 = rectangle.width - origin.x;   val ly1 = -origin.y
    val lx2 = rectangle.width - origin.x;   val ly2 = rectangle.height - origin.y
    val lx3 = -origin.x;                    val ly3 = rectangle.height - origin.y

    val r0x = lx0*cos - ly0*sin + ox; val r0y = lx0*sin + ly0*cos + oy
    val r1x = lx1*cos - ly1*sin + ox; val r1y = lx1*sin + ly1*cos + oy
    val r2x = lx2*cos - ly2*sin + ox; val r2y = lx2*sin + ly2*cos + oy
    val r3x = lx3*cos - ly3*sin + ox; val r3y = lx3*sin + ly3*cos + oy

    Drawing.getCurrentShader match
      case Some(cs) =>
        flush()
        immediateSetup(cs, color)
        scratchBuffer(0) = r0x; scratchBuffer(1) = r0y
        scratchBuffer(2) = r1x; scratchBuffer(3) = r1y
        scratchBuffer(4) = r2x; scratchBuffer(5) = r2y
        scratchBuffer(6) = r0x; scratchBuffer(7) = r0y
        scratchBuffer(8) = r2x; scratchBuffer(9) = r2y
        scratchBuffer(10) = r3x; scratchBuffer(11) = r3y
        immediateDraw(6, 12, GL_TRIANGLES.toUInt)

      case None =>
        checkBatch(GL_TRIANGLES.toUInt, 6)
        val r = color.rNorm; val g = color.gNorm; val b = color.bNorm; val a = color.aNorm
        bv(r0x, r0y, r, g, b, a); bv(r1x, r1y, r, g, b, a); bv(r2x, r2y, r, g, b, a)
        bv(r0x, r0y, r, g, b, a); bv(r2x, r2y, r, g, b, a); bv(r3x, r3y, r, g, b, a)

  def renderRectangleOutline(x: Float, y: Float, width: Float, height: Float, color: Color): Unit =
    if !isInitialized then if !initialize() then return

    Drawing.getCurrentShader match
      case Some(cs) =>
        flush()
        immediateSetup(cs, color)
        scratchBuffer(0)  = x;         scratchBuffer(1)  = y
        scratchBuffer(2)  = x + width; scratchBuffer(3)  = y
        scratchBuffer(4)  = x + width; scratchBuffer(5)  = y
        scratchBuffer(6)  = x + width; scratchBuffer(7)  = y + height
        scratchBuffer(8)  = x + width; scratchBuffer(9)  = y + height
        scratchBuffer(10) = x;         scratchBuffer(11) = y + height
        scratchBuffer(12) = x;         scratchBuffer(13) = y + height
        scratchBuffer(14) = x;         scratchBuffer(15) = y
        immediateDraw(8, 16, GL_LINES.toUInt)

      case None =>
        checkBatch(GL_LINES.toUInt, 8)
        val r = color.rNorm; val g = color.gNorm; val b = color.bNorm; val a = color.aNorm
        bv(x,         y,          r, g, b, a); bv(x + width, y,          r, g, b, a)
        bv(x + width, y,          r, g, b, a); bv(x + width, y + height, r, g, b, a)
        bv(x + width, y + height, r, g, b, a); bv(x,         y + height, r, g, b, a)
        bv(x,         y + height, r, g, b, a); bv(x,         y,          r, g, b, a)

  def renderRoundedRectangle(rect: Rectangle, roundness: Float, segments: Int, color: Color): Unit =
    if !isInitialized then if !initialize() then return
    if segments < 3 then return
    if roundness <= 0.0f then
      renderRectangle(rect.x, rect.y, rect.width, rect.height, color)
      return

    val maxRadius    = math.min(rect.width, rect.height) / 2.0f
    val cornerRadius = (roundness * maxRadius).min(maxRadius)
    val innerWidth   = rect.width  - 2 * cornerRadius
    val innerHeight  = rect.height - 2 * cornerRadius

    val r = color.rNorm; val g = color.gNorm; val b = color.bNorm; val a = color.aNorm

    Drawing.getCurrentShader match
      case Some(cs) =>
        flush()
        immediateSetup(cs, color)
        var n = 0

        def addQuad(x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float, x4: Float, y4: Float): Unit =
          scratchBuffer(n) = x1; n+=1; scratchBuffer(n) = y1; n+=1
          scratchBuffer(n) = x2; n+=1; scratchBuffer(n) = y2; n+=1
          scratchBuffer(n) = x3; n+=1; scratchBuffer(n) = y3; n+=1
          scratchBuffer(n) = x1; n+=1; scratchBuffer(n) = y1; n+=1
          scratchBuffer(n) = x3; n+=1; scratchBuffer(n) = y3; n+=1
          scratchBuffer(n) = x4; n+=1; scratchBuffer(n) = y4; n+=1

        if innerWidth > 0 && innerHeight > 0 then addQuad(rect.x+cornerRadius, rect.y+cornerRadius, rect.x+cornerRadius+innerWidth, rect.y+cornerRadius, rect.x+cornerRadius+innerWidth, rect.y+cornerRadius+innerHeight, rect.x+cornerRadius, rect.y+cornerRadius+innerHeight)
        if innerWidth > 0 then addQuad(rect.x+cornerRadius, rect.y, rect.x+cornerRadius+innerWidth, rect.y, rect.x+cornerRadius+innerWidth, rect.y+cornerRadius, rect.x+cornerRadius, rect.y+cornerRadius)
        if innerWidth > 0 then addQuad(rect.x+cornerRadius, rect.y+cornerRadius+innerHeight, rect.x+cornerRadius+innerWidth, rect.y+cornerRadius+innerHeight, rect.x+cornerRadius+innerWidth, rect.y+rect.height, rect.x+cornerRadius, rect.y+rect.height)
        if innerHeight > 0 then addQuad(rect.x, rect.y+cornerRadius, rect.x+cornerRadius, rect.y+cornerRadius, rect.x+cornerRadius, rect.y+cornerRadius+innerHeight, rect.x, rect.y+cornerRadius+innerHeight)
        if innerHeight > 0 then addQuad(rect.x+cornerRadius+innerWidth, rect.y+cornerRadius, rect.x+rect.width, rect.y+cornerRadius, rect.x+rect.width, rect.y+cornerRadius+innerHeight, rect.x+cornerRadius+innerWidth, rect.y+cornerRadius+innerHeight)

        def addCorner(cx: Float, cy: Float, startAngle: Float): Unit =
          val angleStep = (math.Pi / 2.0f) / segments.toFloat
          var i = 0
          while i < segments do
            val a1 = startAngle + (i * angleStep); val a2 = startAngle + ((i+1) * angleStep)
            scratchBuffer(n) = cx; n+=1; scratchBuffer(n) = cy; n+=1
            scratchBuffer(n) = cx + cornerRadius*math.cos(a1).toFloat; n+=1; scratchBuffer(n) = cy + cornerRadius*math.sin(a1).toFloat; n+=1
            scratchBuffer(n) = cx + cornerRadius*math.cos(a2).toFloat; n+=1; scratchBuffer(n) = cy + cornerRadius*math.sin(a2).toFloat; n+=1
            i += 1

        addCorner(rect.x+cornerRadius, rect.y+cornerRadius, math.Pi.toFloat)
        addCorner(rect.x+rect.width-cornerRadius, rect.y+cornerRadius, 3*math.Pi.toFloat/2)
        addCorner(rect.x+rect.width-cornerRadius, rect.y+rect.height-cornerRadius, 0.0f)
        addCorner(rect.x+cornerRadius, rect.y+rect.height-cornerRadius, math.Pi.toFloat/2)

        if n > 0 then immediateDraw(n/2, n, GL_TRIANGLES.toUInt)

      case None =>
        def addQuad(x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float, x4: Float, y4: Float): Unit =
          checkBatch(GL_TRIANGLES.toUInt, 6)
          bv(x1, y1, r, g, b, a); bv(x2, y2, r, g, b, a); bv(x3, y3, r, g, b, a)
          bv(x1, y1, r, g, b, a); bv(x3, y3, r, g, b, a); bv(x4, y4, r, g, b, a)

        if innerWidth > 0 && innerHeight > 0 then addQuad(rect.x+cornerRadius, rect.y+cornerRadius, rect.x+cornerRadius+innerWidth, rect.y+cornerRadius, rect.x+cornerRadius+innerWidth, rect.y+cornerRadius+innerHeight, rect.x+cornerRadius, rect.y+cornerRadius+innerHeight)
        if innerWidth > 0 then addQuad(rect.x+cornerRadius, rect.y, rect.x+cornerRadius+innerWidth, rect.y, rect.x+cornerRadius+innerWidth, rect.y+cornerRadius, rect.x+cornerRadius, rect.y+cornerRadius)
        if innerWidth > 0 then addQuad(rect.x+cornerRadius, rect.y+cornerRadius+innerHeight, rect.x+cornerRadius+innerWidth, rect.y+cornerRadius+innerHeight, rect.x+cornerRadius+innerWidth, rect.y+rect.height, rect.x+cornerRadius, rect.y+rect.height)
        if innerHeight > 0 then addQuad(rect.x, rect.y+cornerRadius, rect.x+cornerRadius, rect.y+cornerRadius, rect.x+cornerRadius, rect.y+cornerRadius+innerHeight, rect.x, rect.y+cornerRadius+innerHeight)
        if innerHeight > 0 then addQuad(rect.x+cornerRadius+innerWidth, rect.y+cornerRadius, rect.x+rect.width, rect.y+cornerRadius, rect.x+rect.width, rect.y+cornerRadius+innerHeight, rect.x+cornerRadius+innerWidth, rect.y+cornerRadius+innerHeight)

        def addCorner(cx: Float, cy: Float, startAngle: Float): Unit =
          val angleStep = (math.Pi / 2.0f) / segments.toFloat
          var i = 0
          while i < segments do
            val a1 = startAngle + (i * angleStep); val a2 = startAngle + ((i+1) * angleStep)
            checkBatch(GL_TRIANGLES.toUInt, 3)
            bv(cx, cy, r, g, b, a)
            bv(cx + cornerRadius*math.cos(a1).toFloat, cy + cornerRadius*math.sin(a1).toFloat, r, g, b, a)
            bv(cx + cornerRadius*math.cos(a2).toFloat, cy + cornerRadius*math.sin(a2).toFloat, r, g, b, a)
            i += 1

        addCorner(rect.x+cornerRadius, rect.y+cornerRadius, math.Pi.toFloat)
        addCorner(rect.x+rect.width-cornerRadius, rect.y+cornerRadius, 3*math.Pi.toFloat/2)
        addCorner(rect.x+rect.width-cornerRadius, rect.y+rect.height-cornerRadius, 0.0f)
        addCorner(rect.x+cornerRadius, rect.y+rect.height-cornerRadius, math.Pi.toFloat/2)

  def renderRoundedRectangleOutline(rectangle: Rectangle, roundness: Float, segments: Int, color: Color): Unit =
    if !isInitialized then if !initialize() then return
    if segments < 3 then return
    if roundness <= 0.0f then
      renderRectangleOutline(rectangle.x, rectangle.y, rectangle.width, rectangle.height, color)
      return

    val maxRadius    = math.min(rectangle.width, rectangle.height) / 2.0f
    val cornerRadius = (roundness * maxRadius).min(maxRadius)
    val angleStep    = (math.Pi / 2.0f) / segments.toFloat
    val r = color.rNorm; val g = color.gNorm; val b = color.bNorm; val a = color.aNorm

    Drawing.getCurrentShader match
      case Some(cs) =>
        flush()
        immediateSetup(cs, color)
        var n = 0
        def sv(x: Float, y: Float): Unit = { scratchBuffer(n) = x; n+=1; scratchBuffer(n) = y; n+=1 }

        sv(rectangle.x+cornerRadius, rectangle.y)
        sv(rectangle.x+rectangle.width-cornerRadius, rectangle.y)

        var i = 0
        while i < segments do
          val a1 = (3*math.Pi/2.0f)+(i*angleStep); val a2 = (3*math.Pi/2.0f)+((i+1)*angleStep)
          sv(rectangle.x+rectangle.width-cornerRadius+cornerRadius*math.cos(a1).toFloat, rectangle.y+cornerRadius+cornerRadius*math.sin(a1).toFloat)
          sv(rectangle.x+rectangle.width-cornerRadius+cornerRadius*math.cos(a2).toFloat, rectangle.y+cornerRadius+cornerRadius*math.sin(a2).toFloat)
          i += 1

        sv(rectangle.x+rectangle.width, rectangle.y+cornerRadius)
        sv(rectangle.x+rectangle.width, rectangle.y+rectangle.height-cornerRadius)

        i = 0
        while i < segments do
          val a1 = i*angleStep; val a2 = (i+1)*angleStep
          sv(rectangle.x+rectangle.width-cornerRadius+cornerRadius*math.cos(a1).toFloat, rectangle.y+rectangle.height-cornerRadius+cornerRadius*math.sin(a1).toFloat)
          sv(rectangle.x+rectangle.width-cornerRadius+cornerRadius*math.cos(a2).toFloat, rectangle.y+rectangle.height-cornerRadius+cornerRadius*math.sin(a2).toFloat)
          i += 1

        sv(rectangle.x+rectangle.width-cornerRadius, rectangle.y+rectangle.height)
        sv(rectangle.x+cornerRadius, rectangle.y+rectangle.height)

        i = 0
        while i < segments do
          val a1 = (math.Pi/2.0f)+(i*angleStep); val a2 = (math.Pi/2.0f)+((i+1)*angleStep)
          sv(rectangle.x+cornerRadius+cornerRadius*math.cos(a1).toFloat, rectangle.y+rectangle.height-cornerRadius+cornerRadius*math.sin(a1).toFloat)
          sv(rectangle.x+cornerRadius+cornerRadius*math.cos(a2).toFloat, rectangle.y+rectangle.height-cornerRadius+cornerRadius*math.sin(a2).toFloat)
          i += 1

        sv(rectangle.x, rectangle.y+rectangle.height-cornerRadius)
        sv(rectangle.x, rectangle.y+cornerRadius)

        i = 0
        while i < segments do
          val a1 = math.Pi.toFloat+(i*angleStep); val a2 = math.Pi.toFloat+((i+1)*angleStep)
          sv(rectangle.x+cornerRadius+cornerRadius*math.cos(a1).toFloat, rectangle.y+cornerRadius+cornerRadius*math.sin(a1).toFloat)
          sv(rectangle.x+cornerRadius+cornerRadius*math.cos(a2).toFloat, rectangle.y+cornerRadius+cornerRadius*math.sin(a2).toFloat)
          i += 1

        if n > 0 then immediateDraw(n/2, n, GL_LINES.toUInt)

      case None =>
        def seg(x1: Float, y1: Float, x2: Float, y2: Float): Unit =
          checkBatch(GL_LINES.toUInt, 2)
          bv(x1, y1, r, g, b, a); bv(x2, y2, r, g, b, a)

        seg(rectangle.x+cornerRadius, rectangle.y, rectangle.x+rectangle.width-cornerRadius, rectangle.y)

        var i = 0
        while i < segments do
          val a1 = (3*math.Pi/2.0f)+(i*angleStep); val a2 = (3*math.Pi/2.0f)+((i+1)*angleStep)
          seg(rectangle.x+rectangle.width-cornerRadius+cornerRadius*math.cos(a1).toFloat, rectangle.y+cornerRadius+cornerRadius*math.sin(a1).toFloat,
              rectangle.x+rectangle.width-cornerRadius+cornerRadius*math.cos(a2).toFloat, rectangle.y+cornerRadius+cornerRadius*math.sin(a2).toFloat)
          i += 1

        seg(rectangle.x+rectangle.width, rectangle.y+cornerRadius, rectangle.x+rectangle.width, rectangle.y+rectangle.height-cornerRadius)

        i = 0
        while i < segments do
          val a1 = i*angleStep; val a2 = (i+1)*angleStep
          seg(rectangle.x+rectangle.width-cornerRadius+cornerRadius*math.cos(a1).toFloat, rectangle.y+rectangle.height-cornerRadius+cornerRadius*math.sin(a1).toFloat,
              rectangle.x+rectangle.width-cornerRadius+cornerRadius*math.cos(a2).toFloat, rectangle.y+rectangle.height-cornerRadius+cornerRadius*math.sin(a2).toFloat)
          i += 1

        seg(rectangle.x+rectangle.width-cornerRadius, rectangle.y+rectangle.height, rectangle.x+cornerRadius, rectangle.y+rectangle.height)

        i = 0
        while i < segments do
          val a1 = (math.Pi/2.0f)+(i*angleStep); val a2 = (math.Pi/2.0f)+((i+1)*angleStep)
          seg(rectangle.x+cornerRadius+cornerRadius*math.cos(a1).toFloat, rectangle.y+rectangle.height-cornerRadius+cornerRadius*math.sin(a1).toFloat,
              rectangle.x+cornerRadius+cornerRadius*math.cos(a2).toFloat, rectangle.y+rectangle.height-cornerRadius+cornerRadius*math.sin(a2).toFloat)
          i += 1

        seg(rectangle.x, rectangle.y+rectangle.height-cornerRadius, rectangle.x, rectangle.y+cornerRadius)

        i = 0
        while i < segments do
          val a1 = math.Pi.toFloat+(i*angleStep); val a2 = math.Pi.toFloat+((i+1)*angleStep)
          seg(rectangle.x+cornerRadius+cornerRadius*math.cos(a1).toFloat, rectangle.y+cornerRadius+cornerRadius*math.sin(a1).toFloat,
              rectangle.x+cornerRadius+cornerRadius*math.cos(a2).toFloat, rectangle.y+cornerRadius+cornerRadius*math.sin(a2).toFloat)
          i += 1

  def renderTriangle(v1: Vector2, v2: Vector2, v3: Vector2, color: Color): Unit =
    if !isInitialized then if !initialize() then return

    Drawing.getCurrentShader match
      case Some(cs) =>
        flush()
        immediateSetup(cs, color)
        scratchBuffer(0) = v1.x; scratchBuffer(1) = v1.y
        scratchBuffer(2) = v2.x; scratchBuffer(3) = v2.y
        scratchBuffer(4) = v3.x; scratchBuffer(5) = v3.y
        immediateDraw(3, 6, GL_TRIANGLES.toUInt)

      case None =>
        checkBatch(GL_TRIANGLES.toUInt, 3)
        val r = color.rNorm; val g = color.gNorm; val b = color.bNorm; val a = color.aNorm
        bv(v1.x, v1.y, r, g, b, a); bv(v2.x, v2.y, r, g, b, a); bv(v3.x, v3.y, r, g, b, a)

  def renderTriangleOutline(v1: Vector2, v2: Vector2, v3: Vector2, color: Color): Unit =
    if !isInitialized then if !initialize() then return

    Drawing.getCurrentShader match
      case Some(cs) =>
        flush()
        immediateSetup(cs, color)
        scratchBuffer(0)  = v1.x; scratchBuffer(1)  = v1.y
        scratchBuffer(2)  = v2.x; scratchBuffer(3)  = v2.y
        scratchBuffer(4)  = v2.x; scratchBuffer(5)  = v2.y
        scratchBuffer(6)  = v3.x; scratchBuffer(7)  = v3.y
        scratchBuffer(8)  = v3.x; scratchBuffer(9)  = v3.y
        scratchBuffer(10) = v1.x; scratchBuffer(11) = v1.y
        immediateDraw(6, 12, GL_LINES.toUInt)

      case None =>
        checkBatch(GL_LINES.toUInt, 6)
        val r = color.rNorm; val g = color.gNorm; val b = color.bNorm; val a = color.aNorm
        bv(v1.x, v1.y, r, g, b, a); bv(v2.x, v2.y, r, g, b, a)
        bv(v2.x, v2.y, r, g, b, a); bv(v3.x, v3.y, r, g, b, a)
        bv(v3.x, v3.y, r, g, b, a); bv(v1.x, v1.y, r, g, b, a)

  def renderTriangleFan(points: Array[Vector2], color: Color): Unit =
    if !isInitialized then if !initialize() then return
    if points.length < 3 then return

    val cx = points(0).x; val cy = points(0).y
    val r = color.rNorm; val g = color.gNorm; val b = color.bNorm; val a = color.aNorm

    Drawing.getCurrentShader match
      case Some(cs) =>
        flush()
        immediateSetup(cs, color)
        var n = 0; var i = 1
        while i < points.length - 1 do
          scratchBuffer(n) = cx;            n+=1; scratchBuffer(n) = cy;            n+=1
          scratchBuffer(n) = points(i).x;   n+=1; scratchBuffer(n) = points(i).y;   n+=1
          scratchBuffer(n) = points(i+1).x; n+=1; scratchBuffer(n) = points(i+1).y; n+=1
          i += 1
        if n > 0 then immediateDraw(n/2, n, GL_TRIANGLES.toUInt)

      case None =>
        var i = 1
        while i < points.length - 1 do
          checkBatch(GL_TRIANGLES.toUInt, 3)
          bv(cx, cy, r, g, b, a)
          bv(points(i).x,   points(i).y,   r, g, b, a)
          bv(points(i+1).x, points(i+1).y, r, g, b, a)
          i += 1

  def renderTriangleStrip(points: Array[Vector2], color: Color): Unit =
    if !isInitialized then if !initialize() then return
    if points.length < 3 then return

    val r = color.rNorm; val g = color.gNorm; val b = color.bNorm; val a = color.aNorm

    Drawing.getCurrentShader match
      case Some(cs) =>
        flush()
        immediateSetup(cs, color)
        var n = 0; var i = 0
        while i < points.length - 2 do
          if i % 2 == 0 then
            scratchBuffer(n) = points(i).x;   n+=1; scratchBuffer(n) = points(i).y;   n+=1
            scratchBuffer(n) = points(i+1).x; n+=1; scratchBuffer(n) = points(i+1).y; n+=1
            scratchBuffer(n) = points(i+2).x; n+=1; scratchBuffer(n) = points(i+2).y; n+=1
          else
            scratchBuffer(n) = points(i).x;   n+=1; scratchBuffer(n) = points(i).y;   n+=1
            scratchBuffer(n) = points(i+2).x; n+=1; scratchBuffer(n) = points(i+2).y; n+=1
            scratchBuffer(n) = points(i+1).x; n+=1; scratchBuffer(n) = points(i+1).y; n+=1
          i += 1
        if n > 0 then immediateDraw(n/2, n, GL_TRIANGLES.toUInt)

      case None =>
        var i = 0
        while i < points.length - 2 do
          checkBatch(GL_TRIANGLES.toUInt, 3)
          if i % 2 == 0 then
            bv(points(i).x, points(i).y, r, g, b, a)
            bv(points(i+1).x, points(i+1).y, r, g, b, a)
            bv(points(i+2).x, points(i+2).y, r, g, b, a)
          else
            bv(points(i).x, points(i).y, r, g, b, a)
            bv(points(i+2).x, points(i+2).y, r, g, b, a)
            bv(points(i+1).x, points(i+1).y, r, g, b, a)
          i += 1

  def renderPolygon(center: Vector2, sides: Int, radius: Float, rotation: Float, color: Color): Unit =
    if !isInitialized then if !initialize() then return
    if sides < 3 then return

    val rotRad    = math.toRadians(rotation).toFloat
    val angleStep = (2.0f * math.Pi / sides.toFloat).toFloat
    val r = color.rNorm; val g = color.gNorm; val b = color.bNorm; val a = color.aNorm

    Drawing.getCurrentShader match
      case Some(cs) =>
        flush()
        immediateSetup(cs, color)
        var n = 0; var i = 0
        while i < sides do
          val a1 = (i * angleStep) + rotRad; val a2 = ((i+1) * angleStep) + rotRad
          scratchBuffer(n) = center.x; n+=1; scratchBuffer(n) = center.y; n+=1
          scratchBuffer(n) = center.x + radius*math.cos(a1).toFloat; n+=1; scratchBuffer(n) = center.y + radius*math.sin(a1).toFloat; n+=1
          scratchBuffer(n) = center.x + radius*math.cos(a2).toFloat; n+=1; scratchBuffer(n) = center.y + radius*math.sin(a2).toFloat; n+=1
          i += 1
        immediateDraw(n/2, n, GL_TRIANGLES.toUInt)

      case None =>
        var i = 0
        while i < sides do
          val a1 = (i * angleStep) + rotRad; val a2 = ((i+1) * angleStep) + rotRad
          checkBatch(GL_TRIANGLES.toUInt, 3)
          bv(center.x, center.y, r, g, b, a)
          bv(center.x + radius*math.cos(a1).toFloat, center.y + radius*math.sin(a1).toFloat, r, g, b, a)
          bv(center.x + radius*math.cos(a2).toFloat, center.y + radius*math.sin(a2).toFloat, r, g, b, a)
          i += 1

  def renderPolygonOutline(center: Vector2, sides: Int, radius: Float, rotation: Float, color: Color): Unit =
    if !isInitialized then if !initialize() then return
    if sides < 3 then return

    val rotRad    = math.toRadians(rotation).toFloat
    val angleStep = (2.0f * math.Pi / sides.toFloat).toFloat
    val r = color.rNorm; val g = color.gNorm; val b = color.bNorm; val a = color.aNorm

    Drawing.getCurrentShader match
      case Some(cs) =>
        flush()
        immediateSetup(cs, color)
        var n = 0; var i = 0
        while i < sides do
          val angle  = (i * angleStep) + rotRad
          val angleN = ((i+1) % sides * angleStep) + rotRad
          scratchBuffer(n) = center.x + radius*math.cos(angle).toFloat;  n+=1
          scratchBuffer(n) = center.y + radius*math.sin(angle).toFloat;  n+=1
          scratchBuffer(n) = center.x + radius*math.cos(angleN).toFloat; n+=1
          scratchBuffer(n) = center.y + radius*math.sin(angleN).toFloat; n+=1
          i += 1
        immediateDraw(n/2, n, GL_LINES.toUInt)

      case None =>
        var i = 0
        while i < sides do
          val angle  = (i * angleStep) + rotRad
          val angleN = ((i+1) % sides * angleStep) + rotRad
          checkBatch(GL_LINES.toUInt, 2)
          bv(center.x + radius*math.cos(angle).toFloat,  center.y + radius*math.sin(angle).toFloat,  r, g, b, a)
          bv(center.x + radius*math.cos(angleN).toFloat, center.y + radius*math.sin(angleN).toFloat, r, g, b, a)
          i += 1

  def renderCircle(centerX: Float, centerY: Float, radius: Float, color: Color): Unit =
    if !isInitialized then if !initialize() then return

    val segments  = circleSegments(radius)
    val r = color.rNorm; val g = color.gNorm; val b = color.bNorm; val a = color.aNorm

    Drawing.getCurrentShader match
      case Some(cs) =>
        flush()
        immediateSetup(cs, color)
        var n = 0; var i = 0
        while i < segments do
          val a1 = (i * 2.0f * math.Pi / segments).toFloat
          val a2 = ((i+1) * 2.0f * math.Pi / segments).toFloat
          scratchBuffer(n) = centerX; n+=1; scratchBuffer(n) = centerY; n+=1
          scratchBuffer(n) = centerX + radius*math.cos(a1).toFloat; n+=1; scratchBuffer(n) = centerY + radius*math.sin(a1).toFloat; n+=1
          scratchBuffer(n) = centerX + radius*math.cos(a2).toFloat; n+=1; scratchBuffer(n) = centerY + radius*math.sin(a2).toFloat; n+=1
          i += 1
        immediateDraw(n/2, n, GL_TRIANGLES.toUInt)

      case None =>
        var i = 0
        while i < segments do
          val a1 = (i * 2.0f * math.Pi / segments).toFloat
          val a2 = ((i+1) * 2.0f * math.Pi / segments).toFloat
          checkBatch(GL_TRIANGLES.toUInt, 3)
          bv(centerX, centerY, r, g, b, a)
          bv(centerX + radius*math.cos(a1).toFloat, centerY + radius*math.sin(a1).toFloat, r, g, b, a)
          bv(centerX + radius*math.cos(a2).toFloat, centerY + radius*math.sin(a2).toFloat, r, g, b, a)
          i += 1

  def renderCircleSector(center: Vector2, radius: Float, startAngle: Float, endAngle: Float, segments: Int, color: Color): Unit =
    if !isInitialized then if !initialize() then return
    if segments < 3 then return

    val startRad  = math.toRadians(startAngle).toFloat
    val endRad    = math.toRadians(endAngle).toFloat
    val angleStep = (endRad - startRad) / segments.toFloat
    val r = color.rNorm; val g = color.gNorm; val b = color.bNorm; val a = color.aNorm

    Drawing.getCurrentShader match
      case Some(cs) =>
        flush()
        immediateSetup(cs, color)
        var n = 0; var i = 0
        while i < segments do
          val a1 = startRad + (i * angleStep); val a2 = startRad + ((i+1) * angleStep)
          scratchBuffer(n) = center.x; n+=1; scratchBuffer(n) = center.y; n+=1
          scratchBuffer(n) = center.x + radius*math.cos(a1).toFloat; n+=1; scratchBuffer(n) = center.y + radius*math.sin(a1).toFloat; n+=1
          scratchBuffer(n) = center.x + radius*math.cos(a2).toFloat; n+=1; scratchBuffer(n) = center.y + radius*math.sin(a2).toFloat; n+=1
          i += 1
        immediateDraw(n/2, n, GL_TRIANGLES.toUInt)

      case None =>
        var i = 0
        while i < segments do
          val a1 = startRad + (i * angleStep); val a2 = startRad + ((i+1) * angleStep)
          checkBatch(GL_TRIANGLES.toUInt, 3)
          bv(center.x, center.y, r, g, b, a)
          bv(center.x + radius*math.cos(a1).toFloat, center.y + radius*math.sin(a1).toFloat, r, g, b, a)
          bv(center.x + radius*math.cos(a2).toFloat, center.y + radius*math.sin(a2).toFloat, r, g, b, a)
          i += 1

  def renderCircleSectorOutline(center: Vector2, radius: Float, startAngle: Float, endAngle: Float, segments: Int, color: Color): Unit =
    if !isInitialized then if !initialize() then return
    if segments < 3 then return

    val startRad  = math.toRadians(startAngle).toFloat
    val endRad    = math.toRadians(endAngle).toFloat
    val angleStep = (endRad - startRad) / segments.toFloat
    val r = color.rNorm; val g = color.gNorm; val b = color.bNorm; val a = color.aNorm

    Drawing.getCurrentShader match
      case Some(cs) =>
        flush()
        immediateSetup(cs, color)
        var n = 0; var i = 0
        while i < segments do
          val a1 = startRad + (i * angleStep); val a2 = startRad + ((i+1) * angleStep)
          scratchBuffer(n) = center.x + radius*math.cos(a1).toFloat; n+=1; scratchBuffer(n) = center.y + radius*math.sin(a1).toFloat; n+=1
          scratchBuffer(n) = center.x + radius*math.cos(a2).toFloat; n+=1; scratchBuffer(n) = center.y + radius*math.sin(a2).toFloat; n+=1
          i += 1
        scratchBuffer(n) = center.x; n+=1; scratchBuffer(n) = center.y; n+=1
        scratchBuffer(n) = center.x + radius*math.cos(startRad).toFloat; n+=1; scratchBuffer(n) = center.y + radius*math.sin(startRad).toFloat; n+=1
        scratchBuffer(n) = center.x; n+=1; scratchBuffer(n) = center.y; n+=1
        scratchBuffer(n) = center.x + radius*math.cos(endRad).toFloat; n+=1; scratchBuffer(n) = center.y + radius*math.sin(endRad).toFloat; n+=1
        immediateDraw(n/2, n, GL_LINES.toUInt)

      case None =>
        var i = 0
        while i < segments do
          val a1 = startRad + (i * angleStep); val a2 = startRad + ((i+1) * angleStep)
          checkBatch(GL_LINES.toUInt, 2)
          bv(center.x + radius*math.cos(a1).toFloat, center.y + radius*math.sin(a1).toFloat, r, g, b, a)
          bv(center.x + radius*math.cos(a2).toFloat, center.y + radius*math.sin(a2).toFloat, r, g, b, a)
          i += 1
        checkBatch(GL_LINES.toUInt, 4)
        bv(center.x, center.y, r, g, b, a)
        bv(center.x + radius*math.cos(startRad).toFloat, center.y + radius*math.sin(startRad).toFloat, r, g, b, a)
        bv(center.x, center.y, r, g, b, a)
        bv(center.x + radius*math.cos(endRad).toFloat, center.y + radius*math.sin(endRad).toFloat, r, g, b, a)

  def renderCircleOutline(centerX: Float, centerY: Float, radius: Float, color: Color): Unit =
    if !isInitialized then if !initialize() then return

    val segments  = circleSegments(radius)
    val r = color.rNorm; val g = color.gNorm; val b = color.bNorm; val a = color.aNorm

    Drawing.getCurrentShader match
      case Some(cs) =>
        flush()
        immediateSetup(cs, color)
        var n = 0; var i = 0
        while i < segments do
          val a1 = (i * 2.0f * math.Pi / segments).toFloat
          val a2 = ((i+1) % segments * 2.0f * math.Pi / segments).toFloat
          scratchBuffer(n) = centerX + radius*math.cos(a1).toFloat; n+=1; scratchBuffer(n) = centerY + radius*math.sin(a1).toFloat; n+=1
          scratchBuffer(n) = centerX + radius*math.cos(a2).toFloat; n+=1; scratchBuffer(n) = centerY + radius*math.sin(a2).toFloat; n+=1
          i += 1
        immediateDraw(n/2, n, GL_LINES.toUInt)

      case None =>
        var i = 0
        while i < segments do
          val a1 = (i * 2.0f * math.Pi / segments).toFloat
          val a2 = ((i+1) % segments * 2.0f * math.Pi / segments).toFloat
          checkBatch(GL_LINES.toUInt, 2)
          bv(centerX + radius*math.cos(a1).toFloat, centerY + radius*math.sin(a1).toFloat, r, g, b, a)
          bv(centerX + radius*math.cos(a2).toFloat, centerY + radius*math.sin(a2).toFloat, r, g, b, a)
          i += 1

  private inline def circleSegments(radius: Float): Int =
    math.max(8, math.min(64, (radius * 0.5f).toInt))

end BasicRenderer