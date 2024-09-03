#ifndef CG_PERSPECTIVE_CAMERA_H
#define CG_PERSPECTIVE_CAMERA_H
//==============================================================================
#include <cassert>
#include "camera.h"
#include "ray.h"
#include "vec.h"
//==============================================================================
namespace cg {
//==============================================================================
/// \brief Perspective cameras are able to cast rays from one point called 'eye'
/// through an image plane.
///
/// Based on the eye position, a look-at point and a field of view angle the
/// image plane gets constructed.
/// This camera class constructs a right-handed coordinate system.
class PerspectiveCamera : public Camera {
 public:
  make_clonable(Camera, PerspectiveCamera);

 private:
  //----------------------------------------------------------------------------
  // class variables
  //----------------------------------------------------------------------------
  static constexpr vec3 up{0, 1, 0};

  //----------------------------------------------------------------------------
  // member variables
  //----------------------------------------------------------------------------
  const vec3   m_eye, m_u, m_v;
  const double m_plane_half_width, m_plane_half_height;
  const vec3   m_bottom_left;
  const vec3   m_plane_base_x, m_plane_base_y;

 public:
  //----------------------------------------------------------------------------
  // constructors / destructor
  //----------------------------------------------------------------------------
  /// Constructor generates bottom left image plane pixel position and pixel
  /// offset size.
  PerspectiveCamera(const vec3& eye, const vec3& lookat, double fov,
                    size_t res_x, size_t res_y);
  //----------------------------------------------------------------------------
  // object methods
  //----------------------------------------------------------------------------
  /// gets a ray through plane at pixel with coordinate [x,y].
  /// [0,0] is bottom left.
  /// ray goes through center of pixel
  Ray ray(double x, double y) const override;
};
//==============================================================================
}  // namespace cg
//==============================================================================
#endif
