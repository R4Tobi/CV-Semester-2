#include "bezier_patch_mesh.h"
//==============================================================================
namespace cg {
//==============================================================================
BezierPatchMesh::BezierPatchMesh(const Material &material, size_t m, size_t n,
                                 size_t res_u, size_t res_v)
    : Mesh{material}, m_m{m}, m_n{n}, m_res_u{res_u}, m_res_v{res_v} {
  // Allocate memory for Bezier points
  m_control_points.resize(m * n);
}
//------------------------------------------------------------------------------
void BezierPatchMesh::construct_triangle(
    const std::vector<BezierPatchSample> &samples, size_t i0, size_t i1,
    size_t i2) {
  const auto &v0   = samples[i0].position;
  const auto &v1   = samples[i1].position;
  const auto &v2   = samples[i2].position;
  const auto &uv0  = samples[i0].uv;
  const auto &uv1  = samples[i1].uv;
  const auto &uv2  = samples[i2].uv;
  auto        n0   = samples[i0].normal;
  auto        n1   = samples[i1].normal;
  auto        n2   = samples[i2].normal;
  const auto  n0sl = n0.squared_length();
  const auto  n1sl = n1.squared_length();
  const auto  n2sl = n2.squared_length();
  if (n0sl > 0 && n1sl > 0 && n2sl > 0) {
    n0 /= std::sqrt(n0sl);
    n1 /= std::sqrt(n1sl);
    n2 /= std::sqrt(n2sl);
    size_t idx0 = insert_vertex(v0);
    size_t idx1 = insert_vertex(v1);
    size_t idx2 = insert_vertex(v2);
    insert_triangle(idx0, idx1, idx2);
    insert_normal(n0);
    insert_normal(n1);
    insert_normal(n2);
    insert_uv_coordinate(uv0);
    insert_uv_coordinate(uv1);
    insert_uv_coordinate(uv2);
    //} else {
    //  // fall back to triangle normals if normals are not defined
    //  auto       n   = cross(v1 - v0, v2 - v0);
    //  const auto nsl = n.squared_length();
    //  if (nsl > 0) { n /= std::sqrt(nsl); }
    //  n0 = n1 = n2 = n;
  }
}
//------------------------------------------------------------------------------
void BezierPatchMesh::initialize() {
  // this function samples the underlying continuous patch and tessellates it
  // regularly with triangles

  // sample at triangle vertices at uniform uv parameters
  std::vector<BezierPatchSample> samples;
  samples.reserve(m_res_u * m_res_v);

  for (size_t j = 0; j < m_res_v; ++j) {
    for (size_t i = 0; i < m_res_u; ++i) {
      samples.push_back(
          sample(double(i) / (m_res_u - 1), double(j) / (m_res_v - 1)));
    }
  }

  // construct triangle strips based on the computed samples
  for (size_t j = 0; j < m_res_v - 1; ++j) {
    for (size_t i = 0; i < m_res_u - 1; ++i) {
      const size_t i00 = m_res_u * j + i;
      const size_t i10 = m_res_u * j + (i + 1);
      const size_t i01 = m_res_u * (j + 1) + i;
      const size_t i11 = m_res_u * (j + 1) + (i + 1);
      construct_triangle(samples, i00, i10, i01);
      construct_triangle(samples, i10, i11, i01);
    }
  }
}
//------------------------------------------------------------------------------
BezierPatchMesh::BezierPatchSample BezierPatchMesh::sample(double u,
                                                           double v) const {
  std::vector<vec3> u_control_points(m_m), v_control_points(m_n);

  // create control points in u direction
  for (size_t i = 0; i < m_m; ++i) {
    for (size_t j = 0; j < m_n; ++j) {
      v_control_points[j] = control_point(i, j);
    }
    u_control_points[i] = de_casteljau(v_control_points, v).first;
  }
  auto [point_u, tangent_u] = de_casteljau(u_control_points, u);

  // create control points in v direction
  for (size_t j = 0; j < m_n; ++j) {
    for (size_t i = 0; i < m_m; ++i) {
      u_control_points[i] = control_point(i, j);
    }
    v_control_points[j] = de_casteljau(u_control_points, u).first;
  }
  auto [point_v, tangent_v] = de_casteljau(v_control_points, v);

  return {std::move(point_u), cross(tangent_u, tangent_v), vec2{u, v}};
}
//------------------------------------------------------------------------------
std::pair<vec3, vec3> BezierPatchMesh::de_casteljau(
    std::vector<vec3> control_points, double t) const {

  /*
  ============================================================================
   Task 2
  ============================================================================
   Implement De Casteljau's algorithm for a curve. It should return the point
   x(t) of the curve at a given t and the tangent of the curve at that point. 
   The function should work for an arbitrary number of control points and an
   arbitrary t.
  */

  // The following just linearly interpolates the control points.
  // Replace it by an actual implementation of De Casteljau's algorithm
  // ----------------------------

  // index of last control point
  int n_segments = control_points.size() - 1;
  // the segment in which the parameter t is
  int seg = int(t * n_segments);

  // this means t==1, so we handle this special case separately
  // because it would break the calculation of s
  if(seg == n_segments){
    return{ control_points.back(), control_points.back() - control_points[n_segments-1] };
  }

  // variable for lin. interpolation
  double s = n_segments*t - seg;
  
  vec3 point = (1-s)*control_points[seg] + s*control_points[seg+1];
  vec3 tangent = control_points[seg+1] - control_points[seg];
  // ----------------------------

  return { std::move(point), std::move(tangent) };
  }
//==============================================================================
}  // namespace cg
//==============================================================================
