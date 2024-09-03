#ifndef CG_COLOR_SOURCE_H
#define CG_COLOR_SOURCE_H
//==============================================================================
#include "vec.h"
#include "clonable.h"
//==============================================================================
namespace cg {
//==============================================================================
class ColorSource : public Clonable<ColorSource> {
 public:
  virtual ~ColorSource()                        = default;
  virtual vec3 sample(double u, double v) const = 0;
  auto         sample(const vec2& uv) const { return sample(uv(0), uv(1)); }
  auto         operator()(const vec2& uv) const { return sample(uv(0), uv(1)); }
  auto         operator()(double u, double v) const { return sample(u, v); }
};
//==============================================================================
}  // namespace cg
//==============================================================================
#endif
