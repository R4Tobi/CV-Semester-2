#include "octree.h"

#include "mesh.h"
//==============================================================================
namespace cg {
//==============================================================================
Octree::Octree(vec3 const& min, vec3 const& max)
    : AABB{min, max}, m_level{0} {}
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
Octree::Octree(vec3 const& min, vec3 const& max, size_t const level)
    : AABB{min, max}, m_level{level} {}
//----------------------------------------------------------------------------
auto Octree::create_left_bottom_front() -> void {
  left_bottom_front() = std::unique_ptr<Octree>(
      new Octree{vec3{min(0), min(1), min(2)},
                 vec3{center(0), center(1), center(2)}, m_level + 1});
}
//----------------------------------------------------------------------------
auto Octree::create_right_bottom_front() -> void {
  right_bottom_front() = std::unique_ptr<Octree>(
      new Octree{vec3{center(0), min(1), min(2)},
                 vec3{max(0), center(1), center(2)}, m_level + 1});
}
//----------------------------------------------------------------------------
auto Octree::create_left_top_front() -> void {
  left_top_front() = std::unique_ptr<Octree>(
      new Octree{vec3{min(0), center(1), min(2)},
                 vec3{center(0), max(1), center(2)}, m_level + 1});
}
//----------------------------------------------------------------------------
auto Octree::create_right_top_front() -> void {
  right_top_front() = std::unique_ptr<Octree>(
      new Octree{vec3{center(0), center(1), min(2)},
                 vec3{max(0), max(1), center(2)}, m_level + 1});
}
//----------------------------------------------------------------------------
auto Octree::create_left_bottom_back() -> void {
  left_bottom_back() = std::unique_ptr<Octree>(
      new Octree{vec3{min(0), min(1), center(2)},
                 vec3{center(0), center(1), max(2)}, m_level + 1});
}
//----------------------------------------------------------------------------
auto Octree::create_right_bottom_back() -> void {
  right_bottom_back() = std::unique_ptr<Octree>(
      new Octree{vec3{center(0), min(1), center(2)},
                 vec3{max(0), center(1), max(2)}, m_level + 1});
}
//----------------------------------------------------------------------------
auto Octree::create_left_top_back() -> void {
  left_top_back() = std::unique_ptr<Octree>(
      new Octree{vec3{min(0), center(1), center(2)},
                 vec3{center(0), max(1), max(2)}, m_level + 1});
}
//----------------------------------------------------------------------------
auto Octree::create_right_top_back() -> void {
  right_top_back() = std::unique_ptr<Octree>(
      new Octree{vec3{center(0), center(1), center(2)},
                 vec3{max(0), max(1), max(2)}, m_level + 1});
}
//----------------------------------------------------------------------------
auto Octree::create_children() -> void {
  create_left_bottom_front();
  create_right_bottom_front();
  create_left_top_front();
  create_right_top_front();
  create_left_bottom_back();
  create_right_bottom_back();
  create_left_top_back();
  create_right_top_back();
}
//------------------------------------------------------------------------------
auto Octree::insert_vertex(Mesh const& mesh, size_t const vertex_idx) -> bool {
  auto const v = mesh.transform_point(mesh.vertices()[vertex_idx]);
  if (!is_inside(v)) {
    return false; }
  if (holds_vertices()) {
    if (is_at_max_depth()) {
      m_vertex_handles.push_back(vertex_idx);
    } else {
      split_and_distribute(mesh);
      distribute_vertex(mesh, vertex_idx);
    }
  } else {
    if (is_splitted()) {
      distribute_vertex(mesh, vertex_idx);
    } else {
      m_vertex_handles.push_back(vertex_idx);
    }
  }
  return true;
}
//------------------------------------------------------------------------------
auto Octree::insert_triangle(Mesh const& mesh, size_t const triangle_idx)
    -> bool {
  auto const& [vi0, vi1, vi2] = mesh.triangles()[triangle_idx];
  auto const v0               = mesh.transform_point(mesh.vertices()[vi0]);
  auto const v1               = mesh.transform_point(mesh.vertices()[vi1]);
  auto const v2               = mesh.transform_point(mesh.vertices()[vi2]);
  if (!is_triangle_inside(v0, v1, v2)) { return false; }
  if (holds_triangles()) {
    if (is_at_max_depth()) {
      m_triangle_handles.push_back(triangle_idx);
    } else {
      split_and_distribute(mesh);
      distribute_triangle(mesh, triangle_idx);
    }
  } else {
    if (is_splitted()) {
      distribute_triangle(mesh, triangle_idx);
    } else {
      m_triangle_handles.push_back(triangle_idx);
    }
  }
  return true;
}
//----------------------------------------------------------------------------
auto Octree::split_and_distribute(Mesh const& mesh) -> void {
  create_children();
  if (!m_vertex_handles.empty()) {
    distribute_vertex(mesh, m_vertex_handles.front());
    m_vertex_handles.clear();
  }
  if (!m_triangle_handles.empty()) {
    distribute_triangle(mesh, m_triangle_handles.front());
    m_triangle_handles.clear();
  }
}
//----------------------------------------------------------------------------
auto Octree::distribute_vertex(Mesh const& mesh, size_t const vertex_idx) -> void {
  for (auto& child : m_children) { child->insert_vertex(mesh, vertex_idx); }
}
//----------------------------------------------------------------------------
auto Octree::distribute_triangle(Mesh const& mesh, size_t const triangle_idx) -> void {
  for (auto& child : m_children) { child->insert_triangle(mesh, triangle_idx); }
}
//============================================================================
auto Octree::collect_possible_intersections(Ray const& r,
                                    std::set<size_t>& possible_collisions) const
    -> void {
  if (AABB::check_intersection(r)) {
    if (is_splitted()) {
      for (auto const& child : m_children) {
        child->collect_possible_intersections(r, possible_collisions);
      }
    } else {
      std::copy(begin(m_triangle_handles), end(m_triangle_handles),
                std::inserter(possible_collisions, end(possible_collisions)));
    }
  }
}
//----------------------------------------------------------------------------
auto Octree::collect_possible_intersections(Ray const& r) const
    -> std::set<size_t> {
  std::set<size_t> possible_collisions;
  collect_possible_intersections(r, possible_collisions);
  return possible_collisions;
}
//==============================================================================
}  // namespace cg
//==============================================================================
