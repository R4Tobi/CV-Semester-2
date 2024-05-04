#ifndef CG_OCTREE_H
#define CG_OCTREE_H
//==============================================================================
#include <memory>
#include <array>
#include <set>

#include "aabb.h"
//==============================================================================
namespace cg {
//==============================================================================
class Mesh;
class Octree : public AABB {
 public:
  static constexpr size_t max_depth = 6;
  enum class dim_x : std::uint8_t { left = 0, right = 1 };
  enum class dim_y : std::uint8_t { bottom = 0, top = 2 };
  enum class dim_z : std::uint8_t { front = 0, back = 4 };
  friend class std::unique_ptr<Octree>;

 private:
  size_t                                 m_level;
  std::vector<size_t>                    m_vertex_handles;
  std::vector<size_t>                    m_triangle_handles;
  std::array<std::unique_ptr<Octree>, 8> m_children;
  //============================================================================
 public:
  Octree(Octree&&) noexcept = default;
  auto operator=(Octree&&) noexcept -> Octree& = default;
  virtual ~Octree()                            = default;
  //----------------------------------------------------------------------------
  Octree(vec3 const& min, vec3 const& max);
  // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 private:
  Octree(vec3 const& min, vec3 const& max, size_t const level);
  //============================================================================
 public:
  auto num_vertex_handles() const { return size(m_vertex_handles); }
  auto num_triangle_handles() const { return size(m_triangle_handles); }
  //----------------------------------------------------------------------------
  auto is_splitted() const { return m_children.front() != nullptr; }
  auto holds_vertices() const { return !m_vertex_handles.empty(); }
  auto holds_triangles() const { return !m_triangle_handles.empty(); }
  constexpr auto is_at_max_depth() const { return m_level == max_depth; }
  //----------------------------------------------------------------------------
  static constexpr auto index(dim_x const d0, dim_y const d1, dim_z const d2) {
    return static_cast<std::uint8_t>(d0) +
           static_cast<std::uint8_t>(d1) +
           static_cast<std::uint8_t>(d2);
  }
  //----------------------------------------------------------------------------
  static constexpr auto left_bottom_front_index() {
    return index(dim_x::left, dim_y::bottom, dim_z::front);
  }
  static constexpr auto right_bottom_front_index() {
    return index(dim_x::right, dim_y::bottom, dim_z::front);
  }
  static constexpr auto left_top_front_index() {
    return index(dim_x::left, dim_y::top, dim_z::front);
  }
  static constexpr auto right_top_front_index() {
    return index(dim_x::right, dim_y::top, dim_z::front);
  }
  static constexpr auto left_bottom_back_index() {
    return index(dim_x::left, dim_y::bottom, dim_z::back);
  }
  static constexpr auto right_bottom_back_index() {
    return index(dim_x::right, dim_y::bottom, dim_z::back);
  }
  static constexpr auto left_top_back_index() {
    return index(dim_x::left, dim_y::top, dim_z::back);
  }
  static constexpr auto right_top_back_index() {
    return index(dim_x::right, dim_y::top, dim_z::back);
  }
  //----------------------------------------------------------------------------
  auto left_bottom_front() const -> auto const& {
    return m_children[left_bottom_front_index()];
  }
  // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
  auto left_bottom_front() -> auto& {
    return m_children[left_bottom_front_index()];
  }
  //----------------------------------------------------------------------------
  auto right_bottom_front() const -> auto const& {
    return m_children[right_bottom_front_index()];
  }
  // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
  auto right_bottom_front() -> auto& {
    return m_children[right_bottom_front_index()];
  }
  //----------------------------------------------------------------------------
  auto left_top_front() const -> auto const& {
    return m_children[left_top_front_index()];
  }
  // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
  auto left_top_front() -> auto& {
    return m_children[left_top_front_index()];
  }
  //----------------------------------------------------------------------------
  auto right_top_front() const -> auto const& {
    return m_children[right_top_front_index()];
  }
  // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
  auto right_top_front() -> auto& {
    return m_children[right_top_front_index()];
  }
  //----------------------------------------------------------------------------
  auto left_bottom_back() const -> auto const& {
    return m_children[left_bottom_back_index()];
  }
  // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
  auto left_bottom_back() -> auto& {
    return m_children[left_bottom_back_index()];
  }
  //----------------------------------------------------------------------------
  auto right_bottom_back() const -> auto const& {
    return m_children[right_bottom_back_index()];
  }
  // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
  auto right_bottom_back() -> auto& {
    return m_children[right_bottom_back_index()];
  }
  //----------------------------------------------------------------------------
  auto left_top_back() const -> auto const& {
    return m_children[left_top_back_index()];
  }
  // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
  auto left_top_back() -> auto& {
    return m_children[left_top_back_index()];
  }
  //----------------------------------------------------------------------------
  auto right_top_back() const -> auto const& {
    return m_children[right_top_back_index()];
  }
  // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
  auto right_top_back() -> auto& {
    return m_children[right_top_back_index()];
  }

 private:
  //----------------------------------------------------------------------------
  auto create_left_bottom_front() -> void;
  auto create_right_bottom_front() -> void;
  auto create_left_top_front() -> void;
  auto create_right_top_front() -> void;
  auto create_left_bottom_back() -> void;
  auto create_right_bottom_back() -> void;
  auto create_left_top_back() -> void;
  auto create_right_top_back() -> void;
  auto create_children() -> void;
  //------------------------------------------------------------------------------
 public:
  auto insert_vertex(Mesh const& mesh, size_t const vertex_idx) -> bool;
  auto insert_triangle(Mesh const& mesh, size_t const triangle_idx) -> bool;
  //----------------------------------------------------------------------------
  auto split_and_distribute(Mesh const& mesh) -> void;
  //----------------------------------------------------------------------------
  auto distribute_vertex(Mesh const& mesh, size_t const vertex_idx) -> void;
  auto distribute_triangle(Mesh const& mesh, size_t const triangle_idx) -> void;
  //============================================================================
  auto collect_possible_intersections(
      Ray const& r, std::set<size_t>& possible_collisions) const -> void;
  //----------------------------------------------------------------------------
  auto collect_possible_intersections(Ray const& r) const -> std::set<size_t>;
};
//==============================================================================
}  // namespace cg
//==============================================================================
#endif
