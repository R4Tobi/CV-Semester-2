find_package(Sphinx QUIET)
find_package(Doxygen QUIET  
             OPTIONAL_COMPONENTS dot)

if (${Doxygen_FOUND})
  set(DOC_IMAGE_DIR ${CMAKE_CURRENT_BINARY_DIR}/images)
  file(MAKE_DIRECTORY ${DOC_IMAGE_DIR})
  file(COPY ${CMAKE_CURRENT_SOURCE_DIR}/images DESTINATION ${CMAKE_CURRENT_BINARY_DIR})
  set(DOXYGEN_HTML_DIR  ${CMAKE_CURRENT_BINARY_DIR}/html_doxygen)
  set(SRC_DOXYFILE      ${CMAKE_CURRENT_SOURCE_DIR}/Doxyfile.in )
  set(BUILD_DOXYFILE    ${CMAKE_CURRENT_BINARY_DIR}/Doxyfile    )
  if(${DOXYGEN_HAVE_DOT})
    set(DOXYGEN_DOT YES)
  else()
    set(DOXYGEN_DOT NO)
  endif()
  set(DOXYGEN_GENERATE_LATEX  "NO")
  set(DOXYGEN_GENERATE_HTML   "YES")
  if (${ImageMagick_FOUND})
    add_custom_target(doc.doxygen ALL
      COMMAND ${CMAKE_COMMAND} -E copy ../main.jpg images
      COMMAND doxygen ${BUILD_DOXYFILE}
      COMMENT "Building HTML documentation with Doxygen"
      WORKING_DIRECTORY ${CMAKE_CURRENT_BINARY_DIR}
      DEPENDS ../main.jpg
    )
  else()
    add_custom_target(doc.doxygen ALL
      COMMAND doxygen ${BUILD_DOXYFILE}
      COMMENT "Building HTML documentation with Doxygen"
      WORKING_DIRECTORY ${CMAKE_CURRENT_BINARY_DIR}
    )
  endif()
  set(DOXYGEN_GENERATE_XML "NO")
  configure_file(${SRC_DOXYFILE} ${BUILD_DOXYFILE})
  add_custom_target(doc ALL
    COMMENT "Building HTML documentation with doxygen"
    WORKING_DIRECTORY ${CMAKE_CURRENT_BINARY_DIR}
    DEPENDS doc.doxygen
  )
endif()
if(NOT ${Doxygen_FOUND})
  message(STATUS "Doxygen not found which is mandatory for building doc")
endif()
