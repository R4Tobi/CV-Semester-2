find_package(ImageMagick QUIET)
find_package(OpenMP)
#source files are in ARGN whiche are all parameters additionally give to the macro
macro(create_raytracer_executable EXECUTABLE_NAME)
  set(SOURCE_FILES ${ARGN})
  if (${CG_USE_PREVIEW_WINDOW} AND ${PREVIEW_WINDOW_POSSIBLE})
    list(APPEND SOURCE_FILES glwindow.cpp)
  endif()
  add_executable(${EXECUTABLE_NAME} ${EXECUTABLE_NAME}.cpp ${SOURCE_FILES})
  target_link_libraries(${EXECUTABLE_NAME} PUBLIC cgraytracer)
  add_custom_command(OUTPUT ${EXECUTABLE_NAME}.ppm
                     COMMAND ./${EXECUTABLE_NAME}
                     DEPENDS ${EXECUTABLE_NAME})

  if (${CG_USE_PREVIEW_WINDOW} AND ${PREVIEW_WINDOW_POSSIBLE})
    add_dependencies(${EXECUTABLE_NAME} glfw)
    set_target_properties(${EXECUTABLE_NAME} PROPERTIES COMPILE_DEFINITIONS CG_USE_PREVIEW_WINDOW=1)
    target_link_libraries(${EXECUTABLE_NAME} PUBLIC ${CMAKE_THREAD_LIBS_INIT} glfw)
    if (WIN32)
      target_link_libraries(${EXECUTABLE_NAME} PUBLIC opengl32)
    else()
      target_link_libraries(${EXECUTABLE_NAME} PUBLIC OpenGL)
    endif()
    if(CMAKE_DL_LIBS)
      target_link_libraries(${EXECUTABLE_NAME} PUBLIC ${CMAKE_DL_LIBS})
    endif()
  else()
    set_target_properties(${EXECUTABLE_NAME} PROPERTIES COMPILE_DEFINITIONS CG_USE_PREVIEW_WINDOW=0)
  endif()

  if (${ImageMagick_FOUND})
    add_custom_command(OUTPUT ${EXECUTABLE_NAME}.jpg
      COMMAND convert ${EXECUTABLE_NAME}.ppm ${EXECUTABLE_NAME}.jpg
      WORKING_DIRECTORY ${CMAKE_CURRENT_BINARY_DIR}
      DEPENDS ${EXECUTABLE_NAME}.ppm
    )
    add_custom_target(${EXECUTABLE_NAME}.run
      WORKING_DIRECTORY ${CMAKE_CURRENT_BINARY_DIR}
      DEPENDS ${EXECUTABLE_NAME}.jpg)
  elseif()
    message(STATUS "ImageMagick not found")
    add_custom_target(${EXECUTABLE_NAME}.run
      WORKING_DIRECTORY ${CMAKE_CURRENT_BINARY_DIR}
      DEPENDS ${EXECUTABLE_NAME}.ppm)
  endif()

  if ("${CMAKE_CXX_COMPILER_ID}" STREQUAL "GNU" OR
      "${CMAKE_CXX_COMPILER_ID}" STREQUAL "Clang")
    target_compile_options(${EXECUTABLE_NAME} PUBLIC -Wall -Wextra)
  endif()

  set_target_properties(${EXECUTABLE_NAME} PROPERTIES RUNTIME_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR})
endmacro()
