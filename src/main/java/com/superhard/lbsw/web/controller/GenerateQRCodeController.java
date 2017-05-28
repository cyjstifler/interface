package com.superhard.lbsw.web.controller;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;  
import java.io.File;  
import java.io.IOException;
import java.util.Hashtable;
import javax.imageio.ImageIO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.encoder.QRCode;

@Controller  
@RequestMapping("/generateQrCode")
public class GenerateQRCodeController { 

	
	private static final int QR_WIDTH = 300;
	private static final int QR_HEIGHT = 300;
	
	/** 
     * (QRCode)ͼƬ 
     * @param content 
     * @param imgPath 
     */  
    public  void encoderQRCode(String content, String imgPath,int version) {  
    	File file=new File(imgPath);    

    	if  (!file .exists()  && !file .isDirectory())      
		  {       
		      file .mkdir();    
		  } else   
		  {  
		      System.out.println("//");  
		  }  
		  try {  
  
            QRCode qrcodeHandler = new QRCode();  
//            qrcodeHandler.setQrcodeErrorCorrect('M');  
//            qrcodeHandler.setQrcodeEncodeMode('B');  
//            qrcodeHandler.setQrcodeVersion(version);  
            
            int imgSize = 300;//67 + 12 * (size - 1) ;  
            System.out.println(content);  
            byte[] contentBytes = content.getBytes("utf-8");  
  
            BufferedImage bufImg = new BufferedImage(imgSize , imgSize ,BufferedImage.TYPE_INT_RGB);  
            Graphics2D gs = bufImg.createGraphics();  
  
            gs.setBackground(Color.WHITE);  
            gs.clearRect(0, 0, imgSize , imgSize );  
  
            gs.setColor(Color.BLACK);  
  
            int pixoff = 2;  
            System.out.println(contentBytes.length);  
            /*if (contentBytes.length > 0 && contentBytes.length < 130) {  
                boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);  
                for (int i = 0; i < codeOut.length; i++) {  
                    for (int j = 0; j < codeOut.length; j++) {  
                        if (codeOut[j][i]) {  
                            gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);  
                        }  
                    }  
                }  
            } else {  
                System.err.println("QRCode content bytes length = "  
                        + contentBytes.length + " not in [ 0,130 ]. ");  
                boolean[][] codeOut = qrcodeHandler.getMatrix();
//                		.calQrcode(contentBytes);  
                for (int i = 0; i < codeOut.length; i++) {  
                    for (int j = 0; j < codeOut.length; j++) {  
                        if (codeOut[j][i]) {  
                            gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);  
                        }  
                    }  
                }  
            }  
  
            gs.dispose();  
            img = bufImg;
            bufImg.flush();  
  
            File imgFile = new File(imgPath);  
            ImageIO.write(bufImg, "png", imgFile);  */
  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
    }  

    @RequestMapping(value = "/generate", method=RequestMethod.POST)
	public @ResponseBody	 String execute() throws Exception {
		try{
			  String imgPath = "" + "/Relieved_QRCode.png";  
			    
		        String content = ":{\"proId\":\""+"}";
		  
		        encoderQRCode(content, imgPath,20);  
		  
		        System.out.println("encoder QRcode success");  
			return "true";
			}catch(Exception ex){
				return "false";
			}
	}

	 public void createQRImage(String url ,String imgPath){
		  File file=new File(imgPath);    
		  
		  if  (!file.exists()  && !file .isDirectory())      
		  {       
		      System.out.println("//");  
		      file.mkdir();    
		  } else   
		  {  
		      System.out.println("//");  
		  }  
	        try
	        {
	            if (url == null || "".equals(url) || url.length() < 1)
	            {
	                return;
	            }
	            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
	            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
	            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
	            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
	            for (int y = 0; y < QR_HEIGHT; y++)
	            {
	                for (int x = 0; x < QR_WIDTH; x++)
	                {
	                    if (bitMatrix.get(x, y))
	                    {
	                        pixels[y * QR_WIDTH + x] = 0xff000000;
	                    }
	                    else
	                    {
	                        pixels[y * QR_WIDTH + x] = 0xffffffff;
	                    }
	                }
	            }

	            
	        	BufferedImage image = new BufferedImage(QR_WIDTH, QR_HEIGHT,BufferedImage.TYPE_INT_ARGB);

	        	for (int x = 0; x < QR_WIDTH; x++) {

	        	     for (int y = 0; y < QR_HEIGHT; y++) {

	        	           image.setRGB(x, y, bitMatrix.get(x, y) == true ? 

	        	           Color.BLACK.getRGB():Color.WHITE.getRGB());

	        	    }

	        	}

	        	ImageIO.write(image,"png", file);
	        }
	        catch (WriterException e)
	        {
	            e.printStackTrace();
	        } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }

}  