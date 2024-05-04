# Distributed under the OSI-approved BSD 3-Clause License.  See accompanying
# file Copyright.txt or https://cmake.org/licensing for details.

cmake_minimum_required(VERSION 3.5)

file(MAKE_DIRECTORY
  "C:/Users/tobia/Documents/GitHub/CV-Semester-2/computergrafik/Programmieraufgaben/cg_raytracer_task1/cmake-build-debug/_deps/glfw-src"
  "C:/Users/tobia/Documents/GitHub/CV-Semester-2/computergrafik/Programmieraufgaben/cg_raytracer_task1/cmake-build-debug/_deps/glfw-build"
  "C:/Users/tobia/Documents/GitHub/CV-Semester-2/computergrafik/Programmieraufgaben/cg_raytracer_task1/cmake-build-debug/_deps/glfw-subbuild/glfw-populate-prefix"
  "C:/Users/tobia/Documents/GitHub/CV-Semester-2/computergrafik/Programmieraufgaben/cg_raytracer_task1/cmake-build-debug/_deps/glfw-subbuild/glfw-populate-prefix/tmp"
  "C:/Users/tobia/Documents/GitHub/CV-Semester-2/computergrafik/Programmieraufgaben/cg_raytracer_task1/cmake-build-debug/_deps/glfw-subbuild/glfw-populate-prefix/src/glfw-populate-stamp"
  "C:/Users/tobia/Documents/GitHub/CV-Semester-2/computergrafik/Programmieraufgaben/cg_raytracer_task1/cmake-build-debug/_deps/glfw-subbuild/glfw-populate-prefix/src"
  "C:/Users/tobia/Documents/GitHub/CV-Semester-2/computergrafik/Programmieraufgaben/cg_raytracer_task1/cmake-build-debug/_deps/glfw-subbuild/glfw-populate-prefix/src/glfw-populate-stamp"
)

set(configSubDirs )
foreach(subDir IN LISTS configSubDirs)
    file(MAKE_DIRECTORY "C:/Users/tobia/Documents/GitHub/CV-Semester-2/computergrafik/Programmieraufgaben/cg_raytracer_task1/cmake-build-debug/_deps/glfw-subbuild/glfw-populate-prefix/src/glfw-populate-stamp/${subDir}")
endforeach()
if(cfgdir)
  file(MAKE_DIRECTORY "C:/Users/tobia/Documents/GitHub/CV-Semester-2/computergrafik/Programmieraufgaben/cg_raytracer_task1/cmake-build-debug/_deps/glfw-subbuild/glfw-populate-prefix/src/glfw-populate-stamp${cfgdir}") # cfgdir has leading slash
endif()
