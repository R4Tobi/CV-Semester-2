#ifndef CG_ORTHOGRAPHIC_CAMERA_H
#define CG_ORTHOGRAPHIC_CAMERA_H
//==============================================================================
#include "camera.h"
//==============================================================================
namespace cg {
//==============================================================================
class OrthographicCamera : public Camera {
 public:
  make_clonable(Camera, OrthographicCamera);

 private:
  static constexpr vec3 up{0, 1, 0};
  const vec3            m_viewdir;
  const vec3            m_plane_basis0, m_plane_basis1;
  const vec3            m_plane_origin;
  const vec2            m_pixel_size;

 public:
  OrthographicCamera(const vec3& eye, const vec3& lookat, double left,
                     double right, double bottom, double top, size_t res_x,
                     size_t res_y);
  Ray ray(double x, double y) const override;
  //============================================================================
};
//==============================================================================
}  // namespace cg
//==============================================================================
#endif
