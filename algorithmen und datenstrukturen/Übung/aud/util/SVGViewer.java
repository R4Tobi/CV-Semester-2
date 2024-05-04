package aud.util;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;

import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.JSVGScrollPane;

/** Simple SVG viewer based on
 *  <a href="http://xmlgraphics.apache.org/batik">Batik</a>'s
    <a href="http://xmlgraphics.apache.org/batik/using/swing.html#creatingJSVGCanvas">
    SVGCanvas</a>.

    <p>

    Usage:<p>

    <ul>

    <li>Shift+Mouse Left = pan</li>
    <li>Shift+Mouse Right (drag) = zoom in/out</li>
    <li>Ctrl+Mouse Left = rectangle zoom</li>
    <li>Ctrl+Mouse Right  (drag) = rotate <b>[disabled]</b></li>
    <li>Ctrl+I = zoom in</li>
    <li>Ctrl+O = zoom out</li>

    </ul>
    @see DotViewer
 */
public class SVGViewer {

  protected JFrame     frame;
  protected JSVGCanvas svgCanvas;
  protected JLabel     label;
  protected File       svgfile;

  /** create new instance */
  public SVGViewer(JFrame parent,File svgfile) {
    frame=parent;
    svgCanvas=new JSVGCanvas();
    //svgCanvas.setEnableRotateInteractor(false);

    label=new JLabel();
    parent.getContentPane().add(createComponents());

    if (svgfile==null) {
      try {
        svgfile=File.createTempFile("aud-svgviewer-",".svg");
      } catch (IOException e) {
        System.err.println("ERROR: "+e.getMessage());
        System.exit(-1);
      }
      svgfile.deleteOnExit();
    }
  }

  /** get parent widget */
  public JFrame parent() { return frame; }
  /** get status bar */
  public JLabel statusbar() { return label; }

  /** close viewer */
  public void close() {
    frame.setVisible(false);
    frame.dispose();
  }

  protected JComponent createComponents() {
    final JPanel panel = new JPanel(new BorderLayout());
    @SuppressWarnings("unused")
    final JSVGScrollPane pane = new JSVGScrollPane(svgCanvas);

    panel.add("Center", svgCanvas);
    panel.add("South", label);
    return panel;
  }

  /** display svg file  */
  public void display(File file) {
    svgfile=file;
    display();
  }
  /** display current svg file  */
  public void display() {
    svgCanvas.setURI(svgfile.toURI().toString());
  }

  /** print help (mouse/key bindings) to stdout */
  public static void help() {
    System.out.println
    ( "Batik JSVGCanvas overview\n"+
        "-------------------------\n"+
        "Shift+Mouse Left\tpan\n"+
        "Shift+Mouse Right\tzoom in/out (drag)\n"+
        "Ctrl+Mouse Left\trectangle zoom\n"+
        "Ctrl+Mouse Right\trotate [disabled!]\n"+
        "Ctrl+I\tzoom in\n"+
        "Ctrl+O\tzoom out\n"
    );
  }

  /** create new SVGViewer (toplevel window) and display {@code file} */
  public static SVGViewer displayWindow(File file,String caption) {
    JFrame f=new JFrame(caption!=null ? caption : "aud.util.SVGViewer");
    SVGViewer svg=new SVGViewer(f,file);
    f.setSize(800, 600);
    f.setVisible(true);
    if (file!=null)
      svg.display(file);
    return svg;
  }

  /** exit application if viewer is closed */
  public void setExitOnClose() {
    parent().addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
  }

  /** visualize given dot files (file names as command line arguments) */
  public static void main(String[] args) {
    if (args.length==0) {
      System.err.println("usage: java aud.util.SVGViewer file.svg\n");
      SVGViewer.help();
    }
    else
      for (String arg : args) {
        SVGViewer v=SVGViewer.displayWindow(new File(arg),arg);
        v.setExitOnClose();
      }
  }
}
