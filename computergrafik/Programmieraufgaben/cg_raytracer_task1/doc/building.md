# Building the project {#building}

This section tells you how to build and execute the sources of the raytracer.

## Windows

In Windows you have different options for building the project. The easiest is probably using Visual Studio (Community Edition) because it is a complete IDE with compiler and support for importing CMake projects. Visual Studio is NOT the same as Visual Studio *Code*!

#### Visual Studio

Download Visual Studio 2022 (or later) Community Edition from
[here](https://visualstudio.microsoft.com/downloads/). **Make sure to select the "Desktop development with C++" option.** Under installation details, make sure the "C++ CMake tools for Windows" option is selected as well. See also [the install instructions](https://learn.microsoft.com/en-us/cpp/build/vscpp-step-0-installation?view=msvc-170).

If you already installed Visual Studio but didn't select the C++ option at the time, you can still install the components now.

Open Visual Studio. In the start screen, choose "Open folder" and navigate to the ray tracer code. Visual Studio will now open the folder and automatically configure the project based on the CMakeLists.txt provided in the folder. This may take some time.

You now want to add another build configuration. The standard build configuration is in "Debug" mode, however, running the raytracer in this configuration lengthens the runtime considerably.

In the toolbar left to a green arrow button, you should see a drop-down menu which currently says something similar to "x64-Debug". Click on that and choose "Manage Configurations". The CMake Settings should open. On the right side, click "Clone Configuration". Select the new Configuration. Change the new configuration name to "x64-Release" and change the configuration type to "Release". Safe the settings file.
See also [Microsoft Documentation](https://learn.microsoft.com/de-de/cpp/build/customize-cmake-settings?view=msvc-170).

Now you should be able to select new configuration in the drop-down menu in the toolbar. Do that.

You can now trigger the build and run the different executables by choosing the executable you want to run on the right side (green arrow).

#### Visual Studio Code
If you want to use Visual Studio Code instead, you need to install a compiler and CMake separately.

As a compiler, you may use the one bundled with Visual Studio (see above), [Mingw](https://www.mingw-w64.org/downloads/) or by using [Windows Subsystem for Linux](https://learn.microsoft.com/en-us/windows/wsl/install) (WSL). For Mingw, the recommended installation method would be via MSYS2. WSL is untested by us.

When installing CMake, note that you need both the *extension* for Visual Studio Code AND the software [CMake](https://cmake.org/) installed. Those are NOT the same thing!
When installing CMake, make sure to select the "Add CMake to the system path" option.

## Linux

#### Installing all needed tools

You will need a compiler like [GCC](https://gcc.gnu.org/) or
[Clang](https://clang.llvm.org/) and [CMake](https://cmake.org/).
Use your system's package manager to install the required programs:

Debian / Ubuntu
```bash
sudo apt install gcc cmake
```

Arch / Manjaro
```bash
sudo pacman -S gcc cmake
```

Fedora, Red Hat
```bash
sudo yum install gcc cmake
```

Mandriva
```bash
sudo urpmi install gcc cmake
```

#### Build using CMake GUI

CMake works either with a graphical user interface or in terminal. Using the GUI you first have to specify where the source is located and where to build the binaries. Typically the binaries go into a subdirectory of the source directory ``build/``.

![cmake_1](../images/cmake_1.jpg)

Next hit *Configure* to let CMake create its caching files.

![cmake_2](../images/cmake_2.jpg)

Press yes in the prompt to let CMake create the build directory if it does not yet exist. In the window CMakeSetup leave everything as is unless you know what you are doing and press *Finish*.

![cmake_3](../images/cmake_3.jpg)

Now you may configure the project. The variable ``CMAKE_BUILD_TYPE`` should be set to ``Release``. 

![cmake_4](../images/cmake_4.jpg)

This will significantly speed up execution of the compiled
program. Lastly press *Generate* to finally let CMake generate the actual build files.

![cmake_5](../images/cmake_5.jpg)

Now you have to open a terminal and follow the steps from the [Execute](#execute) section.

#### Build using Terminal

**TL;DR**
```bash
cd <path/to>/raytracer
mkdir build
cmake . -Bbuild -DCMAKE_BUILD_TYPE=Release
cd build
cmake --build . -- raytracer
./<executable name>
```

**Step by Step**

Once everything is installed open a terminal and navigate to project directory where the sources are located:

```bash
cd <path/to>/raytracer
```

Create a build directory by entering the command:
```bash
mkdir build
```

We will need CMake to setup the build process. By executing the following command CMake will automatically create a Makefile in your build directory.
The option ``-DCMAKE_BUILD_TYPE=Release`` tells CMake to configure the Makefile in such way that compiler optimizations are turned on so the program will take much less time in execution:

```bash
cmake . -Bbuild -DCMAKE_BUILD_TYPE=Release
```

Navigate to the build directory:
```bash
cd build
```

Now you have to open a terminal and follow the steps from the [Execute](#execute) section.

#### Build using Visual Studio Code

You can also use Visual Studio Code to build and run the raytracer code. For this you need to **additionally** install Visual Studio Code (NOT Visual Studio) and the CMake *extension*.

Choose a compiler (gcc or clang). Choose "Release" as a build variant.

TODO add rest

#### Execute

Now everything is set up and the actual build process can start. You can either let CMake execute your Makefile:

```bash
cmake --build . -- raytracer
```

Or directly execute the Makefile:
```bash
make raytracer
```

To start the compiled binary just type:
```bash
./raytracer
```

## Mac

On Mac, you will also need to install a compiler and CMake.

To check if a compiler is already installed, type `clang --version` in a terminal and hit enter. If you don't receive an output that states the version of the clang compiler, type `xcode-select --install` to install clang on your system.

You can check if Cmake is already installed by using the command `cmake --version` in the terminal. If it is not yet installed, the easiest method to install CMake is via homebrew (`brew install cmake`).

Now you can either follow the [Build using Terminal](#build-using-terminal) section from the Linux part, or install Visual Studio code and use that.