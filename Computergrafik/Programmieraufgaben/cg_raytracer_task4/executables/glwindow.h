#ifndef CG_GLWINDOW_H
#define CG_GLWINDOW_H
#include <GLFW/glfw3.h>

#include <memory>
#include <thread>

#include "raytracer.h"
//==============================================================================
namespace cg {
//==============================================================================
class GLWindow {
  const Raytracer& m_raytracer;
  GLFWwindow* m_window;
  std::unique_ptr<std::thread> m_render_thread;
  //============================================================================
 public:
  GLWindow(const Raytracer& raytracer);
  //----------------------------------------------------------------------------
  ~GLWindow();
  //============================================================================
  void close();
  void join_thread();
  void run();
};
//==============================================================================
}  // namespace cg
//==============================================================================
#endif
