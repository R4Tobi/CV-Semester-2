#ifndef CG_MESH_H
#define CG_MESH_H
//==============================================================================
#include <array>
#include <optional>
#include <mutex>
#include <vector>

#include "ray.h"
#include "octree.h"
#include "renderable.h"
#include "vec.h"
//==============================================================================
namespace cg {
//==============================================================================
class Mesh : public Renderable {
 public:
  //============================================================================
  make_clonable(Renderable, Mesh);

 private:
  //============================================================================
  std::vector<vec3>                         m_vertices;
  std::vector<vec3>                         m_normals;
  std::vector<vec2>                         m_uv_coordinates;
  std::vector<std::array<unsigned long, 3>> m_triangles;
  mutable std::unique_ptr<Octree>           m_hierarchy;
  mutable std::mutex                        m_hierarchy_mutex;
  //============================================================================
 public:
  Mesh(const Mesh& other)
      : Renderable{other},
        m_vertices(other.m_vertices),
        m_normals(other.m_normals),
        m_uv_coordinates(other.m_uv_coordinates),
        m_triangles{other.m_triangles},
        m_hierarchy{nullptr} {}
  Mesh(Mesh&& other) noexcept
      : Renderable{std::move(other)},
        m_vertices(std::move(other.m_vertices)),
        m_normals(std::move(other.m_normals)),
        m_uv_coordinates(std::move(other.m_uv_coordinates)),
        m_triangles{std::move(other.m_triangles)},
        m_hierarchy{std::move(other.m_hierarchy)} {}
  Mesh& operator=(const Mesh& other) {
    Renderable::operator=(other);
    m_vertices          = other.m_vertices;
    m_normals           = other.m_normals;
    m_uv_coordinates    = other.m_uv_coordinates;
    m_triangles         = other.m_triangles;
    m_hierarchy         = nullptr;
    return *this;
  }
  Mesh& operator=(Mesh&& other) {
    Renderable::operator=(std::move(other));
    m_vertices          = std::move(other.m_vertices);
    m_normals           = std::move(other.m_normals);
    m_uv_coordinates    = std::move(other.m_uv_coordinates);
    m_triangles         = std::move(other.m_triangles);
    m_hierarchy         = std::move(other.m_hierarchy);
    return *this;
  }
  Mesh(const Material& m);
  Mesh(const std::string& filepath, const Material& m);
  //----------------------------------------------------------------------------
  virtual ~Mesh() = default;
  //----------------------------------------------------------------------------
  auto&       vertices() { return m_vertices; }
  const auto& vertices() const { return m_vertices; }
  auto        num_vertices() const { return size(m_vertices); }
  size_t      insert_vertex(double c0, double c1, double c2) {
    m_vertices.push_back(vec3{c0, c1, c2});
    return m_vertices.size() - 1;
  }
  size_t      insert_vertex(const vec3& v) {
    m_vertices.push_back(v);
    return m_vertices.size() - 1;
  }
  size_t insert_vertex(vec3&& v) {
    m_vertices.push_back(std::move(v));
    return m_vertices.size() - 1;
  }
  //----------------------------------------------------------------------------
  auto&       normals() { return m_normals; }
  const auto& normals() const { return m_normals; }
  void        insert_normal(double c0, double c1, double c2) {
    m_normals.push_back(vec3{c0, c1, c2});
  }
  void        insert_normal(const vec3& n) { m_normals.push_back(n); }
  void        insert_normal(vec3&& n) { m_normals.push_back(std::move(n)); }
  //----------------------------------------------------------------------------
  auto&       uv_coordinates() { return m_uv_coordinates; }
  const auto& uv_coordinates() const { return m_uv_coordinates; }
  void        insert_uv_coordinate(double c0, double c1) {
    m_uv_coordinates.push_back(vec2{c0, c1});
  }
  void insert_uv_coordinate(const vec2& uv) { m_uv_coordinates.push_back(uv); }
  void insert_uv_coordinate(vec2&& uv) {
    m_uv_coordinates.push_back(std::move(uv));
  }
  //----------------------------------------------------------------------------
  auto&       triangles() { return m_triangles; }
  const auto& triangles() const { return m_triangles; }
  void insert_triangle(unsigned long v0, unsigned long v1, unsigned long v2) {
    m_triangles.push_back(std::array<unsigned long, 3>{v0, v1, v2});
  }
  auto num_triangles() const { return size(m_triangles); }
  //----------------------------------------------------------------------------
  void calc_normals();
  //----------------------------------------------------------------------------
  std::optional<Intersection> check_intersection(
      const Ray& r, double min_t = 0) const override;
  //------------------------------------------------------------------------------
  /// ray - triangle intersection test
  /// \param r Ray for intersection
  /// \param v0 triangles's first vertex
  /// \param v1 triangles's second vertex
  /// \param v2 triangles's third vertex
  /// \return barycentric coordinates if there is an intersection
  std::optional<std::pair<double, vec3>> check_intersection(
      const Ray& r, const vec3& v0, const vec3& v1, const vec3& v2) const;
  //------------------------------------------------------------------------------
  /// reads mesh from obj file format
  void read_obj(const std::string& filepath);
  //----------------------------------------------------------------------------
  Movable& translate(const vec3& xyz) override {
    Renderable::translate(xyz);
    update_hierarchy();
    return *this;
  }
  Movable& translate(double x, double y, double z) override {
    Renderable::translate(x, y, z);
    update_hierarchy();
    return *this;
  }
  Movable& scale(const vec3& xyz) override {
    Renderable::scale(xyz);
    update_hierarchy();
    return *this;
  }
  Movable& scale(double x, double y, double z) override {
    Renderable::scale(x, y, z);
    update_hierarchy();
    return *this;
  }
  Movable& scale(double factor) override {
    Renderable::scale(factor);
    update_hierarchy();
    return *this;
  }
  Movable& rotate_x(double alpha) override {
    Renderable::rotate_x(alpha);
    update_hierarchy();
    return *this;
  }
  Movable& rotate_y(double alpha) override {
    Renderable::rotate_y(alpha);
    update_hierarchy();
    return *this;
  }
  Movable& rotate_z(double alpha) override {
    Renderable::rotate_z(alpha);
    update_hierarchy();
    return *this;
  }
  Movable& rotate(const vec3& xyz, double alpha) override {
    Renderable::rotate(xyz, alpha);
    update_hierarchy();
    return *this;
  }
  Movable& rotate(double x, double y, double z, double alpha) override {
    Renderable::rotate(x, y, z, alpha);
    update_hierarchy();
    return *this;
  }
  //----------------------------------------------------------------------------
  void set_parent(const Movable& m) {
    Renderable::set_parent(m);
    update_hierarchy();
  }
  void unset_parent() {
    Renderable::unset_parent();
    update_hierarchy();
  }
  //----------------------------------------------------------------------------
  const auto& hierarchy() const { return m_hierarchy; }
  //----------------------------------------------------------------------------
  void update_hierarchy() const {
    std::lock_guard lock{m_hierarchy_mutex};
    m_hierarchy.reset();
    auto min_pos = vec3::ones() * std::numeric_limits<double>::infinity();
    auto max_pos = -vec3::ones() * std::numeric_limits<double>::infinity();
    for (auto const& v : vertices()) {
      for (size_t i = 0; i < 3; ++i) {
        auto const vt = transform_point(v);
        min_pos(i)    = std::min(min_pos(i), vt(i));
        max_pos(i)    = std::max(max_pos(i), vt(i));
      }
    }
    m_hierarchy = std::make_unique<Octree>(min_pos, max_pos);
    for (size_t i = 0; i < num_vertices(); ++i) {
      m_hierarchy->insert_vertex(*this, i);
    }
    for (size_t i = 0; i < num_triangles(); ++i) {
      m_hierarchy->insert_triangle(*this, i);
    }
  }
};
//==============================================================================
}  // namespace cg
//==============================================================================
#endif
