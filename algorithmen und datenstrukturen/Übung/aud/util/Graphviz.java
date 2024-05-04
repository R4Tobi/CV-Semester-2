package aud.util;

import java.io.*;

/** Use GraphViz to render graph structures.<p>

    Requires an installation of <a
    href="http://www.graphviz.org/">GraphViz</a>.

    @see Sys
    @see Sys.ExternalProgram
    @see Graphvizable
    @see DotViewer
 */
public class Graphviz {

  static final Sys.ExternalProgram DOT =
    new Sys.ExternalProgram("dot (GraphViz graph layout and rendering)",
                            "AUD_DOT",
                            "/usr/bin/dot"+       // standard Unix installation
                            File.pathSeparator+
                            "/usr/local/bin/dot"+ // OS X
                            File.pathSeparator+
                            "/opt/homebrew/bin/dot"+ // macOS Homebrew
                            File.pathSeparator+
                            "/local/usr/bin/dot",  // Sun pool
                            true
                            );

  /** Render dot file.<p>

      Excutes <a
      href="http://www.graphviz.org/content/command-line-invocation">GraphViz
      dot</a> and writes an output file {@code ditfile+"."+format}.
   */
  public File renderDotFileToFile(File dotfile,String format) {
    File out=new File(dotfile.getPath()+"."+format);
    String command=DOT.getPath()+" -T"+format+" "+dotfile.getPath()+" -o "+out;
    //System.err.println(command);
    // note: command is obsolete (used only in error message)
    try {
      ProcessBuilder pb = new ProcessBuilder
        (DOT.getPath(),"-T" + format,dotfile.getPath(),"-o",out.getPath());
      Process dot=pb.start();

      if (dot.waitFor()!=0) {
        System.err.println("ERROR: '"+command+"' failed");
      }
    }
    catch (IOException e) {
      System.err.println("ERROR: "+e.getMessage());
      return null; // note: probably triggers NullPointerException
    }
    catch (InterruptedException e) {
      System.err.println("ERROR: "+e.getMessage());
      return null; // note: probably triggers NullPointerException
    }
    return out;
  }

  /** Render and display {@code dotCode}.<p>
      \bug The generated PDF file is not removed! (There is no
      portable and trivial way to ensure removal.)
   */
  public void displayAsPDF(String dotCode) {
    File dotfile=Sys.writeToTempFile(dotCode,".dot");
    File pdffile=renderDotFileToFile(dotfile,"pdf");
    //dotfile.delete();
    Sys.viewPDFFile(pdffile.getPath());
  }

  /** Display {@code object}.
      @see #displayAsPDF
   */
  public void displayAsPDF(Graphvizable object) {
    displayAsPDF(object.toDot());
  }
  /** Render and display {@code dotCode}.
      @return viewer instance
  */
  public DotViewer display(String dotCode) {
    return DotViewer.displayWindow(dotCode,"aud.util.DotViewer");
  }
  /** Display {@code object}.
      @return viewer instance
  */
  public DotViewer display(Graphvizable object) {
    return DotViewer.displayWindow(object,"aud.util.DotViewer");
  }
}
