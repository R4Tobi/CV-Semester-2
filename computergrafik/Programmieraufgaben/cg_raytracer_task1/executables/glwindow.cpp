#include "glwindow.h"
#include <GL/gl.h>
#include <stdexcept>
#include "renderable.h"

//==============================================================================
namespace cg {
//==============================================================================
void error_callback(int /*error*/, const char* description){
  throw std::runtime_error{"Error: " + std::string{description}};
}
//==============================================================================
GLWindow::GLWindow(const Raytracer& raytracer)
    : m_raytracer{raytracer} {
  if (!glfwInit()) {
    glfwTerminate();
    throw std::runtime_error{"[GLFW] could not initialize."};
  }
  glfwSetErrorCallback(error_callback);
  glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 2);
  glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);
  m_window = glfwCreateWindow((GLsizei)m_raytracer.get_texture().res_u(),
                              (GLsizei)m_raytracer.get_texture().res_v(),
                              "Computergraphics Ray Tracer", nullptr, nullptr);
  if (!m_window) {
    throw std::runtime_error{"[GLFW] could not OpenGL Context."};
  }
  run();
}
//------------------------------------------------------------------------------
GLWindow::~GLWindow() {
  close();
  glfwDestroyWindow(m_window);
  glfwTerminate();
}
//==============================================================================
void GLWindow::join_thread() {
  if (m_render_thread) {
    m_render_thread->join();
    m_render_thread.reset();
  }
}
//------------------------------------------------------------------------------
void GLWindow::close() {
  glfwSetWindowShouldClose(m_window, true);
  join_thread();
}
//------------------------------------------------------------------------------
void GLWindow::run() {
  m_render_thread = std::make_unique<std::thread>([this] {
    glfwMakeContextCurrent(m_window);
    glfwSwapInterval(1);
    while (!glfwWindowShouldClose(m_window)) {
      const auto&        tex = m_raytracer.get_texture();
      std::vector<float> gldata;
      gldata.reserve(tex.res_u() * tex.res_v() * 3);
      for (size_t y = 0; y < tex.res_v(); ++y) {
        for (size_t x = 0; x < tex.res_u(); ++x) {
          for (size_t i = 0; i < 3; ++i) {
            gldata.push_back(static_cast<float>(tex.pixel(x, y)(i)));
          }
        }
      }
      //glClear(GL_COLOR_BUFFER_BIT)
      glViewport(0, 0, (GLsizei)tex.res_u(), (GLsizei)tex.res_v());
      glDrawPixels((GLsizei)tex.res_u(), (GLsizei)tex.res_v(), GL_RGB, GL_FLOAT,
                   gldata.data());

      glfwSwapBuffers(m_window);
      glfwPollEvents();
      std::this_thread::sleep_for(std::chrono::milliseconds{10});
    }
  });
}
//==============================================================================
}  // namespace cg
//==============================================================================
