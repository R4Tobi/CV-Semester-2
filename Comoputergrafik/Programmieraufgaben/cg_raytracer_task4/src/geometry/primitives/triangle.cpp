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
std::optional<Intersection> Triangle::check_intersection(const Ray& r,
                                                         double min_t) const {

  // using a small epsilon to test for 0 later on.
  constexpr double eps = 1e-06;

  const vec3 v0 = transform_point(m_vertices[0]);
  const vec3 v1 = transform_point(m_vertices[1]);
  const vec3 v2 = transform_point(m_vertices[2]);

  const vec3 n = normalize(normal());
  

  // The following is an implementation of the Möller - Trumbore algorithm
  // which is considered a very fast ray - triangle intersection test.

  auto v0v1 = v1 - v0;
  auto v0v2 = v2 - v0;
  auto pvec = cross(r.direction(), v0v2);
  auto det = dot(v0v1, pvec);
  // r and triangle are parallel if det is close to 0
  if (std::abs(det) < eps) { return {}; }
  auto inv_det = 1 / det;

  vec3 tvec = r.origin() - v0;
  auto u = dot(tvec, pvec) * inv_det;
  if (u < 0 || u > 1) { return {}; }

  vec3 qvec = cross(tvec, v0v1);
  auto v = dot(r.direction(), qvec) * inv_det;
  if (v < 0 || u + v > 1) { return {}; }

  const auto t = dot(v0v2, qvec) * inv_det;
  const vec3 barycentric_coord{ 1 - u - v, u, v };
  if (t > min_t) {
    vec3 pos = barycentric_coord(0) * v0 + barycentric_coord(1) * v1 +
      barycentric_coord(2) * v2;

    vec2 uv = barycentric_coord(0) * m_uv_coordinates[0] +
      barycentric_coord(1) * m_uv_coordinates[1] +
      barycentric_coord(2) * m_uv_coordinates[2];

    return Intersection{ this, r, t, pos, n, uv };
  }
  return {};

  /*
  This would be the more straight forward solution you learned
  in the exercise class.

  // vectors are orthogonal -> no intersection
  if(std::abs(dot(n, r.direction())) < eps){
    return {};
  }

  // the ray parameter t that hits the triangle plane
  double t = (dot(v0, n) - dot(r.origin(), n))/dot(r.direction(), n);
  if(t < min_t){
    return {};
  }

  vec3 pos = r(t);

  const vec3 e10 = v1 - v0;
  const vec3 e20 = v2 - v0;

  // barycentric coordinates
  double area_triangle = cross(e10, e20).length();
  double u = cross((v2 - v1), (pos - v1)).length()/area_triangle;
  double v = cross((v2 - v0), (pos - v0)).length()/area_triangle;
  double w = cross((v1 - v0), (pos - v0)).length()/area_triangle;;

  // need to actually hit the triangle
  if(u < 0.0 || u > 1.0 || v < 0.0 || v > 1.0 ||
     w < 0.0 || w > 1.0 || u+v+w > 1.0 + eps){

      return {};
  }

  // uv coordinates of the point are the weighted average
  // of the uv coordinates of the corners
  vec2 uv = u * m_uv_coordinates[0]
          + v * m_uv_coordinates[1]
          + w * m_uv_coordinates[2];

  return Intersection{ this, r, t, pos, n, uv };
  */
  }
//==============================================================================
}  // namespace cg
//==============================================================================
