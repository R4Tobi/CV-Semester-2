#ifndef CG_BEZIER_PATCH_MESH_H
#define CG_BEZIER_PATCH_MESH_H
//==============================================================================
#include "mesh.h"
//==============================================================================
namespace cg {
//==============================================================================
/// Bezier surface stores the set of control points and allows
/// creation of a triangle mesh by sampling the parametric surface
class BezierPatchMesh : public Mesh {
 public:
  //============================================================================
  make_clonable(Renderable, BezierPatchMesh);

 private:
  size_t            m_m, m_n;          ///< patch control point dimensions
  size_t            m_res_u, m_res_v;  ///< triangle resolution
  std::vector<vec3> m_control_points;  ///< patch control points
 public:
  struct BezierPatchSample {
    vec3 position;
    vec3 normal;
    vec2 uv;
  };
  /// \brief BezierMeshPatch constructor
  /// \param material Material for reflective properties
  /// \param m    number of control points in u direction
  /// \param n    number of control points in v direction
  /// \param res_u triangle resolution in u direction
  /// \param res_v triangle resolution in v direction
  BezierPatchMesh(const Material& material, size_t m, size_t n, size_t res_u,
                  size_t res_v);
  BezierPatchMesh(const BezierPatchMesh&)           = default;
  BezierPatchMesh(BezierPatchMesh&& other) noexcept = default;
  BezierPatchMesh& operator=(const BezierPatchMesh&) = default;
  BezierPatchMesh& operator=(BezierPatchMesh&&) noexcept = default;

  // Must be called before rendering and after control point manipulation
  // Creates the set of triangles.
  void initialize();
  void construct_triangle(const std::vector<BezierPatchSample>& samples,
                          size_t i0, size_t i1, size_t i2);

  void set_control_point(size_t i, size_t j, const vec3& p) {
    m_control_points[m_m * j + i] = p;
  }
  const vec3& control_point(size_t i, size_t j) const {
    return m_control_points[m_m * j + i];
  }

  ///  Samples the Bezier surface at the given point.
  /// \brief sample
  /// \param u coordinate of domain point
  /// \param v coordinate of domain point
  /// \return BezierPatchSample sample
  BezierPatchSample sample(double u, double v) const;

  /// Perform De Casteljau's algorithm on a curve
  /// \param control_points control points of the curve in order
  /// \param t parameter value for which to evaluate the curve
  /// \return (point at t, tangent at t) 
  std::pair<vec3, vec3> de_casteljau(std::vector<vec3> control_points,
                                     double            t) const;
};
//==============================================================================
}  // namespace cg
//==============================================================================
#endif
