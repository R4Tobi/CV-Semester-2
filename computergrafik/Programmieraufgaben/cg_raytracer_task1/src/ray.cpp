#include "ray.h"

namespace cg{
  std::optional<vec3> refract(const vec3& d, const vec3& n, double ior){
    /*
    ============================================================================
     Task 3
    ============================================================================
     Calculate the refracted ray. See task description for more information.

     Be aware that the ray enters but also exits an object! The normal n will 
     always point away from the object. ior is always the index of refraction for
     the object material. We assume the ior for air is always 1 and that the 
     refraction always happens at an air-to-material or material-to-air boundary.
    */

    return d; // <- dummy output!
      }
}