/** Graph data structures and algorithms.

    <strong>Important note:</strong>

    Some algorithm depend on storing elements of a graph such as nodes
    in a {@code HashMap}. These are in particular all algorithms using
    a {@code PriorityQueue}.

    The current ("default") implementation relies on the default
    implementations of {@code hashCode} and {@code equals}, which use
    memory locations.

    If you redefine any of these functions, you must ensure that they
    do <strong>not</strong> depend on the current state of the node
    (such as color) but solely on its identity!
*/
package aud.graph;
