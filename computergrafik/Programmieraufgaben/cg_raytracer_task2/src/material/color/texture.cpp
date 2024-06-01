#include "texture.h"
#include <cassert>
#include <fstream>
//==============================================================================
namespace cg {
//==============================================================================
Texture::Texture(const std::string& filepath) : m_resolution{0, 0} {
  read_ppm(filepath);
}
//----------------------------------------------------------------------------
Texture::Texture(size_t res_u, size_t res_v, const vec3& col)
    : m_resolution{res_u, res_v}, m_pixel_data(res_u * res_v, col) {}
//----------------------------------------------------------------------------
Texture::Texture(size_t res_u, size_t res_v,
                 const std::vector<vec3>& pixel_data)
    : m_resolution{res_u, res_v}, m_pixel_data(pixel_data) {
  assert(res_u * res_v == pixel_data.size());
}
//----------------------------------------------------------------------------
vec3 Texture::sample(double u, double v) const {
  // scale position to have u in range of [0, res_u - 1]
  u *= res_u() - 1;

  // scale position to have v in range of [0, res_v - 1]
  v *= res_v() - 1;

  //             ut
  //      lu      |     ru
  //   tv []---- p1 ----[] tv
  //      |       .      |
  //      |       .      |
  //  vt -|......p2......|- vt
  //      |       .      |
  //      |       .      |
  //   bv []---- p0 ----[] bv
  //      lu      |     ru
  //             ut
  //
  // [] are given pixel values stored in m_pixel_data.
  // lu, ru, bv, tv are left, right, bottom and top pixel indices.
  // ut and vt are interpolation factors.
  // p0 and p1 are linearly interpolated pixel colors.
  // p2 is final bilinearly interpolated pixel color.

  // get left and right pixel indices.
  // using modulo we get a texture with repeat behavior
  size_t lu =
      std::abs(static_cast<int>(std::floor(u)) % static_cast<int>(res_u()));
  size_t ru = std::min(lu + 1, res_u() - 1);

  // get bottom and top pixel indices
  // using modulo we get a texture with repeat behavior
  size_t bv =
      std::abs(static_cast<int>(std::floor(v)) % static_cast<int>(res_v()));
  size_t tv = std::min(bv + 1, res_v() - 1);

  // interpolation factor for first two linear interpolations
  double ut = u - std::floor(u);
  // interpolation factor for final bilinear interpolations
  double vt = v - std::floor(v);

  // bottom linear interpolation
  vec3 p0 = (1 - ut) * pixel(lu, bv) + ut * pixel(ru, bv);

  // top linear interpolation
  vec3 p1 = (1 - ut) * pixel(lu, tv) + ut * pixel(ru, tv);

  // final biliniear interpolation
  vec3 p2 = (1 - vt) * p0 + vt * p1;

  return p2;
}
//----------------------------------------------------------------------------
vec3& Texture::pixel(size_t u, size_t v) {
  return m_pixel_data[u + v * m_resolution[0]];
}
//----------------------------------------------------------------------------
const vec3& Texture::pixel(size_t u, size_t v) const {
  return m_pixel_data[u + v * m_resolution[0]];
}
//----------------------------------------------------------------------------
void Texture::read_ppm(const std::string& filepath) {
  std::ifstream file{filepath};
  if (file) {
    std::string P3;
    size_t      max;
    file >> P3 >> m_resolution[0] >> m_resolution[1] >> max;
    assert(P3 == "P3");
    m_pixel_data.resize(m_resolution[0] * m_resolution[1]);

    double      r, g, b;
    for (size_t v = 0; v < res_v(); ++v) {
      for (size_t u = 0; u < res_u(); ++u) {
        file >> r >> g >> b;
        pixel(u, res_v() - v - 1)(0) = r / max;
        pixel(u, res_v() - v - 1)(1) = g / max;
        pixel(u, res_v() - v - 1)(2) = b / max;
      }
    }
    file.close();
  }
}
//----------------------------------------------------------------------------
void Texture::write_ppm(const std::string& filepath) const {
  std::ofstream file{filepath};
  const size_t  max   = 255;
  auto          remap = [&](auto u, auto v, auto i) {
    const auto& comp = pixel(u, res_v() - v - 1)(i);
    return std::abs(std::floor(std::max(0.0, std::min(1.0, comp)) * max));
  };
  if (file) {
    file << "P3 " << res_u() << ' ' << res_v() << " " << max << " ";
    for (size_t v = 0; v < res_v(); ++v) {
      for (size_t u = 0; u < res_u(); ++u) {
        file << remap(u, v, 0) << ' ' << remap(u, v, 1) << ' ' << remap(u, v, 2)
             << ' ';
      }
    }
    file.close();
  }
}
//==============================================================================
}  // namespace cg
//==============================================================================
