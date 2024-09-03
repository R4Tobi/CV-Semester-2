#ifndef CG_PHONG_H
#define CG_PHONG_H
//==============================================================================
#include "light.h"
#include "material.h"
#include "ray.h"
//==============================================================================
namespace cg {
//==============================================================================
struct Phong : Material {
  double m_shininess;
  //----------------------------------------------------------------------------
  // reflective color:
  // ~~~
  // steel ~0.55
  // aluminum ~0.95
  // water glass and plastic ~0.04
  // diamond ~0.15
  // gold ~0.6 blue-channel and ~0.9 red-channel
  // copper ~0.4 blue-channel and ~0.85 red-channel
  //----------------------------------------------------------------------------

 public:
  //----------------------------------------------------------------------------
  Phong(const ColorSource& colorsource, double shininess);
  Phong(const ColorSource& albedo_color, const double reflectance,
        const ColorSource& reflective_color, double shininess);
  Phong(const ColorSource& albedo_color, const double reflectance,
        const ColorSource& reflective_color, const double refractance,
        const double index_of_refraction, const ColorSource& refractive_color,
        double shininess);
  //----------------------------------------------------------------------------
  Phong(const color& col, double shininess);
  Phong(const color& albedo_color, const double reflectance,
        const color& reflective_color, double shininess);
  Phong(const color& albedo_color, const double reflectance,
        const color& reflective_color, const double refractance,
        const double index_of_refraction, const color& refractive_color,
        double shininess);
  //----------------------------------------------------------------------------
  Phong(const Phong&) = default;
  Phong(Phong&&)      = default;
  //----------------------------------------------------------------------------
  Phong& operator=(const Phong&) = default;
  Phong& operator=(Phong&&)      = default;
  //----------------------------------------------------------------------------
  double&      shininess() { return m_shininess; }
  double       shininess() const { return m_shininess; }
  //----------------------------------------------------------------------------
  /// Calculate the brdf of the physically plausible phong shading model.
  /// \param V the ray direction of the incoming ray (e.g. view ray)
  /// \param L the ray direction of the incoming light
  /// \param N the surface normal of the hit point
  /// \param uv texture uv coordinates
  color brdf_phong(const vec3& V, const vec3& L, const vec3& N,
                    const vec2& uv) const;
  //----------------------------------------------------------------------------
  virtual vec3 shade(const Light& li, const Intersection& i) const override;
  //----------------------------------------------------------------------------
  std::unique_ptr<Material> clone() const override {
    return std::make_unique<Phong>(*this);
  }
};
//==============================================================================
}  // namespace cg
//==============================================================================
#endif
