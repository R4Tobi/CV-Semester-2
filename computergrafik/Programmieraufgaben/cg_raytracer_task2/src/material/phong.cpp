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

  /*
  ============================================================================
   Task 3
  ============================================================================
   Implement the physically plausible Phong BRDF function f_r.
   The diffuse and specular reflectance coefficients rho_d and rho_s are
   colors instead of scalar values (see task description). See material.h for
   what colors are available.

   The test assumes that the parameters L and V point towards the surface and
   N away from it.

   The number pi is given by M_PI.

   See header file for a description of the parameters of the function.
  */

  // calculate diffuse
  vec3 diffuse = vec3::zeros();

  // calculate specular
  vec3 specular = vec3::zeros();

  //return diffuse + specular;
  return sample_albedo_color(uv); // <- dummy output!
  }
//------------------------------------------------------------------------------
vec3 Phong::shade(const Light& light_source, const Intersection& hit) const {

  /*
  ============================================================================
   Task 3
  ============================================================================
   Implement the outgoing radiance f_r * E. Use the function brdf_phong.
   Also check what methods and members the light_source (light.h) and 
   intersection (intersection.h) offer for accessing the needed values to
   calculate the value.

   The vector class (vec.h) already implements functions such as dot and cross 
   product as well as basic arithmetic operations such as + and -.
  */

  return sample_albedo_color(hit.uv) / 4; // <- dummy output!
  }
//==============================================================================
}  // namespace cg
//==============================================================================
