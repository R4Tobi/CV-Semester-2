#include "aabb.h"
#include "utility.h"

#include <limits>
//==============================================================================
namespace cg {
//==============================================================================
AABB::AABB(vec3&& _min, vec3&& _max) noexcept
    : m_min{std::move(_min)}, m_max{std::move(_max)} {}
//----------------------------------------------------------------------------
AABB::AABB(vec3&& _min, const vec3& _max)
    : m_min{std::move(_min)}, m_max{_max} {}
//----------------------------------------------------------------------------
AABB::AABB(const vec3& _min, vec3&& _max)
    : m_min{_min}, m_max{std::move(_max)} {}
//----------------------------------------------------------------------------
AABB::AABB(const vec3& _min, const vec3& _max) : m_min{_min}, m_max{_max} {}
//============================================================================
const vec3& AABB::min() const {
  return m_min;
}
vec3& AABB::min() {
  return m_min;
}
double AABB::min(size_t i) const {
  return m_min(i);
}
double& AABB::min(size_t i) {
  return m_min(i);
}
void AABB::set_min(const vec3& min) {
  m_min = min;
}
void AABB::set_min(vec3&& min) {
  m_min = std::move(min);
}
void AABB::set_min(size_t i, double c) {
  m_min(i) = c;
}
//----------------------------------------------------------------------------
const vec3& AABB::max() const {
  return m_max;
}
vec3& AABB::max() {
  return m_max;
}
double AABB::max(size_t i) const {
  return m_max(i);
}
double& AABB::max(size_t i) {
  return m_max(i);
}
void AABB::set_max(const vec3& max) {
  m_max = max;
}
void AABB::set_max(vec3&& max) {
  m_max = std::move(max);
}
void AABB::set_max(size_t i, double c) {
  m_max(i) = c;
}
//----------------------------------------------------------------------------
void AABB::operator+=(const vec3& point) {
  for (size_t i = 0; i < 3; ++i) {
    m_min(i) = std::min(m_min(i), point(i));
    m_max(i) = std::max(m_max(i), point(i));
  }
}
//----------------------------------------------------------------------------
void AABB::reset() {
  for (size_t i = 0; i < 3; ++i) {
    m_min(i) = std::numeric_limits<double>::max();
    m_max(i) = -std::numeric_limits<double>::max();
  }
}
//----------------------------------------------------------------------------
vec3 AABB::center() const {
  return (m_max + m_min) * double(0.5);
}
//----------------------------------------------------------------------------
double AABB::center(size_t i) const {
  return (m_max(i) + m_min(i)) * double(0.5);
}
//----------------------------------------------------------------------------
vec3 AABB::extents() const {
  return m_max - m_min;
}
//----------------------------------------------------------------------------
bool AABB::is_inside(const vec3& p) const {
  for (size_t i = 0; i < 3; ++i) {
    if (p(i) < m_min(i) || m_max(i) < p(i)) { return false; }
  }
  return true;
}
//----------------------------------------------------------------------------
std::optional<Intersection> AABB::check_intersection(const Ray& ray,
                                                     double     /*min_t*/) const {
  enum Quadrant { right, left, middle };
  vec3                    coord;
  bool                    inside = true;
  std::array<Quadrant, 3> quadrant;
  size_t                  which_plane;
  std::array<double, 3>   max_t;
  std::array<double, 3>   candidate_plane;

  // Find candidate planes; this loop can be avoided if rays cast all from the
  // eye(assume perpsective view)
  for (size_t i = 0; i < 3; i++) {
    if (ray.origin(i) < min(i)) {
      quadrant[i]        = left;
      candidate_plane[i] = min(i);
      inside             = false;
    } else if (ray.origin(i) > max(i)) {
      quadrant[i]        = right;
      candidate_plane[i] = max(i);
      inside             = false;
    } else {
      quadrant[i] = middle;
    }
  }

  // Ray origin inside bounding box
  if (inside) {
    return Intersection{this,         ray,           0,
                        ray.origin(), vec3::zeros(), vec2::zeros()};
  }

  // Calculate T distances to candidate planes
  for (size_t i = 0; i < 3; i++)
    if (quadrant[i] != middle && ray.direction(i) != 0.) {
      max_t[i] = (candidate_plane[i] - ray.origin(i)) / ray.direction(i);
    } else {
      max_t[i] = -1.;
    }

  // Get largest of the max_t's for final choice of intersection
  which_plane = 0;
  for (size_t i = 1; i < 3; i++)
    if (max_t[which_plane] < max_t[i]) { which_plane = i; }

  /* Check final candidate actually inside box */
  if (max_t[which_plane] < 0.) { return {}; }
  for (size_t i = 0; i < 3; i++)
    if (which_plane != i) {
      coord(i) = ray.origin(i) + max_t[which_plane] * ray.direction(i);
      if (coord(i) < min(i) || coord(i) > max(i)) { return {}; }
    } else {
      coord(i) = candidate_plane[i];
    }
  return Intersection{this,  ray,           max_t[which_plane],
                      coord, vec3::zeros(), vec2::zeros()};
}
//------------------------------------------------------------------------------
auto AABB::is_triangle_inside(vec3 x0, vec3 x1, vec3 x2) const -> bool {
  auto const c = center();
  auto const e = extents() / 2;

  x0 -= c;
  x1 -= c;
  x2 -= c;

  auto const f0 = x1 - x0;
  auto const f1 = x2 - x1;
  auto const f2 = x0 - x2;

  vec3 const u0{1, 0, 0};
  vec3 const u1{0, 1, 0};
  vec3 const u2{0, 0, 1};

  auto is_separating_axis = [&](auto const& axis) {
    auto const p0 = dot(x0, axis);
    auto const p1 = dot(x1, axis);
    auto const p2 = dot(x2, axis);
    auto r = e(0) * std::abs(dot(u0, axis)) +
             e(1) * std::abs(dot(u1, axis)) +
             e(2) * std::abs(dot(u2, axis));
    return cg::max(-cg::max(p0, p1, p2), cg::min(p0, p1, p2)) > r;
  };

  if (is_separating_axis(cross(u0, f0))) { return false; }
  if (is_separating_axis(cross(u0, f1))) { return false; }
  if (is_separating_axis(cross(u0, f2))) { return false; }
  if (is_separating_axis(cross(u1, f0))) { return false; }
  if (is_separating_axis(cross(u1, f1))) { return false; }
  if (is_separating_axis(cross(u1, f2))) { return false; }
  if (is_separating_axis(cross(u2, f0))) { return false; }
  if (is_separating_axis(cross(u2, f1))) { return false; }
  if (is_separating_axis(cross(u2, f2))) { return false; }
  if (is_separating_axis(u0)) { return false; }
  if (is_separating_axis(u1)) { return false; }
  if (is_separating_axis(u2)) { return false; }
  if (is_separating_axis(cross(f0, f1))) { return false; }
  return true;
}
//==============================================================================
// ostream output
//==============================================================================
std::ostream& operator<<(std::ostream& out, const AABB& aabb) {
  out << std::scientific;
  for (size_t i = 0; i < 3; ++i) {
    out << "[ ";
    if (aabb.min(i) >= 0) { out << ' '; }
    out << aabb.min(i) << " .. ";
    if (aabb.max(i) >= 0) { out << ' '; }
    out << aabb.max(i) << " ]\n";
  }
  out << std::defaultfloat;
  return out;
}
//==============================================================================
}  // namespace cg
//==============================================================================
