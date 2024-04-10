#ifndef CG_VEC_H
#define CG_VEC_H
//==============================================================================
#include <algorithm>
#include <cmath>
#include <ostream>
#include <random>
#include <vector>
#include "utility.h"
//==============================================================================
namespace cg {
//==============================================================================
template <size_t N>
class vec {
 public:
  //===========================================================================
  using real_t               = double;
  static constexpr auto size = N;
  using data_container_t     = std::array<real_t, size>;
  using iterator             = typename data_container_t::iterator;
  using const_iterator       = typename data_container_t::const_iterator;

 private:
  //===========================================================================
  data_container_t m_data;

 public:
  //===========================================================================
  /// default constructor
  constexpr vec() : vec{0.0} {}
  //----------------------------------------------------------------------------
  /// copy constructor
  constexpr vec(const vec& other) = default;
  //----------------------------------------------------------------------------
  /// move constructor
  constexpr vec(vec&& other) = default;
  //----------------------------------------------------------------------------
  /// copy assignment
  constexpr vec& operator=(const vec& other) = default;
  //----------------------------------------------------------------------------
  /// move assignment
  constexpr vec& operator=(vec&& other) = default;
  //----------------------------------------------------------------------------
  /// creates a vector with specified components in initializer list
  template <typename... Components>
  constexpr vec(Components... components) : m_data{static_cast<real_t>(components)...} {
    static_assert((std::is_arithmetic_v<Components> && ...),
                  "arithmetic values are needed");
  }

 private:
  //----------------------------------------------------------------------------
  /// creates empty vector
  explicit constexpr vec(real_t c) : m_data{make_array<real_t, size>(c)} {}

