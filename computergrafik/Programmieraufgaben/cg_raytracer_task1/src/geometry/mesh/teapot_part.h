#ifndef CG_TEAPOT_PART_H
#define CG_TEAPOT_PART_H
//==============================================================================
#include "bezier_patch_mesh.h"
//==============================================================================
namespace cg {
//==============================================================================
class TeapotPart : public BezierPatchMesh {
  //============================================================================
 public:
  make_clonable(Renderable, TeapotPart);

  //============================================================================
 private:
  static const std::vector<vec3> control_points;

  //============================================================================
 public:
  TeapotPart(const Material& material, size_t res_u, size_t res_v,
             size_t index);
  TeapotPart(const TeapotPart&)     = default;
  TeapotPart(TeapotPart&& other) noexcept = default;
  TeapotPart& operator=(const TeapotPart&) = default;
  TeapotPart& operator=(TeapotPart&&) noexcept = default;
};
//==============================================================================
}  // namespace cg
//==============================================================================
#endif
