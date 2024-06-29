#ifndef CG_MAT_H
#define CG_MAT_H
#define _USE_MATH_DEFINES
//==============================================================================
#include <algorithm>
#include <array>
#include <cassert>
#include <cmath>
#include <optional>
#include <ostream>
#include <vector>
#include "utility.h"
#include "vec.h"
//==============================================================================
namespace cg {
//==============================================================================
/// Matrix with column-major memory layout
template <size_t R, size_t C>
struct mat {
 public:
  //============================================================================
  using real_t           = double;
  using dimension_t      = std::array<size_t, 2>;
  using data_container_t = std::vector<real_t>;
  static constexpr auto num_rows       = R;
  static constexpr auto num_columns    = C;
  static constexpr auto num_components = R * C;
  using data_block_t = std::array<real_t, num_components>;

 private:
  //============================================================================
  data_block_t m_data;

 public:
  //============================================================================
  /// copy constructor
  constexpr mat(const mat& other) = default;
  //----------------------------------------------------------------------------
  /// move constructor
  constexpr mat(mat&& other) = default;
  //----------------------------------------------------------------------------
  /// copy assignment
  constexpr mat& operator=(const mat& other) = default;
  //----------------------------------------------------------------------------
  /// move assignment
  constexpr mat& operator=(mat&& other) = default;
  //----------------------------------------------------------------------------
  /// taking variadic list of references to c-style arrays representing rows
  template <typename... row_ts>
  constexpr mat(row_ts const (&... rows)[C]) : m_data{} {
    static_assert(
        sizeof...(rows) == R,
        "number of given rows does not match specified number of rows");

    // lambda inserting row into data block
    auto insert_row = [r = 0UL, this](const auto& row_data) mutable {
      size_t c = 0;
      for (auto v : row_data) at(r, c++) = static_cast<real_t>(v);
      ++r;
    };

    // apply insert_row to each row
    for_each(insert_row, rows...);
  }

 private:
  /// constructor that sets all components to c
  constexpr mat(real_t c) : m_data{make_array<real_t, num_components>(c)} {}

 public:
  //----------------------------------------------------------------------------
  /// creates a matrix filled with zeros
  constexpr static auto zeros() { return mat{0.0}; }
  //----------------------------------------------------------------------------
  /// creates a matrix filled with ones
  constexpr static auto ones() { return mat{1.0}; }
  //----------------------------------------------------------------------------
  /// creates an identity matrix
  constexpr static auto eye() {
    auto m = zeros();
    for (size_t i = 0; i < std::min(num_rows, num_columns); ++i) m(i, i) = 1;
    return m;
  }
  //============================================================================
  /// adds another matrix
  constexpr auto& operator+=(const mat& other) {
    std::transform(begin(m_data), end(m_data), begin(other.m_data),
                   begin(m_data), [](auto l, auto r) { return l + r; });
    return *this;
  }
  //----------------------------------------------------------------------------
  /// subtracts another matrix
  constexpr auto& operator-=(const mat& other) {
    std::transform(begin(m_data), end(m_data), begin(other.m_data),
                   begin(m_data), [](auto l, auto r) { return l - r; });
    return *this;
  }
  //----------------------------------------------------------------------------
  /// scales matrix
  constexpr auto& operator*=(real_t scale) {
    std::transform(begin(m_data), end(m_data), begin(m_data),
                   [scale](auto c) { return c * scale; });
    return *this;
  }
  //----------------------------------------------------------------------------
  /// inverse-scales matrix
  constexpr auto& operator/=(real_t scale) {
    std::transform(begin(m_data), end(m_data), begin(m_data),
                   [scale](auto c) { return c / scale; });
    return *this;
  }
  //----------------------------------------------------------------------------
  /// access to component at position [row, col]
  constexpr auto& at(size_t row, size_t col) {
    assert(row < num_rows);
    assert(col < num_columns);
    return m_data[row + num_rows * col];
  }
  //----------------------------------------------------------------------------
  /// access to component at position [row, col]
  constexpr auto at(size_t row, size_t col) const {
    assert(row < num_rows);
    assert(col < num_columns);
    return m_data[row + num_rows * col];
  }
  //----------------------------------------------------------------------------
  /// access to component at position [row, col]
  constexpr auto& operator()(size_t row, size_t col) {
    assert(row < num_rows);
    assert(col < num_columns);
    return at(row, col);
  }
  //----------------------------------------------------------------------------
  /// access to component at position [row, col]
  constexpr auto operator()(size_t row, size_t col) const {
    assert(row < num_rows);
    assert(col < num_columns);
    return at(row, col);
  }
  //----------------------------------------------------------------------------
  /// access to raw data block
  constexpr auto& operator[](size_t i) {return m_data[i];} 
  
