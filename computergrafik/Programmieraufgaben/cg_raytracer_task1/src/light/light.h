#ifndef CG_LIGHT_H
#define CG_LIGHT_H
//==============================================================================
#include "clonable.h"
#include "movable.h"
#include "ray.h"
//==============================================================================
namespace cg {
//==============================================================================
// forward declaring scene to avoid include cycles
class Scene;
//==============================================================================
class Light : public Movable, public Clonable<Light> {
  vec3 m_spectral_intensity;

 public:
  Light() = default;
  Light(double r, double g, double b) : m_spectral_intensity{r, g, b} {}
  Light(const vec3& spectral_intensity)
      : m_spectral_intensity{spectral_intensity} {}

  Light(const Light&) = default;
  Light(Light&&)      = default;

  virtual ~Light() = default;
  //----------------------------------------------------------------------------
  /// Returns the incident radiance \f$L_i = \frac{I}{distance^2}\f$ received at
  /// a point pos from this light source. Also considers intensity fall off 
  /// based on distance to the point.
  virtual vec3 incident_radiance_at(const vec3& pos) const = 0;
  //----------------------------------------------------------------------------
  /// Returns light direction to a position
  virtual vec3 light_direction_to(const vec3& pos) const = 0;
  //----------------------------------------------------------------------------
  /// Returns distance of light source to a position
  virtual double distance_to(const vec3& pos) const = 0;
  //----------------------------------------------------------------------------
  /// Returns the spectral intensity \f$I\f$ that also specifies luminous 
  /// intensity. 
  vec3 spectral_intensity() const { return m_spectral_intensity; }
  //----------------------------------------------------------------------------
  /// Position of light sources can be changed by transform
  constexpr vec3 pos() const { return transform_point(vec3::zeros()); }
};
//==============================================================================
}  // namespace cg
//==============================================================================
#endif
