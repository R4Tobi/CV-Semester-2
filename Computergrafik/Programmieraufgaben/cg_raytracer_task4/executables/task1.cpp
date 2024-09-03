#define _USE_MATH_DEFINES
#include <cmath>

#include "colors.h"
#include "checkerboard.h"
#include "constantcolormaterial.h"
#include "directionallight.h"
#include "pointlight.h"
#include "spotlight.h"
#include "plane.h"
#include "orthographiccamera.h"
#include "perspectivecamera.h"
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
  constexpr size_t width  = 500;
  constexpr size_t height = 500;
  constexpr vec3   eye{0, 0, 3};
  constexpr vec3   lookat{0, 0, 0};

  //OrthographicCamera cam{ eye, lookat, -2, 2, -2, 2, width, height };
  PerspectiveCamera cam{eye, lookat, 20, width, height};

  Raytracer raytracer{build_scene(), cam};
#if CG_USE_PREVIEW_WINDOW
  GLWindow w{raytracer};
#endif
  raytracer.render().write_ppm("task1.ppm");
#if CG_USE_PREVIEW_WINDOW
  w.join_thread();
#endif
}
//------------------------------------------------------------------------------
Scene build_scene() {
  Scene scene;

  // create materials
  ConstantColorMaterial blue_material{blue};
  Checkerboard check{ vec3{1.0, 0.8, 0.4}, 0.9*vec3{1.0, 0.8, 0.4}, 2, 2 };
  ConstantColorMaterial yellow_material{check};
  ConstantColorMaterial orange_material{orange};
  ConstantColorMaterial red_material{red};

  // insert renderables in scene. scene.insert() inserts copies of the specified
  // renderable
  XYPlane plane{{0.2, 0.2}, blue_material};
  plane.rotate_z(-0.2);
  plane.translate({0.0, -0.5, 0.0});
  scene.insert(plane);

  XYPlane plane2{{0.8, 0.8}, red_material};
  plane2.rotate_x(M_PI/8);
  plane2.translate({0.0, 0.2, -1});
  scene.insert(plane2);

  XYPlane plane3{yellow_material };
  plane3.translate({ 0, 0, -3 });
  scene.insert(plane3);

  XYPlane plane4{ {2, 1}, orange_material };
  plane4.rotate_y(0.8*M_PI/2.0);
  plane4.rotate_z(0.2);
  plane4.translate({ -0.7, -0.5, -1.5 });
  scene.insert(plane4);

  // add light
  // do not use directional light because it always needs special treatment
  //DirectionalLight dl1{{0.4, 0.0, -1}, white};
  //scene.insert(dl1);
  //DirectionalLight dl2{ {-0.05, 0.0, -1}, 0.2*white };
  //scene.insert(dl2);

  PointLight p1{40*white};
  p1.translate({2,2,10});
  scene.insert(p1);

  PointLight p2{5 * white};
  p2.translate({ -3,-3,6 });
  scene.insert(p2);

  PointLight p3{ 0.2 * white };
  p3.translate({ -0.1, -0.5, -0.7 });
  scene.insert(p3);

  SpotLight sl{vec3{-1,-1, -0.001}, M_PI/7, white};
  sl.translate({1.9, 1.9, -2.9});
  scene.insert(sl);

  return scene;
}
