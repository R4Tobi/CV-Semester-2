#ifndef CG_TRIANGLE_H
#define CG_TRIANGLE_H
//==============================================================================
#include <array>
#include <optional>
#include <vector>

#include "ray.h"
#include "renderable.h"
#include "vec.h"
//==============================================================================
namespace cg {
//==============================================================================
class Triangle : public Renderable {
  
  std::array<vec3, 3> m_vertices;
  std::array<vec2, 3> m_uv_coordinates;
  vec3                m_normal;

  public:
  //============================================================================
  Triangle(const vec3& x0, const vec3& x1, const vec3& x2,
           const Material& mat);
  Triangle(const Triangle& other) = default;
  Triangle(Triangle&& other)      = default;
  //----------------------------------------------------------------------------
  std::array<vec3, 3> vertices() const;
  inline vec3 normal() const { return transform_normal(m_normal); }
  //----------------------------------------------------------------------------
  std::optional<Intersection> check_intersection(
      const Ray& r, double min_t = 0) const override;
  //------------------------------------------------------------------------------
  /// ray - triangle intersection test
  /// \param r Ray to intersect
  /// \param v0 triangles's first vertex
  /// \param v1 triangles's second vertex
  /// \param v2 triangles's third vertex
  /// \return barycentric coordinates if there is an intersection
  std::optional<std::pair<double, vec3>> check_intersection(
      const Ray& r, const vec3& v0, const vec3& v1, const vec3& v2) const;
   //---------------------------------------------------------------------------
   /// needed for polymorphic copy
  make_clonable(Renderable, Triangle);
};
//==============================================================================
}  // namespace cg
//==============================================================================

#endif
