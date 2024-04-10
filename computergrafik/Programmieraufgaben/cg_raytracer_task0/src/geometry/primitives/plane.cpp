#include "plane.h"
#include<iostream>
//==============================================================================
namespace cg {
//==============================================================================

//------------------------------------------------------------------------------
Plane::Plane(const vec2& extends, const Material& m)
    : Renderable{m}, m_x0{vec3::zeros()}, m_normal{vec3{0, 1, 0}}, 
      base0{vec3{1, 0, 0}}, base1{vec3{0, 0, 1}}, extends{extends} {}
//------------------------------------------------------------------------------
Plane::Plane(const Material& m)
    : Plane{vec2{-1, -1}, m} {}
//------------------------------------------------------------------------------
std::optional<Intersection> Plane::check_intersection(
    const Ray& r, double min_t) const {

  // Task 1
  // Something is missing in this method. Can you find it? How can you fix it?
  // Write your answer in scene.cpp
  
  vec3 trDir = inverse_direction(r.direction());
  vec3 trOrig = inverse_point(r.origin());

  double denom = dot(trDir, m_normal);

  
  double t = (dot(m_normal, m_x0) - dot(m_normal, trOrig)) / denom;

  if (t > min_t) {

    vec3 hit_pos = trOrig + t*trDir;
    vec2 uv = {dot(base0, m_x0 - hit_pos), dot(base1, m_x0 - hit_pos)};
    
    // check bounds
    if( (extends(0) > 0 && std::abs(dot(hit_pos, base0)) > extends(0)) ||
        (extends(1) > 0 && std::abs(dot(hit_pos, base1)) > extends(1))){
      return {};
    }
    else {
      if(extends(0) > 0){
        uv(0) = (uv(0) + extends(0)) / 2;
      }
      if(extends(1) > 0){
        uv(1) = (uv(1) + extends(1)) / 2;
      }
    }

    vec3 world_hit_pos = r(t);
    vec3 world_normal  = normalize(transform_normal(m_normal));

    return Intersection{
        this, r, t,
        world_hit_pos,
        world_normal, 
        uv};
  }
  return {};
}

//------------------------------------------------------------------------------

XYPlane::XYPlane(const vec2& extends, const Material& m) : Plane{extends, m} {
    m_normal = vec3{0, 0, 1};
    base0    = vec3{1, 0, 0};
    base1    = vec3{0, 1, 0};
}

XYPlane::XYPlane(const Material& m) : XYPlane{vec2{-1, -1}, m}{}

//------------------------------------------------------------------------------

YZPlane::YZPlane(const vec2& extends, const Material& m) : Plane{extends, m} {
    m_normal = vec3{1, 0, 0};
    base0    = vec3{0, 1, 0};
    base1    = vec3{0, 0, 1};
}

YZPlane::YZPlane(const Material& m) : YZPlane{vec2{-1, -1}, m}  {}

XZPlane::XZPlane(const vec2& extends, const Material& m) : Plane{ extends, m } {
    m_normal = vec3{ 0, 1, 0 };
    base0 = vec3{ 1, 0, 0 };
    base1 = vec3{ 0, 0, 1 };
}

XZPlane::XZPlane(const Material& m) : XZPlane{ vec2{-1, -1}, m } {}

//==============================================================================
}  // namespace cg
//==============================================================================
