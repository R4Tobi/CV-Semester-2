#define _USE_MATH_DEFINES
#include "sphere.h"

#include <cmath>
#include <iostream>
//==============================================================================
namespace cg {
//==============================================================================
Sphere::Sphere(const Material& m) : Renderable{m} {}
//------------------------------------------------------------------------------
std::optional<std::pair<double, double>> Sphere::solve_quadratic(double a,
                                                               double b,
                                                               double c) const {

  /*
  ============================================================================
   Task 2
  ============================================================================
   Implement the method that solves a quadratic equation of the form
                     a*x^2 + b*x + c = 0
   The return type is std::optional<>. This means you can return 
   an std::pair<double, double> if there is a solution or {} 
   if there is no solution.
   You can assign values to a std::pair like this:
     solutions.first = x1;
     solutions.second = x2;
   See e.g.
   https://en.cppreference.com/w/cpp/utility/optional
   and
   https://en.cppreference.com/w/cpp/utility/pair
  */

  std::pair<double, double> solutions;

  return {};
  }
//------------------------------------------------------------------------------
std::optional<Intersection> Sphere::check_intersection(const Ray& r,
                                                       double     min_t) const {
  
  /*
  ============================================================================
   Task 2
  ============================================================================
   Implement a ray-sphere intersection. You should use solve_quadratic.
   
   The sphere center and radius are provided for your convenience.

   The class vec3 already implements basic vector operations like + and - as
   well as the dot and cross product and length. You don't need to implement 
   it yourself. (see vec.h)

   The return type is std::optional<>. This means you can return
   an Intersection if the ray is intersecting or {} if there is no 
   intersection.

   See e.g.
   https://en.cppreference.com/w/cpp/utility/optional
   and
   intersection.h
  */

  vec3 center = transform_point(vec3::zeros());
  double radius = transform_direction(vec3{1.0, 0.0, 0.0}).length();

  // you can return an intersection like this
  // return Intersection{this, r, t, pos, normal, uv};
  /* where
      this     : is a pointer to the sphere
      r        : the ray intersecting
      t, pos   : t value and world space position of the intersection
                 (needs to be calculated by you)
      normal   : the surface normal in world space coordinates
                 (needs to be calculated by you) 
      uv       : local uv texture coordinates of the sphere 
                 (just use vec2{0.0, 0.0} if unsure)
  */

  return {};
  }
//==============================================================================
}  // namespace cg
//==============================================================================
