package com.eshore.yxt.media.core.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * 生成压缩图
 *
 */
public class ImageScale {
 private int width;
 private int height;
 private int scaleWidth;
 double support = (double) 3.0;
 double PI = (double) 3.14159265358978;
 double[] contrib;
 double[] normContrib;
 double[] tmpContrib;
 int startContrib, stopContrib;
 int nDots;
 int nHalfDots;

 /**
  * Start: Use Lanczos filter to replace the original algorithm for image
  * scaling. Lanczos improves quality of the scaled image modify by :blade
  */
 public BufferedImage imageZoomOut(BufferedImage srcBufferImage, int w, int h) {
  width = srcBufferImage.getWidth();
  height = srcBufferImage.getHeight();
  scaleWidth = w;

  if (DetermineResultSize(w, h) == 1) {
   return srcBufferImage;
  }
  CalContrib();
  BufferedImage pbOut = HorizontalFiltering(srcBufferImage, w);
  BufferedImage pbFinalOut = VerticalFiltering(pbOut, h);
  return pbFinalOut;
 }

 /**
  * 决定图像尺寸
  */
 private int DetermineResultSize(int w, int h) {
  double scaleH, scaleV;

  // update by libra
  double wt = w > width ? width : w;
  double ht = h > height ? height : h;

  scaleH = (double) wt / (double) width;
  scaleV = (double) ht / (double) height;

  // 需要判断一下scaleH，scaleV，不做放大操作[注释可以放大，若不需要放大则注释掉]
  /*if (scaleH >= 1.0 && scaleV >= 1.0) {
   return 1;
  }*/
  return 0;

 } // end of DetermineResultSize()

 private double Lanczos(int i, int inWidth, int outWidth, double Support) {
  double x;

  x = (double) i * (double) outWidth / (double) inWidth;

  return Math.sin(x * PI) / (x * PI) * Math.sin(x * PI / Support)
    / (x * PI / Support);

 } // end of Lanczos()

 //  
 // Assumption: same horizontal and vertical scaling factor
 //  
 private void CalContrib() {
  nHalfDots = (int) ((double) width * support / (double) scaleWidth);
  nDots = nHalfDots * 2 + 1;
  try {
   contrib = new double[nDots];
   normContrib = new double[nDots];
   tmpContrib = new double[nDots];
  } catch (Exception e) {
   System.out.println("init   contrib,normContrib,tmpContrib" + e);
  }

  int center = nHalfDots;
  contrib[center] = 1.0;

  double weight = 0.0;
  int i = 0;
  for (i = 1; i <= center; i++) {
   contrib[center + i] = Lanczos(i, width, scaleWidth, support);
   weight += contrib[center + i];
  }

  for (i = center - 1; i >= 0; i--) {
   contrib[i] = contrib[center * 2 - i];
  }

  weight = weight * 2 + 1.0;

  for (i = 0; i <= center; i++) {
   normContrib[i] = contrib[i] / weight;
  }

  for (i = center + 1; i < nDots; i++) {
   normContrib[i] = normContrib[center * 2 - i];
  }
 } // end of CalContrib()

 // 处理边缘
 private void CalTempContrib(int start, int stop) {
  double weight = 0;

  int i = 0;
  for (i = start; i <= stop; i++) {
   weight += contrib[i];
  }

  for (i = start; i <= stop; i++) {
   tmpContrib[i] = contrib[i] / weight;
  }

 } // end of CalTempContrib()

 private int GetRedValue(int rgbValue) {
  int temp = rgbValue & 0x00ff0000;
  return temp >> 16;
 }

 private int GetGreenValue(int rgbValue) {
  int temp = rgbValue & 0x0000ff00;
  return temp >> 8;
 }

 private int GetBlueValue(int rgbValue) {
  return rgbValue & 0x000000ff;
 }

 private int ComRGB(int redValue, int greenValue, int blueValue) {

  return (redValue << 16) + (greenValue << 8) + blueValue;
 }

 // 行水平滤波
 private int HorizontalFilter(BufferedImage bufImg, int startX, int stopX,
   int start, int stop, int y, double[] pContrib) {
  double valueRed = 0.0;
  double valueGreen = 0.0;
  double valueBlue = 0.0;
  int valueRGB = 0;
  int i, j;

  for (i = startX, j = start; i <= stopX; i++, j++) {
   valueRGB = bufImg.getRGB(i, y);

   valueRed += GetRedValue(valueRGB) * pContrib[j];
   valueGreen += GetGreenValue(valueRGB) * pContrib[j];
   valueBlue += GetBlueValue(valueRGB) * pContrib[j];
  }

  valueRGB = ComRGB(Clip((int) valueRed), Clip((int) valueGreen),
    Clip((int) valueBlue));
  return valueRGB;

 } // end of HorizontalFilter()

