#ifndef CG_INTERSECTABLE_H
#define CG_INTERSECTABLE_H
//==============================================================================
#include <optional>

#include "intersection.h"
#include "ray.h"
//==============================================================================
namespace cg {
//==============================================================================
class Intersectable {
  public:
  //----------------------------------------------------------------------------
  /// \brief Interface method needed for checking if a ray intersects with the
  /// renderable object.
  /// \returns either nothing or an intersection.
  ///
  /// <a href="https://en.cppreference.com/w/cpp/utility/optional">std::optional
  /// at cppreference.com</a>
  virtual std::optional<Intersection> check_intersection(
      const Ray& r, double min_t = 0) const = 0;
};
//==============================================================================
}  // namespace cg
//==============================================================================
#endif
