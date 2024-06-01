#include "triangle.h"
#include <limits>
#include <iostream>
//==============================================================================
namespace cg {
//==============================================================================
Triangle::Triangle(const vec3& x0, const vec3& x1, const vec3& x2,
                   const Material& mat)
    : Renderable{mat},
      m_vertices{x0, x1, x2},
      m_uv_coordinates{vec2{0.0, 0.0}, vec2{1.0, 0.0}, vec2{0.0, 1.0}},
      m_normal{normalize(cross(x1 - x0, x2 - x0))} {}

std::array<vec3, 3> Triangle::vertices() const {
  std::array<vec3, 3> trVertices;
  std::transform(m_vertices.begin(), m_vertices.end(),
                 trVertices.begin(),
                 [this](const vec3& v) {return transform_point(v);});
  return trVertices;
}
//------------------------------------------------------------------------------
/**
 * @brief Checks if a ray intersects with the triangle.
 *
 * @param r The ray to check.
 * @param min_t The minimum t value for the intersection.
 * @return An optional Intersection object. If the ray intersects with the triangle, the Intersection object contains the details of the intersection. Otherwise, the returned optional is empty.
 */
std::optional<Intersection> Triangle::check_intersection(const Ray& r, double min_t) const {

  /*
  ============================================================================
   Task 2
  ============================================================================
   Implement a ray-triangle intersection.
   
   The triangle vertices are given below as v0, v1 and v2. The normal is given
   by n.

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
  const vec3 v0 = transform_point(m_vertices[0]);
  const vec3 v1 = transform_point(m_vertices[1]);
  const vec3 v2 = transform_point(m_vertices[2]);

  vec3 edge1 = v1 - v0;
  vec3 edge2 = v2 - v0;

  vec3 h = cross(r.direction(), edge2);
  double a = dot(edge1, h);

  if (std::abs(a) < 1e-8) {
    return {};
  }

  double f = 1.0 / a;
  vec3 s = r.origin() - v0;
  double u = f * dot(s, h);

  if (u < 0.0 || u > 1.0) {
    return {};
  }

  vec3 q = cross(s, edge1);
  double v = f * dot(r.direction(), q);

  if (v < 0.0 || u + v > 1.0) {
    return {};
  }

  double t = f * dot(edge2, q);

  if (t > min_t) {
    vec3 pos = r(t);
    vec3 normal = normalize(cross(edge1, edge2));
    vec2 uv = {u, v};
    return Intersection{this, r, t, pos, normal, uv};
  }

  return {};

  // you can return an intersection like this
  // return Intersection{this, r, t, pos, normal, uv};
  /* where
      this     : is a pointer to the triangle
      r        : the ray intersecting
      t, pos   : t value and world space position of the intersection
                 (needs to be calculated by you)
      normal   : the surface normal in world space coordinates
                 (given) 
      uv       : local uv texture coordinates of the triangle 
                 (needs to be calculated by you, 
                 hint: barycentric coordinates and uv coordinated on vertices)
  */

  return {};
  }
//==============================================================================
}  // namespace cg
//==============================================================================
