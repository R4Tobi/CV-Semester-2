#include "orthographiccamera.h"
//==============================================================================
namespace cg {
//==============================================================================
OrthographicCamera::OrthographicCamera(const vec3& eye, const vec3& lookat,
                                       double left, double right, double bottom,
                                       double top, size_t res_x, size_t res_y)
    : Camera{res_x, res_y},
      m_viewdir{lookat - eye},
      m_plane_basis0{normalize(cross(up, -m_viewdir))},
      m_plane_basis1{normalize(cross( -m_plane_basis0, -m_viewdir))},
      m_plane_origin{eye + (m_plane_basis0 * left) + (m_plane_basis1 * bottom)},
      m_pixel_size{(right - left) / (res_x - 1), (top - bottom) / (res_y - 1)} {
}
//------------------------------------------------------------------------------
Ray OrthographicCamera::ray(double x, double y) const {
  const auto moved_eye = m_plane_origin +
                         m_plane_basis0 * m_pixel_size(0) * x +
                         m_plane_basis1 * m_pixel_size(1) * y;
  return Ray{moved_eye, m_viewdir};
}
//==============================================================================
}  // namespace cg
//==============================================================================
