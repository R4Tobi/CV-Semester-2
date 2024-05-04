package aud.util;

import aud.DList;

/** Simple demo to test use of GraphViz/Batik.
 */
class GraphVizDemo {

  public static void main(String[] args) {
    DList<Integer> list=new DList<Integer>();

    list.push_back(1);
    list.push_back(2);
    list.push_back(3);

    DotViewer.displayWindow(list,"DList").setExitOnClose();
  }
}
