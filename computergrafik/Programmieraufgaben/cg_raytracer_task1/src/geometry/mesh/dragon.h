#ifndef CG_DRAGON_H
#define CG_DRAGON_H
//==============================================================================
#include "material.h"
#include "mesh.h"
//==============================================================================
namespace cg {
//==============================================================================
/// stanford dragon as renderable triangular mesh
class Dragon : public Mesh {
 public:
  Dragon(const Material&);
  Dragon(const Dragon&) = default;
  Dragon(Dragon&&)      = default;
  //---------------------------------------------------------------------------
  make_clonable(Renderable, Dragon);
};
//==============================================================================
}  // namespace cg
//==============================================================================
#endif
