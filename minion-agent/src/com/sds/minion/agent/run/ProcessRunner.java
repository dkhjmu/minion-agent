package com.sds.minion.agent.run;

import java.io.File;
import java.io.IOException;

public class ProcessRunner {

  public static boolean isWindows() {
    String osName = System.getProperty("os.name");
    return osName.indexOf("Windows") > -1;
  }

  public String runProcess(String dir, String cmd) {
    if(cmd.indexOf("\n")>-1){
      String[] cmds = cmd.split("\n");
      String result = "";
      for(int i=0;i<cmds.length;i++){
        result += runProcess(dir, cmds[i], true)+"\n";
      }
      return result;
    }else{
      return runProcess(dir, cmd, false);
    }
  }

  public String runProcess(String dir, String cmd, boolean sync) {
    int exitCode = 1;
    String result = "";
    try {
      if (isWindows()) {
        ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C", cmd);
        if (dir != null && !"".equals(dir))
          pb.directory(new File(dir));
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process p = pb.start();
        if (sync) {
          exitCode = p.waitFor();
        }
      } else {
        Process pc = Runtime.getRuntime().exec(cmd);
        if (sync) {
          exitCode = pc.waitFor();
        }
      }
      System.out.println("cmd: " + cmd);
      System.out.println("exitCode: " + exitCode);
      result += "cmd:"+cmd;
      result += ",ExitCode:"+exitCode;
      Thread.sleep(5000);
    } catch (IOException e) {
      e.printStackTrace();
      return "ERROR:" + e.getMessage();
    } catch (InterruptedException e) {
      e.printStackTrace();
      return "ERROR:" + e.getMessage();
    }
    return "OK:"+result;
  }
}
