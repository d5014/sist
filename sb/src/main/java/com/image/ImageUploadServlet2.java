package com.image;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

@WebServlet("/image/generalUpload.do")
@MultipartConfig
public class ImageUploadServlet2 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");

		HttpSession session = req.getSession();

		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "image";
		File f = new File(pathname);
		if (! f.exists()) {
			f.mkdirs();
		}
		
		// HTML 5을 지원하지 않는 브라우저
		ImageManager imageManager = new ImageManager();
		String cp = req.getContextPath();
		String strUrl = "";
		
		try {
			strUrl =  req.getParameter("callback") + "?callback_func=" + req.getParameter("callback_func");
			
			Part p = req.getPart("Filedata");
			
			String originalFilename = getOriginalFilename(p);
			if(originalFilename != null) {
				String fileExt = originalFilename.substring(originalFilename.lastIndexOf("."));
				String saveFilename = String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS", Calendar.getInstance());
				saveFilename += System.nanoTime();
				saveFilename += fileExt;
				String fullpath = pathname + File.separator + saveFilename;
				p.write(fullpath);
				
				int width = imageManager.getImageWidth(fullpath);
				if(width > 600) {
					width = 600;
				}
				
				strUrl += "&bNewLine=true&sFileName="; 
                strUrl += saveFilename;
                strUrl += "&sWidth=" + width;
                strUrl += "&sFileURL=" + cp + "/uploads/image/" + saveFilename;				
			}
		} catch (Exception e) {
			strUrl += "&errstr=error";
		}
		
		PrintWriter out = resp.getWriter();
		out.print(strUrl);
	}
	
	private String getOriginalFilename(Part p) {
		try {
			for (String s : p.getHeader("content-disposition").split(";")) {
				if (s.trim().startsWith("filename")) {
					return s.substring(s.indexOf("=") + 1).trim().replace("\"", "");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
