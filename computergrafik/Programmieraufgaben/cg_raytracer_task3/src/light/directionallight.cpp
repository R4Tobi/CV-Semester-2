#include "directionallight.h"
#include "colors.h"
#include "scene.h"
#include "renderable.h"
//==============================================================================
namespace cg {
//==============================================================================
DirectionalLight::DirectionalLight(const vec3& direction)
    : m_direction{normalize(direction)} {}
DirectionalLight::DirectionalLight(const vec3& direction,
                                   const vec3& spectral_intensity)
    : Light{spectral_intensity}, m_direction{normalize(direction)} {}
//----------------------------------------------------------------------------
// TODO quadratic falloff
vec3 DirectionalLight::incident_radiance_at(const vec3& /*pos*/) const {
  return spectral_intensity();
}
//----------------------------------------------------------------------------
vec3 DirectionalLight::light_direction_to(const vec3& /*pos*/) const {
  return m_direction;
}
//----------------------------------------------------------------------------
double DirectionalLight::distance_to(const vec3& /*pos*/) const{
  return std::numeric_limits<double>::max();
}
//==============================================================================
}  // namespace cg
//==============================================================================
