#include "raytracer.h"

#include "light.h"
#include "renderable.h"
//==============================================================================
namespace cg {
Raytracer::Raytracer(const Scene& scene, const Camera& camera)
    : m_scene{scene},
      m_camera{camera.clone()},
      m_texture{m_camera->plane_width(), m_camera->plane_height()} {}
//==============================================================================
const Texture& Raytracer::render() {
// this line lets the two for loops run parallel if openmp is installed
#ifdef NDEBUG
#pragma omp parallel for schedule(guided)
#endif
  for (int y = 0; y < (int)m_camera->plane_height(); ++y) {
    for (int x = 0; x < (int)m_camera->plane_width(); ++x) {
      m_texture.pixel(x, y) =
          m_scene.shade_closest_intersection(m_camera->ray(x, y));
    }
  }
  return m_texture;
}
//==============================================================================
}  // namespace cg
//==============================================================================
