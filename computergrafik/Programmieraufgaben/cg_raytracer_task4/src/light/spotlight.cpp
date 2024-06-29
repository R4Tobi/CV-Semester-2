#include "spotlight.h"

//==============================================================================
namespace cg {
//==============================================================================
  SpotLight::SpotLight(const vec3& direction, double angle) : 
    m_direction(normalize(direction)), m_angle(angle) {};
  SpotLight::SpotLight(const vec3& direction, double angle, 
                       double r, double g, double b) :
    Light{ r, g, b }, m_direction(normalize(direction)), m_angle(angle) {};
  SpotLight::SpotLight(const vec3& direction, double angle, 
                       const vec3& spectral_intensity) :
    Light{ spectral_intensity }, 
    m_direction(normalize(direction)), 
    m_angle(angle) {};
  //----------------------------------------------------------------------------
  vec3 SpotLight::incident_radiance_at(const vec3& p) const {
 
    const auto L = light_direction_to(p);

    // check if in cone
    double cosLD = dot(normalize(L), normalize(transform_direction(m_direction)));
    if(cosLD > std::abs(std::cos(m_angle))){
        //quadratic fall-off since receiver area decreases quadratically
        return spectral_intensity() / L.squared_length();
    }

    return vec3::zeros();
      }
  //----------------------------------------------------------------------------
  vec3 SpotLight::light_direction_to(const vec3& p) const {
    return p - pos();
  }
  //----------------------------------------------------------------------------
  double SpotLight::distance_to(const vec3& p) const {
    return (p - pos()).length();
  }
//==============================================================================
}  // namespace cg
//==============================================================================
