#ifndef CG_QUADRIC_H
#define CG_QUADRIC_H
//==============================================================================
#include "renderable.h"
#include "vec.h"
#include "mat.h"
//==============================================================================
namespace cg {
//==============================================================================
class Quadric : public Renderable {
  mat4 A;
  //===========================================================================
 public:
  Quadric(const mat4& A, const Material& m);
  Quadric(const mat3& A, const vec3& b, double c, const Material& m);
   //---------------------------------------------------------------------------
 private:
   // delete virtual methods
   using Movable::scale;
   using Movable::rotate_x;
   using Movable::rotate_y;
   using Movable::rotate_z;
   using Movable::rotate;

 public:
  //---------------------------------------------------------------------------
  std::optional<std::pair<double, double>> solve_quadratic(const Ray& r) const;
  //---------------------------------------------------------------------------
  std::optional<Intersection> check_intersection(
      const Ray& r, double min_t = 0) const override;
  //---------------------------------------------------------------------------
  make_clonable(Renderable, Quadric);
};
//==============================================================================
}  // namespace cg
//==============================================================================
#endif
