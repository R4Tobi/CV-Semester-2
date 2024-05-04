package aud.util;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.FileSystems;

import javax.swing.*;

import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.JSVGScrollPane;
import org.apache.batik.util.XMLResourceDescriptor;
//import org.apache.batik.dom.svg.SAXSVGDocumentFactory; // Batik 1.7
import org.apache.batik.anim.dom.SAXSVGDocumentFactory; // Batik 1.8
import org.apache.batik.dom.svg.*;
import org.apache.batik.anim.dom.*;
import org.w3c.dom.svg.SVGDocument;

/** Simple viewer for {@link Graphvizable}.<p>
    Based on <a href="http://xmlgraphics.apache.org/batik">Batik</a>'s
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

    {@code DotViewer} can be used with {@link SingleStepper} to
    visualize progress of an algorithm. <code>GraphDemo</code> shows an
    example.

    @see SingleStepper

    TODO: link <code>GraphDemo</code>
 */
public class DotViewer {

  protected File       dotfile;
  protected JFrame     frame;
  protected JSVGCanvas svgCanvas;
  protected Graphviz   graphviz;
  protected JLabel     label;

  /** create new instance */
  public DotViewer(JFrame parent) {
    frame=parent;
    svgCanvas=new JSVGCanvas();
    svgCanvas.setEnableRotateInteractor(false);//

    label=new JLabel();
    parent.getContentPane().add(createComponents());

    try {
      dotfile=File.createTempFile("aud-dotviewer-",".dot");
    } catch (IOException e) {
      System.err.println("ERROR: "+e.getMessage());
      System.exit(-1);
    }
    //System.err.println(dotfile);
    graphviz=new Graphviz();
    dotfile.deleteOnExit();
  }

  /** get parent widget */
  public JFrame parent() { return frame; }
  /** get status bar */
  public JLabel statusbar() { return label; }

  protected JComponent createComponents() {
    final JPanel panel = new JPanel(new BorderLayout());
    @SuppressWarnings("unused")
    final JSVGScrollPane pane = new JSVGScrollPane(svgCanvas);

    panel.add("Center", svgCanvas);
    panel.add("South", label);
    return panel;
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

  /** display dot code
      @see Graphviz
   */
  public void display(String code) {
    Sys.writeToFile(dotfile,code);
    //System.err.println(dotfile);
    File svgfile=graphviz.renderDotFileToFile(dotfile,"svg");
    //svgfile.deleteOnExit();
    //svgCanvas.setURI(svgfile.toURI().toString());

    Path path=FileSystems.getDefault().getPath(svgfile.getPath());

    try {
      String data=new String(Files.readAllBytes(path));
      data=data.replaceAll("stroke=\"transparent\"","stroke-opacity=\"0\"");

      String parser=XMLResourceDescriptor.getXMLParserClassName();
      SAXSVGDocumentFactory factory=new SAXSVGDocumentFactory(parser);
      SVGDocument document=
          factory.createSVGDocument("",new ByteArrayInputStream(data.getBytes("UTF-8")));
      svgCanvas.setDocument(document);
    } catch (IOException e) {
      System.err.println(e);
      System.exit(-1);
    }

    svgfile.delete();
  }

  /** display {@code object} */
  public void display(Graphvizable object) {
    display(object.toDot());
  }

  /** create new DotViewer (toplevel window) and display {@code object} */
  public static DotViewer displayWindow(Graphvizable object,String caption) {
    return displayWindow(object.toDot(),caption);
  }
  /** create new DotViewer (toplevel window) and display {@code code} */
  public static DotViewer displayWindow(String code,String caption) {
    JFrame f=new JFrame(caption!=null ? caption : "aud.util.DotViewer");
    DotViewer dot=new DotViewer(f);
    f.setSize(800, 600);
    f.setVisible(true);
    if (code!=null)
      dot.display(code);
    return dot;
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
      System.err.println("usage: java aud.util.DotViewer file.dot\n");
      DotViewer.help();
    }
    else
      for (String arg : args) {
        String dotCode=Sys.readFile(new File(arg));
        DotViewer v=DotViewer.displayWindow(dotCode,arg);
        v.setExitOnClose();
      }
  }
}
