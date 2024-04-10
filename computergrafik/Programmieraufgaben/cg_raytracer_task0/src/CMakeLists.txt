find_package(OpenMP)

include(${CMAKE_CURRENT_LIST_DIR}/acceleration/CMakeLists.txt)
include(${CMAKE_CURRENT_LIST_DIR}/camera/CMakeLists.txt)
include(${CMAKE_CURRENT_LIST_DIR}/geometry/CMakeLists.txt)
include(${CMAKE_CURRENT_LIST_DIR}/interface/CMakeLists.txt)
include(${CMAKE_CURRENT_LIST_DIR}/light/CMakeLists.txt)
include(${CMAKE_CURRENT_LIST_DIR}/material/CMakeLists.txt)
include(${CMAKE_CURRENT_LIST_DIR}/utility/CMakeLists.txt)

set(RAYTRACER_SOURCES
    ${ACCELERATION_SOURCES}
    ${CAMERA_SOURCES}
    ${GEOMETRY_SOURCES}
    ${INTERFACE_SOURCES}
    ${LIGHT_SOURCES}
    ${MATERIAL_SOURCES}
    ${UTILITY_SOURCES}
    "ray.cpp"
    "raytracer.cpp"
    "scene.cpp"
)

set(RAYTRACER_INCLUDE_DIR
    ${ACCELERATION_INCLUDE_DIR}
    ${CAMERA_INCLUDE_DIR}
    ${GEOMETRY_INCLUDE_DIR}
    ${INTERFACE_INCLUDE_DIR}
    ${LIGHT_INCLUDE_DIR}
    ${MATERIAL_INCLUDE_DIR}
    ${UTILITY_INCLUDE_DIR}
    ${CMAKE_CURRENT_SOURCE_DIR}
)

add_library(cgraytracer ${RAYTRACER_SOURCES})
target_include_directories(cgraytracer PUBLIC ${RAYTRACER_INCLUDE_DIR})

# compile the raytracer in the unsolved task version
if(${COMPILE_AS_TASK} GREATER -1)
    set(task_nums 5 4 3 2 1 0 CACHE INTERNAL "")
    list(FIND task_nums ${COMPILE_AS_TASK} end)
    MATH(EXPR end "${end}+1")
    list(SUBLIST task_nums 0 ${end} task_nums)
    list(TRANSFORM task_nums PREPEND "TASK_")
    target_compile_definitions(cgraytracer PUBLIC ${task_nums})
endif()

if (${OpenMP_FOUND})
    target_link_libraries(cgraytracer PUBLIC OpenMP::OpenMP_CXX)
endif()