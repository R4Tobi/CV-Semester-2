#ifndef CG_CONSTANT_COLOR_MATERIAL_H
#define CG_CONSTANT_COLOR_MATERIAL_H
//==============================================================================
#include "light.h"
#include "material.h"
#include "ray.h"
//==============================================================================
namespace cg {
//==============================================================================
struct ConstantColorMaterial : Material {
 public:
  //----------------------------------------------------------------------------
  ConstantColorMaterial(double r, double g, double b);
  ConstantColorMaterial(color col);
  ConstantColorMaterial(const color& col, const double reflectance,
                        const color& reflective_color);
  ConstantColorMaterial(const ColorSource& colorsource);
  ConstantColorMaterial(const ConstantColorMaterial&) = default;
  ConstantColorMaterial(ConstantColorMaterial&&)      = default;
  //----------------------------------------------------------------------------
  virtual vec3 shade(const Light& li, const Intersection& i) const override;
  //----------------------------------------------------------------------------
  std::unique_ptr<Material> clone() const override {
    return std::make_unique<ConstantColorMaterial>(*this);
  }
};
//==============================================================================
}  // namespace cg
//==============================================================================
#endif
