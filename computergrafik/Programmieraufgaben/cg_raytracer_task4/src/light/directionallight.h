#ifndef CG_DIRECTIONALLIGHT_H
#define CG_DIRECTIONALLIGHT_H
//==============================================================================
#include "light.h"
//==============================================================================
namespace cg {
//==============================================================================
class DirectionalLight : public Light {
  vec3 m_direction;
 public:
  //----------------------------------------------------------------------------
  DirectionalLight(const DirectionalLight&) = default;
  DirectionalLight(DirectionalLight&&) = default;
  //----------------------------------------------------------------------------
  DirectionalLight(const vec3& direction);
  DirectionalLight(const vec3& direction, const vec3& spectral_intensity);
  //----------------------------------------------------------------------------
  vec3 incident_radiance_at(const vec3& pos) const override ;
  //----------------------------------------------------------------------------
  vec3 light_direction_to(const vec3& pos) const override;
  //----------------------------------------------------------------------------
  double distance_to(const vec3& pos) const override;
  //----------------------------------------------------------------------------
  make_clonable(Light, DirectionalLight);
};
//==============================================================================
}  // namespace cg
//==============================================================================
#endif
