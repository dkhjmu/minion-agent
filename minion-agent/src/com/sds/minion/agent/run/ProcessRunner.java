package com.sds.minion.agent.run;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class ProcessRunner {

	public static boolean isWindows() {
		String osName = System.getProperty("os.name");
		return osName.indexOf("Windows") > -1;
	}

	public void runProcess(String dir, String cmd) {
		runProcess(dir, cmd, false);
	}
	
	public void runProcess(String dir, String cmd, boolean sync) {
		try {
			int exitCode = 1;
			if (isWindows()) {
                ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C", cmd);
                if(dir != null && !"".equals(dir)) pb.directory(new File(dir));
                pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
                pb.redirectError(ProcessBuilder.Redirect.INHERIT);
                Process p = pb.start();
				if(sync){
                    exitCode = p.waitFor();
				}
			} else{
				Process pc = Runtime.getRuntime().exec(cmd);
				if(sync){
					exitCode = pc.waitFor();
				}
			}
			System.out.println("cmd: " + cmd);
			System.out.println("exitCode: " + exitCode);
            Thread.sleep(5000);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

    public static void main(String... args) throws InterruptedException {
        ProcessRunner processRunner = new ProcessRunner();
        processRunner.runProcess("d:/dev/apache-tomcat-7.0.47/bin", "startup.bat", false);
        Thread.sleep(10000);

    }
}
