#include "ray.h"

namespace cg{
  std::optional<vec3> refract(const vec3& d, const vec3& n, double ior){
    vec3 nn     = normalize(n);
    double cosi = dot(normalize(d), nn);
    double etai = 1, etat = ior;
    if (cosi < 0) {
      cosi = -cosi;
    } else {
      std::swap(etai, etat);
      nn = -nn;
    }
    double eta = etai / etat;
    double k   = 1 - eta * eta * (1 - cosi * cosi);
    if (k < 0) { return {}; }
    return eta * d + (eta * cosi - std::sqrt(k)) * nn;
      }
}