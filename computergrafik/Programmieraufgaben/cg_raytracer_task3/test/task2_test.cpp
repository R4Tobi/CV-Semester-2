#define _USE_MATH_DEFINES
#include <cmath>
#include <cassert>
#include <iostream>
#include <iomanip>      // std::setprecision
#include <sstream>
#include <random>

#include "colors.h"
#include "constantcolormaterial.h"

#include "sphere.h"
#include "triangle.h"
#include "bezier_patch_mesh.h"

namespace cg{
namespace test{
  void test_triangle_intersection();
  void test_sphere_intersection();
  void test_solve_quadratic();
  void test_de_casteljau();
}
}

int main(){
  std::string seperator1 = "============================\n";
  std::string seperator2 = "----------------------------\n";

  std::cout << seperator1 << "Test Task 2\n" << seperator1;
  cg::test::test_solve_quadratic();
  std::cout << seperator2;
  cg::test::test_sphere_intersection();
  std::cout << seperator2;
  cg::test::test_triangle_intersection();
  std::cout << seperator2;
  cg::test::test_de_casteljau();
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

  void test_triangle_intersection(){
    std::cout << "- Test Triangle::check_intersection -" << std::endl;
    bool test_status = true;

    ConstantColorMaterial mat{ white };

    vec3 v0 = vec3{ 0.2, -1.3, 0.1 };
    vec3 v1 = vec3{ 4.1, 2.5, 1 };
    vec3 v2 = vec3{ -1.2, 0.9, -2.0 };
    Triangle triangle{v0, v1, v2,  mat};

    std::cout << "Vertices:\nv0: " << v0 << "\nv1: " << v1 << "\nv2: " << v2 << std::endl; 

    std::optional<Intersection> hit;
    
    // hit middle of triangle
    hit = triangle.check_intersection(Ray{{0,0,0}, {0, 0, -1}}, 0.001);
    
    std::stringstream case1_message;
    case1_message << "Triangle intersection failed for ray orig=[0,0,0], dir=[0,0,-1].";
    if(hit){
      case1_message << "Correct values are\n  t=0.691439\n  position=[ 0, 0, -0.691439 ]\n  uv=[ 0.0992806, 0.419424 ]"
        << "\nbut received\n  t=" << hit->t << "\n  position=" << hit->position << "\n  uv=" << hit->uv << std::endl;
    }
    else{
      case1_message << "No hit but should.";
    }

    test_status &= log_fail(hit &&
                            std::abs(hit->t - 0.691439) < 1e-6 && 
                            (hit->position - vec3{ 1.11022e-16, 5.55112e-17, -0.691439 }).length() < 1e-06 &&
                            (hit->uv - vec2{0.0992806, 0.419424}).length() < 1e-06,
                            case1_message.str());

    // hit triangle edge
    hit = triangle.check_intersection(Ray{ {-0.25,0.5,0.7}, {1.538818448, 1.151341418, -1.291234840} }, 0.001);

    std::stringstream case2_message;
    case2_message << "Triangle intersection failed for ray orig=[-0.25,0.5,0.7], dir=[1.538818448, 1.151341418, -1.291234840].";
    if (hit) {
      case2_message << "Correct values are\n  t=2.315348\n  position=[ 1.288818, 1.651341, -0.591235 ]\n  uv=[ 0.469588, 0.530412 ]"
        << "\nbut received\n  t=" << hit->t << "\n  position=" << hit->position << "\n  uv=" << hit->uv << std::endl;
    }
    else {
      case2_message << "No hit but should.";
    }

    test_status &= log_fail(hit &&
                            std::abs(hit->t - 2.315348) < 1e-6 &&
                            (hit->position - vec3{ 1.288818, 1.651341, -0.591235 }).length() < 1e-06 &&
                            (hit->uv - vec2{ 0.469588, 0.530412 }).length() < 1e-06,
                            case2_message.str());

    // hit triangle corner v0
    hit = triangle.check_intersection(Ray{ {2.5, 0.5, -0.5}, {-2.3, -1.7999, 0.6} }, 0.001);

    std::stringstream case3_message;
    case3_message << "Triangle intersection failed for ray orig=[2.5, 0.5, -0.5], dir=[-2.3, -1.7999, 0.6].\n";
    if (hit) {
      case3_message << "Correct values are\n  t=2.981439881\n  position=[ 0.2000848962, -1.299833563, 0.09997785317 ]\n  uv=[ 3.020022751e-05, 2.348906584e-05 ]"
        << "\nbut received\n  t=" << hit->t << "\n  position=" << hit->position << "\n  uv=" << hit->uv << std::endl;
    }
    else {
      case3_message << "No hit but should.";
    }

    test_status &= log_fail(hit &&
                            std::abs(hit->t - 2.981439881) < 1e-6 &&
                            (hit->position - vec3{ 0.2000848962, -1.299833563, 0.09997785317 }).length() < 1e-06 &&
                            (hit->uv - vec2{ 3.020022751e-05, 2.348906584e-05 }).squared_length() < 1e-06,
                            case3_message.str());

    // no hit ray parallel to triangle
    hit = triangle.check_intersection(Ray{ {4.0744292089, 3.4089423478, 0.5285139892}, {-0.3658157709, -0.3398658693, -0.092169396} }, 0.001);
    test_status &= log_fail(!hit,
                            "Triangle intersection failed for ray orig = [4.0744292089, 3.4089423478, 0.5285139892], dir = [-0.3658157709, -0.3398658693, -0.092169396].\n Hit detected but should not hit");

    // no hit point too close
    hit = triangle.check_intersection(Ray{ {2.24,1.31,0.26}, {1, -1, -1} }, 0.1);
    test_status &= log_fail(!hit,
                            "Triangle intersection failed for ray orig = [2.24,1.31,0.26], dir = [1, -1, -1].\n Hit detected but should not hit");

    print_status(test_status);
  }

  void test_sphere_intersection(){
    std::cout << "- Test Sphere::check_intersection -" << std::endl;
    bool test_status = true;

    ConstantColorMaterial mat{ white };
    Sphere sphere{mat};

    std::cout << "Sphere at [0, 0, 0] with radius 1\n";

    std::optional<Intersection> hit;
    hit = sphere.check_intersection(Ray{{0, 2, 0}, {0, -1, 0}}, 0.001);

    std::stringstream case1_message;
    case1_message << "Sphere intersection failed for ray orig = [0, 2, 0], dir = [0, -1, 0].\n";
    if (hit) {
      case1_message << "Correct values are\n  t=1\n  position=[ 0, 1, 0 ]\n"
        << "\nbut received\n  t=" << hit->t << "\n  position=" << hit->position << std::endl;
    }
    else {
      case1_message << "No hit but should.";
    }
    test_status &= log_fail(hit && std::abs(hit->t - 1) < 1e-8, case1_message.str());

    hit = sphere.check_intersection(Ray{ {0, 1, 0}, {0, -1, 0} }, 0.001);
    std::stringstream case2_message;
    case2_message << "Sphere intersection failed for ray orig = [0, 1, 0], dir = [0, -1, 0].\n";
    if (hit) {
      case2_message << "Correct values are\n  t=2\n  position=[ 0, -1, 0 ]\n"
        << "\nbut received\n  t=" << hit->t << "\n  position=" << hit->position << std::endl;
    }
    else {
      case2_message << "No hit but should.";
    }
    test_status &= log_fail(hit && std::abs(hit->t - 2) < 1e-8, case2_message.str());

    hit = sphere.check_intersection(Ray{ {0, 0, 0}, {1, -1, 1} }, 0.001);
    std::stringstream case3_message;
    case3_message << "Sphere intersection failed for ray orig = [0, 0, 0], dir = [1, -1, 1].\n";
    if (hit) {
      case3_message << "Correct values are\n  t=1\n  position=[ 0.57735, -0.57735, 0.57735 ]\n"
        << "\nbut received\n  t=" << hit->t << "\n  position=" << hit->position << std::endl;
    }
    else {
      case3_message << "No hit but should.";
    }
    test_status &= log_fail(hit && std::abs(hit->t - 1) < 1e-8, case3_message.str());

    hit = sphere.check_intersection(Ray{ {0, -1, 0}, {0, -1, 0} }, 0.001);
    test_status &= log_fail(!hit,
      "Sphere intersection failed for ray orig = [0, -1, 0], dir = [0, -1, 0]. Hit but shouldn't");

    hit = sphere.check_intersection(Ray{ {0, -1.2, 0}, {0, -1, 0} }, 0.001);
    test_status &= log_fail(!hit,
      "Sphere intersection failed for ray orig = [0, -1.2, 0], dir = [0, -1, 0]. Hit but shouldn't");

    print_status(test_status);
  }

  void test_solve_quadratic(){
    std::cout << "- Test Sphere::solve_quadratic -" << std::endl;
    bool test_status = true;

    ConstantColorMaterial mat{ white };
    Sphere sphere{ mat };

    std::optional<std::pair<double, double>> solutions;
    solutions = sphere.solve_quadratic(0.5, 1.0, 3.0);
    test_status &= log_fail(!solutions,
      "Quadratic equation with a=0.5, b=1, c=3. Solution was returned but shouldn't.");

    solutions = sphere.solve_quadratic(0.5, 1.0, -3.0);
    std::stringstream case1_message;
    case1_message << "Quadratic equation with a=0.5, b=1, c=-3.\n";
    if (solutions) {
      case1_message << "Correct values are\n  x1=-3.64575\n  x2=1.64575\n"
        << "\nbut received\n  x1=" << solutions->first << "\n  x2=" << solutions->second << std::endl;
    }
    else {
      case1_message << "No solution but should.";
    }
    test_status &= log_fail(solutions && 
      std::abs(std::min(solutions->first, solutions->second) + 3.645751311) < 1e-06 &&
      std::abs(std::max(solutions->first, solutions->second) - 1.6457513111) < 1e-06,
      case1_message.str());

    solutions = sphere.solve_quadratic(0.5, 1.0, 0.50);
    std::stringstream case2_message;
    case2_message << "Quadratic equation with a=0.5, b=1, c=0.5\n";
    if (solutions) {
      case2_message << "Correct values are\n  x1=-1\n  x2=-1\n"
        << "\nbut received\n  x1=" << solutions->first << "\n  x2=" << solutions->second << std::endl;
    }
    else {
      case2_message << "No solution but should.";
    }
    test_status &= log_fail(solutions &&
      std::abs(std::min(solutions->first, solutions->second) + 1) < 1e-06 &&
      std::abs(std::max(solutions->first, solutions->second) + 1) < 1e-06,
      case2_message.str());

    print_status(test_status);
  }

  void test_de_casteljau(){
    std::cout << "- Test BezierPatchMesh::de_casteljau -" << std::endl;
    bool test_status = true;

    BezierPatchMesh bpm { ConstantColorMaterial{ white }, 3, 3, 3, 3};
 
    std::vector<vec3> control_points{
      vec3{0.1, 0.2, 0.3},
      vec3{1, 1, -0.2},
      vec3{0.1, 1.1, -0.5},
      vec3{0.4, 1.5, 0.0}
    };

    std::cout << "Control points are \n";
    for(auto& cp : control_points){
      std::cout << cp << "\n";
    }

    std::vector<double> ts{0.0, 0.2, 0.5, 0.9058434, 1.0};
    std::vector<vec3> solutions{control_points[0], vec3{ 0.448, 0.604, 0.0288 },
      vec3{ 0.475, 1, -0.225 }, vec3{ 0.3446705089, 1.394156257, -0.1204586285 },
      control_points.back()};

    for(unsigned i = 0; i < ts.size(); ++i){
      std::pair<vec3, vec3> p;
      
      std::stringstream message;
      message << "Wrong result for t=" << ts[i] << ". Should be " << solutions[i]
              << "but is " << p.first; 

      p = bpm.de_casteljau(control_points, ts[i]);
      test_status &= log_fail( (p.first - solutions[i]).length() < 1e-6, 
        message.str());
    }

    print_status(test_status);
  }
}
}