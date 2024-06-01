Visual Computing - C++ Raytracer
===

## Quick Overview over the Most Important Classes

See [here](@ref quick-overview-most-important-classes)

## Building

Compiles with
#### Linux
* clang version 10.0.0+
* gcc version 10.1.0+

#### Windows
* Visual Studio 2022
* Visual Studio Code with Visual Studio 2022 Compiler
* Visual Studio Code with mingw 1.5+

#### Mac
* clang version 10.0.0+

See also [Build Guide](@ref building)

## JPG output

If ImageMagick is installed the target `<executbable>.run` firstly creates a `<executable>.ppm` and afterwards uses ImageMagick to create `<executable>.jpg`

## Preview window

Set the CMake variable `CG_USE_PREVIEW_WINDOW` to `ON` to enable an OpenGL preview window.  This lets CMake download [GLFW](https://www.glfw.org/) in version 3.3.2 from GitHub and tries to compile it.  You need some packages to be able to build GLFW:

Some additional packages are required to build GLFW:

#### Ubuntu
* xorg-dev
* libxrandr

#### Arch
* libxrandr
