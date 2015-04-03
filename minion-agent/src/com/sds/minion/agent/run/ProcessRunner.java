package com.sds.minion.agent.run;

import java.io.IOException;

public class ProcessRunner {

	public static boolean isWindows() {
		String osName = System.getProperty("os.name");
		return osName.indexOf("Windows") > -1;
	}

	public void runProcess(String cmd) {
		runProcess(cmd, false);
	}
	
	public void runProcess(String cmd, boolean sync) {
		try {
			int exitCode = 1;
			if (isWindows()) {
				Process pc = Runtime.getRuntime().exec("cmd.exe /c " + cmd);
				if(sync){
					exitCode = pc.waitFor();
				}
			}else{
				Process pc = Runtime.getRuntime().exec(cmd);
				if(sync){
					exitCode = pc.waitFor();
				}
			}
			System.out.println("cmd:"+cmd);
			System.out.println("exitCode:"+exitCode);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
