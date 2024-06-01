#define _USE_MATH_DEFINES
#include <cmath>
#include <iostream>

#include "perspectivecamera.h"
#include "raytracer.h"
#include "scene.h"
#include "task4_scene.h"

#if CG_USE_PREVIEW_WINDOW
#include "glwindow.h"
#endif
//==============================================================================
using namespace cg;
//==============================================================================
// definitions
// Scene build_scene();
//==============================================================================
// declarations
int main() {
  constexpr size_t width  = 500;
  constexpr size_t height = 500;
  constexpr double fov    = 25; //30;  // in degrees
  constexpr vec3   eye{-2, 7, -6};
  constexpr vec3   lookat{1, 3.2, 0};

  //constexpr vec3   eye{0, 0, -2.6};
  //constexpr vec3   lookat{0, 0, 0};

  PerspectiveCamera cam = PerspectiveCamera{ eye, lookat, fov, width, height };
  Raytracer raytracer{build_scene(),
                      cam};
#if CG_USE_PREVIEW_WINDOW
  GLWindow w{raytracer};
#endif
  raytracer.render().write_ppm("task4.ppm");
#if CG_USE_PREVIEW_WINDOW
  w.join_thread();
#endif
}
//------------------------------------------------------------------------------
