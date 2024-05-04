#define _USE_MATH_DEFINES
#include <cmath>
#include <cassert>
#include <iostream>
#include <iomanip>      // std::setprecision
#include <sstream>
#include <random>

#include "colors.h"
#include "phong.h"
#include "task3_materials.h"

#include "movable.h"
#include "ray.h"

#include "directionallight.h"
#include "pointlight.h"

namespace cg{
namespace test{
  void test_phong_brdf();
  void test_phong_shade();
  void test_materials();
  void test_refract();
}
}

int main(){
  std::string seperator1 = "============================\n";
  std::string seperator2 = "----------------------------\n";

  std::cout << seperator1 << "Test Task 3\n" << seperator1;
  cg::test::test_phong_brdf();
  std::cout << seperator2;
  cg::test::test_phong_shade();
  std::cout << seperator2;
  cg::test::test_materials();
  std::cout << seperator2;
  cg::test::test_refract();
  std::cout << seperator1;

  return 0;
}

namespace cg{
namespace test{
  bool log_fail(bool condition, const std::string& message){
    if(!condition){
      std::cout << message << std::endl;
    }
    return condition;
  }

  void print_status(bool status){
    if(status){
      std::cout << "PASS" << std::endl;
    }
    else{
      std::cout << "FAIL" << std::endl;
    }
  }

  void test_phong_brdf(){
    std::cout << "- Test Phong::brdf_phong -" << std::endl;
    bool test_status = true;

    Phong phong_d{white, 0.0, black, 9.0};
    Phong phong_s{black, 0.0, white, 9.0};
    Phong phong_c{{0.8, 0.7, 0.6}, 0.0, {0.9, 0.5, 0.8}, 3.0};

    std::vector<std::vector<vec3>> tests{
      {
        { -0.386639, -0.808321, -0.385844 },  // v
        { -0.293876, -0.550168, -0.781634 },  // l
        { 0.25891, 0.825857, 0.500924 },      // n
        { 0.954837, 0.954837, 0.954837 },     // result phong_s
        { 0.839804, 0.547903, 0.711124 }      // result phong_c
      },
      {
        { -0.995408, -0.1789, 0.128509 },
        { -0.585231, 0.807517, -0.0736307 },
        { 0.997214, 0.0553776, 0.0499716 },
        { 0.0314614, 0.0314614, 0.0314614 },
        { 0.442243, 0.327036, 0.357737 }
      },
      {
        { -0.166114, -0.592747, -0.65547 },
        { -0.164844, 0.43399, -0.885708 },
        { 0.235559, 0.771189, 0.591421 },
        { 1.33689e-11, 1.33689e-11, 1.33689e-11 },
        { 0.254789, 0.222895, 0.191111 }
      },
      {
        { -0.494815, -0.827985, -0.230557 },
        { -0.672079, -0.417092, -0.611837 },
        { 0.556004, 0.808891, 0.191195 },
        { 0.3121, 0.3121, 0.3121 },
        { 0.65773, 0.446751, 0.549281 }
      },
      {
        { -0.805004, -0.581002, 0.120019 },
        { -3.07832, -2, -3.69014 },
        { 0, 1, 0 },
        { 0, 0, 0 },
        { 0.254648, 0.222817, 0.190986 }
      },
      {
        { -0.700274, -0.125704, -0.70272 },
        { -4.37221, -1.10533, -7.38399 },
        { 0.837059, 0.19289, -0.511983 },
        { 0, 0, 0 },
        { 0.254648, 0.222817, 0.190986 }
      }
    };

    for(auto& test : tests){

      vec3 v = normalize(test[0]); 
      vec3 l = normalize(test[1]); 
      vec3 n = normalize(test[2]);

      std::cout << "V: " << v << "\nL: " << l << "\nN: " << n << std::endl;

      vec3 c_s = phong_s.brdf_phong(v, l, n,{0.0,0.0});
      std::stringstream message;
      message << "Phong Brdf specular is wrong. Is " << c_s 
        << " but should be " << test[3];
      test_status &= log_fail((c_s-test[3]).length() < 1e-6, 
      message.str());
      
      vec3 c_d = phong_d.brdf_phong(v, l, n,{0.0,0.0});
      message.str(std::string());
      message << "Phong Brdf diffuse is wrong. Is " << c_d 
        << " but should be " << vec3{0.31831, 0.31831, 0.31831};
      test_status &= log_fail((c_d-vec3{0.31831, 0.31831, 0.31831}).length() < 1e-6, 
      message.str());

      vec3 c_c = phong_c.brdf_phong(v, l, n,{0.0,0.0});
      message.str(std::string());
      message << "Phong Brdf for colored Phong is wrong. Is " << c_c 
        << " but should be " << test[4];
      test_status &= log_fail((c_c-test[4]).length() < 1e-6, 
      message.str());

      std::cout << std::endl;
    }

    print_status(test_status);
  }

