include(${CMAKE_CURRENT_LIST_DIR}/color/CMakeLists.txt)

set( MATERIAL_SOURCES
    ${COLOR_SOURCES}
    ${CMAKE_CURRENT_LIST_DIR}/constantcolormaterial.cpp
    ${CMAKE_CURRENT_LIST_DIR}/phong.cpp
)

set( MATERIAL_INCLUDE_DIR
    ${COLOR_INCLUDE_DIR}
    ${CMAKE_CURRENT_LIST_DIR}
)