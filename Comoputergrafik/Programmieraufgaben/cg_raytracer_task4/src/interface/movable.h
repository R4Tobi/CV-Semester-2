#ifndef CG_MOVABLE_H
#define CG_MOVABLE_H
//==============================================================================
#include "mat.h"
//==============================================================================
namespace cg {
//==============================================================================
// homogeneous vectors
//==============================================================================
/// adds 1 as 4th component to vector
constexpr inline auto homogeneous_point(const vec3& v) {
  return vec4{v(0), v(1), v(2), 1};
}
//------------------------------------------------------------------------------
/// adds 0 as 4th component to vector
constexpr inline auto homogeneous_direction(const vec3& v) {
  return vec4{v(0), v(1), v(2), 0};
}
//==============================================================================
// transform matrices
//==============================================================================
/// creates a translation matrix
mat4 translation_matrix(double x, double y, double z);
//------------------------------------------------------------------------------
/// creates a scale matrix
mat4 scale_matrix(double x, double y, double z);
//------------------------------------------------------------------------------
/// creates a rotation matrix around x-axis with angle a
mat4 rotation_x_matrix(double a);
//------------------------------------------------------------------------------
/// creates a rotation matrix around y-axis with angle a
mat4 rotation_y_matrix(double a);
//------------------------------------------------------------------------------
/// creates a rotation matrix around z-axis with angle a
mat4 rotation_z_matrix(double a);
//------------------------------------------------------------------------------
/// creates a rotation matrix around axis [x,y,z] with angle a
mat4 rotation_matrix(double x, double y, double z, double a);
//==============================================================================
/// \brief Derive from this class to make points and directions transformable.
///
/// A derived movable class is able to be translated, rotated and scaled using
/// a 4x4 homogeneous transform matrix. It is also possible to declare a movable
/// object as a parent of another movable object. All transformations done to
/// the parent will also be applied to its children.
class Movable {
  mat4           m_transform;
  mat4           m_normalmatrix;
  const Movable* m_parent;

 public:
  Movable();
  Movable(const Movable&) = default;
  Movable(Movable&&)      = default;
  Movable& operator=(const Movable&) = default;
  Movable& operator=(Movable&&) = default;
  virtual ~Movable()            = default;

  virtual Movable& translate(const vec3& xyz);
  virtual Movable& translate(double x, double y, double z);
  virtual Movable& scale(const vec3& xyz);
  virtual Movable& scale(double x, double y, double z);
  virtual Movable& scale(double factor);
  virtual Movable& rotate_x(double alpha);
  virtual Movable& rotate_y(double alpha);
  virtual Movable& rotate_z(double alpha);
  virtual Movable& rotate(const vec3& xyz, double alpha);
  virtual Movable& rotate(double x, double y, double z, double alpha);
  //----------------------------------------------------------------------------
  void set_parent(const Movable& m) { m_parent = &m; }
  void unset_parent() { m_parent = nullptr; }
  //----------------------------------------------------------------------------
  constexpr mat4 transform_matrix() const {
    if (m_parent) { return m_transform * m_parent->transform_matrix(); }
    return m_transform;
  }
  //----------------------------------------------------------------------------
  constexpr mat4 normal_matrix() const {
    if (m_parent) { return m_normalmatrix * m_parent->normal_matrix(); }
    return m_normalmatrix;
  }
  //----------------------------------------------------------------------------
  constexpr vec3 transform_point(const vec3& p) const {
    auto t = transform_matrix() * homogeneous_point(p);
    return {t(0), t(1), t(2)};
  }
  //----------------------------------------------------------------------------
  constexpr vec3 inverse_point(const vec3& p) const {
    auto t = transpose(normal_matrix()) * homogeneous_point(p);
    return {t(0), t(1), t(2)};
  }
  //----------------------------------------------------------------------------
  constexpr vec3 transform_direction(const vec3& d) const {
    auto t = transform_matrix() * homogeneous_direction(d);
    return {t(0), t(1), t(2)};
  }
  //----------------------------------------------------------------------------
  constexpr vec3 inverse_direction(const vec3& d) const {
    auto t = transpose(normal_matrix()) * homogeneous_direction(d);
    return {t(0), t(1), t(2)};
  }
  //----------------------------------------------------------------------------
  vec3 transform_normal(const vec3& d) const {
    auto t = normal_matrix() * homogeneous_direction(d);
    return {t(0), t(1), t(2)};
  }
};
//==============================================================================
}  // namespace cg
//==============================================================================
#endif
