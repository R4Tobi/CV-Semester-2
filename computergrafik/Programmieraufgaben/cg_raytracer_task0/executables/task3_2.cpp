#define _USE_MATH_DEFINES
#include <cmath>
#include <iostream>

#include "colors.h"
#include "checkerboard.h"
#include "constantcolorsource.h"

#include "constantcolormaterial.h"
#include "phong.h"
#include "task3_materials.h"

#include "perspectivecamera.h"

#include "box.h"
#include "plane.h"
#include "sphere.h"

#include "directionallight.h"
#include "spotlight.h"

#include "raytracer.h"
#include "scene.h"
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
  constexpr size_t width  = 1000;
  constexpr size_t height = 1000;
  constexpr double fov = 30;  // in degrees
  constexpr vec3   eye{ 0, 3, -4 };
  constexpr vec3   lookat{ 0, 1.5, 0 };

  //constexpr vec3   eye{ 0, 0, -2.6 };
  //constexpr vec3   lookat{ 0, 0, 0 };

  //OrthographicCamera cam = OrthographicCamera{ eye, lookat, -3., 3., -3., 3., width, height };
  PerspectiveCamera cam = PerspectiveCamera{ eye, lookat, fov, width, height };

  Raytracer raytracer{
      build_scene(),
      cam};
#if CG_USE_PREVIEW_WINDOW
  GLWindow w{raytracer};
#endif
  raytracer.render().write_ppm("task3_2.ppm");
#if CG_USE_PREVIEW_WINDOW
  w.join_thread();
#endif
}
//------------------------------------------------------------------------------
Scene build_scene() {

  Scene scene(black);

  Checkerboard checkerboard{ 2, 2, white, black };
  Phong phong_checkerboard{ checkerboard, 0, ConstantColorSource(offwhite), 1 };
  Phong phong_gray{ 0.8 * vec3::ones(), 0.0, offwhite, 1.0 };

  // renderable objects
  Plane plane{ phong_checkerboard };

  Box box1{phong_gray};
  box1.scale({0.5, 1, 0.5});
  box1.translate({1.25, 0, -0.25});
  scene.insert(box1);

  Box box2{ phong_gray };
  box2.scale({ 0.5, 1, 0.5 });
  box2.translate({ -1.75, 0, -0.25 });
  scene.insert(box2);

  Sphere sphere1{ phong_redblack };
  sphere1.scale(0.25);
  sphere1.translate({ 1.5, 1.25, 0 });
  scene.insert(sphere1);

  Sphere sphere2{ phong_diffuse };
  sphere2.scale(0.25);
  sphere2.translate({ -1.5, 1.25, 0 });
  scene.insert(sphere2);

  Sphere sphere3{ phong_mirror };
  sphere3.scale(5);
  sphere3.translate({ 0, 5, 5 });
  scene.insert(sphere3);

  // lights
  SpotLight sl{ {0, -1, 0.5}, M_PI / 10.0, 10 * vec3{1.0, 0.0, 0.0} };
  sl.translate({ 1.5, 3, -1.25 });
  scene.insert(sl);

  SpotLight sl2{ {0, -1, 0.5}, M_PI / 10.0, 10 * vec3{1.0, 1.0, 1.0} };
  sl2.translate({ -1.5, 3, -1.25 });
  scene.insert(sl2);

  SpotLight sl3{ {0, -0.2, 4}, M_PI / 4.0, 10 * vec3{1.0, 1.0, 1.0} };
  sl3.translate({ 0, 5, -3 });
  scene.insert(sl3);

  // insert renderables and light sources
  scene.insert(plane);

  return scene;
}