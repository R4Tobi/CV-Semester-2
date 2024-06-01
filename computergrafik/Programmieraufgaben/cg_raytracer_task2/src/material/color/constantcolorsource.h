#ifndef CG_CONSTANT_COLOR_SOURCE_H
#define CG_CONSTANT_COLOR_SOURCE_H
//==============================================================================
#include "vec.h"
#include "colorsource.h"
//==============================================================================
namespace cg {
//==============================================================================
class ConstantColorSource : public ColorSource {
  vec3 m_color;
 public:
  ConstantColorSource(double r, double g, double b) : m_color{r, g, b} {}
  ConstantColorSource(const vec3& c) : m_color{c} {}
  ConstantColorSource(vec3&& c) : m_color{std::move(c)} {}
  //----------------------------------------------------------------------------
  vec3 sample(double /*u*/, double /*v*/) const override { return m_color; }
  //----------------------------------------------------------------------------
  auto&       color() { return m_color; }
  const auto& color() const { return m_color; }
  //----------------------------------------------------------------------------
  void        set_color(const vec3& color) { m_color = color; }
  //---------------------------------------------------------------------------
  make_clonable(ColorSource, ConstantColorSource);
};
//==============================================================================
}  // namespace cg
//==============================================================================
#endif
