package aud.util;

import aud.DList;

/** Demonstrate use of SingleStepper
 */
public class SingleStepperDemo extends SingleStepper {

  protected DList<?>       list = null;
  protected DotViewer      v    = DotViewer.displayWindow((String) null,
                                                       "aud.util.GraphDemo");

  SingleStepperDemo(DList<?> list) {
    super("aud.util.GraphDemo");
    this.list=list;
  }

  protected void onHalt() {
    if (list!=null)
      v.display(list);
  }

  public static void main(String args[]) {

    DList<String> list=new DList<String>();
    SingleStepperDemo app=new SingleStepperDemo(list);

    list.push_back("b"); app.halt();
    list.push_back("d"); app.halt();
    list.push_back("e"); app.halt();

    list.push_front("a"); app.halt();
    list.insert(2,"c");   app.halt();

    app.halt("QUIT");
    System.exit(0);
  }

}
