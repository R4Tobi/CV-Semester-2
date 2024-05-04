#ifndef CG_TEAPOT_H
#define CG_TEAPOT_H
//==============================================================================
#include "assembled_renderable.h"
#include "teapot_part.h"
//==============================================================================
namespace cg {
//==============================================================================
class Teapot : public AssembledRenderable {
 public:
  static constexpr size_t num_parts = 32;
  Teapot(const Teapot&)     = default;
  Teapot(Teapot&&) noexcept = default;
  Teapot& operator=(const Teapot&) = default;
  Teapot& operator=(Teapot&&) noexcept = default;
  //------------------------------------------------------------------------------
  Teapot(const Material& material, size_t res_u, size_t res_v);
};
//==============================================================================
}  // namespace cg
//==============================================================================
#endif