 public:
  //----------------------------------------------------------------------------
  /// creates a vector filled with zeros
  constexpr static auto zeros() { return vec{0.0}; }
  //----------------------------------------------------------------------------
  /// creates a vector filled with ones
  constexpr static auto ones() { return vec{1.0}; }
  //----------------------------------------------------------------------------
  /// creates a vector filled with random values between min and max
  template <typename random_engine_t = std::mt19937_64>
  static auto randu(real_t min = 0, real_t max = 1,
                    random_engine_t&& e = random_engine_t{
                        std::random_device{}()}) {
    vec<size>                              v;
    std::uniform_real_distribution<real_t> dist{min, max};
    std::generate(v.begin(), v.end(), [&] { return dist(e); });
    return v;
  }
  //----------------------------------------------------------------------------
  /// adds another vector
  constexpr vec& operator+=(const vec& other) {
    std::transform(begin(), end(), other.begin(), begin(),
                   [](auto l, auto r) { return l + r; });
    return *this;
  }
  //----------------------------------------------------------------------------
  /// adds scalar to every component
  constexpr vec& operator+=(double add) {
    std::transform(begin(), end(), begin(),
                   [add](auto c) { return c + add; });
    return *this;
  }
  //----------------------------------------------------------------------------
  /// subtracts another vector
  constexpr vec& operator-=(const vec& other) {
    std::transform(m_data.begin(), m_data.end(), other.m_data.begin(),
                   m_data.begin(), [](auto l, auto r) { return l - r; });
    return *this;
  }
  //----------------------------------------------------------------------------
  /// subtracts scalar to every component
  constexpr vec& operator-=(double add) {
    std::transform(begin(), end(), begin(),
                   [add](auto c) { return c - add; });
    return *this;
  }
  //----------------------------------------------------------------------------
  /// scales vector
  constexpr vec& operator*=(vec other) {
    std::transform(begin(), end(), other.begin(), begin(),
                   [](auto l, auto r) { return l * r; });
    return *this;
  }
  //----------------------------------------------------------------------------
  /// inverse-scales vector
  constexpr vec& operator/=(vec other) {
    std::transform(begin(), end(), other.begin(), begin(),
                   [](auto l, auto r) { return l / r; });
    return *this;
  }
  //----------------------------------------------------------------------------
  /// scales vector
  constexpr vec& operator*=(real_t scale) {
    std::transform(begin(), end(), begin(),
                   [scale](auto c) { return c * scale; });
    return *this;
  }
  //----------------------------------------------------------------------------
  /// inverse-scales vector
  constexpr vec& operator/=(real_t scale) {
    std::transform(begin(), end(), begin(),
                   [scale](auto c) { return c / scale; });
    return *this;
  }
  //----------------------------------------------------------------------------
  /// access to component at position i
  constexpr real_t& at(size_t i) { return m_data[i]; }
  //----------------------------------------------------------------------------
  /// access to component at position i
  constexpr real_t at(size_t i) const { return m_data[i]; }
  //----------------------------------------------------------------------------
  /// access to component at position i
  constexpr real_t& operator()(size_t i) { return at(i); }
  //----------------------------------------------------------------------------
  /// access to component at position i
  constexpr real_t operator()(size_t i) const { return at(i); }
  //----------------------------------------------------------------------------
  constexpr real_t squared_length() const {
    real_t l = 0.0;
    for (auto c : m_data) { l += c * c; }
    return l;
  }
  //----------------------------------------------------------------------------
  constexpr real_t length() const { return std::sqrt(squared_length()); }
  //----------------------------------------------------------------------------
  constexpr auto begin() { return m_data.begin(); }
  constexpr auto begin() const { return m_data.begin(); }
  constexpr auto end() { return m_data.end(); }
  constexpr auto end() const { return m_data.end(); }
  //----------------------------------------------------------------------------
  bool has_nan() const {
    for (size_t i = 0; i < N; ++i) {
      if (std::isnan(at(i))) { return true; }
    }
    return false;
  }
  //----------------------------------------------------------------------------
  bool has_inf() const {
    for (size_t i = 0; i < N; ++i) {
      if (std::isinf(at(i))) { return true; }
    }
    return false;
  }
};
//==============================================================================
using vec2  = vec<2>;
using vec3  = vec<3>;
using vec4  = vec<4>;
using color = vec<3>;
//==============================================================================
/// deduction guide gets matrix dimensions when creating object
template <typename... Ts>
vec(Ts... ) -> vec<sizeof...(Ts)>;
//==============================================================================
/// dot product of two vectors
template <size_t N>
constexpr auto dot(const vec<N>& v0, const vec<N>& v1) {
  typename vec<N>::real_t dp = 0;
  for (size_t i = 0; i < N; ++i) { dp += v0(i) * v1(i); }
  return dp;
}
//------------------------------------------------------------------------------
/// squared euclidean distance between two points of two vectors
template <size_t N>
constexpr auto distance_squared(const vec<N>& v0, const vec<N>& v1) {
  const auto distance_vec = v1 - v0;
  return dot(distance_vec, distance_vec);
}
//------------------------------------------------------------------------------
/// euclidean distance between two points of two vectors
template <size_t N>
constexpr auto distance(const vec<N>& v0, const vec<N>& v1) {
  return std::sqrt(distance_squared(v0, v1));
}
//------------------------------------------------------------------------------
/// cross product of two vectors
constexpr inline auto cross(const vec3& v0, const vec3& v1) {
  return vec3{v0(1) * v1(2) - v0(2) * v1(1),
              v0(2) * v1(0) - v0(0) * v1(2),
              v0(0) * v1(1) - v0(1) * v1(0)};
}
//------------------------------------------------------------------------------
/// returns a normalized copy of v
template <size_t N>
constexpr auto normalize(const vec<N>& v) {
  vec<N>  n{v};
  auto l = n.length();
  for (auto& c : n) c /= l;
  return n;
}
//------------------------------------------------------------------------------
/// component-wise addition of two vectors
template <size_t N>
constexpr auto operator+(const vec<N>& v0, const vec<N>& v1) {
  vec<N> v{v0};
  return v += v1;
}
//------------------------------------------------------------------------------
/// addition of vector and scalar
template <size_t N>
constexpr auto operator+(const vec<N>& v0, double scalar) {
  vec<N> v{v0};
  return v += scalar;
}
//------------------------------------------------------------------------------
/// addition of scalar and vector
template <size_t N>
constexpr auto operator+(double scalar, const vec<N>& v0) {
  vec<N> v{v0};
  return v += scalar;
}
//------------------------------------------------------------------------------
/// component-wise subtraction of two vectors
template <size_t N>
constexpr auto operator-(const vec<N>& v0, const vec<N>& v1) {
  vec<N> v{v0};
  return v -= v1;
}
//------------------------------------------------------------------------------
/// addition of vector and scalar
template <size_t N>
constexpr auto operator-(const vec<N>& v0, double scalar) {
  vec<N> v{v0};
  return v -= scalar;
}
//------------------------------------------------------------------------------
/// addition of scalar and vector
template <size_t N>
constexpr auto operator-(double scalar, const vec<N>& v0) {
  vec<N> v{v0};
  return v -= scalar;
}
//------------------------------------------------------------------------------
/// negation of a vector
template <size_t N>
constexpr auto operator-(const vec<N>& v) {
  vec<N> neg{v};
  for (auto& c : neg) c *= -1;
  return neg;
}
//------------------------------------------------------------------------------
/// component-wise mutliplication of two vectors
template <size_t N, size_t M>
constexpr auto operator*(const vec<N>& lhs, const vec<M>& rhs) {
  static_assert(N == M, "vector dimensions do not match");
  vec<N> v{lhs};
  return v *= rhs;
}
//------------------------------------------------------------------------------
/// component-wise division of two vectors
template <size_t N, size_t M>
constexpr auto operator/(const vec<N>& lhs, const vec<M>& rhs) {
  static_assert(N == M, "vector dimensions do not match");
  vec<N> v{lhs};
  return v /= rhs;
}
//------------------------------------------------------------------------------
/// scaling of a vector
template <size_t N, typename real_t>
constexpr vec<N> operator*(const vec<N>& v, real_t s) {
  vec<N> scaled{v};
  return scaled *= s;
}
//------------------------------------------------------------------------------
/// scaling of a vector
template <size_t N, typename real_t>
constexpr auto operator*(real_t s, const vec<N>& v) {
  vec<N> scaled{v};
  return scaled *= s;
}
//------------------------------------------------------------------------------
/// inverse scaling of a vector
template <size_t N, typename real_t>
constexpr auto operator/(const vec<N>& v, real_t s) {
  vec<N> scaled{v};
  return scaled /= (double)s;
}
//------------------------------------------------------------------------------
/// inverse scaling of a vector
template <size_t N, typename real_t>
constexpr auto operator/(real_t s, const vec<N>& v) {
  vec<N> scaled{v};
  return scaled /= s;
}
//------------------------------------------------------------------------------
/// printing a vector into a stream
template <size_t N>
auto& operator<<(std::ostream& str, const vec<N>& v) {
  str << "[ " << v(0);
  for (size_t i = 1; i < N; ++i) { str << ", " << v(i); }
  str << " ]";
  return str;
}
//==============================================================================
}  // namespace cg
//==============================================================================
#endif
