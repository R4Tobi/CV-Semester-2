#include "checkerboard.h"
//==============================================================================
namespace cg {
//==============================================================================
color Checkerboard::sample(double u, double v) const {
  bool u_odd = std::abs(int(u * m_res_x) % 2) == 1;
  bool v_odd = std::abs(int(v * m_res_y) % 2) == 1;
  if (u < 0) { u_odd = !u_odd; }
  if (v < 0) { u_odd = !u_odd; }
  if (v_odd) { u_odd = !u_odd; }
  if (u_odd) { return m_col0; }
  return m_col1;
}
//==============================================================================
}  // namespace cg
//==============================================================================
