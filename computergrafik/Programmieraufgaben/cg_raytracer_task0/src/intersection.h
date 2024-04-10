#ifndef CG_INTERSECTION_H
#define CG_INTERSECTION_H
//==============================================================================
#include "ray.h"
#include "vec.h"
//==============================================================================
namespace cg {
//==============================================================================
// forward declare to avoid include cycles
class Intersectable;
//==============================================================================
/// Intersections are created when a \ref cg::Ray "ray" hits an \ref
/// cg::Intersectable "intersectable object".
struct Intersection {
  /// pointer to renderable object (the object that was intersected)
  const Intersectable* intersectable;
  /// incident ray (the ray that intersects the object)
  Ray incident_ray;
  /// position on ray
  double t;
  /// world position of intersection
  vec3 position;
  /// world normal of intersection point on the renderable
  vec3 normal;
  /// uv-coordinate on the renderable at this intersection point.
  vec2 uv;
};
//==============================================================================
}  // namespace cg
//==============================================================================
#endif
