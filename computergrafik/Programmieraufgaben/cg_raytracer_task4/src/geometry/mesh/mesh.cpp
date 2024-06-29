#include "mesh.h"

#include <fstream>
#include <iostream>
#include <limits>
#include <sstream>
//==============================================================================
namespace cg {
//==============================================================================
auto split(std::string str, const std::string& delimiter) {
  std::vector<std::string> splits;
  auto                     pos = str.find(delimiter);
  while (pos != std::string::npos) {
    splits.push_back(str.substr(0, pos));
    str.erase(0, pos + delimiter.length());
    pos = str.find(delimiter);
  }
  splits.push_back(str);
  return splits;
}
//==============================================================================
Mesh::Mesh(const Material& m) : Renderable{m} {}
//------------------------------------------------------------------------------
Mesh::Mesh(const std::string& filepath, const Material& m) : Renderable{m} {
  read_obj(filepath);
}
//------------------------------------------------------------------------------
void Mesh::calc_normals() {
  m_normals.resize(m_vertices.size(), vec3::zeros());

  // sum up triangle normals per vertex
  for (const auto& triangle : m_triangles) {
    const auto e0 = m_vertices[triangle[1]] - m_vertices[triangle[0]];
    const auto e1 = m_vertices[triangle[2]] - m_vertices[triangle[0]];
    const auto triangle_normal = cross(e0, e1);
    for (size_t i = 0; i < 3; ++i) {
      m_normals[triangle[i]] += triangle_normal;
    }
  }

  // normalize
  for (size_t i = 0; i < m_normals.size(); ++i) {
    m_normals[i] = normalize(m_normals[i]);
  }
}
//------------------------------------------------------------------------------
// solving linear system
//std::optional<std::pair<double, vec3>> Mesh::check_intersection(
//    const Ray& r, const vec3& a, const vec3& b, const vec3& c) const {
//  const auto& o = r.origin();
//  const auto& d = r.direction();
//  double      u =
//      -(b(0) * (d(1) * (o(2) - c(2)) - d(2) * o(1) + c(1) * d(2)) +
//        d(0) * (c(1) * (o(2) - b(2)) + b(1) * (c(2) - o(2)) +
//                (b(2) - c(2)) * o(1)) +
//        c(0) * (d(1) * (b(2) - o(2)) + d(2) * o(1) - b(1) * d(2)) +
//        (-c(1) * d(2) + b(1) * d(2) + (c(2) - b(2)) * d(1)) * o(0)) /
//      (a(0) * (c(1) * d(2) - b(1) * d(2) + (b(2) - c(2)) * d(1)) +
//       b(0) * (-c(1) * d(2) + a(1) * d(2) + (c(2) - a(2)) * d(1)) +
//       c(0) * (b(1) * d(2) - a(1) * d(2) + (a(2) - b(2)) * d(1)) +
//       (a(1) * (c(2) - b(2)) + b(1) * (a(2) - c(2)) + (b(2) - a(2)) * c(1)) *
//           d(0));
//  double v =
//      (a(0) * (d(1) * (o(2) - c(2)) - d(2) * o(1) + c(1) * d(2)) +
//       d(0) * (c(1) * (o(2) - a(2)) + a(1) * (c(2) - o(2)) +
//               (a(2) - c(2)) * o(1)) +
//       c(0) * (d(1) * (a(2) - o(2)) + d(2) * o(1) - a(1) * d(2)) +
//       (-c(1) * d(2) + a(1) * d(2) + (c(2) - a(2)) * d(1)) * o(0)) /
//      (a(0) * (c(1) * d(2) - b(1) * d(2) + (b(2) - c(2)) * d(1)) +
//       b(0) * (-c(1) * d(2) + a(1) * d(2) + (c(2) - a(2)) * d(1)) +
//       c(0) * (b(1) * d(2) - a(1) * d(2) + (a(2) - b(2)) * d(1)) +
//       (a(1) * (c(2) - b(2)) + b(1) * (a(2) - c(2)) + (b(2) - a(2)) * c(1)) *
//           d(0));
//
//  double w =
//      -(a(0) * (d(1) * (o(2) - b(2)) - d(2) * o(1) + b(1) * d(2)) +
//        d(0) * (b(1) * (o(2) - a(2)) + a(1) * (b(2) - o(2)) +
//                (a(2) - b(2)) * o(1)) +
//        b(0) * (d(1) * (a(2) - o(2)) + d(2) * o(1) - a(1) * d(2)) +
//        (-b(1) * d(2) + a(1) * d(2) + (b(2) - a(2)) * d(1)) * o(0)) /
//      (a(0) * (c(1) * d(2) - b(1) * d(2) + (b(2) - c(2)) * d(1)) +
//       b(0) * (-c(1) * d(2) + a(1) * d(2) + (c(2) - a(2)) * d(1)) +
//       c(0) * (b(1) * d(2) - a(1) * d(2) + (a(2) - b(2)) * d(1)) +
//       (a(1) * (c(2) - b(2)) + b(1) * (a(2) - c(2)) + (b(2) - a(2)) * c(1)) *
//           d(0));
//
//  double t =
//      -(b(0) * (a(1) * (o(2) - c(2)) + c(1) * (a(2) - o(2)) +
//                (c(2) - a(2)) * o(1)) +
//        a(0) * (c(1) * (o(2) - b(2)) + b(1) * (c(2) - o(2)) +
//                (b(2) - c(2)) * o(1)) +
//        c(0) * (b(1) * (o(2) - a(2)) + a(1) * (b(2) - o(2)) +
//                (a(2) - b(2)) * o(1)) +
//        (a(1) * (c(2) - b(2)) + b(1) * (a(2) - c(2)) + (b(2) - a(2)) * c(1)) *
//            o(0)) /
//      (a(0) * (c(1) * d(2) - b(1) * d(2) + (b(2) - c(2)) * d(1)) +
//       b(0) * (-c(1) * d(2) + a(1) * d(2) + (c(2) - a(2)) * d(1)) +
//       c(0) * (b(1) * d(2) - a(1) * d(2) + (a(2) - b(2)) * d(1)) +
//       (a(1) * (c(2) - b(2)) + b(1) * (a(2) - c(2)) + (b(2) - a(2)) * c(1)) *
//           d(0));
//  if (u >= 0 && v >= 0 && w >= 0 && u <= 1 && v <= 1 && w <= 1) {
//    return std::pair{t, vec{u, v, w}};
//  }
//  return {};
//}
//------------------------------------------------------------------------------
// faster solution
 std::optional<std::pair<double, vec3>> Mesh::check_intersection(
     const Ray& r, const vec3& v0, const vec3& v1, const vec3& v2) const {
   constexpr double eps  = 1e-6;
   auto             v0v1 = v1 - v0;
   auto             v0v2 = v2 - v0;
   auto             pvec = cross(r.direction(), v0v2);
   auto             det  = dot(v0v1, pvec);
   // r and triangle are parallel if det is close to 0
   if (std::abs(det) < eps) { return {}; }
   auto inv_det = 1 / det;

   vec3 tvec = r.origin() - v0;
   auto u    = dot(tvec, pvec) * inv_det;
   if (u < 0 || u > 1) { return {}; }

   vec3 qvec = cross(tvec, v0v1);
   auto v    = dot(r.direction(), qvec) * inv_det;
   if (v < 0 || u + v > 1) { return {}; }

   auto t = dot(v0v2, qvec) * inv_det;

   return std::pair{t, vec3{1 - u - v, u, v}};
 }
//------------------------------------------------------------------------------
std::optional<Intersection> Mesh::check_intersection(const Ray& r,
                                                     double     min_t) const {

  std::set<size_t> possible_triangles;
  {
    std::unique_lock lock{m_hierarchy_mutex};
    if (!m_hierarchy) {
      lock.unlock();
      update_hierarchy();
      lock.lock();
    }
    possible_triangles =
        m_hierarchy->collect_possible_intersections(r);
  }
  //auto const possible_triangles =
  //    m_hierarchy->collect_possible_intersections(r);

  double t                 = std::numeric_limits<double>::max();
  auto   barycentric_coord = vec3::zeros();
  std::optional<std::pair<std::tuple<vec3, vec3, vec3>, size_t>> hit_tri;

  for (auto const i : possible_triangles) {
    // for (size_t i = 0; i < m_triangles.size(); ++i) {
    auto const& triangle = m_triangles[i];
    auto const  v0       = transform_point(m_vertices[triangle[0]]);
    auto const  v1       = transform_point(m_vertices[triangle[1]]);
    auto const  v2       = transform_point(m_vertices[triangle[2]]);

    auto hit = check_intersection(r, v0, v1, v2);
    if (hit && hit->first < t && hit->first > min_t) {
      t                 = hit->first;
      barycentric_coord = hit->second;
      hit_tri           = std::pair{std::tuple{v0, v1, v2}, i};
    }
  }
  if (!hit_tri) { return {}; }
  const auto& [tri, tri_index] = *hit_tri;
  const auto& [v0, v1, v2]     = tri;
  vec3 pos = barycentric_coord(0) * v0 + barycentric_coord(1) * v1 +
             barycentric_coord(2) * v2;

  vec3 nor =
      m_vertices.size() == m_normals.size()

          // interpolated vertex normal
          ? normalize(
                barycentric_coord(0) *
                    transform_normal(m_normals[m_triangles[tri_index][0]]) +
                barycentric_coord(1) *
                    transform_normal(m_normals[m_triangles[tri_index][1]]) +
                barycentric_coord(2) *
                    transform_normal(m_normals[m_triangles[tri_index][2]]))

          // triangle normal
          : cross(normalize(v1 - v0), normalize(v2 - v0));

  vec2 uv =
      m_vertices.size() == m_uv_coordinates.size()

          // interpolated uv coordinates
          ? barycentric_coord(0) * m_uv_coordinates[m_triangles[tri_index][0]] +
                barycentric_coord(1) *
                    m_uv_coordinates[m_triangles[tri_index][1]] +
                barycentric_coord(2) *
                    m_uv_coordinates[m_triangles[tri_index][2]]

          : vec2::zeros();

  return Intersection{this, r, t, pos, nor, uv};
}
//------------------------------------------------------------------------------
void Mesh::read_obj(const std::string& filepath) {
  std::ifstream     fin(filepath);
  std::vector<vec3> obj_normals;
  std::vector<vec2> obj_uv;
  if (fin) {
    std::string line;
    while (std::getline(fin, line)) {
      std::stringstream line_stream{line};
      std::string       tag;
      line_stream >> tag;
      if (tag == "v") {
        // VERTEX
        double x, y, z;
        line_stream >> x >> y >> z;
        m_vertices.emplace_back(x, y, z);

      } else if (tag == "vn") {
        // NORMAL
        double x, y, z;
        line_stream >> x >> y >> z;
        obj_normals.emplace_back(x, y, z);

      } else if (tag == "vt") {
        // TEX COORD
        double u, v;
        line_stream >> u >> v;
        obj_uv.emplace_back(u, v);

      } else if (tag == "f") {
        // FACE
        
        // erase tag
        line.erase(0, 2);

        auto                                  indices = split(line, " ");
        std::vector<std::vector<std::string>> ptns;
        ptns.push_back(split(indices[0], "/"));
        ptns.push_back(split(indices[1], "/"));
        ptns.push_back(split(indices[2], "/"));

        // position index
        if (ptns.front().size() == 1) {
          m_triangles.push_back(std::array{std::stoul(ptns[0][0]) - 1,
                                   std::stoul(ptns[1][0]) - 1,
                                   std::stoul(ptns[2][0]) - 1});
        }
        // position and texture coordinate index
        else if (ptns.front().size() == 2) {
          if (m_uv_coordinates.size() < m_vertices.size()) {
            m_uv_coordinates.resize(m_vertices.size());
          }
          m_triangles.push_back(std::array{std::stoul(ptns[0][0]) - 1,
                                   std::stoul(ptns[1][0]) - 1,
                                   std::stoul(ptns[2][0]) - 1});
          for (size_t i = 0; i < 3; ++i) {
            m_uv_coordinates[std::stoul(ptns[i][0]) - 1] =
                obj_uv[std::stoull(ptns[i][1]) - 1];
          }
        } else {
          if (ptns[0][1].empty()) {
            // position and normal index
            if (m_normals.size() < m_vertices.size()) {
              m_normals.resize(m_vertices.size());
            }
            m_triangles.push_back(std::array{std::stoul(ptns[0][0]) - 1,
                                     std::stoul(ptns[1][0]) - 1,
                                     std::stoul(ptns[2][0]) - 1});
            for (size_t i = 0; i < 3; ++i) {
              m_normals[std::stoull(ptns[i][0]) - 1] =
                  obj_normals[std::stoull(ptns[i][2]) - 1];
            }
          }
          // position, texture coordinate and normal index
          else {
            if (m_normals.size() < m_vertices.size()) {
              m_normals.resize(m_vertices.size());
            }
            if (m_uv_coordinates.size() < m_vertices.size()) {
              m_uv_coordinates.resize(m_vertices.size());
            }
            m_triangles.push_back(std::array{std::stoul(ptns[0][0]) - 1,
                                     std::stoul(ptns[1][0]) - 1,
                                     std::stoul(ptns[2][0]) - 1});
            for (size_t i = 0; i < 3; ++i) {
              m_uv_coordinates[std::stoull(ptns[i][0]) - 1] =
                  obj_uv[std::stoull(ptns[i][1]) - 1];
              m_normals[std::stoull(ptns[i][0]) - 1] =
                  obj_normals[std::stoull(ptns[i][2]) - 1];
            }
          }
        }
      }
    }
    fin.close();
  } else {
    std::cerr << "could not read file " << filepath << '\n';
  }
}
//==============================================================================
}  // namespace cg
//==============================================================================
