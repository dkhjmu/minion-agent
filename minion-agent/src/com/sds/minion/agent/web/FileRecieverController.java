package com.sds.minion.agent.web;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2)
public class FileRecieverController {

  @RequestMapping(value="/upload", method=RequestMethod.POST)
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String savePath = request.getHeader("dest"); 
    savePath = savePath.replace("\\\\", "/");
    File fileSaveDir = new File(savePath);
    if (!fileSaveDir.exists()) {
      if (fileSaveDir.mkdirs()) {
        // log.info("Make success");
      } else {
        System.out.println("Make Failed:[" + fileSaveDir + "]");
      }
    }

    for (Part part : request.getParts()) {
      String fileName = extractFileName(part);
      if (fileName != null && !fileName.equals("")) {
        part.write(savePath + File.separator + fileName);
        System.out.println("UploadedFile:" + fileName + "(" + FileUtils.byteCountToDisplaySize(part.getSize()) + ")");
      }
    }

    request.setAttribute("message", "Upload has been done successfully!");
    response.setContentType("text/javascript");
    PrintWriter out = response.getWriter();
    out.print("OK");
    out.flush();
  }

  /**
   * Extracts file name from HTTP header content-disposition
   */
  private String extractFileName(Part part) {
    String contentDisp = part.getHeader("content-disposition");
    String[] items = contentDisp.split(";");
    for (String s : items) {
      if (s.trim().startsWith("filename")) {
        return s.substring(s.indexOf("=") + 2, s.length() - 1);
      }
    }
    return "";
  }
}
