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
      // Calculate the direction from the spotlight to the point p and normalize it
      // direction_to_p = normalize(p - pos)
      vec3 direction_to_p = normalize(p - pos());

      // Calculate the distance from the spotlight to the point p
      // distance = |p - pos|
      double distance = (p - pos()).length();

      // Calculate the cosine of the angle between the direction to the point and the spotlights direction
      // cos_angle = dot(m_direction, direction_to_p)
      double cos_angle = dot(m_direction, direction_to_p);

      // Check if the point is within the spotlight's cone
      // If cos_angle < cos(m_angle), the point is outside the spotlights cone, so return zero radiance
      if (cos_angle < cos(m_angle)) {
          return vec3::zeros();
      }

      // Calculate the incident radiance
      // The intensity of the light decreases with the square of the distance (inverse square law),
      // and also decreases as the angle between the direction to the point and the spotlight's direction increases
      // intensity = (cos_angle^m_angle) / (distance^2)
      double intensity = pow(cos_angle, m_angle) / (distance * distance);

      // The incident radiance is the spectral intensity of the light multiplied by this intensity
      // incident_radiance = spectral_intensity() * intensity
      return spectral_intensity() * intensity;
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
