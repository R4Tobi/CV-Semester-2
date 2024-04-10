#define _USE_MATH_DEFINES
#include <cmath>
#include <cassert>
#include <iostream>
#include <random>

#include "colors.h"
#include "constantcolormaterial.h"

#include "directionallight.h"
#include "pointlight.h"
#include "spotlight.h"

#include "ray.h"

#include "plane.h"
#include "scene.h"

namespace cg{
namespace test{
  void test_closest_intersection();
  void test_any_intersection();
  void test_in_shadow();
  void test_spotlight();
}
}

int main(){
  std::string seperator1 = "============================\n";
  std::string seperator2 = "----------------------------\n";

  std::cout << seperator1 << "Test Task 1\n" << seperator1;
  cg::test::test_closest_intersection();
  std::cout << seperator2;
  cg::test::test_any_intersection();
  std::cout << seperator2;
  cg::test::test_in_shadow();
  std::cout << seperator2;
  cg::test::test_spotlight();
  std::cout << seperator1;

  return 0;
}

namespace cg{
namespace test{
  bool log_fail(bool condition, const std::string& message){
    if(!condition){
      std::cout << message << std::endl;
    }
    return condition;
  }

  void print_status(bool status){
    if(status){
      std::cout << "PASS" << std::endl;
    }
    else{
      std::cout << "FAIL" << std::endl;
    }
  }

  void test_closest_intersection(){
    std::cout << "- Test closest_intersection -" << std::endl;
    bool test_status = true;

    Scene scene(black);

    ConstantColorMaterial mat{white};
    
    XZPlane plane1{{1,1}, mat};
    scene.insert(plane1);

    XZPlane plane2{{1,1}, mat};
    plane2.translate({1.9, 0.1, 0.0});
    scene.insert(plane2);

    std::optional<Intersection> hit;

    hit = scene.closest_intersection(Ray{{0,1,0}, {0,-1,0}}, 0.001);
    test_status &= log_fail(hit && std::abs(hit->t - 1.0) < 1e-14, 
      "Closest intersection failed for single intersecting object");

    hit = scene.closest_intersection(Ray{ {1,1,0}, {0,-1,0} }, 0.001);
    test_status &= log_fail(hit && std::abs(std::abs(hit->t - 0.9) < 1e-14), 
      "Closest intersection failed for two intersecting objects");

    hit = scene.closest_intersection(Ray{ {1,1,0}, {0,-1,0} }, 0.91);
    test_status &= log_fail(hit && std::abs(hit->t - 1.0) < 1e-14,
      "Closest intersection failed for min_t");

    hit = scene.closest_intersection(Ray{ {-2,1,0}, {0,-1,0} }, 0.001);
    test_status &= log_fail(!hit, 
      "Closest intersection failed for no intersecting object");

    print_status(test_status);
  }

  void test_any_intersection(){
    std::cout << "- Test any_intersection -" << std::endl;
    bool test_status = true;

    Scene scene(black);

    ConstantColorMaterial mat{ white };

    XZPlane plane1{ {1,1}, mat };
    scene.insert(plane1);

    XZPlane plane2{ {1,1}, mat };
    plane2.translate({ 1.9, 0.1, 0.0 });
    scene.insert(plane2);

    bool hit;
    hit = scene.any_intersection(Ray{ {0,1,0}, {0,-1,0} }, 0.001, 1000);
    test_status &= log_fail(hit,
      "Any intersection failed for single intersecting object");

    hit = scene.any_intersection(Ray{ {1,1,0}, {0,-1,0} }, 0.001, 1000);
    test_status &= log_fail(hit,
      "Any intersection failed for two intersecting objects");

    hit = scene.any_intersection(Ray{ {1,1,0}, {0,-1,0} }, 0.91, 1000);
    test_status &= log_fail(hit,
      "Any intersection failed for min_t=0.91");

    hit = scene.any_intersection(Ray{ {1,1,0}, {0,-1,0} }, 0.001, 0.89);
    test_status &= log_fail(!hit,
      "Any intersection failed for max_t=0.89");

    hit = scene.any_intersection(Ray{ {1,1,0}, {0,-1,0} }, 0.91, 0.99);
    test_status &= log_fail(!hit,
      "Any intersection failed for min_t=0.91, max_t=0.99");

    hit = scene.any_intersection(Ray{ {1,1,0}, {0,-1,0} }, 1.1, 1000);
    test_status &= log_fail(!hit,
      "Any intersection failed for min_t=1.1");

    hit = scene.any_intersection(Ray{ {-2,1,0}, {0,-1,0} }, 0.001, 1000);
    test_status &= log_fail(!hit,
      "Any intersection failed for no intersecting object");

    print_status(test_status);
  }

