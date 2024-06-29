#define _USE_MATH_DEFINES
#include <cmath>
#include <iostream>

#include "sphere.h"
#include "bunny.h"
#include "box.h"
#include "checkerboard.h"
#include "colors.h"
#include "constantcolormaterial.h"
#include "constantcolorsource.h"
#include "directionallight.h"
#include "pointlight.h"
#include "plane.h"
#include "phong.h"
#include "plane.h"
#include "scene.h"
#include "teapot.h"
#include "texture.h"

using namespace cg;

Scene build_scene() {

  /*
  ============================================================================
   Task 5
  ============================================================================
   Build a Cornell box based on the specification given in the task 
   description. You can use any type of object given in the project.
   
   Be aware that meshes, especially transparent ones, can slow down rendering
   times significantly. Primitive objects like spheres, cubes, planes and 
   single triangles are much faster. You might need to include additional 
   headers.
  */
  Scene scene;

  Phong phong_white{ white, 0.0, black, 0.0 };
  
  XYPlane bottom{ {1,1}, phong_white };
  bottom.rotate_x(-M_PI / 2);
  bottom.translate({ 0, -1, 0 });
  scene.insert(bottom);
  
  PointLight pl1{ 2, 2, 2 };
  pl1.translate({ 0, 0.0, -0.6 });
  scene.insert(pl1);

  return scene;
    }