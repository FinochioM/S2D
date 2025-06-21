package s2d.shapes

import s2d.types.{Color, Vector2}
import s2d.math.Matrix4
import s2d.core.Window
import s2d.gl.GL.*
import s2d.gl.GLExtras.*
import s2d.gl.GLEWHelper
import s2d.core.Shaders
import scalanative.unsafe.*
import scalanative.unsigned.*

object BasicRenderer:
  private val vertexShaderSource = """
    #version 330 core

    layout (location = 0) in vec2 aPos;

    uniform mat4 uProjection;
    uniform vec4 uColor;

    out vec4 vertexColor;

    void main() {
        gl_Position = uProjection * vec4(aPos, 0.0, 1.0);
        vertexColor = uColor;
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

  private var defaultShader: Option[s2d.types.Shader] = None
  private var VAO: UInt = 0.toUInt
  private var VBO: UInt = 0.toUInt
  private var projectionLocation: Int = -1
  private var colorLocation: Int = -1
  private var isInitialized: Boolean = false

  def initialize(): Boolean =
    if isInitialized then return true

    defaultShader = Shaders.loadFromMemory(vertexShaderSource, fragmentShaderSource)

    defaultShader match
      case Some(shader) =>
        Zone {
          val projName = toCString("uProjection")
          val colorName = toCString("uColor")

          projectionLocation = GLEWHelper.glGetUniformLocation(shader.id.toUInt, projName)
          colorLocation = GLEWHelper.glGetUniformLocation(shader.id.toUInt, colorName)
        }

        val vaoArray = stackalloc[GLuint]()
        val vboArray = stackalloc[GLuint]()

        GLEWHelper.glGenVertexArrays(1.toUInt, vaoArray)
        GLEWHelper.glGenBuffers(1.toUInt, vboArray)

        VAO = !vaoArray
        VBO = !vboArray

        isInitialized = true
        true

      case None =>
        println("Failed to create default shader for BasicRenderer")
        false
  end initialize

  def cleanup(): Unit =
    if isInitialized then
      val vaoArray = stackalloc[GLuint]()
      val vboArray = stackalloc[GLuint]()
      !vaoArray = VAO
      !vboArray = VBO

      GLEWHelper.glDeleteVertexArrays(1.toUInt, vaoArray)
      GLEWHelper.glDeleteBuffers(1.toUInt, vboArray)

      defaultShader.foreach(Shaders.unloadShader)
      defaultShader = None
      isInitialized = false

  def setProjectionMatrix(matrix: Array[Float]): Unit =
    defaultShader.foreach { shader =>
      GLEWHelper.glUseProgram(shader.id.toUInt)
      Zone {
        val matrixPtr = stackalloc[GLfloat](16)
        for i <- matrix.indices do
          matrixPtr(i) = matrix(i)
        GLEWHelper.glUniformMatrix4fv(projectionLocation, 1.toUInt, GL_FALSE, matrixPtr)
      }
    }

  def updateProjectionFromDrawing(): Unit =
    val matrix = Matrix4.ortho(0.0f, Window.windowWidth.toFloat, Window.windowHeight.toFloat, 0.0f, -1.0f, 1.0f)
    setProjectionMatrix(matrix)

  private def setColor(color: Color): Unit =
    val r = color.r / 255.0f
    val g = color.g / 255.0f
    val b = color.b / 255.0f
    val a = color.a / 255.0f
    GLEWHelper.glUniform4f(colorLocation, r, g, b, a)

  def renderLine(startPos: Vector2, endPos: Vector2, color: Color): Unit =
    if !isInitialized then
      if !initialize() then return

    defaultShader.foreach { shader =>
      GLEWHelper.glUseProgram(shader.id.toUInt)
      setColor(color)

      val vertices = Array(
        startPos.x, startPos.y,
        endPos.x, endPos.y
      )

      GLEWHelper.glBindVertexArray(VAO)
      GLEWHelper.glBindBuffer(GL_ARRAY_BUFFER.toUInt, VBO)

      Zone {
        val verticesPtr = stackalloc[GLfloat](vertices.length)
        for i <- vertices.indices do
          verticesPtr(i) = vertices(i)

        GLEWHelper.glBufferData(
          GL_ARRAY_BUFFER.toUInt,
          (vertices.length * sizeof[GLfloat].toInt),
          verticesPtr.asInstanceOf[Ptr[Byte]],
          GL_DYNAMIC_DRAW.toUInt
        )
      }

      GLEWHelper.glVertexAttribPointer(0.toUInt, 2, GL_FLOAT.toUInt, GL_FALSE, (2 * sizeof[GLfloat].toInt).toUInt, null)
      GLEWHelper.glEnableVertexAttribArray(0.toUInt)

      glDrawArrays(GL_LINES.toUInt, 0, 2.toUInt)

      GLEWHelper.glBindVertexArray(0.toUInt)
    }

end BasicRenderer