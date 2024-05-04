#include "mat.h"
#include <cassert>
#include <iostream>
#include <utility>

//==============================================================================
namespace cg {
void test1();
void test2();
void test3();
void test4();
}

//==============================================================================
int main() {
  cg::test1();
  cg::test2();
  cg::test3();
  cg::test4();
}

namespace cg {
//==============================================================================
//! basic access and size checks
void test1() {
  // create a 3x2 matrix
  mat m{{1, 2}, {3, 4}, {5, 6}};
  std::cout << m;

  // check sizes
  assert(m.num_rows == 3);
  assert(m.num_columns == 2);

  // check correct access
  assert(m(0, 0) == 1);
  assert(m(0, 1) == 2);
  assert(m(1, 0) == 3);
  assert(m(1, 1) == 4);
  assert(m(2, 0) == 5);
  assert(m(2, 1) == 6);

  // check column major layout
  assert(m[0] == 1);
  assert(m[1] == 3);
  assert(m[2] == 5);
  assert(m[3] == 2);
  assert(m[4] == 4);
  assert(m[5] == 6);
}

//------------------------------------------------------------------------------
//! 4x4 inverse matrix
void test2() {
  mat m{{0.8909, 0.1493, 0.8143, 0.1966},
        {0.9593, 0.2575, 0.2435, 0.2511},
        {0.5472, 0.8407, 0.9293, 0.6160},
        {0.1386, 0.2543, 0.3500, 0.4733}};
  mat expected_inverse{
      {0.1027, 1.1506, -0.2856, -0.2814},
      {-1.4312, 0.5368, 1.9553, -2.2353},
      {1.4604, -1.4685, 0.2281, -0.1245},
      {-0.3411, 0.4605, -1.1355, 3.4883},
  };
  [[maybe_unused]] auto inv = *inverse(m);
  for (size_t i = 0; i < 16; ++i) {
    assert(std::abs(inv[i] - expected_inverse[i]) < 1e-3);
  }
}
//------------------------------------------------------------------------------
//! 3x3 inverse matrix
void test3() {
  mat  m{{0.3517, 0.5497, 0.7572},
        {0.8308, 0.9172, 0.7537},
        {0.5853, 0.2858, 0.3804}};
  mat  expected_inverse{{-1.2031, -0.0658, 2.5249},
                       {-1.1270, 2.7882, -3.2810},
                       {2.6976, -1.9937, 1.2094}};
  [[maybe_unused]] auto inv = *inverse(m);
  for (size_t i = 0; i < 9; ++i) {
    assert(std::abs(inv[i] - expected_inverse[i]) < 1e-2);
  }
}

//------------------------------------------------------------------------------
//! 2x2 inverse matrix
void test4() {
  mat m{{0.5678, 0.0540}, {0.0759, 0.5308}};
  mat expected_inverse{{1.7854, -0.1815}, {-0.2551, 1.9099}};

  [[maybe_unused]] auto inv = *inverse(m);
  for (size_t i = 0; i < 4; ++i) {
    assert(std::abs(inv[i] - expected_inverse[i]) < 1e-3);
  }
}
}  // namespace cg
