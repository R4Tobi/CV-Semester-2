package aud.util;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

//
// Font size selection by Martin Haase.
//

/** Simple framework for single stepping code.<p>

    The {@code SingleStepper} widget consists of a text area that
    displays messages and a push button that triggers the next step.<p>

    The idea is to include calls to {@link #halt} in your code: this
    prints a message and waits for the user to push the button to
    continue execution.

    The following environment variables are recognized <a
    href="http://en.wikipedia.org/wiki/Environment_variable">
    envionment variables</a>.

    <table>
    <caption>environment variables</caption>

    <tr><td><b>environment variable</b></td>
    <td><b>purpose</b></td></tr>

    <tr><td><em>AUD_TIMEOUT</em></td>
    <td>set global timeout for {@link #halt} in milliseconds
    {@link #setTimeout}</td></tr>
    </table>

    {@link DotViewer} can be used to visualize the state of an
    algorithm that is run with {@code
    SingleStepper}. <code>GraphDemo</code> shows an example.

    @see DotViewer
    TODO: link <code>GraphDemo</code>
 */
public class SingleStepper {

  protected JFrame    frame;
  protected JTextArea history;
  protected JButton   next;
  protected Object    monitor = new Object();
  protected int       timeout = 0;

  /** create new instance */
  public SingleStepper(JFrame parent) {
    frame=parent;
    parent.getContentPane().add(createComponents());
    if (Sys.env("AUD_TIMEOUT")!=null) {
      try {
        timeout=Integer.parseInt(Sys.env("AUD_TIMEOUT"));
      } catch (NumberFormatException e) {}
    }
  }
  /** create new instance */
  public SingleStepper(String caption) {
    this(new JFrame(caption!=null ? caption : "aud.util.SingleStepper"));
    JFrame f=parent();

    // TODO: read geometry and font size from environment variable(s);
    //       and hide spinner (below) in case
    f.setSize(400,400);
    f.setVisible(true);

    // exit on close
    f.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
          System.exit(0);
        }
      });
  }

  /** get parent widget */
  public JFrame parent() { return frame; }


  protected JComponent createComponents() {
    final JSpinner spinner=new JSpinner();
    spinner.setValue(20);

    final JPanel panel=new JPanel(new BorderLayout());

    history=new JTextArea();
    history.setFont(new Font("",Font.BOLD,(int) spinner.getValue()));
    history.setEditable(false);
    history.setLineWrap(false);

    final JScrollPane historyPane = new JScrollPane(history);
    historyPane.setPreferredSize(new Dimension(400, 300));

    next=new JButton("continue");

    next.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent ae) {
          onNext();
        }
      });

    spinner.addChangeListener(new ChangeListener(){
        @Override
        public void stateChanged(ChangeEvent arg0) {
          // TODO Auto-generated method stub
          if ((int) spinner.getValue()<10)
            spinner.setValue(10);
          if ((int) spinner.getValue()>40)
            spinner.setValue(40);

          history.setFont(new Font("",Font.BOLD,(int)spinner.getValue()));
        }
      });

    panel.add("North",spinner);
    panel.add("Center", historyPane);
    panel.add("South", next);
    return panel;
  }

  /** call on button pressed */
  protected void onNext() {
    try {
      synchronized (monitor) {
        monitor.notify();
      }
    } catch (IllegalMonitorStateException e) {
      System.err.println("ERROR: "+e);
      System.exit(-1);
    }
  }

  /** display {@code text} and wait for user or {@code timeout} */
  public void halt(String text,int timeout) {
    println(text);
    try {
      onHalt();
      synchronized(monitor) {
        monitor.wait(timeout>=0 ? timeout : 0);
      }

    } catch (InterruptedException e) {
      System.err.println(e);
      history.append("--- interrupted ---\n");
    } catch (IllegalMonitorStateException e) {
      System.err.println("ERROR: "+e);
      System.exit(-1);
    }
  }

  /** print to both, text area and stdout */
  protected void println(String text) {
     history.append(text+"\n");
     System.out.println(text);
  }

  protected void onHalt() {}

  /** Set global timeout.
      Continue {@code halt} after {@code timeout} milliseconds.
      @param timeout in milliseconds
   */
  public void setTimeout(int timeout) {
    this.timeout=timeout;
  }

  /** print location of calling code */
  public SingleStepper whereAmI() {
    println(Sys.whereAmI(1));
    return this;
  }
  /** jmp to caller's location in editor (<em>emacs only</em>) */
  public SingleStepper showSource() {
    Sys.openCallersSourceInEmacs(1);
    return this;
  }

  /** display {@code text} and wait for user (or global timeout) */
  public void halt(String text) {
    halt(text,timeout);
  }

  /** wait for user */
  public void halt() { halt(Sys.whereAmI(1));  }

  public static void main(String[] args) {
    SingleStepper s=new SingleStepper("aud.util.SingleStepper");

    for (int i=0;i<4;++i) {
      System.out.println(i);
      s.halt("some message "+i);
    }
    s.halt("QUIT");
    System.exit(0);
  }

}
