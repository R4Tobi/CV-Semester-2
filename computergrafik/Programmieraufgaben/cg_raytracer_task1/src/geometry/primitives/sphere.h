#ifndef CG_SPHERE_H
#define CG_SPHERE_H
//==============================================================================
#include "renderable.h"
#include "vec.h"
//==============================================================================
namespace cg {
//==============================================================================
/// Implicit function of a sphere.
class Sphere : public Renderable {
  //============================================================================

 public:
  //============================================================================
  Sphere(const Material& m);
  //---------------------------------------------------------------------------
  /// solves a quadratic polynomial with coeffincients a, b and c.
  std::optional<std::pair<double, double>> solve_quadratic(double a, double b,
                                                           double c) const;
  //---------------------------------------------------------------------------
  std::optional<Intersection> check_intersection(
      const Ray& r, double min_t = 0) const override;
  //---------------------------------------------------------------------------
  /// needed for polymorphic copy
  make_clonable(Renderable, Sphere);
};
//==============================================================================
}  // namespace cg
//==============================================================================
#endif
