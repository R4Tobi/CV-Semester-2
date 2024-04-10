enable_testing()

add_executable(matrix_test matrix_test.cpp)
target_link_libraries(matrix_test cgraytracer)

if("${CMAKE_CXX_COMPILER_ID}" STREQUAL "GNU" OR
    "${CMAKE_CXX_COMPILER_ID}" STREQUAL "Clang")
    target_compile_options(matrix_test PUBLIC -Wall -Wextra)
endif()
add_test(matrix matrix_test)

add_executable(task1_test task1_test.cpp)
target_link_libraries(task1_test cgraytracer)
#add_test(task1 task1_test)

add_executable(task2_test task2_test.cpp)
target_link_libraries(task2_test cgraytracer)

add_executable(task3_test task3_test.cpp)
target_link_libraries(task3_test cgraytracer)
target_include_directories(task3_test PUBLIC ../executables)