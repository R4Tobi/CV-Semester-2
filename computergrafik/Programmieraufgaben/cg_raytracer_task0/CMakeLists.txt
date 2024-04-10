cmake_minimum_required(VERSION 3.10)
project(raytracer)
set(CMAKE_MODULE_PATH "${CMAKE_SOURCE_DIR}/cmake/Modules")
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_STANDARD 17)

option(COMPILE_AS_TASK "Compile code as for task number given" 0)

#-------------------------------------------------------------------------------
# copy assets
#-------------------------------------------------------------------------------
if (CMAKE_CXX_COMPILER_ID STREQUAL "MSVC")
  file(COPY ${CMAKE_SOURCE_DIR}/assets DESTINATION ${CMAKE_CURRENT_BINARY_DIR}/Debug)
  file(COPY ${CMAKE_SOURCE_DIR}/assets DESTINATION ${CMAKE_CURRENT_BINARY_DIR}/Release)
endif()
file(COPY ${CMAKE_SOURCE_DIR}/assets DESTINATION ${CMAKE_CURRENT_BINARY_DIR})

#-------------------------------------------------------------------------------
# Raytracer Library
#-------------------------------------------------------------------------------
add_subdirectory(src)

#-------------------------------------------------------------------------------
# Executables
#-------------------------------------------------------------------------------
add_subdirectory(executables)

#-------------------------------------------------------------------------------
# Documentation
#-------------------------------------------------------------------------------
option(CG_BUILD_DOC "Create CMAKE target doc" OFF)
if (CG_BUILD_DOC)
  add_subdirectory(doc)
endif()

#-------------------------------------------------------------------------------
# Tests
#-------------------------------------------------------------------------------
option(CG_BUILD_TESTS "Create CMAKE test targets" ON)
if (CG_BUILD_TESTS)
  add_subdirectory(test)
endif()

#-------------------------------------------------------------------------------
# For Windows to define M_PI and others
#-------------------------------------------------------------------------------
if (CMAKE_CXX_COMPILER_ID STREQUAL "MSVC")
  add_compile_definitions(_USE_MATH_DEFINES)
endif()

#-------------------------------------------------------------------------------
# color for ninja
#-------------------------------------------------------------------------------
if ("${CMAKE_GENERATOR}" STREQUAL "Ninja")
  if ("${CMAKE_CXX_COMPILER_ID}" STREQUAL "Clang")
    list(APPEND CMAKE_CXX_FLAGS "-fcolor-diagnostics")
  elseif ("${CMAKE_CXX_COMPILER_ID}" STREQUAL "GNU")
    list(APPEND CMAKE_CXX_FLAGS "-fdiagnostics-color=always")
  endif()
endif()
