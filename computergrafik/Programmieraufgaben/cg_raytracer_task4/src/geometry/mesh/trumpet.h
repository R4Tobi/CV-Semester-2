#ifndef CG_TRUMPET_H
#define CG_TRUMPET_H
//==============================================================================
#include "material.h"
#include "mesh.h"
//==============================================================================
namespace cg {
//==============================================================================
/// trumpet as renderable triangular mesh
class Trumpet : public Mesh {
 public:
  Trumpet(const Material& m);
  Trumpet(const Trumpet&) = default;
  Trumpet(Trumpet&&)      = default;
  //---------------------------------------------------------------------------
  make_clonable(Renderable, Trumpet);
};
//==============================================================================
}  // namespace cg
//==============================================================================
#endif
