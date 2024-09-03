#ifndef CG_RAYTRACER_H
#define CG_RAYTRACER_H
//==============================================================================
#include <fstream>
#include "camera.h"
#include "scene.h"
#include "texture.h"
//==============================================================================
/// Computer graphics
namespace cg {
//==============================================================================
/// Renders a scene viewed from a camera into an image
class Raytracer {
  Scene   m_scene;
  std::unique_ptr<Camera>  m_camera;
  Texture m_texture;

 public:
  Raytracer(const Scene& s, const Camera& camera);
  //----------------------------------------------------------------------------
  const Texture& render();
  const auto&    get_texture() const { return m_texture; }
};
//==============================================================================
}  // namespace cg
//==============================================================================
#endif
