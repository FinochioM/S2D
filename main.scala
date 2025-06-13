import sdl3.all.*
import scalanative.unsafe.*
import scalanative.unsigned.*

@main def hello =
  SDL_Init(SDL_InitFlags(0x00000020.toUInt))
  val window = SDL_CreateWindow(
    c"Hello, Scala",
    1024,
    768,
    SDL_WindowFlags(0x00000002.toULong)
  )

  if window == null then
    SDL_LogError(
      SDL_LogCategory.SDL_LOG_CATEGORY_ERROR.int,
      c"Could not create window: %s\n",
      SDL_GetError()
    )
    sys.exit(1)
  end if

  var done = false
  while !done do
    val event = stackalloc[SDL_Event]()
    while SDL_PollEvent(event) do
      if (!event).`type` == SDL_EventType.SDL_EVENT_QUIT.uint then done = true

  SDL_DestroyWindow(window)
  SDL_Quit()
end hello
