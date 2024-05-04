package aud.util;

import java.util.Map;
import java.io.*;

/** System related utilities.<p>

    Some functions require external tools. The paths of the tools can
    be set as <a
    href="http://en.wikipedia.org/wiki/Environment_variable">
    envionment variables</a>.

    <table>
    <caption>Required GraphViz binaries</caption>

    <tr><td><b>tool</b></td>
    <td><b>environment variable</b></td>
    <td><b>default path (colon-separated list)</b></td>
    <td><b>purpose</b></td></tr>

    <tr><td>PDF viewer</td>
    <td>AUD_PDFVIEWER</td>
    <td>/usr/bin/evince</td>
    <td>view PDF files</td></tr>

    <tr><td>GraphViz dot</td>
    <td>AUD_DOT</td>
    <td>/usr/bin/dot:/usr/local/bin/dot:/local/usr/bin/dot:/opt/homebrew/bin</td>
    <td>layout and render graphs</td></tr>

    <tr><td>Emacs</td>
    <td><em>AUD_EMACS</em></td>
    <td>/usr/bin/emacs</td>
    <td>editor (Un*x only)</td></tr>

    <tr><td>Emacs client</td>
    <td><em>AUD_EMACSCLIENT</em></td>
    <td>/usr/bin/emacsclient</td>
    <td>editor (Un*x only)</td></tr>

    </table>

    <b>Notes</b>

    <ul>

    <li>This is not "production code"! There are tests missing, and
    not all potential errors are handled. <em>In this sense, this is
    "bad" code!</em></li>

    <li>In particular, some functions just return {@code null} on
    error and use the fact that Java checks references and would raise
    a {@code NullPointerException} if return values are not
    checked. -- <em>Don't try this in C++! Never!</em></li>

    </ul>
 */
public class Sys {

  static Map<String,String> env_ = null;

  /** Get path of an external program<p>
      Configuration via
      <a href="http://en.wikipedia.org/wiki/Environment_variable">
      envionment variables</a> possible. Throw {@code RuntimeException}
      on access if program is not available.
      @see Sys
   */
  static class ExternalProgram {
    String  description = null;
    String  envvar      = null;
    String  guess       = null;
    File    program     = null;

    ExternalProgram(String description,String envvar,String guess,
                    boolean warn) {
      this.description=description;
      this.envvar=envvar;
      this.guess=guess;

      aud.Vector<String> paths=new aud.Vector<String>();

      if (envvar!=null)
        paths.push_back(env(envvar));
      for (String p : guess.split(File.pathSeparator))
        paths.push_back(p);
      // for (String p : paths)
      //   System.err.println(p);
      for (String p : paths)
        if ((program=getProgram(p))!=null)
          break;

      // Search on windows
      if (program==null &&
          System.getProperty("os.name").startsWith("Windows")) {
        program = searchOnWindows();
      }

      if (warn && program==null) {
        System.err.println("Could not find program '"+description+"'");
        System.err.println("Tried at paths");
        for (String p : paths)
          if (p!=null)
            System.err.println("  '"+p+"'");
        System.err.println("\n\n");
      }
    }
    // provided by Kilian Gaertner
    private File searchOnWindows() {
      String programDir1 = System.getenv("ProgramFiles(X86)");
      String programDir2 = System.getenv("ProgramFiles");
      // Windows X 64 bit
      if (programDir1 != null) {
        File programDir = new File(programDir1);
        for (File subDir : programDir.listFiles()) {
          if (subDir.getName().startsWith("Graphviz")) {
            return new File(subDir, "bin"+File.separator+"dot.exe");
          }
        }
      }
      // Windows X 32 bit
      File programDir = new File(programDir2);
      for (File subDir : programDir.listFiles()) {
        if (subDir.getName().startsWith("Graphviz")) {
          return new File(subDir, "bin"+File.separator+"dot.exe");
        }
      }
      return null;
    }

    /** helper: test if program exists at path */
    File getProgram(String path) {
      if (path!=null) {
        File prog=new File(path);
        if (prog.exists())
          return prog;
      }
      return null;
    }

    /** return {@code false} if program was not found */
    public boolean isAvailable() { return program!=null; }
    /** query path
	      @throws RuntimeException
     */
    public String getPath() {
      if (program==null) {
        String message="ERROR:\nExternal program '"+description+"' not found.\n";
        if (guess!=null)
          message+="The standard path is '"+guess+"'.\n";
        if (envvar!=null) {
          message+="Set environment variable '"+envvar+"' to customize path.";
          if (env(envvar)!=null)
            message+="\n(The current value is '"+envvar+"="+env(envvar)+"'.).";
        }
        message+="\n\n";

        throw new RuntimeException(message);
      }
      return program.getPath();
    }
  }

