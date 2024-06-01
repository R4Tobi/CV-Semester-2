#include "movable.h"

//==============================================================================
namespace cg {
//==============================================================================

//------------------------------------------------------------------------------
Movable::Movable()
    : m_transform{mat4::eye()},
      m_normalmatrix{mat4::eye()},
      m_parent{nullptr} {}

//------------------------------------------------------------------------------
Movable& Movable::translate(const vec3& xyz) {
  return translate(xyz(0), xyz(1), xyz(2));
}

//------------------------------------------------------------------------------
Movable& Movable::translate(double x, double y, double z) {
  m_transform = translation_matrix(x, y, z) * m_transform;
  m_normalmatrix = transpose(*inverse(m_transform));
  return *this;
}

//------------------------------------------------------------------------------
Movable& Movable::scale(const vec3& xyz) {
  return scale(xyz(0), xyz(1), xyz(2));
}

//------------------------------------------------------------------------------
Movable& Movable::scale(double x, double y, double z) {
  m_transform = scale_matrix(x, y, z) * m_transform;
  m_normalmatrix = transpose(*inverse(m_transform));
  return *this;
}

//------------------------------------------------------------------------------
Movable& Movable::scale(double factor) {
  return scale(factor, factor, factor);
}

//------------------------------------------------------------------------------
Movable& Movable::rotate_x(double alpha) {
  m_transform = rotation_x_matrix(alpha) * m_transform;
  m_normalmatrix = transpose(*inverse(m_transform));
  return *this;
}

//------------------------------------------------------------------------------
Movable& Movable::rotate_y(double alpha) {
  m_transform = rotation_y_matrix(alpha) * m_transform;
  m_normalmatrix = transpose(*inverse(m_transform));
  return *this;
}

//------------------------------------------------------------------------------
Movable& Movable::rotate_z(double alpha) {
  m_transform = rotation_z_matrix(alpha) * m_transform;
  m_normalmatrix = transpose(*inverse(m_transform));
  return *this;
}

//------------------------------------------------------------------------------
Movable& Movable::rotate(const vec3& xyz, double alpha) {
  return rotate(xyz(0), xyz(1), xyz(2), alpha);
}

//------------------------------------------------------------------------------
Movable& Movable::rotate(double x, double y, double z, double alpha) {
  m_transform = rotation_matrix(x, y, z, alpha) * m_transform;
  m_normalmatrix = transpose(*inverse(m_transform));
  return *this;
}

//==============================================================================
// transform matrices
//==============================================================================

//------------------------------------------------------------------------------
mat4 translation_matrix(double x, double y, double z) {
  return mat4{{1.0, 0.0, 0.0, x},
              {0.0, 1.0, 0.0, y},
              {0.0, 0.0, 1.0, z},
              {0.0, 0.0, 0.0, 1.0}};
}

//------------------------------------------------------------------------------
mat4 scale_matrix(double x, double y, double z) {
  return mat4{{x, 0.0, 0.0, 0.0},
              {0.0, y, 0.0, 0.0},
              {0.0, 0.0, z, 0.0},
              {0.0, 0.0, 0.0, 1.0}};
}

//------------------------------------------------------------------------------
mat4 rotation_x_matrix(double a) {
  return mat4{{1.0,         0.0,          0.0, 0.0},
              {0.0, std::cos(a), -std::sin(a), 0.0},
              {0.0, std::sin(a),  std::cos(a), 0.0},
              {0.0,         0.0,          0.0, 1.0}};
}

//------------------------------------------------------------------------------
mat4 rotation_y_matrix(double a) {
  return mat4{{std::cos(a), 0.0, -std::sin(a), 0.0},
              {0.0,         1.0,          0.0, 0.0},
              {std::sin(a), 0.0,  std::cos(a), 0.0},
              {0.0,         0.0,          0.0, 1.0}};
}

//------------------------------------------------------------------------------
mat4 rotation_z_matrix(double a) {
  return mat4{{std::cos(a), -std::sin(a), 0.0, 0.0},
              {std::sin(a),  std::cos(a), 0.0, 0.0},
              {        0.0,          0.0, 1.0, 0.0},
              {        0.0,          0.0, 0.0, 1.0}};
}

//------------------------------------------------------------------------------
mat4 rotation_matrix(double x, double y, double z, double a) {
  return mat4{{x * x * (1 - std::cos(a)) + std::cos(a),
               x * y * (1 - std::cos(a)) - z * std::sin(a),
               x * z * (1 - std::cos(a)) + y * std::sin(a),
               0.0},
              {x * y * (1 - std::cos(a)) + z * std::sin(a),
               y * y * (1 - std::cos(a)) + std::cos(a),
               y * z * (1 - std::cos(a)) - x * std::sin(a),
               0.0},
              {x * z * (1 - std::cos(a)) - y * std::sin(a),
               y * z * (1 - std::cos(a)) + x * std::sin(a),
               z * z * (1 - std::cos(a)) + std::cos(a),
               0.0},
              {0.0, 0.0, 0.0, 1.0}};
}

//==============================================================================
}  // namespace cg
//==============================================================================
