#ifndef CG_MATERIAL_H
#define CG_MATERIAL_H
//==============================================================================
#include <memory>
#include "clonable.h"
#include "colorsource.h"
#include "intersection.h"
#include "vec.h"
#include "light.h"
//==============================================================================
namespace cg {
//==============================================================================
class Material : public Clonable<Material> {
  std::unique_ptr<ColorSource> m_albedo_color;

  double                       m_reflectance;
  std::unique_ptr<ColorSource> m_reflective_color;

  double                       m_refractance;
  double                       m_index_of_refraction;
  std::unique_ptr<ColorSource> m_refractive_color;

 public:
  Material(const ColorSource& albedo_color)
      : m_albedo_color{albedo_color.clone()},
        m_reflectance{0},
        m_reflective_color{albedo_color.clone()},
        m_refractance{0},
        m_index_of_refraction{1},
        m_refractive_color{albedo_color.clone()} {}
  Material(const ColorSource& albedo, const double reflectance,
           const ColorSource& reflective)
      : m_albedo_color{albedo.clone()},
        m_reflectance{reflectance},
        m_reflective_color{reflective.clone()},
        m_refractance{0},
        m_index_of_refraction{1},
        m_refractive_color{albedo.clone()} {}
  Material(const ColorSource& albedo, const double reflectance,
           const ColorSource& reflective, const double refractance,
           const double index_of_refraction, const ColorSource& refractive)
      : m_albedo_color{albedo.clone()},
        m_reflectance{reflectance},
        m_reflective_color{reflective.clone()},
        m_refractance{refractance},
        m_index_of_refraction{index_of_refraction},
        m_refractive_color{refractive.clone()} {}
  Material(const Material& other)
      : m_albedo_color{other.m_albedo_color->clone()},
        m_reflectance{other.m_reflectance},
        m_reflective_color{other.m_reflective_color->clone()},
        m_refractance{other.m_refractance},
        m_index_of_refraction{other.m_index_of_refraction},
        m_refractive_color{other.m_refractive_color->clone()} {}
  Material(Material&& other) noexcept
      : m_albedo_color{std::move(other.m_albedo_color)},
        m_reflectance{other.m_reflectance},
        m_reflective_color{std::move(other.m_reflective_color)},
        m_refractance{other.m_refractance},
        m_index_of_refraction{other.m_index_of_refraction},
        m_refractive_color{std::move(other.m_refractive_color)} {}
  //----------------------------------------------------------------------------
  Material& operator=(const Material& other) {
    m_albedo_color        = other.m_albedo_color->clone();
    m_reflectance         = other.m_reflectance;
    m_reflective_color    = other.m_reflective_color->clone();
    m_refractance         = other.m_refractance;
    m_index_of_refraction = other.m_index_of_refraction;
    m_refractive_color    = other.m_refractive_color->clone();
    return *this;
  }
  Material& operator=(Material&& other) noexcept {
    m_albedo_color        = std::move(other.m_albedo_color);
    m_reflectance         = other.m_reflectance;
    m_reflective_color    = std::move(other.m_reflective_color);
    m_refractance         = other.m_refractance;
    m_index_of_refraction = other.m_index_of_refraction;
    m_refractive_color    = std::move(other.m_refractive_color);
    return *this;
  }
  //----------------------------------------------------------------------------
  virtual ~Material() = default;
  //----------------------------------------------------------------------------
  /// Calculates the outgoing radiance \f$f_r \cdot E\f$ at the intersection
  /// point for the light source `l`
  /// \param l Light source used for shading.
  /// \param i Intersection point that should be shaded.
  /// \returns \f$f_r \cdot E\f$ as an RGB color.
  virtual color shade(const Light& l, const Intersection& i) const = 0;
  //----------------------------------------------------------------------------
  /// The reflectance of the material, i.e. what fraction of the light is
  /// reflected back into the scene. The reflectance also gives the color
  /// contribution of reflected rays to the outgoing color.
  /// E.g. A mirror would have reflectance close to 1, this means the color
  /// seen in the mirror are the ones of the objects that are reflected by the
  /// mirror.
  double reflectance() const { return m_reflectance; }
  void set_reflectance(const double reflectance) {
    m_reflectance = reflectance;
  }
  //----------------------------------------------------------------------------
  /// The refractance of the material, i.e. what fraction of the light is
  /// refracted inside the material. The refractance also gives the color
  /// contribution of refracted rays to the outgoing color.
  /// E.g. Colorless glass would have refractance close to 1, this means
  /// the actual perceived color would be anything behind the glass object.
  double refractance() const { return m_refractance; }
  void set_refractance(const double refractance) {
    m_refractance = refractance;
  }
  //----------------------------------------------------------------------------
  /// The materials index of refraction (IOR), also called refractive index.
  /// The refractive index determines how much the path of light is bent, 
  /// or refracted, when entering or exiting a material.
  double index_of_refraction() const { return m_index_of_refraction; }
  auto set_index_of_refraction(const double ior) {
    return m_index_of_refraction = ior;
  }
  //----------------------------------------------------------------------------
  /// Sample the albedo (diffuse) color of the material. This is the color 
  /// equivalent to the scalar diffuse reflection constant.
  color sample_albedo_color(const vec2& uv) const {
    return m_albedo_color->sample(uv);
  }
  // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
  /// @copydoc cg::Material::sample_albedo_color(const vec2&) const
  color sample_albedo_color(const double u, const double v) const {
    return m_albedo_color->sample(u, v);
  }
  //----------------------------------------------------------------------------
  /// Sample the reflective (specular) color of the material. This is the color 
  /// equivalent to the scalar specular reflection constant.
  color sample_reflective_color(const vec2& uv) const {
    return m_reflective_color->sample(uv);
  }
  // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
  /// @copydoc cg::Material::sample_reflective_color(const vec2&) const
  color sample_reflective_color(const double u, const double v) const {
    return m_reflective_color->sample(u, v);
  }
  //----------------------------------------------------------------------------
  /// Sample the refractive color of the material.
  color sample_refractive_color(const vec2& uv) const {
    return m_refractive_color->sample(uv);
  }
  // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
  /// @copydoc cg::Material::sample_refractive_color(const vec2&) const
  color sample_refractive_color(const double u, const double v) const {
    return m_refractive_color->sample(u, v);
  }
  //----------------------------------------------------------------------------
  const auto& albedo_color_source() const { return *m_albedo_color; }
  void        set_albedo_color_source(const ColorSource& albedo_color_source) {
    m_albedo_color = albedo_color_source.clone();
  }
  //----------------------------------------------------------------------------
  const auto& reflective_color_source() const { return *m_reflective_color; }
  void set_reflective_color_source(const ColorSource& reflective_color_source) {
    m_reflective_color = reflective_color_source.clone();
  }
  //----------------------------------------------------------------------------
  const auto& refractive_color_source() const { return *m_refractive_color; }
  void set_refractive_color_source(const ColorSource& refractive_color_source) {
    m_refractive_color = refractive_color_source.clone();
  }
};
//==============================================================================
}  // namespace cg
//==============================================================================
#endif
