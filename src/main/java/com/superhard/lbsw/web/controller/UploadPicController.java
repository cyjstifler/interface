package com.superhard.lbsw.web.controller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/uploadPic")
public class UploadPicController{
	private HttpServletRequest request;  
    private HttpServletResponse response; 

  
	
	
	 public List<String> list = new ArrayList<String>();  
	    private File file;  
	    private String fileContentType;  
	    private String fileFileName;  
	  
	    public File getFile() {  
	        return file;  
	    }  
	  
	    public void setFile(File file) {  
	        this.file = file;  
	    }  
	  
	    public String getFileContentType() {  
	        return fileContentType;  
	    }  
	  
	    public void setFileContentType(String fileContentType) {  
	        this.fileContentType = fileContentType;  
	    }  
	  
	    public String getFileFileName() {  
	        return fileFileName;  
	    }  
	  
	    public void setFileFileName(String fileFileName) {  
	        this.fileFileName = fileFileName;  
	    }  
	  
	    public List<String> getList() {  
	        return list;  
	    }  
	  
	    public String httpAllImages() {  
	        String path = request.getSession().getServletContext().getRealPath(  
	                "images");  
	        File filePath = new File(path);  
	        File[] files = filePath.listFiles();  
	        for (int i = 0; i < files.length; i++) {  
	            File file = files[i];  
	            if (!file.isDirectory()) {  
	                String fileName = file.getName();  
	                String img = fileName.substring(fileName.lastIndexOf(".") + 1);  
	                if ("jpg".equals(img) || "jpeg".equals(img)  
	                        || "gif".equals(img) || "png".equals(img)) {  
	  
	                    list.add(fileName);  
	                }  
	            }  
	        }  
	        System.out.println(list.size());  
	        return "images";  
	    }  
	    
	    @RequestMapping(value = "/post", method=RequestMethod.POST)
		public @ResponseBody void doPost(HttpServletRequest req) throws ServletException, IOException {  
	           this.request = req;
		 System.out.println("-----------------");  
	     System.out.println(fileFileName + "------------------" + file.length());  
	     String photospath1 = req.getSession().getServletContext().getRealPath(  
                 "/picture/");  
	     File file1 = new File(photospath1);    

	     if  (!file1 .exists()  && !file1 .isDirectory())      
			  {       
			      System.out.println("//������");  
			      file1 .mkdir();    
			  } else   
			  {  
			      System.out.println("//Ŀ¼����");  
			  }  
			  
	        try {  
	            FileInputStream fis = new FileInputStream(file);  
	            String photospath = request.getSession().getServletContext().getRealPath("/picture/");  
	            
	            System.out.println(photospath);  
	            System.out.println("photospath1" + "------------------" + photospath1); 
	            File fs = new File(photospath, fileFileName);  
	            FileOutputStream fos = new FileOutputStream(fs);  
	            int len = 0;  
	            byte[] buffer = new byte[1024];  
	  
	            while ((len = fis.read(buffer)) != -1) {  
	                fos.write(buffer, 0, len);  
	            }  
	            fos.flush();  
	            fos.close();  
	            fis.close();  
	  
	            
	        } catch (FileNotFoundException e) {  
	            e.printStackTrace();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
         
	    }  
	
	public void uploadForTri() throws ServletException, IOException {  
        
		 System.out.println("--------uploadForTri---------");  
	     System.out.println(fileFileName + "------------------" + file.length());  
	     String photospath1 = request.getSession().getServletContext().getRealPath(  
                "/picture/pictureForTri");  
	     File file1 = new File(photospath1);    
			  if  (!file1 .exists()  && !file1 .isDirectory())      
			  {       
			      System.out.println("//������");  
			      file1 .mkdir();    
			  } else   
			  {  
			      System.out.println("//Ŀ¼����");  
			  }  
			  
	        try {  
	            FileInputStream fis = new FileInputStream(file);  
	            String photospath = request.getSession().getServletContext().getRealPath("/picture/pictureForTri/");  
	            
	            System.out.println(photospath);  
	            System.out.println("photospath1" + "------------------" + photospath1); 
	            File fs = new File(photospath, fileFileName);  
	            FileOutputStream fos = new FileOutputStream(fs);  
	            int len = 0;  
	            byte[] buffer = new byte[1024];  
	  
	            while ((len = fis.read(buffer)) != -1) {  
	                fos.write(buffer, 0, len);  
	            }  
	            fos.flush();  
	            fos.close();  
	            fis.close();  
	  
	            
	        } catch (FileNotFoundException e) {  
	            e.printStackTrace();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
        
	    }  
	

}
