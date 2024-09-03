#include "phong.h"
#include "colors.h"

namespace cg{
  Phong phong_redblack{ blue, 0.0, blue, 1.0 };
  Phong phong_diffuse{ white, 0.0, black, 1.0 };
  Phong phong_mirror{ white, 1.0, white, 1.0 };
  }