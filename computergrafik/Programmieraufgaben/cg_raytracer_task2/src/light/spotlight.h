#ifndef CG_SPOTLIGHT_H
#define CG_SPOTLIGHT_H
//==============================================================================
#include "light.h"
//==============================================================================
namespace cg {
//==============================================================================
  class SpotLight : public Light {
  vec3 m_direction;
  double m_angle;

  public:
   //----------------------------------------------------------------------------
   // constructors
   //----------------------------------------------------------------------------
    SpotLight(const SpotLight&) = default;
    SpotLight(SpotLight&&) = default;
    SpotLight(const vec3& direction, double angle);
    SpotLight(const vec3& direction, double angle, double r, double g, double b);
    SpotLight(const vec3& direction, double angle, const vec3& spectral_intensity);
    //----------------------------------------------------------------------------
    // methods
    //----------------------------------------------------------------------------
    vec3 incident_radiance_at(const vec3& pos) const override;
    //----------------------------------------------------------------------------
    vec3 light_direction_to(const vec3& pos) const override;
    //----------------------------------------------------------------------------
    double distance_to(const vec3& pos) const override;
    //----------------------------------------------------------------------------
    make_clonable(Light, SpotLight);
  };
//==============================================================================
}  // namespace cg
//==============================================================================
#endif