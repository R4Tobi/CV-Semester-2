#ifndef CG_AABB_H
#define CG_AABB_H
//==============================================================================
#include <ostream>

#include "intersectable.h"
#include "vec.h"
//==============================================================================
namespace cg {
//==============================================================================
/// Axis-aligned bounding box
class AABB : public Intersectable {
 private:
  vec3 m_min, m_max;
  //============================================================================
 public:
  constexpr AABB()                = default;
  constexpr AABB(const AABB&)     = default;
  constexpr AABB(AABB&&) noexcept = default;
  constexpr AABB& operator=(const AABB&) = default;
  constexpr AABB& operator=(AABB&&) noexcept = default;
  //----------------------------------------------------------------------------
  AABB(vec3&& min, vec3&& max) noexcept;
  AABB(vec3&& min, const vec3& max);
  AABB(const vec3& min, vec3&& max);
  AABB(const vec3& min, const vec3& max);
  //============================================================================
  const vec3& min() const;
  vec3&       min();
  double      min(size_t i) const;
  double&     min(size_t i);
  void        set_min(const vec3& min);
  void        set_min(vec3&& min);
  void        set_min(size_t i, double c);
  //----------------------------------------------------------------------------
  const vec3& max() const;
  vec3&       max();
  double      max(size_t i) const;
  double&     max(size_t i);
  void        set_max(const vec3& max);
  void        set_max(vec3&& max);
  void        set_max(size_t i, double c);
  //----------------------------------------------------------------------------
  void operator+=(const vec3& point);
  //----------------------------------------------------------------------------
  void reset();
  //----------------------------------------------------------------------------
  vec3 center() const;
  double center(size_t i) const;
  //----------------------------------------------------------------------------
  vec3 extents() const;
  //----------------------------------------------------------------------------
  bool is_inside(const vec3& p) const;
  //----------------------------------------------------------------------------
  std::optional<Intersection> check_intersection(
      const Ray& r, double min_t = 0) const override;
  //----------------------------------------------------------------------------
  /// from here:
  /// https://gdbooks.gitbooks.io/3dcollisions/content/Chapter4/aabb-triangle.html
  auto is_triangle_inside(vec3 x0, vec3 x1, vec3 x2) const -> bool;
};
//==============================================================================
// ostream output
std::ostream& operator<<(std::ostream& out, const AABB& bb);
//==============================================================================
}  // namespace cg
//==============================================================================
#endif
