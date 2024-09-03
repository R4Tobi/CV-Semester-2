#ifndef CG_CHECKERBOARD_H
#define CG_CHECKERBOARD_H
//==============================================================================
#include "vec.h"
#include "colorsource.h"
//==============================================================================
namespace cg {
//==============================================================================
class Checkerboard : public ColorSource {
  size_t m_res_x, m_res_y;
  color  m_col0, m_col1;

 public:
  Checkerboard(size_t res_x, size_t res_y, const color& col0, const color col1)
      : m_res_x{res_x}, m_res_y{res_y}, m_col0{col0}, m_col1{col1} {}
  Checkerboard(const color& col0, const color col1, size_t res_x, size_t res_y)
      : m_res_x{res_x}, m_res_y{res_y}, m_col0{col0}, m_col1{col1} {}
  //---------------------------------------------------------------------------
  color sample(double u, double v) const override;
  //---------------------------------------------------------------------------
  make_clonable(ColorSource, Checkerboard);
};
//==============================================================================
}  // namespace cg
//==============================================================================
#endif
