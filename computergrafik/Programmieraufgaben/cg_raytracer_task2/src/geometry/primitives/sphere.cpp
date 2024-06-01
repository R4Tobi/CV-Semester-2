#define _USE_MATH_DEFINES
#include "sphere.h"

#include <cmath>
#include <iostream>
//==============================================================================
namespace cg {
//==============================================================================
Sphere::Sphere(const Material& m) : Renderable{m} {}
//------------------------------------------------------------------------------
/**
 * @brief Solves a quadratic equation of the form a*x^2 + b*x + c = 0.
 *
 * @param a Coefficient of x^2.
 * @param b Coefficient of x.
 * @param c Constant term.
 * @return An optional pair of roots. If the equation has no real roots, returns an empty optional.
 */
std::optional<std::pair<double, double>> Sphere::solve_quadratic(double a, double b, double c) const {

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
  //verwenden der mitternachtsformel
    double discriminant = b * b - 4 * a * c;
    if (discriminant < 0) {
      return {};
    }
    double sqrt_discriminant = std::sqrt(discriminant);
    double root1 = (-b - sqrt_discriminant) / (2 * a);
    double root2 = (-b + sqrt_discriminant) / (2 * a);
    return std::make_pair(root1, root2);
  }
//------------------------------------------------------------------------------
  /**
 * @brief Checks if a ray intersects with the sphere.
 *
 * @param r The ray to check.
 * @param min_t The minimum t value for the intersection.
 * @return An optional Intersection object. If the ray intersects with the sphere,
 *         the Intersection object contains the details of the intersection.
 *         Otherwise, the returned optional is empty.
   */
  std::optional<Intersection> Sphere::check_intersection(const Ray& r, double min_t) const {
  
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


    vec3 center = transform_point(vec3::zeros());
    double radius = transform_direction(vec3{1.0, 0.0, 0.0}).length();
    vec3 oc = r.origin() - center;
    double a = dot(r.direction(), r.direction());
    double b = 2.0 * dot(oc, r.direction());
    double c = dot(oc, oc) - radius * radius;
    auto roots = solve_quadratic(a, b, c);
    if (!roots.has_value()) {
      return {};
    }
    double t = roots->first;
    if (t < min_t) {
      t = roots->second;
      if (t < min_t) {
        return {};
      }
    }
    vec3 pos = r(t);
    vec3 normal = (pos - center) / radius;

    // Flip the normal if it is not facing the ray
    if (dot(normal, r.direction()) > 0) {
      normal = -normal;
    }

    return Intersection{this, r, t, pos, normal, vec2{0.0, 0.0}};
  }
//==============================================================================
}  // namespace cg
//==============================================================================
