#ifndef CG_BUNNY_H
#define CG_BUNNY_H
//==============================================================================
#include "material.h"
#include "mesh.h"
//==============================================================================
namespace cg {
//==============================================================================
/// stanford bunny as renderable triangular mesh
class Bunny : public Mesh {
 public:
  Bunny(const Material&);
  Bunny(const Bunny&)     = default;
  Bunny(Bunny&&) noexcept = default;
  Bunny& operator=(const Bunny&) = default;
  Bunny& operator=(Bunny&&) noexcept = default;

  make_clonable(Renderable, Bunny);
};
//==============================================================================
}  // namespace cg
//==============================================================================
#endif
