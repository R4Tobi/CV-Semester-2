#include "bezier_wave.h"
//==============================================================================
namespace cg{
//==============================================================================
BezierWave::BezierWave(const Material& material, size_t res_u, size_t res_v)
    : BezierPatchMesh{material, 3, 3, res_u, res_v} {
  set_control_point(0, 0, vec3{-0.4, -0.4, 0.3});
  set_control_point(1, 0, vec3{0.0, -0.4, 0.2});
  set_control_point(2, 0, vec3{0.4, -0.4, 0.1});

  set_control_point(0, 1, vec3{-0.4, 0.0, 0.0});
  set_control_point(1, 1, vec3{0.0, 0.0, -0.2});
  set_control_point(2, 1, vec3{0.4, 0.0, 0.2});

  set_control_point(0, 2, vec3{-0.4, 0.4, 0.3});
  set_control_point(1, 2, vec3{0.0, 0.4, 0.2});
  set_control_point(2, 2, vec3{0.4, 0.4, 0.0});
  initialize();
}

BezierWave2::BezierWave2(const Material& material, size_t res_u, size_t res_v)
  : BezierPatchMesh{ material, 4, 3, res_u, res_v } {
  set_control_point(0, 0, vec3{ -0.3,   0.0, -0.3 });
  set_control_point(1, 0, vec3{ -0.35, -0.5, -0.1 });
  set_control_point(2, 0, vec3{ -0.3,   0.5,  0.1 });
  set_control_point(3, 0, vec3{ -0.35,  0.0,  0.3 });

  set_control_point(0, 1, vec3{  0.0,  0.5, -0.3 });
  set_control_point(1, 1, vec3{  0.0, -0.5, -0.1 });
  set_control_point(2, 1, vec3{  0.0,  0.5,  0.1 });
  set_control_point(3, 1, vec3{  0.0, -0.5,  0.3 });

  set_control_point(0, 2, vec3{  0.3,   0.0, -0.3 });
  set_control_point(1, 2, vec3{  0.35, -0.5, -0.1});
  set_control_point(2, 2, vec3{  0.3,   0.5,  0.1});
  set_control_point(3, 2, vec3{  0.35,  0.0,  0.3});
  initialize();
}
//==============================================================================
}  // namespace cg
//==============================================================================
