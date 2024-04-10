cmake_minimum_required(VERSION 3.10)
project(cg_cpp_intro
		LANGUAGES CXX)
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)

# ready made implementation
set(INCLUDE_DIR "${CMAKE_SOURCE_DIR}/include")
set(SRC_DIR "${CMAKE_SOURCE_DIR}/src")

add_executable(cpp_intro_full
  ${SRC_DIR}/main.cpp
  ${SRC_DIR}/vector2.cpp)

target_include_directories(cpp_intro_full PUBLIC
  ${INCLUDE_DIR}
)

add_executable(cpp_intro
  ${SRC_DIR}/main_temp.cpp
  ${SRC_DIR}/vector2_temp.cpp)
  
target_include_directories(cpp_intro PUBLIC
  ${INCLUDE_DIR}
)
