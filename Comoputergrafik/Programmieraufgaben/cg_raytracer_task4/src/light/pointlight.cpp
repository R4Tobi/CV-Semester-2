#include "pointlight.h"
#include "colors.h"
#include "scene.h"
#include "renderable.h"
//==============================================================================
namespace cg {
//==============================================================================
PointLight::PointLight(double r, double g, double b) : Light{r, g, b} {}
PointLight::PointLight(const vec3& spectral_intensity) : Light{spectral_intensity} {}
//----------------------------------------------------------------------------
vec3 PointLight::incident_radiance_at(const vec3& p) const {
  const auto L = light_direction_to(p);
  //quadratic fall-off since receiver area decreases quadratically
  return spectral_intensity() / L.squared_length();
}
//----------------------------------------------------------------------------
vec3 PointLight::light_direction_to(const vec3& p) const {
  return p - pos();
}
//----------------------------------------------------------------------------
double PointLight::distance_to(const vec3& p) const{
  return (p - pos()).length();
}
//==============================================================================
}  // namespace cg
//==============================================================================