  //----------------------------------------------------------------------------
  /// access to raw data block
  constexpr auto operator[](size_t i) const {return m_data[i];} 
  //----------------------------------------------------------------------------
  /// returns reference to data block
  constexpr auto& data() { return m_data; }
};

//==============================================================================
// typedefs
using mat2 = mat<2, 2>;
using mat3 = mat<3, 3>;
using mat4 = mat<4, 4>;

//==============================================================================
/// deduction guide gets matrix dimensions when creating object
template <size_t C, typename... Ts>
mat(Ts const (&...arr)[C]) -> mat<sizeof...(Ts), C>;

//==============================================================================
// operations
//==============================================================================

//------------------------------------------------------------------------------
/// component-wise addition of two matrices
template <size_t R, size_t C>
constexpr auto operator+(const mat<R, C>& m0, const mat<R, C>& m1) {
  mat m{m0};
  return m += m1;
}

//------------------------------------------------------------------------------
/// component-wise subtraction of two matrices
template <size_t R, size_t C>
constexpr auto operator-(const mat<R, C>& m0, const mat<R, C>& m1) {
  mat m{m0};
  return m -= m1;
}

//------------------------------------------------------------------------------
/// negation of a matrix
template <size_t R, size_t C>
constexpr auto operator-(const mat<R, C>& m) {
  mat neg{m};
  for (auto& c : neg.data()) c *= -1;
  return neg;
}

//------------------------------------------------------------------------------
/// scaling of a matrix
template <size_t R, size_t C, typename real_t>
constexpr auto operator*(const mat<R, C>& m, real_t s) {
  mat copy{m};
  return copy *= s;
}

//------------------------------------------------------------------------------
/// scaling of a matrix
template <size_t R, size_t C, typename real_t>
constexpr auto operator*(real_t s, const mat<R, C>& m) {
  mat copy{m};
  return copy /= s;
}

//------------------------------------------------------------------------------
/// inverse scaling of a matrix
template <size_t R, size_t C, typename real_t>
constexpr auto operator/(const mat<R, C>& m, real_t s) {
  mat copy{m};
  return copy /= s;
}

//------------------------------------------------------------------------------
/// inverse scaling of a matrix
template <size_t R, size_t C, typename real_t>
constexpr auto operator/(real_t s, const mat<R, C>& m) {
  mat copy{m};
  return copy /= s;
}

//------------------------------------------------------------------------------
/// matrix-vector-multiplication
template <size_t R, size_t C, size_t N>
constexpr auto operator*(const mat<R, C>& lhs, const vec<N>& rhs) {
  static_assert(C == N,
                "size of vector does not match number of columns of matrix");
  vec<R> multiplicated;

  for (size_t m_row = 0; m_row < lhs.num_rows; ++m_row) {
    for (size_t i = 0; i < lhs.num_columns; ++i) {
      multiplicated(m_row) += lhs(m_row, i) * rhs(i);
    }
  }

  return multiplicated;
}

//------------------------------------------------------------------------------
/// matrix-matrix-multiplication
template <size_t R0, size_t C0, size_t R1, size_t C1>
constexpr auto operator*(const mat<R0, C0>& lhs, const mat<R1, C1>& rhs) {
  static_assert(C0 == R1,
                "left number of columns does not match right number of rows");
  auto m = mat<R0, C1>::zeros() ;

  for (size_t lhs_row = 0; lhs_row < lhs.num_rows; ++lhs_row) {
    for (size_t rhs_col = 0; rhs_col < rhs.num_columns; ++rhs_col) {
      for (size_t i = 0; i < lhs.num_columns; ++i) {
        m(lhs_row, rhs_col) += lhs(lhs_row, i) * rhs(i, rhs_col);
      }
    }
  }
  return m;
}


//------------------------------------------------------------------------------
/// creates transposed version of m
template <size_t R, size_t C>
constexpr auto transpose(const mat<R,C>& m) {
  auto t = mat<C,R>::zeros();

  for (size_t r = 0; r < R; ++r) {
    for (size_t c = 0; c < C; ++c) { t(c, r) = m(r, c); }
  }

  return t;
}

//------------------------------------------------------------------------------
/// Inverse of a 2x2 matrix.
/// Returns nothing if determinant is 0.
inline std::optional<mat<2, 2>> inverse(const mat2& m) {
  auto det = (m(0, 0) * m(1, 1) - m(1, 0) * m(0, 1));
  if (std::abs(det) < 1e-7) { return {}; }

  mat<2, 2> inv{{ m(1, 1), -m(0, 1)},
                {-m(1, 0),  m(0, 0)}};

  return inv / det;
}

//------------------------------------------------------------------------------
/// Inverse of a 3x3 matrix.
/// Returns nothing if determinant is 0.
inline std::optional<mat<3, 3>> inverse(const mat3& m) {
  auto det = m(0, 0) * (m(1, 1) * m(2, 2) - m(2, 1) * m(1, 2)) -
             m(0, 1) * (m(1, 0) * m(2, 2) - m(1, 2) * m(2, 0)) +
             m(0, 2) * (m(1, 0) * m(2, 1) - m(1, 1) * m(2, 0));
  if (std::abs(det) < 1e-7) { return {}; }
  mat<3, 3> inv{{(m(1, 1) * m(2, 2) - m(2, 1) * m(1, 2)),
                 -(m(0, 1) * m(2, 2) - m(0, 2) * m(2, 1)),
                 (m(0, 1) * m(1, 2) - m(0, 2) * m(1, 1))},
                {-(m(1, 0) * m(2, 2) - m(1, 2) * m(2, 0)),
                 (m(0, 0) * m(2, 2) - m(0, 2) * m(2, 0)),
                 -(m(0, 0) * m(1, 2) - m(1, 0) * m(0, 2))},
                {(m(1, 0) * m(2, 1) - m(2, 0) * m(1, 1)),
                 -(m(0, 0) * m(2, 1) - m(2, 0) * m(0, 1)),
                 (m(0, 0) * m(1, 1) - m(1, 0) * m(0, 1))}};
  return inv / det;
}

//------------------------------------------------------------------------------
/// Inverse of a 4x4 matrix.
/// Returns nothing if determinant is 0.
inline std::optional<mat<4, 4>> inverse(const mat4& m) {
  auto inv = mat<4, 4>::zeros();

  inv[0] = m[5] * m[10] * m[15] - m[5] * m[11] * m[14] - m[9] * m[6] * m[15] +
           m[9] * m[7] * m[14] + m[13] * m[6] * m[11] - m[13] * m[7] * m[10];

  inv[4] = -m[4] * m[10] * m[15] + m[4] * m[11] * m[14] + m[8] * m[6] * m[15] -
           m[8] * m[7] * m[14] - m[12] * m[6] * m[11] + m[12] * m[7] * m[10];

  inv[8] = m[4] * m[9] * m[15] - m[4] * m[11] * m[13] - m[8] * m[5] * m[15] +
           m[8] * m[7] * m[13] + m[12] * m[5] * m[11] - m[12] * m[7] * m[9];

  inv[12] = -m[4] * m[9] * m[14] + m[4] * m[10] * m[13] + m[8] * m[5] * m[14] -
            m[8] * m[6] * m[13] - m[12] * m[5] * m[10] + m[12] * m[6] * m[9];

  inv[1] = -m[1] * m[10] * m[15] + m[1] * m[11] * m[14] + m[9] * m[2] * m[15] -
           m[9] * m[3] * m[14] - m[13] * m[2] * m[11] + m[13] * m[3] * m[10];

  inv[5] = m[0] * m[10] * m[15] - m[0] * m[11] * m[14] - m[8] * m[2] * m[15] +
           m[8] * m[3] * m[14] + m[12] * m[2] * m[11] - m[12] * m[3] * m[10];

  inv[9] = -m[0] * m[9] * m[15] + m[0] * m[11] * m[13] + m[8] * m[1] * m[15] -
           m[8] * m[3] * m[13] - m[12] * m[1] * m[11] + m[12] * m[3] * m[9];

  inv[13] = m[0] * m[9] * m[14] - m[0] * m[10] * m[13] - m[8] * m[1] * m[14] +
            m[8] * m[2] * m[13] + m[12] * m[1] * m[10] - m[12] * m[2] * m[9];

  inv[2] = m[1] * m[6] * m[15] - m[1] * m[7] * m[14] - m[5] * m[2] * m[15] +
           m[5] * m[3] * m[14] + m[13] * m[2] * m[7] - m[13] * m[3] * m[6];

  inv[6] = -m[0] * m[6] * m[15] + m[0] * m[7] * m[14] + m[4] * m[2] * m[15] -
           m[4] * m[3] * m[14] - m[12] * m[2] * m[7] + m[12] * m[3] * m[6];

  inv[10] = m[0] * m[5] * m[15] - m[0] * m[7] * m[13] - m[4] * m[1] * m[15] +
            m[4] * m[3] * m[13] + m[12] * m[1] * m[7] - m[12] * m[3] * m[5];

  inv[14] = -m[0] * m[5] * m[14] + m[0] * m[6] * m[13] + m[4] * m[1] * m[14] -
            m[4] * m[2] * m[13] - m[12] * m[1] * m[6] + m[12] * m[2] * m[5];

  inv[3] = -m[1] * m[6] * m[11] + m[1] * m[7] * m[10] + m[5] * m[2] * m[11] -
           m[5] * m[3] * m[10] - m[9] * m[2] * m[7] + m[9] * m[3] * m[6];

  inv[7] = m[0] * m[6] * m[11] - m[0] * m[7] * m[10] - m[4] * m[2] * m[11] +
           m[4] * m[3] * m[10] + m[8] * m[2] * m[7] - m[8] * m[3] * m[6];

  inv[11] = -m[0] * m[5] * m[11] + m[0] * m[7] * m[9] + m[4] * m[1] * m[11] -
            m[4] * m[3] * m[9] - m[8] * m[1] * m[7] + m[8] * m[3] * m[5];

  inv[15] = m[0] * m[5] * m[10] - m[0] * m[6] * m[9] - m[4] * m[1] * m[10] +
            m[4] * m[2] * m[9] + m[8] * m[1] * m[6] - m[8] * m[2] * m[5];

  auto det = m[0] * inv[0] + m[1] * inv[4] +
             m[2] * inv[8] + m[3] * inv[12];
  if (std::abs(det) < 1e-7) return {};
  return inv / det;
}

//==============================================================================
// Printing
//==============================================================================
/// printing a matrix into a stream
template <size_t R, size_t C>
constexpr auto& operator<<(std::ostream& str, const mat<R, C>& m) {
  for (size_t row = 0; row != m.num_rows; ++row) {
    str << "[ ";
    for (size_t col = 0; col != m.num_columns; ++col) {
      str << m(row, col) << ' ';
    }
    str << "]\n";
  }
  return str;
}

//==============================================================================
}  // namespace cg
//==============================================================================
#endif