 // 图片水平滤波
 private BufferedImage HorizontalFiltering(BufferedImage bufImage, int iOutW) {
  int dwInW = bufImage.getWidth();
  int dwInH = bufImage.getHeight();
  int value = 0;
  BufferedImage pbOut = new BufferedImage(iOutW, dwInH,
    BufferedImage.TYPE_INT_RGB);

  for (int x = 0; x < iOutW; x++) {

   int startX;
   int start;
   int X = (int) (((double) x) * ((double) dwInW) / ((double) iOutW) + 0.5);
   int y = 0;

   startX = X - nHalfDots;
   if (startX < 0) {
    startX = 0;
    start = nHalfDots - X;
   } else {
    start = 0;
   }

   int stop;
   int stopX = X + nHalfDots;
   if (stopX > (dwInW - 1)) {
    stopX = dwInW - 1;
    stop = nHalfDots + (dwInW - 1 - X);
   } else {
    stop = nHalfDots * 2;
   }

   if (start > 0 || stop < nDots - 1) {
    CalTempContrib(start, stop);
    for (y = 0; y < dwInH; y++) {
     value = HorizontalFilter(bufImage, startX, stopX, start,
       stop, y, tmpContrib);
     pbOut.setRGB(x, y, value);
    }
   } else {
    for (y = 0; y < dwInH; y++) {
     value = HorizontalFilter(bufImage, startX, stopX, start,
       stop, y, normContrib);
     pbOut.setRGB(x, y, value);
    }
   }
  }

  return pbOut;

 } // end of HorizontalFiltering()

 private int VerticalFilter(BufferedImage pbInImage, int startY, int stopY,
   int start, int stop, int x, double[] pContrib) {
  double valueRed = 0.0;
  double valueGreen = 0.0;
  double valueBlue = 0.0;
  int valueRGB = 0;
  int i, j;

  for (i = startY, j = start; i <= stopY; i++, j++) {
   valueRGB = pbInImage.getRGB(x, i);

   valueRed += GetRedValue(valueRGB) * pContrib[j];
   valueGreen += GetGreenValue(valueRGB) * pContrib[j];
   valueBlue += GetBlueValue(valueRGB) * pContrib[j];
   // System.out.println(valueRed+"->"+Clip((int)valueRed)+"<-");
   //  
   // System.out.println(valueGreen+"->"+Clip((int)valueGreen)+"<-");
   // System.out.println(valueBlue+"->"+Clip((int)valueBlue)+"<-"+"-->");
  }

  valueRGB = ComRGB(Clip((int) valueRed), Clip((int) valueGreen),
    Clip((int) valueBlue));
  // System.out.println(valueRGB);
  return valueRGB;

 } // end of VerticalFilter()

 private BufferedImage VerticalFiltering(BufferedImage pbImage, int iOutH) {
  int iW = pbImage.getWidth();
  int iH = pbImage.getHeight();
  int value = 0;
  BufferedImage pbOut = new BufferedImage(iW, iOutH,
    BufferedImage.TYPE_INT_RGB);

  for (int y = 0; y < iOutH; y++) {

   int startY;
   int start;
   int Y = (int) (((double) y) * ((double) iH) / ((double) iOutH) + 0.5);

   startY = Y - nHalfDots;
   if (startY < 0) {
    startY = 0;
    start = nHalfDots - Y;
   } else {
    start = 0;
   }

   int stop;
   int stopY = Y + nHalfDots;
   if (stopY > (int) (iH - 1)) {
    stopY = iH - 1;
    stop = nHalfDots + (iH - 1 - Y);
   } else {
    stop = nHalfDots * 2;
   }

   if (start > 0 || stop < nDots - 1) {
    CalTempContrib(start, stop);
    for (int x = 0; x < iW; x++) {
     value = VerticalFilter(pbImage, startY, stopY, start, stop,
       x, tmpContrib);
     pbOut.setRGB(x, y, value);
    }
   } else {
    for (int x = 0; x < iW; x++) {
     value = VerticalFilter(pbImage, startY, stopY, start, stop,
       x, normContrib);
     pbOut.setRGB(x, y, value);
    }
   }

  }

  return pbOut;

 } // end of VerticalFiltering()

