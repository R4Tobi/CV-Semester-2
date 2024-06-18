#define _USE_MATH_DEFINES
#include <cmath>
#include <iostream>

#include "box.h"
#include "checkerboard.h"
#include "colors.h"
#include "constantcolormaterial.h"
#include "constantcolorsource.h"
#include "directionallight.h"
#include "plane.h"
#include "mat.h"
#include "orthographiccamera.h"
#include "phong.h"
#include "plane.h"
#include "raytracer.h"
#include "scene.h"
#include "task4_scene.h"
#include "texture.h"

// #include "trumpet.h"
#if CG_USE_PREVIEW_WINDOW
#include "glwindow.h"
#endif
//==============================================================================
using namespace cg;
//==============================================================================
// definitions
Scene build_scene();
//==============================================================================
// declarations
int main() {
  constexpr size_t width  = 32;
  constexpr size_t height = 32;
  constexpr vec3   eye{-10, 10, -21};
  constexpr vec3   lookat{-5, 6.7, -11};

  Scene     scene = build_scene();
  Raytracer raytracer{scene, OrthographicCamera{eye, lookat, -0.25, 2.25, -2.5,
                                                3.5, width, height}};

  Raytracer raytracer2{scene, OrthographicCamera{eye, lookat, -3.75, -0.5,
                                                 -2.25, 1.25, width, height}};
  raytracer.render().write_ppm("check3a.ppm");
  raytracer2.render().write_ppm("check3b.ppm");
}
//------------------------------------------------------------------------------
