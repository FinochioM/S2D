#include <string.h>
#include "SDL3/SDL.h"

void __sn_wrap_sdl3_SDL_GUIDToString(SDL_GUID *guid, char * pszGUID, int cbGUID) {
 SDL_GUIDToString(*guid, pszGUID, cbGUID);
};


void __sn_wrap_sdl3_SDL_GetGamepadGUIDForID(SDL_JoystickID instance_id, SDL_GUID *____return) {
  SDL_GUID ____ret = SDL_GetGamepadGUIDForID(instance_id);
  memcpy(____return, &____ret, sizeof(SDL_GUID));
}


char * __sn_wrap_sdl3_SDL_GetGamepadMappingForGUID(SDL_GUID *guid) {
 return SDL_GetGamepadMappingForGUID(*guid);
};


void __sn_wrap_sdl3_SDL_GetJoystickGUID(SDL_Joystick * joystick, SDL_GUID *____return) {
  SDL_GUID ____ret = SDL_GetJoystickGUID(joystick);
  memcpy(____return, &____ret, sizeof(SDL_GUID));
}


void __sn_wrap_sdl3_SDL_GetJoystickGUIDForID(SDL_JoystickID instance_id, SDL_GUID *____return) {
  SDL_GUID ____ret = SDL_GetJoystickGUIDForID(instance_id);
  memcpy(____return, &____ret, sizeof(SDL_GUID));
}


void __sn_wrap_sdl3_SDL_GetJoystickGUIDInfo(SDL_GUID *guid, Uint16 * vendor, Uint16 * product, Uint16 * version, Uint16 * crc16) {
 SDL_GetJoystickGUIDInfo(*guid, vendor, product, version, crc16);
};


void __sn_wrap_sdl3_SDL_SetGPUBlendConstants(SDL_GPURenderPass * render_pass, SDL_FColor *blend_constants) {
 SDL_SetGPUBlendConstants(render_pass, *blend_constants);
};


void __sn_wrap_sdl3_SDL_StringToGUID(const char * pchGUID, SDL_GUID *____return) {
  SDL_GUID ____ret = SDL_StringToGUID(pchGUID);
  memcpy(____return, &____ret, sizeof(SDL_GUID));
}