# Quick Overview over the Most Important Classes {#quick-overview-most-important-classes}

> For reference to specific functions and classes, please do not rely solely on this short overview and read the documentation instead.

<h2>General Raytracer Logics</h2>

The [cg::Raytracer](@ref cg::Raytracer) renders a [cg::Scene](@ref cg::Scene) into a [cg::Texture](@ref cg::Texture) by recursively tracing a [cg::Ray](@ref cg::Ray) that is reflected and refracted at a surface. 

The initial rays are being created with help of a [cg::Camera](@ref cg::Camera) that can either be a [cg::OrthographicCamera](@ref cg::OrthographicCamera) or a [cg::PerspectiveCamera](@ref cg::PerspectiveCamera). Cameras have a virtual image plane with a specified resolution. Using the orthographic model the initial ray directions are parallel and go through each pixel of the camera's image plane. Using the perspective model rays have the same origin and shoot through each of the image
plane's pixels. 

Rays can intersect with [cg::Renderable](@ref cg::Renderable) objects of a [cg::Scene](@ref cg::Scene). For each ray that was casted through the camera's image plane, every single renderable object is checked for an intersection. If the ray hits an object at some point, this point is going to be shaded using the [cg::Material](@ref cg::Material) of the renderable object. A [cg::Material](@ref cg::Material) needs a [cg::ColorSource](@ref cg::ColorSource) to provide a base color (and additionally a reflected and refracted color). A [cg::ColorSource](@ref cg::ColorSource) can be a solid color, a pattern or a texture. For this reason it needs a uv coordinate to be sampled.

<h2>cg::Scene</h2>

The [cg::Scene](@ref cg::Scene) contains all objects that should be rendered ([m_renderables](@ref cg::Scene::m_renderables)) as well as all lights that illuminate the scene ([m_lights](@ref cg::Scene::m_light_sources)).

Its most important function is [shade_closest_intersection](@ref cg::Scene::shade_closest_intersection).
This function is used to determine the color that is transported along the given [cg::Ray](@ref cg::Ray) by calling it recursively whenever the ray is reflected or refracted until a maximum recursion depth is reached. The ray coming directly from the camera is the primary ray that determines the color of the pixel. This color is a combination of the color of the object the primary ray intersects with and the color of the secondary ray(s). The ratio of object color and other ray colors is determined by the material properties [reflectance](@ref cg::Material::reflectance) and [refractance](@ref cg::Material::refractance). You can see an illustration of this in the following images. 

![Raytracer Schematic](raytracer.png)
![Pixel Color Composition](pixel_color.png)

<h2>cg::Renderable</h2>

Every object that should appear on screen inherits from [cg::Renderable](@ref cg::Renderable), an abstract class that tells child classes to implement certain traits.

The raytracer already has some classes that are child classes of [cg::Renderable](@ref cg::Renderable), for instance primitive shapes like [cg::Box](@ref cg::Box), [cg::Plane](@ref cg::Plane), [cg::Sphere](@ref cg::Sphere), [cg::Triangle](@ref cg::Triangle), but also the triangle mesh [cg::Mesh](@ref cg::Mesh).

The renderable holds a material and ensures access to its properties, for instance
- [sample_albedo_color](@ref cg::Renderable::sample_albedo_color): get the albedo (diffuse) color of the object at the given uv coordinates, this would be \f$\rho_{diffuse}\f$ in the Phong shading model
- [sample_reflective_color](@ref cg::Renderable::sample_reflective_color): get the color this object reflects at the given uv coordinates, this would be \f$\rho_{specular}\f$ in the Phong shading model
- [sample_refractive_color](@ref cg::Renderable::sample_refractive_color) get the color this object refracts at the given uv coordinates
- [reflectance](@ref cg::Renderable::reflectance): get the ratio of how much light is reflected from this material (i.e. not used for diffuse lighting or refracted)
- [refractance](@ref cg::Renderable::refractance): get the ratio of how much light is refracted from this material (i.e. not used for diffuse lighting or reflected)
- [index_of_refraction](@ref cg::Renderable::index_of_refraction): get the index of refraction for this material

> For reference, it is advised to look at the corresponding functions in [cg::Material](@ref cg::Material) as well!

<h3>cg::Intersectable</h3>

One trait of [cg::Renderable](@ref cg::Renderable) is that it is [cg::Intersectable](@ref cg::Intersectable), i.e. its children implement the function [check_intersection](@ref cg::Intersectable::check_intersection). With it we can check if a given ray intersects the object and if so, where. The return value is an `std::optional<cg::Intersection>`, so it can either be an empty optional (no intersection) or hold a value of type [cg::Intersection](@ref cg::Intersection).

<h2>cg::Light</h2>

Lights are needed so the scene composed of [cg::Renderable](@ref cg::Renderable) is actually visible to the camera. The class [cg::Light](@ref cg::Light) itself is an abstract class, meaning we need to use its child classes ([cg::PointLight](@ref cg::PointLight), [cg::SpotLight](@ref cg::SpotLight), [cg::DirectionalLight](@ref cg::DirectionalLight)) instead. However, they only differ in their implementation details.

> The light class provides different functions and it is advised to use those over manually calculating them whenever possible.
> For instance, while [light_direction_to](cg::Light::light_direction_to) is the same as `pos - light_pos` for the point light, the latter can not be used for the directional light (because the position of the directional light has no meaning in terms of light direction). Using [light_direction_to](cg::Light::light_direction_to) will avoid this error.

<h2>The Vector Class cg::vec</h2>

One important utility class is [cg::vec<N>](@ref cg::vec), that implements N-dimensional vectors and their operations. For convenience, special aliases are provided for:
- 2D vectors: [cg::vec2](@ref cg::vec2)
- 3D vectors: [cg::vec3](@ref cg::vec3)
- RGB color:  [cg::color](@ref cg::color)
- 4D vectors: [cg::vec4](@ref cg::vec4)

The class also implements basic vector operations. See the example below. 
```cpp
// zero vector
vec3 a = vec3::zeros();
// all ones vector
vec3 b = vec3::ones();

// constructor vector/point
vec3 c(1.0, 2.0, 3.0);
// random vector/point
vec3 r = vec3::randu();

// accessing components
double r_third = r(2);
r_third = r.at(2);

// addition
vec3 d = a + b;
d += b;
// subtraction
d = a - b;
d -= a;

// multiplication (component-wise vector-vector)
d = b * c;
d *= c;
// multiplication (component-wise scalar-vector)
d = 2.0 * b;
d *= 4.0;

// division (component-wise vector-vector)
d = b / c;
d /= b;
// division (component-wise scalar-vector)
d = c / 3.0;
d /= 1.0;

// scalar product / dot product
double sc_prod = dot(b, d);

// cross product (only vec3)
d = cross(c, b);

// squared distance
double sq_dist_a_c = squared_distance(a, c);
// distance
double dist_a_c = distance(a, c);

// vector squared length
double sq_length = c.squared_length();
// vector length
double l = c.length();
// normalization to unit length
vec3 c_normalized = normalize(c);
```

<h2>The Ray Class cg::Ray</h2>

Another important utility class you will find all over the raytracer is the ray class. It consists just of a ray origin and a direction plus an integer that holds the "degree" of the ray (i.e. how often it was reflected/refracted). Note that the direction will be normalized on construction, so the direction should always be a unit length vector.

The class also implements two functions for reflecting ([reflect](@ref cg::Ray::reflect)) and refracting ([refract](@ref cg::Ray::refract)) the ray on a surface. One should use these functions instead of constructing a new ray because it automatically takes care of the right "degree".