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

  // reflect incident view vector at normal
  auto R = reflect(L, N);

  // get cosine of angle between light direction and reflected vector
  auto cos_omega = std::max<double>(0, dot(-V, R));
  // compute specular power
  double spec_power = pow(cos_omega, m_shininess);

  vec3 diffuse = sample_albedo_color(uv) / M_PI;
  vec3 specular = (m_shininess + 2.0) / (2.0 * M_PI) *
      sample_reflective_color(uv) *
      spec_power;

  // assemble the normalized phong-brdf
  return diffuse + specular;
  }
//------------------------------------------------------------------------------
vec3 Phong::shade(const Light& light_source, const Intersection& hit) const {

  const auto L = normalize(light_source.light_direction_to(hit.position));
  const auto N = hit.normal;
  const auto V = normalize(hit.incident_ray.direction());

  // get cosine of angle between light direction and normal
  const double cos_nl = std::abs(dot(N, L));

  // compute radiance incoming from the light
  // (quadratic fall-off since receiver area decreases quadratically)
  auto incident_radiance = light_source.incident_radiance_at(hit.position);

  const auto irradiance = incident_radiance * cos_nl;

  // compute, how much of the arriving irradiance is reflected
  // toward the viewer (multiply by BRDF)
  const auto outgoing_radiance = irradiance * brdf_phong(V, L, N, hit.uv);

  return outgoing_radiance;
  }
//==============================================================================
}  // namespace cg
//==============================================================================
