#include "box.h"

#define _USE_MATH_DEFINES
#include <cmath>
#include <iostream>
//==============================================================================
namespace cg {
//==============================================================================
Box::Box(const Material& m) : Renderable{m} {}
//------------------------------------------------------------------------------
std::optional<Intersection> Box::check_intersection(const Ray& r,
                                                    double     min_t) const {
  std::optional<double> t;
  std::optional<vec3>   p;
  vec3                  n;
  vec2                  uv;
  vec3                  trOrg = inverse_point(r.origin());
  vec3                  trDir = inverse_direction(r.direction());

  const double eps = 1e-6;

  // going through x,y,z components
  for (int i1 = 0; i1 < 3; ++i1) {

    // avoid division by 0
    if (std::abs(trDir(i1)) > eps) {
      
      // the other two component indices
      int    i2   = (i1 + 1) % 3;
      int    i3   = (i1 + 2) % 3;
      
      // intersection with plane going through origin
      double t1   = -trOrg(i1) / trDir(i1);
      vec3   pos1 = trOrg + trDir * t1;
      
      // intersection with plane that is offset by 1 in one coordinate axis
      double t2   = (1. - trOrg(i1)) / trDir(i1);
      vec3   pos2 = trOrg + trDir * t2;

      // check if plane intersection is also box intersection
      if (t1 < t2 && t1 >= min_t && pos1(i2) >= 0.0 && pos1(i2) <= 1.0 && 
        pos1(i3) >= 0.0 && pos1(i3) <= 1.0) {
        
        if (!t || t > t1) { 
            t = t1;
            p = pos1;
            n = vec3::zeros();
            n(i1) = -1.0;
            uv = { (2. - i1) / 3. + p.value()(i2) / 3., (1. - p.value()(i3)) / 2. };
        }
      } 
      else if (t2 >= min_t && pos2(i2) >= 0. && pos2(i2) <= 1. && 
        pos2(i3) >= 0. && pos2(i3) <= 1) {
        
        if (!t || t > t2) { 
            t = t2;
            p = pos2;
            n = vec3::zeros();
            n(i1) = 1.0;
            uv = { (2. - i1) / 3. + p.value()(i2) / 3., (1. + p.value()(i3)) / 2. };
        }
      }
    }
  }
  if (!t) { return {}; }

/*
  auto hit_pos = trOrg + trDir * t.value();
  vec3 nor;
  vec2 uv;
  for (int i1 = 0; i1 < 3; i1++) {
    int i2 = (i1 + 1) % 3, i3 = (i1 + 2) % 3;
    if (hit_pos(i1) < eps) {
      nor(i1) = -1.;
      uv      = {(2. - i1) / 3. + hit_pos(i2) / 3., (1. - hit_pos(i3)) / 2.};
      break;
    }
    if (1. - hit_pos(i1) < eps) {
      nor(i1) = +1.;
      uv      = {(2. - i1) / 3. + hit_pos(i2) / 3., (1. + hit_pos(i3)) / 2.};
      break;
    }
  }*/
  //   std::cout << "Org:" << r.origin() << "Dir:" << r.direction()
  //              << "t:" << t.value() << std::endl;
  //   std::cout << "TOrg:" << trOrg << "TDir:" << trDir;
  //   std::cout << "hit" << r(t.value()) << "thit:" << hit_pos << "uv:" << uv
  //             << std::endl;
  //   std::cout << "thit:" << hit_pos << "uv:" << uv << "normal:" << nor
  //             << std::endl;

  return Intersection{this, r, t.value(), r(t.value()), normalize(transform_normal(n)),
                      uv};
}
//==============================================================================
}  // namespace cg
//==============================================================================
