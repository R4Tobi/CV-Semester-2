#define _USE_MATH_DEFINES
#include <cmath>
#include <iostream>

#include "checkerboard.h"
#include "colors.h"
#include "constantcolormaterial.h"
#include "constantcolorsource.h"
#include "directionallight.h"
#include "plane.h"
#include "perspectivecamera.h"
#include "orthographiccamera.h"
#include "phong.h"
#include "plane.h"
#include "pointlight.h"
#include "raytracer.h"
#include "scene.h"
#include "sphere.h"
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
  constexpr size_t width  = 500;
  constexpr size_t height = 500;
  constexpr double fov = 30;  // in degrees
  constexpr vec3   eye{ 3, 1.5, 0 };
  constexpr vec3   lookat{ 0, 1, -1 };

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
  raytracer.render().write_ppm("task3.ppm");
#if CG_USE_PREVIEW_WINDOW
  w.join_thread();
#endif
}
//------------------------------------------------------------------------------
Scene build_scene() {
  Scene scene({0.6, 0.8, 1.0});

  // textures
  Texture ovgu_logo{"assets/ovgu.ppm"};
  Texture billiard15{"assets/bb15.ppm"};

  // materials
  Phong phong_glass{white, 0.1, white, 0.89, 1.517, white, 2.};

  Checkerboard cb {10, 10, white, black};
  Phong flat_white{cb, 0, ConstantColorSource(gray), 1.};

  Phong phong_blue{blue, 0.2, offwhite, 10};

  Phong phong_copper{copper, 0.01, copper, 10};

  Phong phong_gold{gold, 0.7, gold, 50};

  Phong phong_steel{steel, 0.1, steel, 5};

  Checkerboard checkerboard{1, 1, vec{0.3, 0.4, 0.5}, vec(0.9, 0.8, 0.7)};
  Phong phong_checkerboard{checkerboard, 0.1, ConstantColorSource(offwhite),
                           1.};
  Phong phong_ovgu{ovgu_logo, 0.05, ConstantColorSource(offwhite), 1};

  Phong phong_bb15{billiard15, 0.05, ConstantColorSource(offwhite), 50};

  // renderable objects
  Plane plane{phong_checkerboard};

  XYPlane ovgu{{1, 1}, phong_ovgu};
  ovgu.rotate_z(M_PI);
  ovgu.scale(1.280 * 0.75, 0.461 * 0.75, 1);
  ovgu.rotate_x(M_PI * -0.1);
  ovgu.rotate_y(-M_PI * 0.5);
  ovgu.translate(-0.8, 0.3, -0.6);

  Sphere steel_sphere{phong_steel};
  steel_sphere.translate(-1.75, 2.3, -4);

  Sphere flat_sphere{flat_white};
  flat_sphere.scale(0.3);
  flat_sphere.translate(0.5, 0.3, -1.7);

  Sphere blue_sphere{phong_blue};
  blue_sphere.scale(0.75);
  blue_sphere.translate(-1, 0.75, -3);

  Sphere copper_sphere{phong_copper};
  copper_sphere.translate(-2, 1, -1);

  Sphere gold_sphere{phong_gold};
  gold_sphere.scale(0.6);
  gold_sphere.translate(-1.75, 1.6, -2.4);

  Sphere bb_sphere{ phong_bb15 };
  bb_sphere.scale(0.2);
  bb_sphere.translate(-2.5, 0.2, -2.4);

  Sphere glass_sphere{phong_glass};
  glass_sphere.scale(0.6);
  glass_sphere.translate(-0.4, 0.8, -1.5);

  // lights
  // do not use directional light because it always needs special treatment
  //DirectionalLight dl{{0, -1, 0}, {1, 1, 1}};
  //scene.insert(dl);

  PointLight p1{ 80 * vec3{0.8, 0.9, 1.0} };
  p1.translate(vec3{ 4,2,4 });
  scene.insert(p1);

  PointLight p2{ 80 * vec3{1.0, 1.0, 1.0} };
  p2.translate(vec3{ 0,10, 0 });
  scene.insert(p2);

  // insert renderables and light sources
  scene.insert(flat_sphere);
  scene.insert(steel_sphere);
  scene.insert(plane);
  scene.insert(ovgu);
  scene.insert(blue_sphere);
  scene.insert(glass_sphere);
  scene.insert(copper_sphere);
  scene.insert(gold_sphere);
  scene.insert(bb_sphere);

  return scene;
}