package com.superhard.lbsw.web.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.*;
import java.util.*;
import java.io.*;
import org.apache.commons.fileupload.servlet.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.superhard.lbsw.utils.ReadExcel;

import javassist.expr.NewArray;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
@Controller
@RequestMapping("/uploadExcel")
	
public class UploadExcelController  {

	private HttpServletRequest req1;
	private HttpServletResponse res1;

	
	
	@RequestMapping(value = "/post", method=RequestMethod.POST)
	public @ResponseBody void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		System.out.println(" ");
		
		String root = req.getParameter("root");
		int type = Integer.parseInt(req.getParameter("type"));
		res.setContentType("text/html; charset=UTF-8");
		PrintWriter out = res.getWriter();
		System.out.println(req.getContentLength());
		System.out.println(req.getContentType());
		DiskFileItemFactory factory = new DiskFileItemFactory();

		factory.setSizeThreshold(4096);
		factory.setRepository(new File(root));
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setSizeMax(1000000);

		try {
			List fileItems = upload.parseRequest(req);
			Iterator iter = fileItems.iterator();

			if (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				String fileName = item.getName();

				if (!item.isFormField()) {
					try {

						fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length());

						item.write(new File(root, fileName));
						int rows = ReadExcel.main(root + "/" + fileName,type) - 1;
						if (rows > 0) {
							req.setAttribute("key", rows);
							System.out.println(" ");
							String nextJSP = "/importSuccess.jsp";
							
							RequestDispatcher dispatcher = req.getRequestDispatcher(nextJSP);
							dispatcher.forward(req, res);
						}else{
							String nextJSP = "/error.jsp";
							RequestDispatcher dispatcher = req.getRequestDispatcher(nextJSP);
							dispatcher.forward(req, res);
						}
					} catch (Exception e) {
						System.out.println(e);
						String nextJSP = "/error.jsp";
						RequestDispatcher dispatcher = req.getRequestDispatcher(nextJSP);
						dispatcher.forward(req, res);
					}
				} else {
					String nextJSP = "/error.jsp";
					RequestDispatcher dispatcher = req.getRequestDispatcher(nextJSP);
					dispatcher.forward(req, res);
					throw new IOException("fail to upload");

				}
			}
		} catch (FileUploadException e) {
			out.println(e);
		}

	}
}