  void test_phong_shade(){
    std::cout << "- Test Phong::shade -" << std::endl;
    bool test_status = true;

    Phong phong_a{vec3{0.9, 0.8, 0.7}, 0.0, vec3{0.5, 0.5, 0.8}, 10.0};

    std::vector<std::vector<vec3>> tests{
      {
        { -0.386639, -0.808321, -0.385844 },
        { -0.293876, -0.550168, -0.781634 },
        { 0.25891, 0.825857, 0.500924 },
        { 0.0285213, 0.0273473, 0.0369471 }
      },
      {
        { -0.995408, -0.1789, 0.128509 },
        { -0.585231, 0.807517, -0.0736307 },
        { 0.997214, 0.0553776, 0.0499716 },
        { 0.00645558, 0.00576477, 0.00521694 }
      },
      {
        { -0.166114, -0.592747, -0.65547 },
        { -0.164844, 0.43399, -0.885708 },
        { 0.235559, 0.771189, 0.591421 },
        { 0.00261233, 0.00232207, 0.00203181 }
      },
      {
        { -0.494815, -0.827985, -0.230557 },
        { -0.672079, -0.417092, -0.611837 },
        { 0.556004, 0.808891, 0.191195 },
        { 0.014144, 0.0130897, 0.0148287  }
      },
      {
        {-0.805004, -0.581002, 0.120019},
        {-0.591403, -0.384238, -0.708945},
        {0, 1, 0},
        {0.00440305, 0.00391382, 0.00342459}
      },
      {
        { -0.700274, -0.125704, -0.70272 },
        { -4.37221, -1.10533, -7.38399 },
        { 0.837059, 0.19289, -0.511983 },
        { 0.000122545, 0.000108929, 9.53125e-05 }
      }
    };

    for(auto& test : tests){
      vec3 v = normalize(test[0]); vec3 l = normalize(test[1]); vec3 n = normalize(test[2]);

      std::cout << "V : " << v << "\nL: " << l << "\nN: " << n << std::endl;

      PointLight pl{white};
      pl.translate(-5.0*l);

      Intersection i{
        nullptr,
        Ray{-10*v, v},
        10,
        vec3::zeros(),
        n,
        {0,0}
      };

      vec3 c = phong_a.shade(pl, i);

      std::stringstream message;
      message << "Phong shade is wrong. Is " << c 
        << " but should be " << test[3];
      test_status &= log_fail((c-test[3]).length() < 1e-6, 
      message.str());

      std::cout << std::endl;
    }

    print_status(test_status);
  }

  void test_materials(){
    std::cout << "- Test materials -" << std::endl;
    bool test_status = true;

    DirectionalLight dl_red{{0, -1, 0}, red};
    DirectionalLight dl_white{{0, -1, 0}, white};
    Intersection i{
      nullptr,
      Ray{{0,1,0}, {0,0.9,0.1}},
      1,
      vec3::zeros(),
      {0,1,0},
      {0,0}
    };

    vec3 c_r = phong_redblack.shade(dl_red, i);
    test_status &= log_fail(c_r.length() < 1e-6, 
      "Material phong_redblack is not black under red light");

    vec3 c_w = phong_redblack.shade(dl_white, i);
    test_status &= log_fail(c_w.length() > 1e-1, 
      "Material phong_redblack is not colored under white light");
    
    std::vector<std::vector<vec3>> tests{
      {
        { -0.386639, -0.808321, -0.385844 },
        { -0.293876, -0.550168, -0.781634 },
        { 0.25891, 0.825857, 0.500924 }
      },
      {
        { -0.995408, -0.1789, 0.128509 },
        { -0.585231, 0.807517, -0.0736307 },
        { 0.997214, 0.0553776, 0.0499716 }
      }
    };

    test_status &= log_fail(
      (phong_diffuse.brdf_phong(tests[0][0], tests[0][1], tests[0][2], {0,0}) -
       phong_diffuse.brdf_phong(tests[1][0], tests[1][1], tests[1][2], {0,0})).length() < 1e-6, 
      "Material phong_diffuse is not diffuse");

    test_status &= log_fail(
      (phong_mirror.sample_reflective_color(0,0)*phong_mirror.reflectance() + 
       phong_mirror.sample_albedo_color(0,0)*(1-phong_mirror.reflectance()) - vec3::ones()).length() < 1e-6, 
      "Material phong_mirror is no perfect mirror");

    print_status(test_status);
  }

  void test_refract(){
    std::cout << "- Test refraction -" << std::endl;
    bool test_status = true;

    vec3 n{0, 1, 0};
    
    std::vector<std::tuple<vec3, double, vec3>> tests ={
      {
        vec3{0, -1, 0},
        1.52,
        vec3{0, -1, 0}
      },
      {
        normalize(vec3{0.5, -0.5, 0.0}),
        1.52,
        vec3{0.4652018297, -0.8852046417, 0}
      },
      {
        normalize(vec3{0.5, -0.5, 0.0}),
        2.0,
        vec3{0.3535533906, -0.9354143467, 0}
      },
      {
        normalize(vec3{0.5, 0.5, 0.0}),
        1.52,
        vec3{0, 0, 0}
      },
      {
        normalize(vec3{0.9, -0.1, 0.0}),
        1.52,
        vec3{0.6538708781, -0.7566061557, 0}
      },
      {
        normalize(vec3{0.9, -0.1, 0.0}),
        1.0,
        vec3{0.9938837347, -0.1104315261, 0}
      },
      {
        normalize(vec3{0.1, 0.9, 0.0}),
        1.52,
        vec3{0.1678559196, 0.9858115389, 0}
      }
    };

    for(auto& test : tests){
      vec3 d = std::get<0>(test); double ior = std::get<1>(test);
      vec3 solution = std::get<2>(test);

      vec3 r = refract(d, n, ior).value_or(vec3::zeros());

      std::cout << "D: " << d << "\nN: " << n << "\nior: " << ior << "\n\n";

      std::stringstream message;
        message << "Refracted direction is wrong. Is " << r 
          << " but should be " << solution;
        test_status &= log_fail((r-solution).length() < 1e-6, 
        message.str());
    }

    print_status(test_status);
  }
}
}