  void test_in_shadow(){
    std::cout << "- Test in_shadow -" << std::endl;
    bool test_status = true;

    Scene scene(black);

    ConstantColorMaterial mat{ white };

    XZPlane plane1{ mat };
    scene.insert(plane1);

    XZPlane plane2{ mat };
    plane2.translate({0, 1, 0});
    scene.insert(plane2);

    DirectionalLight dl{{1,-1,1}, white};
    
    PointLight pl{white};
    pl.translate({0, 0.5, 0});

    bool shadow;
    
    shadow = scene.in_shadow(dl, vec3{0, 1.1, 0});
    test_status &= log_fail(!shadow, 
      "In_shadow failed for directional light - case 1. Point is in shadow but shouldn't.");

    shadow = scene.in_shadow(dl, vec3{ 0, 0.5, 0 });
    test_status &= log_fail(shadow,
      "In_shadow failed for directional light - case 2. Point is not in shadow but should.");

    shadow = scene.in_shadow(dl, vec3{ 0, -0.1, 0 });
    test_status &= log_fail(shadow,
      "In_shadow failed for directional light - case 3. Point is not in shadow but should.");

    shadow = scene.in_shadow(pl, vec3{ 0, 1.1, 0 });
    test_status &= log_fail(shadow,
      "In_shadow failed for point light - case 1. Point is not in shadow but should.");

    shadow = scene.in_shadow(pl, vec3{ 0, 0.2, 0 });
    test_status &= log_fail(!shadow,
      "In_shadow failed for point light - case 2. Point is in shadow but shouldn't.");

    shadow = scene.in_shadow(pl, vec3{ 0, -0.1, 0 });
    test_status &= log_fail(shadow,
      "In_shadow failed for point light - case 3. Point is not in shadow but should.");

    print_status(test_status);
  }

  void test_spotlight(){
    std::cout << "- Test SpotLight::incident_radiance_at -" << std::endl;
    bool test_status = true;

    std::uniform_real_distribution<double> unif(-M_PI/2, M_PI/2);
    std::random_device rd;

    vec3 dir = normalize(vec3{ unif(rd), unif(rd), unif(rd) });
    double angle = std::abs(unif(rd));
    SpotLight sl{dir, angle, white};
    
    std::cout << "Direction: " << dir << "\nOpening angle: " << angle << std::endl;

    vec3 radiance;
    
    // middle of cone
    radiance = sl.incident_radiance_at(0.1*dir);
    test_status &= log_fail(radiance.squared_length() > 1e-8,
      "Spotlight test failed - case 1. Point should be in light but isn't.");

    // on other side of cone
    radiance = sl.incident_radiance_at(0.1 * -dir);
    test_status &= log_fail(radiance.squared_length() < 1e-8,
      "Spotlight test failed - case 2. Point shouldn't be in light but is.");

    vec3 raxis = vec3{1.0 - dir(0) * dir(0) / (1 + dir(2)),
      -dir(0) * dir(1) / (1 + dir(2)),
      -dir(0)};
    // just outside cone
    vec4 rdir = rotation_matrix(raxis(0), raxis(1), raxis(2), 
                                angle + 0.00001) * homogeneous_direction(dir);
    radiance = sl.incident_radiance_at(0.1 * vec3{rdir(0), rdir(1), rdir(2)});
    test_status &= log_fail(radiance.squared_length() < 1e-8,
      "Spotlight test failed - case 3. Point shouldn't be in light but is.");

    // just inside cone
    rdir = rotation_matrix(raxis(0), raxis(1), raxis(2),
                           angle - 0.00001) * homogeneous_direction(dir);
    radiance = sl.incident_radiance_at(0.1 * vec3{ rdir(0), rdir(1), rdir(2) });
    test_status &= log_fail(radiance.squared_length() > 1e-8,
      "Spotlight test failed - case 4. Point should be in light but isn't.");

    print_status(test_status);
  }
}
}