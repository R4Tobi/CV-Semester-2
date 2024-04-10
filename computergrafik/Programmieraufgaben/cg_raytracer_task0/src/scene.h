#ifndef CG_SCENE_H
#define CG_SCENE_H
//==============================================================================
#include <memory>
#include <optional>
#include <tuple>
#include <vector>
#include "intersection.h"
#include "assembled_renderable.h"
#include "ray.h"
#include "vec.h"
#include "colors.h"
//==============================================================================
namespace cg {
//==============================================================================
// forward declarations to avoid include cycles
class Renderable;
class Light;
//==============================================================================
class Scene {
  //============================================================================
  static const size_t max_num_reflections = 5;
  //============================================================================
  std::vector<std::unique_ptr<Renderable>> m_renderables;
  std::vector<std::unique_ptr<Light>>      m_light_sources;
  color                                    m_background_color;

 public:
  //============================================================================
  Scene() : m_background_color{gray} {}
  Scene(const color& background_color) : m_background_color{background_color} {}
  //----------------------------------------------------------------------------
  Scene(const Scene& other);
  Scene(Scene&&)      = default;
  //----------------------------------------------------------------------------
  Scene& operator=(const Scene& other); 
  Scene& operator=(Scene&&) = default;
  //----------------------------------------------------------------------------
  ~Scene() = default;
  //============================================================================
  void insert(const Renderable& r);
  void insert(const AssembledRenderable& ar);
  void insert(AssembledRenderable&& ar);
  void insert(const Light& l);
  //----------------------------------------------------------------------------
  /// Determine if a ray hits any object in the scene.
  /// \param r The ray in question
  /// \param min_t Minimum offset from ray origin to be regarded as intersection.
  /// \param max_t Maximum offset from ray origin to be regarded as intersection.
  bool any_intersection(const Ray& r, double min_t, double max_t) const;
  /// Determine the closest intersection for a ray in the scene.
  /// \param r The ray in question
  /// \param min_t Minimum offset from ray origin to be regarded as intersection.
  std::optional<Intersection> closest_intersection(const Ray& r,
                                                   double     min_t = 0.01) const;
  //----------------------------------------------------------------------------
  /// \brief Shades the closest intersection point in the scene.
  ///
  /// \param r Ray that shall be traced.
  /// \param min_t Offset from ray origin to avoid self intersection of
  ///              reflected ray.
  vec3 shade_closest_intersection(const Ray& r, double min_t = 0) const;
  //----------------------------------------------------------------------------
  /// Get container of renderable object in the scene.
  const auto& renderables() const { return m_renderables; }
  //----------------------------------------------------------------------------
  /// Get container of light sources in the scene.
  const auto& lights() const { return m_light_sources; }
  //----------------------------------------------------------------------------
  /// Determine if a position in the scene is in shadow of a given light source.
  bool in_shadow(const Light& light_source, const vec3& position) const;
  //----------------------------------------------------------------------------
  const auto& background_color() { return m_background_color; }
  void        set_background_color(const color& background_color) {
    m_background_color = background_color;
  }
  void set_background_color(color&& background_color) {
    m_background_color = std::move(background_color);
  }
};

//==============================================================================
}  // namespace cg
//==============================================================================

#endif