 int Clip(int x) {
  if (x < 0)
   return 0;
  if (x > 255)
   return 255;
  return x;
 }

 /**
  * End: Use Lanczos filter to replace the original algorithm for image
  * scaling. Lanczos improves quality of the scaled image modify by :blade
  */

 public boolean scale(String source, String target, int width, int height) {
  File f = new File(source);
  try {
   BufferedImage bi = ImageIO.read(f);
   BufferedImage out = null;
   ImageScale scal = new ImageScale();
//   int _width = bi.getWidth();//add
//   int _height = bi.getHeight();//add
//   int[] _arr = this.getImageWidthAndHeight(_width, _height, width, height);//add
   out = scal.imageZoomOut(bi, width, height);
   //out = scal.imageZoomOut(bi, _arr[0], _arr[1]);
   File t = new File(target);
   ImageIO.write(out, "jpg", t);
   return true;
  } catch (IOException e) {
   e.printStackTrace();
   return false;
  }
 }

 /**
  * 得到放大或者缩小后的比例
  * 
  * @param W
  *            图片原宽
  * @param H
  *            原高
  * @param tarW
  *            转换后的宽
  * @param zoom
  *            放大还是缩小
  * @return 返回宽和高的数组
  */
 private static int[] getImageWidthAndHeight(int orgW , int orgH, int avW, int avH) {
  int width=0;
  int height = 0;

  if(orgW>0&&orgH>0){
   if(orgW/orgH>=avW/avH){
    if(orgW>avW){
     width = avW;
     height = (orgH*avW)/orgW;
    }else{
     width = orgW;
     height = orgH;
    }
    //System.out.println("++Widht:"+width+" Height"+height);
   }else{
    if(orgH>avH){
     height = avH;
     width = (orgW*avH)/orgH;
    }else{
     width = orgW;
     height = orgH;
    }
    //System.out.println("++Widht:"+width+" Height"+height);
   }
  }
  int[] arr = new int[2];
  
  //关键 jingfei 按比例缩放
  arr[0] = width;
  arr[1] = height;
  
  //不按比例缩放
  /*arr[0] = avW;
  arr[1] = avH;*/
  
  
  
  
//  long start = System.currentTimeMillis();
//  int width = 0;
//  int height = 0;
//  if ((W / tarW) >= (H / tarH)) {// 宽的缩小比例大于高的
//   width = tarW;
//   height = H * tarW / W;
//   System.out.println(width + "  " + height);
//  } else {
//   height = tarH;
//   width = W * tarH / H;
//   System.out.println(width + "  " + height);
//  }
//  int[] arr = new int[2];
//  arr[0] = width;
//  arr[1] = height;
//  long end = System.currentTimeMillis();
//  System.out.println("宽高处理：" + (end - start));
  return arr;
 }

 public void picscale(String source, String target, int w, int h) {
  File f = new File(source);
  int width = 0;
  int height = 0;
  try {
   BufferedImage bi = ImageIO.read(f);
   int[] arr = getImageWidthAndHeight(bi.getWidth(), bi.getHeight(),
     w, h);
   width = arr[0];
   height = arr[1];
   BufferedImage out = null;
   ImageScale scal = new ImageScale();
   out = scal.imageZoomOut(bi, width, height);
   File t = new File(target);
   ImageIO.write(out, "jpg", t);
  } catch (IOException e) {
   e.printStackTrace();
  }
 }
 
 public void picscale(URL url, OutputStream target, int w, int h) {
	  int width = 0;
	  int height = 0;
	  try {
	   BufferedImage bi = ImageIO.read(url);
	   int[] arr = getImageWidthAndHeight(bi.getWidth(), bi.getHeight(),
	     w, h);
	   width = arr[0];
	   height = arr[1];
	   BufferedImage out = null;
	   ImageScale scal = new ImageScale();
	   out = scal.imageZoomOut(bi, width, height);
	   ImageIO.write(out, "jpg", target);
	  } catch (IOException e) {
	   e.printStackTrace();
	  }
	 }

/**

*调用scale(源文件路径，保存路径，最大宽，最大高)

*

*/

 public static void main(String[] args) {
  ImageScale is = new ImageScale();
  long start = System.currentTimeMillis();
  is.scale("D:/1339469816812.bmp", "D:/1339469816812_2.bmp", 50, 50);
  long end = System.currentTimeMillis();
  System.out.println("时间：" + (end - start));
 }
}

