#ifndef UTILITY_H
#define UTILITY_H
//==============================================================================
#include <array>
#include <utility>
#include <functional>
#include <type_traits>
//==============================================================================
namespace cg {
//==============================================================================
/// creates an array of size N filled with val of type T
template <typename T, size_t N, size_t... Is>
auto make_array(const T& val, std::index_sequence<Is...>) {
  return std::array{((void)Is, val)...};
}
//------------------------------------------------------------------------------
/// creates an array of size N filled with val of type T
template <typename T, size_t N>
auto make_array(const T& val) {
  return make_array<T, N>(val, std::make_index_sequence<N>{});
}
//------------------------------------------------------------------------------
/// Applies function F to all elements of parameter pack ts
template <typename F, typename... Ts>
void for_each(F&& f, Ts&&... ts) {
  using discard_t = int[];
  // creates an array filled with zeros. while doing this f gets called with
  // elements of ts
  (void)discard_t{0, ((void)f(std::forward<Ts>(ts)), 0)...};
}
//------------------------------------------------------------------------------
template <template <typename> typename Comparator, typename T0, typename T1,
          typename... TRest>
constexpr auto compare_variadic(T0&& a, T1&& b, TRest&&... rest)
    -> std::conditional_t<
        std::is_same_v<std::decay_t<T0>, std::decay_t<T1>> &&
            (std::is_same_v<std::decay_t<T0>, std::decay_t<TRest>> && ...) &&
            std::is_lvalue_reference_v<T0> && std::is_lvalue_reference_v<T1> &&
            std::is_lvalue_reference_v<T1> &&
            (std::is_lvalue_reference_v<TRest> && ...),
        // if all raw types are equal and l-value references
        std::conditional_t<
            // if at least one of the types is const
            std::is_const_v<std::remove_reference_t<T0>> ||
                std::is_const_v<std::remove_reference_t<T1>> ||
                (std::is_const_v<std::remove_reference_t<TRest>> || ...),
            // return const-ref
            std::decay_t<T0> const&,
            // else return non-const-ref
            std::decay_t<T0>&>,
        // else return copy of common type
        std::common_type_t<std::decay_t<T0>, std::decay_t<T1>,
                           std::decay_t<TRest>...>> {
  using common_t = std::common_type_t<std::decay_t<T0>, std::decay_t<T1>>;
  if constexpr (sizeof...(TRest) == 0) {
    return Comparator<common_t>{}(a, b) ? std::forward<T0>(a)
                                        : std::forward<T1>(b);
  } else {
    return compare_variadic<Comparator>(
        std::forward<T0>(a),
        compare_variadic<Comparator>(std::forward<T1>(b),
                                               std::forward<TRest>(rest)...));
  }
}
//------------------------------------------------------------------------------
template <typename T0, typename T1, typename... TRest>
constexpr auto max(T0&& a, T1&& b, TRest&&... rest) -> decltype(auto) {
  return compare_variadic<std::greater>(
      std::forward<T0>(a), std::forward<T1>(b), std::forward<TRest>(rest)...);
}
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
template <typename T0, typename T1, typename... TRest>
constexpr auto min(T0&& a, T1&& b, TRest&&... rest) -> decltype(auto) {
  return compare_variadic<std::less>(std::forward<T0>(a), std::forward<T1>(b),
                                     std::forward<TRest>(rest)...);
}
//==============================================================================
}  // namespace cg
//==============================================================================
#endif
