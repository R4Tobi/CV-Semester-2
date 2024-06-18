#include "constantcolormaterial.h"
#include "constantcolorsource.h"
//==============================================================================
namespace cg {
//==============================================================================
ConstantColorMaterial::ConstantColorMaterial(double r, double g, double b)
    : Material{ConstantColorSource{r, g, b}} {}
//------------------------------------------------------------------------------
ConstantColorMaterial::ConstantColorMaterial(color col)
    : Material{ConstantColorSource{col}} {}
//------------------------------------------------------------------------------
ConstantColorMaterial::ConstantColorMaterial(const color& col,
                                             const double reflectance,
                                             const color& reflective_color)
    : Material{ConstantColorSource{col}, reflectance,
               ConstantColorSource{reflective_color}} {}
//------------------------------------------------------------------------------
ConstantColorMaterial::ConstantColorMaterial(const ColorSource& colorsource)
    : Material{colorsource} {}
//------------------------------------------------------------------------------
vec3 ConstantColorMaterial::shade(const Light&        li,
                                  const Intersection& i) const {
  const auto& [renderable, incident_ray, t, pos, nor, uv] = i;
  const auto light_intensity = li.incident_radiance_at(pos);
  return sample_albedo_color(uv) * light_intensity;
}
//==============================================================================
}  // namespace cg
//==============================================================================
