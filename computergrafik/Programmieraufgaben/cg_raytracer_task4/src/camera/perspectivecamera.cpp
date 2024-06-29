#define _USE_MATH_DEFINES
#include <cmath>

#include "perspectivecamera.h"
//==============================================================================
namespace cg {
//==============================================================================
PerspectiveCamera::PerspectiveCamera(const vec3& eye, const vec3& lookat,
                                     double fov, size_t res_x, size_t res_y)
    : Camera{res_x, res_y},
      m_eye{eye},
      m_u{cross(normalize(lookat - eye), up)},
      m_v{cross(m_u, normalize(lookat - eye))},
      m_plane_half_width{std::tan(fov * M_PI / 180)},
      m_plane_half_height{double(res_y) / double(res_x) * m_plane_half_width},
      m_bottom_left{ eye + normalize(lookat - eye) - m_u * m_plane_half_width -
                    m_v * m_plane_half_height},
      m_plane_base_x{m_u * 2 * m_plane_half_width / res_x},
      m_plane_base_y{m_v * 2 * m_plane_half_height / res_y} {}
//------------------------------------------------------------------------------
Ray PerspectiveCamera::ray(double x, double y) const {
  assert(x < plane_width());
  assert(y < plane_height());
  const auto view_plane_point =
      m_bottom_left + x * m_plane_base_x + y * m_plane_base_y;
  return {{m_eye}, {view_plane_point - m_eye}};
}
//==============================================================================
}  // namespace cg
//==============================================================================
