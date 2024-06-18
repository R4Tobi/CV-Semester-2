#include "ray.h"

namespace cg{
std::optional<vec3> refract(const vec3& d, const vec3& n, double ior){
  double eta;
  vec3 N = n;
  vec3 I = d;
  double cosi = -dot(N, I);

  // Check if the ray is inside the object to handle total internal reflection
  if (cosi < 0) {
    cosi = -cosi;
    N = -N;
    eta = ior;
  } else {
    eta = 1 / ior;
  }

  double k = 1 - eta * eta * (1 - cosi * cosi);

  // Check for total internal reflection
  if (k < 0) {
    return std::nullopt;
  } else {
    return eta * I + (eta * cosi - sqrt(k)) * N;
  }
}
}