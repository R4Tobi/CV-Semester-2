#define _USE_MATH_DEFINES
#include <cmath>
#include <iostream>

#include "sphere.h"
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
#include "texture.h"

using namespace cg;

Scene build_scene() {
  Scene scene;

  // textures
  Texture billiard15{"assets/bb15.ppm"};
  Texture numbers{"assets/dice.ppm"};

  // materials
  Checkerboard checkerboard{1, 1, vec{0.3, 0.4, 0.5}, vec{0.9, 0.8, 0.7}};
  Phong phong_checkerboard{checkerboard, 0.2, ConstantColorSource{offwhite}, 1.};

  Phong phong_bb15{ billiard15, 0.05, ConstantColorSource{offwhite}, 50 };
  Phong phong_dice{ numbers, 0., ConstantColorSource{black}, 1. };

  // renderable objects
  Plane plane{phong_checkerboard};
  scene.insert(plane);
  
  /*
  ============================================================================
   Task 4
  ============================================================================
   Include all 6 dice in the scene and transform them into the position
   as shown in the image of the task description

   Tips:
   Transform the dice one by one
   All transformations needed are in movable.h
   All transformations are in relation to (0,0,0) (order matters!)
   Little extra translations in between can help you
   Use the checkerboard as a guide for positioning
  */

  // example for the sphere
  Sphere bb_sphere{ phong_bb15 };
  bb_sphere.scale(1.5, 1.5, 1.5);    // scale
  bb_sphere.rotate_z(0.3);           // rotate around z-axis
  bb_sphere.rotate_x(0.4);           // rotate around x-axis
  bb_sphere.rotate_y(0.5);           // rotate around y-axis
                                     // be aware that rotation is in radians
                                     // not degree
  bb_sphere.translate(4., 1.5, 2.);  // move by vector
  scene.insert(bb_sphere);           // inserting the sphere into the scene 
                                     // (needed so the object can be seen!)
                                     // inserting should be the last thing you do

  Box dice1{ phong_dice };
  // transform
  // insert

  Box dice2{ phong_dice };
  // transform
  // insert

  Box dice3{ phong_dice };
  // transform
  // insert

  Box dice4{ phong_dice };
  // transform
  // insert

  Box dice5{ phong_dice };
  // transform
  // insert

  // ==================================================================
  // Task 4 Bonus
  // Transform the die as shown in the picture of the task description
  // ==================================================================
  Box dice6{ phong_dice };
  // transform
  // insert
    // light

  DirectionalLight dl1{{1, -1, 2}, {3, 3, 3}};
  scene.insert(dl1);

  return scene;
}