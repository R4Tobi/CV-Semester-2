#ifndef CG_BEZIER_WAVE
#define CG_BEZIER_WAVE
//==============================================================================
#include "bezier_patch_mesh.h"
//==============================================================================
namespace cg{
//==============================================================================
class BezierWave : public BezierPatchMesh {
  public:
   make_clonable(Renderable, BezierWave);
   BezierWave(const Material& material, size_t res_u, size_t res_v);
   BezierWave(const BezierWave& other)     = default;
   BezierWave(BezierWave&& other) noexcept = default;
   BezierWave& operator=(const BezierWave& other) = default;
   BezierWave& operator=(BezierWave&& other) noexcept = default;
};

class BezierWave2 : public BezierPatchMesh {
public:
  make_clonable(Renderable, BezierWave2);
  BezierWave2(const Material& material, size_t res_u, size_t res_v);
  BezierWave2(const BezierWave2& other) = default;
  BezierWave2(BezierWave2&& other) noexcept = default;
  BezierWave2& operator=(const BezierWave2& other) = default;
  BezierWave2& operator=(BezierWave2&& other) noexcept = default;
};
//==============================================================================
}  // namespace cg
//==============================================================================
#endif
