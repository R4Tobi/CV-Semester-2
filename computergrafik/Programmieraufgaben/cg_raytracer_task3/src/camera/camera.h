#ifndef CG_CAMERA_H
#define CG_CAMERA_H
//==============================================================================
#include "ray.h"
#include "clonable.h"
//==============================================================================
namespace cg {
//==============================================================================
/// \brief Interface for camera implementations.
///
/// Implementations must override the ray method that casts rays through the
/// camera's image plane.
class Camera : public Clonable<Camera> {
  //----------------------------------------------------------------------------
  // member variables
  //----------------------------------------------------------------------------
  const std::array<size_t, 2> m_resolution;
 public:
  //----------------------------------------------------------------------------
  // constructors / destructor
  //----------------------------------------------------------------------------
  Camera(size_t res_x, size_t res_y) : m_resolution{res_x, res_y} {}
  virtual ~Camera() = default;
  //----------------------------------------------------------------------------
  // object methods
  //----------------------------------------------------------------------------
  /// Returns number of pixels of plane in x-direction.
  size_t plane_width() const { return m_resolution[0]; }
  //----------------------------------------------------------------------------
  /// Returns number of pixels of plane in y-direction.
  size_t plane_height() const { return m_resolution[1]; }
  //----------------------------------------------------------------------------
  // interface methods
  //----------------------------------------------------------------------------
  /// \brief Gets a ray through plane at pixel with coordinate [x,y].
  ///
  /// [0,0] is bottom left.
  /// Ray goes through center of pixel.
  /// This method must be overridden in camera implementations.
  virtual Ray ray(double x, double y) const = 0;
};
//==============================================================================
}  // namespace cg
//==============================================================================
#endif
