#define _USE_MATH_DEFINES
#include <cmath>

#include "colors.h"
#include "checkerboard.h"
#include "constantcolormaterial.h"
#include "directionallight.h"
#include "bezier_wave.h"
#include "plane.h"
#include "mat.h"
#include "orthographiccamera.h"
#include "raytracer.h"
#include "scene.h"
#include "sphere.h"
#include "triangle.h"
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
  constexpr size_t width  = 500;
  constexpr size_t height = 500;
  constexpr vec3   eye{0, 2, 4};
  constexpr vec3   lookat{0, 1, 0};

  Raytracer raytracer{build_scene(), OrthographicCamera{eye, lookat, -2, 2, -2,
                                                        2, width, height}};
#if CG_USE_PREVIEW_WINDOW
  GLWindow w{raytracer};
#endif
  raytracer.render().write_ppm("task2.ppm");
#if CG_USE_PREVIEW_WINDOW
  w.join_thread();
#endif
}
//------------------------------------------------------------------------------
Scene build_scene() {
  Scene scene;

  // create materials
  ConstantColorMaterial blue_material{blue};
  ConstantColorMaterial orange_material{orange};
  ConstantColorMaterial red_material{red};

  Checkerboard checkerboard{red, orange, 10, 10};
  ConstantColorMaterial check{checkerboard};

  // insert renderables in scene. scene.insert() inserts copies of the specified
  // renderable
  Plane plane{blue_material};
  scene.insert(plane);

  Sphere orange_sphere{orange_material};
  orange_sphere.scale(0.5);
  orange_sphere.translate(0, 1.5, 0);
  scene.insert(orange_sphere);
  orange_sphere.translate(-1, 0.5, 1.5);
  scene.insert(orange_sphere);
  orange_sphere.translate(1.7, -1, 1.5);
  scene.insert(orange_sphere);

  Triangle red_triangle{vec3{-1, 0.3, -0.5}, vec3{1, 0.4, -0.5},
                        vec3{0, 2, -1.5}, red_material};
  scene.insert(red_triangle);

  BezierWave2 wave{ check, 10, 10 };
  wave.scale(1.5);
  wave.rotate_x(M_PI/4);
  wave.rotate_z(M_PI / 4);
  //wave.translate({ -1.5, 1, 3.5 });
  wave.translate({ 1.3, 1, -2.5 });
  scene.insert(wave);

  // add light
  DirectionalLight dl1{{0.3, -0.5, -1}, white};
  scene.insert(dl1);

  return scene;
}
