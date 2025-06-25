package s2d.types

import s2d.backend.sdl2.Extras.*

enum CursorStyle(val value: Int):
  case Arrow extends CursorStyle(SDL_SYSTEM_CURSOR_ARROW)
  case IBeam extends CursorStyle(SDL_SYSTEM_CURSOR_IBEAM)
  case Wait extends CursorStyle(SDL_SYSTEM_CURSOR_WAIT)
  case Crosshair extends CursorStyle(SDL_SYSTEM_CURSOR_CROSSHAIR)
  case WaitArrow extends CursorStyle(SDL_SYSTEM_CURSOR_WAITARROW)
  case SizeNWSE extends CursorStyle(SDL_SYSTEM_CURSOR_SIZENWSE)
  case SizeNESW extends CursorStyle(SDL_SYSTEM_CURSOR_SIZENESW)
  case SizeWE extends CursorStyle(SDL_SYSTEM_CURSOR_SIZEWE)
  case SizeNS extends CursorStyle(SDL_SYSTEM_CURSOR_SIZENS)
  case SizeAll extends CursorStyle(SDL_SYSTEM_CURSOR_SIZEALL)
  case No extends CursorStyle(SDL_SYSTEM_CURSOR_NO)
  case Hand extends CursorStyle(SDL_SYSTEM_CURSOR_HAND)
end CursorStyle