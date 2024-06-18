#define _USE_MATH_DEFINES
#include "phong.h"

#include <cmath>

#include "colors.h"
#include "constantcolorsource.h"
//==============================================================================
namespace cg {
//==============================================================================
Phong::Phong(const ColorSource& albedo_color, double shininess)
    : Material{albedo_color, 0.5, ConstantColorSource{0.95, 0.95, 0.95}},
      m_shininess{shininess} {}
//------------------------------------------------------------------------------
Phong::Phong(const ColorSource& albedo_color, const double reflectance,
             const ColorSource& reflective_color, double shininess)
    : Material{albedo_color, reflectance, reflective_color},
      m_shininess{shininess} {}
//------------------------------------------------------------------------------
Phong::Phong(const ColorSource& albedo_color, const double reflectance,
             const ColorSource& reflective_color, const double refractance,
             const double       index_of_refraction,
             const ColorSource& refractive_color, double shininess)
    : Material{albedo_color, reflectance,         reflective_color,
               refractance,  index_of_refraction, refractive_color},
      m_shininess{shininess} {}
//------------------------------------------------------------------------------
Phong::Phong(const color& col, double shininess)
    : Material{ConstantColorSource{col}}, m_shininess{shininess} {}
//------------------------------------------------------------------------------
Phong::Phong(const color& albedo_color, const double reflectance,
             const color& reflective_color, double shininess)
    : Material{ConstantColorSource{albedo_color}, reflectance,
               ConstantColorSource{reflective_color}},
      m_shininess{shininess} {}
//------------------------------------------------------------------------------
Phong::Phong(const color& albedo_color, const double reflectance,
             const color& reflective_color, const double refractance,
             const double index_of_refraction, const color& refractive_color,
             double shininess)
    : Material{ConstantColorSource{albedo_color},
               reflectance,
               ConstantColorSource{reflective_color},
               refractance,
               index_of_refraction,
               ConstantColorSource{refractive_color}},
      m_shininess{shininess} {}
//==============================================================================

color Phong::brdf_phong(const vec3& V, const vec3& L, const vec3& N,
                        const vec2& uv) const {
  // normalize the vectors
  vec3 L_normalized = normalize(L);
  vec3 N_normalized = normalize(N);
  vec3 V_normalized = normalize(V);

  // calculate diffuse
  double NdotL = max(0.0, dot(N_normalized, L_normalized));
  vec3 diffuse = (NdotL * sample_albedo_color(uv)) / M_PI;

  // calculate specular
  vec3 R = reflect(-L_normalized, N_normalized); // reflection direction
  double VdotR = max(0.0, dot(V_normalized, R));
  vec3 specular = pow(VdotR, m_shininess) * (m_shininess + 2) / (2 * M_PI) * sample_reflective_color(uv);

  // return the sum of the diffuse and specular components
  return diffuse + specular;
}

vec3 Phong::shade(const Light& light_source, const Intersection& hit) const {
  vec3 L = normalize(light_source.pos() - hit.position); // light direction
  vec3 V = normalize(-hit.incident_ray.direction()); // view direction
  vec3 N = hit.normal; // surface normal

  // calculate the BRDF
  color brdf = brdf_phong(V, L, N, hit.uv);

  // scale the light intensity by the maximum color component
  vec3 light_intensity = light_source.spectral_intensity();

  // calculate the outgoing radiance by multiplying the BRDF with the light intensity
  vec3 radiance = brdf * light_intensity / pow(light_source.distance_to(hit.position), 2);
  return radiance;
}

//==============================================================================
}  // namespace cg
//==============================================================================
