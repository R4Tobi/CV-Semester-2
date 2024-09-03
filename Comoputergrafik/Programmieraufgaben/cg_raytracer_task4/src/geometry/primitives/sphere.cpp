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

  double discr = b * b - 4 * a * c;
  std::pair<double, double> solutions;
  if (discr < 0) {
    return {};
  } else if (discr == 0) {
    solutions.first = solutions.second = -0.5 * b / a;
  } else {
    double q = (b > 0) ? -0.5 * (b + sqrt(discr)) : -0.5 * (b - sqrt(discr));
    solutions.first  = q / a;
    solutions.second = c / q;
  }
  if (solutions.first > solutions.second) {
    std::swap(solutions.first, solutions.second);
  }

  return solutions;
  }
//------------------------------------------------------------------------------
std::optional<Intersection> Sphere::check_intersection(const Ray& r,
                                                       double     min_t) const {
  
  vec3 trOrg = inverse_point(r.origin());
  vec3 trDir = inverse_direction(r.direction());

  // when using center and radius
  /*
  vec3 center = transform_point(vec3::zeros());
  double radius = transform_direction(vec3{1.0, 0.0, 0.0}).length();
  auto   L      = r.origin() - center;
  double a      = dot(r.direction(), r.direction());
  double b      = 2 * dot(r.direction(), L);
  double c      = dot(L, L) - radius * radius;
  */

  auto   L         = trOrg;
  double a         = dot(trDir, trDir);
  double b         = 2 * dot(trDir, L);
  double c         = dot(L, L) - 1.;
  auto   solutions = solve_quadratic(a, b, c);
  if (!solutions) { return {}; }

  double t;
  const auto& [t0, t1] = *solutions;
  if (t0 < min_t && t1 < min_t) { return {}; }

  if (t1 < 0) {
    t = t0;
  } else if (t0 < 0) {
    t = t1;
  } else if (t0 < t1) {
    t = t0 > min_t ? t0 : t1;
  } else {
    t = t1 > min_t ? t1 : t0;
  }

  auto hit_pos = r(t);
  
  // when using radius and center
  // vec3 normal = normalize(hit_pos - center);
  vec3 normal = normalize(trOrg + t*trDir);
  normal = normalize(transform_normal(normal)); //this is not needed if using center
  
  // use spherical coordinates as uv (https://mathworld.wolfram.com/SphericalCoordinates.html)
  // need to convert the range to [0, 1], so we have additional factors of pi in the formula
  // also, we assume y = upwarts -> change z and y coordinates in formula
  vec2 uv{std::atan2(normal(0), normal(2)) / (2 * M_PI) + M_PI / 2,
          std::acos(-normal(1)) / M_PI};
  
  return Intersection{this, r, t, hit_pos, normal,
                      uv};
  }
//==============================================================================
}  // namespace cg
//==============================================================================
