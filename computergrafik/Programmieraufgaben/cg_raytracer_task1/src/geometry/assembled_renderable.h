#ifndef CG_ASSEMBLED_RENDERABLE_H
#define CG_ASSEMBLED_RENDERABLE_H
//==============================================================================
#include <memory>
#include <vector>

#include "renderable.h"
//==============================================================================
namespace cg {
//==============================================================================
struct AssembledRenderable {
 private:
  std::vector<std::unique_ptr<Renderable>> m_renderables;

 public:
  AssembledRenderable()                               = default;
  AssembledRenderable(const AssembledRenderable& other) {
    for (const auto& r : other.m_renderables) {
      m_renderables.push_back(r->clone());
    }
  }
  //----------------------------------------------------------------------------
  AssembledRenderable(AssembledRenderable&&) noexcept = default;
  //============================================================================
  AssembledRenderable& operator=(const AssembledRenderable& other) {
    m_renderables.clear();
    for (const auto& r : other.m_renderables) {
      m_renderables.push_back(r->clone());
    }
    return *this;
  }
  //----------------------------------------------------------------------------
  AssembledRenderable& operator=(AssembledRenderable&&) noexcept = default;
  //============================================================================
  void add_renderable(const Renderable& renderable) {
    m_renderables.push_back(renderable.clone());
  }
  //----------------------------------------------------------------------------
  /// This makes it possible to move renderables
  template <typename T>
  void add_renderable(T&& renderable) {
    m_renderables.push_back(
        std::unique_ptr<Renderable>{new T{std::forward<T>(renderable)}});
  }
  //============================================================================
  const auto& renderables() const { return m_renderables; }
  auto&       renderables() { return m_renderables; }
  //============================================================================
  void translate(const vec3& xyz) {
    for (auto& r : m_renderables) { r->translate(xyz); }
  }
  void translate(double x, double y, double z) {
    for (auto& r : m_renderables) { r->translate(x, y, z); }
  }
  void scale(const vec3& xyz) {
    for (auto& r : m_renderables) { r->scale(xyz); }
  }
  void scale(double x, double y, double z) {
    for (auto& r : m_renderables) { r->scale(x, y, z); }
  }
  void scale(double factor) {
    for (auto& r : m_renderables) { r->scale(factor); }
  }
  void rotate_x(double alpha) {
    for (auto& r : m_renderables) { r->rotate_x(alpha); }
  }
  void rotate_y(double alpha) {
    for (auto& r : m_renderables) { r->rotate_y(alpha); }
  }
  void rotate_z(double alpha) {
    for (auto& r : m_renderables) { r->rotate_z(alpha); }
  }
  void rotate(const vec3& xyz, double alpha) {
    for (auto& r : m_renderables) { r->rotate(xyz, alpha); }
  }
  void rotate(double x, double y, double z, double alpha) {
    for (auto& r : m_renderables) { r->rotate(x, y, z, alpha); }
  }
};
//==============================================================================
}  // namespace cg
//==============================================================================
#endif
