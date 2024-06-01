#include "scene.h"

#include "light.h"
#include "renderable.h"
#include "mesh.h"
//==============================================================================
namespace cg {
//==============================================================================
Scene::Scene(const Scene& other) : m_background_color{other.m_background_color} {
  for (const auto& renderable : other.m_renderables) {
    m_renderables.push_back(renderable->clone());
  }
  for (const auto& light_source : other.m_light_sources) {
    m_light_sources.push_back(light_source->clone());
  }
}
//------------------------------------------------------------------------------
Scene& Scene::operator=(const Scene& other) {
  for (const auto& renderable : other.m_renderables) {
    m_renderables.push_back(renderable->clone());
  }
  for (const auto& light_source : other.m_light_sources) {
    m_light_sources.push_back(light_source->clone());
  }
  m_background_color = other.m_background_color;
  return *this;
}
//==============================================================================
void Scene::insert(const Renderable& r) {
  m_renderables.push_back(r.clone());
}
//------------------------------------------------------------------------------
void Scene::insert(const AssembledRenderable& ar) {
  for (const auto& r : ar.renderables()) {
    m_renderables.push_back(r->clone());
  }
}
//------------------------------------------------------------------------------
void Scene::insert(AssembledRenderable&& ar) {
  for (auto& r : ar.renderables()) { m_renderables.push_back(std::move(r)); }
}
//------------------------------------------------------------------------------
void Scene::insert(const Light& l) {
  m_light_sources.push_back(l.clone());
}
//------------------------------------------------------------------------------
bool Scene::any_intersection(const Ray& r, double min_t, double max_t) const {

  for (const auto& obj : m_renderables) {
    auto hit = obj->check_intersection(r, min_t);
    if (hit && hit->t >= min_t && hit->t <= max_t) { return true; }
  }
  return false;
  }
//------------------------------------------------------------------------------
std::optional<Intersection> Scene::closest_intersection(const Ray& r,
                                                        double min_t) const {

  std::optional<Intersection> closest_hit;

  for (const auto& obj : m_renderables) {
    auto hit = obj->check_intersection(r, min_t);
    if (hit) {
      if (!closest_hit || (hit->t > min_t && hit->t < closest_hit->t)) {
        closest_hit = hit;
      }
    }
  }
  return closest_hit;
  }
//------------------------------------------------------------------------------
/// assembles shading, reflection and refraction
vec3 Scene::shade_closest_intersection(const Ray& incident_ray,
                                       double     min_t) const {
  const auto hit = closest_intersection(incident_ray, min_t);
  if (hit) {
    auto renderable   = dynamic_cast<const Renderable*>(hit->intersectable);
    auto shaded_color = vec3::zeros();
    // first apply the lighting model for each light source
    for (const auto& light_source : lights()) {
      if (!in_shadow(*light_source, hit->position + 1e-6*hit->normal)) {
          shaded_color += 
            renderable->shade(*light_source, *hit) * 
            (1 - renderable->reflectance() - renderable->refractance());
            // only a part of the total incoming light is used for shading the
            // actual object. The rest is reflected or refracted. The fraction
            // of light that is transported away is given by the reflectance 
            // and refractance constants of the material.
      }
    }

    /*
    ============================================================================
     Task 3
    ============================================================================
     Implement the calculation of the color that is showing in the reflection on
     this object.
     Remember that ray tracing is recursive.
    */
    if (renderable->reflectance() > 0 &&
        incident_ray.num_reflections() < max_num_reflections) {
      vec3 reflected_color = vec3::zeros();

      shaded_color +=
        reflected_color *
        renderable->sample_reflective_color(hit->uv) *
        renderable->reflectance();
    }
    
    /*
    ============================================================================
     Task 3
    ============================================================================
     Implement refraction analogously to reflection above.
    */
    if (renderable->refractance() > 0 &&
        incident_ray.num_reflections() < max_num_reflections) {
      vec3 refracted_color = vec3::zeros();

      shaded_color +=
        refracted_color *
        renderable->sample_refractive_color(hit->uv) *
        renderable->refractance();
    }
        return shaded_color;
  }
  return m_background_color;
}
//------------------------------------------------------------------------------
bool Scene::in_shadow(const Light& light_source, const vec3& position) const {

  // **Remark**:
  // One should actually test for refracted rays differently. 
  // In our implementation transparent objects cast shadows.
  return any_intersection(
      Ray{position, -light_source.light_direction_to(position)}, 1e-7,
      light_source.distance_to(position));
  }


//==============================================================================
}  // namespace cg
//==============================================================================
