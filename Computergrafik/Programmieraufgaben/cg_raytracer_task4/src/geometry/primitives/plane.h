#ifndef CG_PLANE_H
#define CG_PLANE_H
//==============================================================================
#include "renderable.h"
#include "vec.h"
//==============================================================================
namespace cg {
//==============================================================================
class Plane : public Renderable {

protected:
  vec3 m_x0;
  vec3 m_normal;
  vec3 base0, base1;  // basis vectors on plane
  vec2 extends;       // plane size, negative if infinitly large

public:
  Plane(const vec2& extends, const Material& m);
  Plane(const Material& m);
  Plane(const Plane&) = default;
  Plane(Plane&&)      = default;

  //---------------------------------------------------------------------------
  std::optional<Intersection> check_intersection(
      const Ray& r, double min_t = 0) const override;
  //---------------------------------------------------------------------------
  make_clonable(Renderable, Plane);
};

class XYPlane : public Plane {

public:
  XYPlane(const vec2& extends, const Material& m);
  XYPlane(const Material& m);
};

class YZPlane : public Plane {

public:
  YZPlane(const vec2& extends, const Material& m);
  YZPlane(const Material& m);
};

class XZPlane : public Plane {

public:
  XZPlane(const vec2& extends, const Material& m);
  XZPlane(const Material& m);
};

//==============================================================================
}  // namespace cg
//==============================================================================
#endif
