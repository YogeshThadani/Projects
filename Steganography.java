import java.io.*;
import java.awt.image.*;
import javax.imageio.*;

class Header
{
 static final int HEADER_LENGTH = 25;
 String name;
 long need;

 Header()
 {
  name = "";
  need = 0;
 }

 Header(String name, long need)
 {
  this.name = name;
  this.need = need;
 }

 byte[] getHeader()
 {
  //10 bytes size, 14 bytes name
  //"5334         ,FileName.extension    "

  StringBuffer sbuff = new StringBuffer();
  sbuff.append(String.valueOf(need));
  while(sbuff.length() < 10)
   sbuff.append(' ');

  sbuff.append(',');

  if(name.length() > 14)
   sbuff.append(name.substring(name.length() - 14));
  else
   sbuff.append(name);

  while(sbuff.length() < HEADER_LENGTH)
   sbuff.append(' ');

  return sbuff.toString().getBytes();        
 }

 long getSize(byte arr[]) throws Exception
 {
  if(arr.length != HEADER_LENGTH)
   throw new Exception("Invalid Header Size");

  //first 10 bytes of length
   return Long.parseLong(new String(arr, 0, 10).trim());
 }

 String getName(byte arr[]) throws Exception
 {
  if(arr.length != HEADER_LENGTH)
   throw new Exception("Invalid Header Size");
 
  return new String(arr, 11, arr.length-11).trim();
 }

}//Header

class Embed
{
 String filetoEmbed, vesselImage;
 
 Embed(String filetoEmbed, String vesselImage)
 {
  this.filetoEmbed = filetoEmbed;
  this.vesselImage = vesselImage;
 } 

 void embedFileinImage(String trgtFile) throws Exception
 {
  File src = new File(filetoEmbed);
  File img = new File(vesselImage);

  if(! src.exists())
   throw new Exception("File to embed (" + filetoEmbed + ") not Found! ");
  
  if(! img.exists())
   throw new Exception(vesselImage + " Not Found! ");

  //load the imagein memory
   BufferedImage bImg = ImageIO.read(img);

  //calculate the embedding capacity
  int h = bImg.getHeight();
  int w = bImg.getWidth();
  long capacity = (long) w * h; //pixel count
  long need = src.length();
 
   if((need + Header.HEADER_LENGTH ) > capacity)
      throw new Exception("File size is greater than the embedding capacity of image");

  //get the source name
  String name = src.getName(); // c:\\java\\imageName.png --> imageName.png

  //create the header(name, need)
  Header hdr = new Header(name, need);

  //get the raster
  WritableRaster wRstr = bImg.getRaster();

  //Open the file for reading
  FileInputStream fin = new FileInputStream(src);

  //per pixel
  int i, j;
  boolean flag = true;
  int b1, b2, b3;
  int data, q = 0; 
  int r, g, b;
  byte arr[] = hdr.getHeader();
  
  for(j=0; j<h && flag; j++)
  {
   for(i=0; i<w && flag; i++)
   {
    if(q < Header.HEADER_LENGTH)
    {
     data = arr[q];
     q++;
    }
    else
     data = fin.read();

   if(data == -1)
   {//embedding done
    flag = false;
   }
   else
   {
    //extract the bands
    r = wRstr.getSample(i, j, 0);
    g = wRstr.getSample(i, j, 1);
    b = wRstr.getSample(i, j, 2);
   
   //split the byte
    b3 = data & 0x3;
    b2 = (data >> 2) & 0x7;
    b1 = (data >> 5) & 0x7;     

   //merge the bits
    r = (r & (~0x7)) | b1;
    g = (g & (~0x7)) | b2;
    b = (b & (~0x3)) | b3;
    
   //update the raster
   wRstr.setSample(i, j, 0, r);
   wRstr.setSample(i, j, 1, g);
   wRstr.setSample(i, j, 2, b);
   }

   }//for(i
  }//for(j

   //update the buffered image
  bImg.setData(wRstr);

 //save back
 File trgt = new File(trgtFile);
 ImageIO.write(bImg, "PNG", trgt);
 } 

}//Embed

class Extract
{
 String vesselImage;
 
 Extract(String vesselImage)
 {
  this.vesselImage = vesselImage;
 }

 String extractFilefromImage() throws Exception
 {
  File img = new File(vesselImage);

  //check if the image exists
  if(! img.exists())
   throw new Exception("VesselImage not found!");

  //load the image in mmeory
  BufferedImage bImg = ImageIO.read(img);

  //retrieve the pixel matrix
  Raster rstr = bImg.getData();

  //form the header
  Header hdr = new Header(); //empty one

  int w = bImg.getWidth();
  int h = bImg.getHeight();
  int q = 0;
  int data; //fetched byte
  int r, g, b; //extract bands
  boolean flag = true;
  int i, j; //loop control
  long size=0; //size of the file to extract
  byte arr[] = new byte[Header.HEADER_LENGTH];

  String trgt = "";
  FileOutputStream fout = null;
  
  for(j=0; j<h && flag; j++)
  {
   for(i=0; i<w && flag; i++)
   {
    //extract the bands
     r = rstr.getSample(i, j, 0);
     g = rstr.getSample(i, j, 1);
     b = rstr.getSample(i, j, 2);
    
    //bits from bands
    r = r & 0x7;
    g = g & 0x7;
    b = b & 0x3;

    //merge the bits
   data = r;
        data = data << 3;
        data = data | g;
        data = data << 2;
        data = data | b;

    if(q < Header.HEADER_LENGTH)
     arr[q++] = (byte) data;
    else
    {
     if(q == Header.HEADER_LENGTH)
     {
      try
      {
       size = hdr.getSize(arr);
       trgt = "f:\\" + hdr.getName(arr);
       fout = new FileOutputStream(trgt);
       q++; //to avoid rexecution of the if
      }
      catch(Exception ex)
      {
      	throw new Exception("Embedding Not Found/Corrupt"); 
      }
     }//if

    fout.write(data);
    size--;
    if(size == 0)
     flag = false;
    }//else

   }//for(i
  }//for(j

  fout.close();
  return trgt;  
 }

}//Extract

class Steganography
{
 public static void main(String  args[])
 {
  try
  {
   Embed emb = new Embed("c:\\java\\secret.mp4", "c:\\java\\v.jpg");
   emb.embedFileinImage("c:\\java\\stego.png");
 
   Extract ext = new Extract("c:\\java\\stego.png");
   String trgt = ext.extractFilefromImage();
   System.out.println("File extracted in " + trgt);
  }
  catch(Exception ex)
  {
   System.out.println(ex);
   ex.printStackTrace();
  }
 }
}