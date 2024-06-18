#ifndef CG_RAY_H
#define CG_RAY_H
//==============================================================================
#include <optional>

#include "vec.h"
//==============================================================================
namespace cg {
//==============================================================================
/// reflects a vector d depending on a normal vector n.
constexpr inline vec3 reflect(const vec3& d, const vec3& n) {
  
  return d - 2 * dot(d, n) * n;
}
//------------------------------------------------------------------------------
/// calculates refraction vector with incident direction d, normal direction n 
/// and index of refration ior.
std::optional<vec3> refract(const vec3& d, const vec3& n, double ior);
//==============================================================================
/// \brief Rays have an origin and a direction.
/// They are used for intersection tests with \ref cg::Renderable "renderables".
class Ray {
  vec3   m_origin, m_direction;
  size_t m_num_reflections;

 public:
  //----------------------------------------------------------------------------
  Ray(const vec3& org, const vec3& dir)
      : m_origin{org}, m_direction{normalize(dir)}, m_num_reflections{0} {}

  Ray(const Ray&) = default;
  Ray(Ray&&)      = default;
  Ray& operator=(const Ray&) = default;
  Ray& operator=(Ray&&) = default;
  //----------------------------------------------------------------------------
  /// \brief Evaluate the ray at position t.
  /// t = 1 returns origin + normalized direction.
  constexpr vec3 operator()(double t) const {
    return m_origin + m_direction * t;
  }
  //----------------------------------------------------------------------------
  const auto& origin() const { return m_origin; }
  auto        origin(size_t i) const { return m_origin(i); }
  //----------------------------------------------------------------------------
  const auto& direction() const { return m_direction; }
  auto        direction(size_t i) const { return m_direction(i); }
  //----------------------------------------------------------------------------
  /// Gives the "degree" of the ray, i.e. how often it was reflected and 
  /// refracted.
  auto num_reflections() const { return m_num_reflections; }
  //----------------------------------------------------------------------------
  /// Reflects the ray at some position with normal.
  auto reflect(const vec3& position, const vec3& normal) const {
    const auto dir = cg::reflect(m_direction, normal);
    return Ray{position + dir * 1e-4, dir, m_num_reflections + 1};
  }
  //----------------------------------------------------------------------------
  /// calculates refracted ray at some position with normal and index of
  /// refraction ior.
  std::optional<Ray> refract(const vec3& position, const vec3& normal,
                             double ior) const {
    const auto dir = cg::refract(m_direction, normal, ior);
    if (!dir) {return {};}
    return Ray{position + *dir * 1e-4, *dir, m_num_reflections + 1};
  }
  //============================================================================
  Ray(const vec3& org, const vec3& dir, size_t num_reflections)
      : m_origin{org},
        m_direction{normalize(dir)},
        m_num_reflections{num_reflections} {}
};
//==============================================================================
}  // namespace cg
//==============================================================================
#endif
