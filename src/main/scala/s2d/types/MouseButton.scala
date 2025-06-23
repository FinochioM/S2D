package s2d.types

import s2d.backend.sdl2.Extras.* 

enum MouseButton(val value: Int):
    case Left extends MouseButton(SDL_BUTTON_LEFT.toInt)
    case Middle extends MouseButton(SDL_BUTTON_MIDDLE.toInt)
    case Right extends MouseButton(SDL_BUTTON_RIGHT.toInt)
    case X1 extends MouseButton(SDL_BUTTON_X1.toInt)
    case X2 extends MouseButton(SDL_BUTTON_X2.toInt)
    case Unknown extends MouseButton(-1)
end MouseButton

object MouseButton:
    def fromValue(value: Int): MouseButton =
        MouseButton.values.find(_.value == value).getOrElse(MouseButton.Unknown)
end MouseButton