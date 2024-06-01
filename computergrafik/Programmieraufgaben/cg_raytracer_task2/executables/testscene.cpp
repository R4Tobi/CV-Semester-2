#define _USE_MATH_DEFINES
#include <cmath>
#include <iostream>

#include "box.h"
#include "bezier_wave.h"
#include "teapot.h"
#include "bunny.h"
#include "checkerboard.h"
#include "colors.h"
#include "constantcolormaterial.h"
#include "constantcolorsource.h"
#include "directionallight.h"
#include "dragon.h"
#include "plane.h"
#include "mat.h"
#include "perspectivecamera.h"
#include "phong.h"
#include "plane.h"
#include "pointlight.h"
#include "spotlight.h"
#include "quadric.h"
#include "raytracer.h"
#include "scene.h"
#include "sphere.h"
#include "trumpet.h"
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
    constexpr size_t width = 2000;
    constexpr size_t height = 1000;
    constexpr double fov = 50;  // in degrees
    constexpr vec3   eye{ 4, 4.5, 7 };
    constexpr vec3   lookat{ 0, 0, 0 };

    Raytracer raytracer{ build_scene(),
                        PerspectiveCamera{eye, lookat, fov, width, height} };
#if CG_USE_PREVIEW_WINDOW
    GLWindow w{ raytracer };
#endif
    raytracer.render().write_ppm("testscene.ppm");
#if CG_USE_PREVIEW_WINDOW
    w.join_thread();
#endif
}
//------------------------------------------------------------------------------
Scene build_scene() {
    Scene scene(black);
    // textures
    Texture ovgu_logo{ "assets/ovgu.ppm" };
    Texture billiard15{ "assets/bb15.ppm" };

    // materials
    Phong phong_glass{ white, 0.1, white, 0.89, 1.517, white, 2. };
    Phong flat_white{ white, 0, white, 1. };
    Phong phong_mirror{ white, 0.95, white, 100 };
    Phong phong_blue{ blue, 0.2, offwhite, 10 };
    Phong phong_copper{ copper, 0.01, copper, 10 };
    Phong phong_gold{ gold, 0.7, gold, 50 };
    Phong phong_steel{ steel, 0.1, steel, 5 };

    Checkerboard checkerboard{ 1, 1, vec{0.3, 0.4, 0.5}, vec(0.9, 0.8, 0.7) };
    Phong phong_checkerboard{ checkerboard, 0.2, ConstantColorSource(offwhite),
                             1. };
    Phong phong_ovgu{ ovgu_logo, 0.05, ConstantColorSource(offwhite), 1 };

    Phong phong_bb15{ billiard15, 0.05, ConstantColorSource(offwhite), 50 };

    // renderable objects
    Plane plane{ phong_checkerboard };

    XYPlane ovgu{ {1, 1}, phong_ovgu };
    ovgu.rotate_z(M_PI);
    ovgu.translate(0.6, 1, -3);
    ovgu.rotate_x(M_PI * -0.1);
    ovgu.rotate_y(M_PI * 0.1);
    ovgu.scale(1.280 * 0.75, 0.461 * 0.75, 1);

    Sphere steel_sphere{ phong_steel };
    steel_sphere.translate(2., 0.5, 4.);

    Sphere flat_sphere{ flat_white };
    flat_sphere.scale(0.75);
    flat_sphere.translate(2., 0.75, -1.);

    Sphere mirror_sphere{ phong_mirror };
    mirror_sphere.scale(0.6);
    mirror_sphere.translate(-1., 0.5, 2.);

    Sphere blue_sphere{ phong_blue };
    blue_sphere.scale(0.75);
    blue_sphere.translate(-1, 0.75, -3);

    Sphere copper_sphere{ phong_copper };
    copper_sphere.translate(-2, 0.75, -1);

    Sphere gold_sphere{ phong_gold };
    gold_sphere.scale(0.5);
    gold_sphere.translate(-1.75, 1.5, -2.4);

    Sphere glass_sphere{ phong_glass };
    glass_sphere.scale(0.75);
    glass_sphere.translate(1, 1, 1);

    Phong phong_red{red, 0.0, black, 0.0};
    Bunny bunny{ phong_red };  // refractive material would slow rendering
    bunny.translate(-4., 0., 3.);

    Sphere bb_sphere{ phong_bb15 };
    bb_sphere.scale(0.8);
    bb_sphere.translate(4., 0.8, 2.);

    Teapot teapot{ phong_glass, 8, 8 };
    teapot.scale(vec3{ 0.3, 0.3, 0.3 });
    teapot.rotate_x(-M_PI / 2.0);
    teapot.translate(0, 1.2, 0.2);

    BezierWave wave{ phong_gold, 8,8 };
    wave.rotate_x(-45);
    wave.translate(-0.5, 0.6, 4.5);

    // lights
    DirectionalLight dl{ {0, -1, 0}, {1.5, 1.5, 1.5} };

    PointLight pl1{ 30, 30., 75. };
    pl1.translate(10., 5., 0);

    PointLight pl2{ 75., 30., 30. };
    pl2.translate(0, 5., 10.);

    PointLight pl3{ 30., 75., 30. };
    pl3.translate(-8., 5., -12.);

    SpotLight sl{{0, -1, 0}, M_PI/4, {10, 0, 10}};
    sl.translate({-4, 3, 3});
    sl.rotate_x(0.1*M_PI);

    // insert renderables and light sources
    scene.insert(flat_sphere);
    scene.insert(steel_sphere);
    scene.insert(plane);
    scene.insert(ovgu);
    scene.insert(blue_sphere);
    //scene.insert(glass_sphere);
    scene.insert(copper_sphere);
    scene.insert(mirror_sphere);
    scene.insert(gold_sphere);
    scene.insert(bb_sphere);
    scene.insert(bunny);
    scene.insert(teapot);
    scene.insert(wave);

    //scene.insert(dl);
    //scene.insert(pl1);
    //scene.insert(pl2);
    //scene.insert(pl3);
    scene.insert(sl);

    return scene;
}