  static final ExternalProgram PDFVIEWER =
    new ExternalProgram("PDF viewer","AUD_PDFVIEWER","/usr/bin/evince",false);
  static final ExternalProgram EMACS =
    new ExternalProgram("emacs editor","AUD_EMACS","/usr/bin/emacs",false);
  static final ExternalProgram EMACSCLIENT =
    new ExternalProgram("emacs-client editor",
                        "AUD_EMACSCLIENT","/usr/bin/emacsclient",false);

  /** Get environment variable {@code varname}.
      @param varname variable name
      @return value or {@code null} if undefined
   */
  public static synchronized String env(String varname) {
    if (env_==null)
      env_=System.getenv();
    return env_.get(varname);
  }

  /** get indentation string filled with spaces */
  public static String indent(int level) {
    String spaces="";
    for (int i=0;i<level;++i) spaces+="  ";
    return spaces;
  }

  /** write {@code text} to {@code file}
      @return file
   */
  public static File writeToFile(File file,String text) {
    try {
      FileOutputStream f=new FileOutputStream(file);
      f.write(text.getBytes());
      f.close();
    } catch (IOException e) {
      System.err.println("ERROR: "+e.getMessage());
      return null; // note: probably triggers NullPointerException
    }
    return file;
  }
  /** write {@code text} to temporary file
      @return file
   */
  public static File writeToTempFile(String text,String suffix) {
    try {
      return writeToFile(File.createTempFile("aud-",suffix),text);
    } catch (IOException e) {
      System.err.println("ERROR: "+e.getMessage());
      return null; // note: probably triggers NullPointerException
    }
  }

  /** read entire file and return contents as {@code String} */
  public static String readFile(File file) {
    byte[] buffer = new byte[(int) file.length()];
    BufferedInputStream f = null;
    try {
      f = new BufferedInputStream(new FileInputStream(file));
      f.read(buffer);
    } catch (IOException e) {
      System.err.println("ERROR: "+e.getMessage());
      return null; // note: probably triggers NullPointerException
    } finally {
      if (f != null) try { f.close(); } catch (IOException ignored) { }
    }
    return new String(buffer);
  }

  /** Execute {@code command} in a new process and <em>detach</em>.<p>
      Java seems unable to "really" detach processes, so we
      start the process within a new Java thread.
      @param command same as for {@code Runtime.exec}
   */
  //@SuppressWarnings( "deprecation" )
  public static void execAndDetach(String command) {
    Thread t=new Thread(new Runnable()
    {
      public void run() {
        try {
          String cmd=Thread.currentThread().getName();
          //System.err.println(cmd);
          //Runtime.getRuntime().exec(cmd); // fix deprecation (since Java 18)
          new ProcessBuilder(cmd.split(" ")).start();
        } catch(IOException e) {
          e.printStackTrace();
        }
      }
    },command);
    t.start();
  }

  /** open PDF viewer */
  public static void viewPDFFile(String filename) {
    Sys.execAndDetach(PDFVIEWER.getPath()+" "+filename);
  }

  /** get code location (like {@code __FILE__,__LINE__} */
  public static String whereAmI(int depth) {
    StackTraceElement location=new Throwable().getStackTrace()[depth+1];
    String rv=location.getFileName().toString()+":";
    rv+=location.getLineNumber()+" ";
    rv+=location.getClassName()+"#"+location.getMethodName();
    return rv;
  }
  /** get code location (like {@code __FILE__,__LINE__} */
  public static String whereAmI() {
    return whereAmI(0);
  }

  /** open emacs client <em>(Un*x only)</em> (or no action otherwise) */
  public static void emacsclient(String file,int line,int column) {
    if (EMACS.isAvailable() && EMACSCLIENT.isAvailable()) {
      String command=
        EMACSCLIENT.getPath()+" -a "+EMACS.getPath()+" -n +"+line+
        (column>0 ? ":"+column+" " : " ")+file;
      System.err.println(command);
      execAndDetach(command);
    }
  }

  /** open emacs {@link #whereAmI} <em>(Un*x only)</em> */
  public static void openCallersSourceInEmacs(int depth) {
    StackTraceElement location=new Throwable().getStackTrace()[depth+1];
    File file=new File(location.getFileName().toString());
    if (file.exists())
      emacsclient(file.getPath(),location.getLineNumber(),0);
    else
      System.err.println("unknown path for source file '"+file+"'");
  }

  //
  // Same for gedit !
  //
}
