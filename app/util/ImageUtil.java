package util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import play.Logger;

public class ImageUtil {

    public static BufferedImage scaleImage(BufferedImage image, int size) {
        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();
        boolean widthBound = originalWidth > originalHeight;
        Image smallImage = null;
        if(widthBound && originalWidth > size){
            smallImage = image.getScaledInstance(size, -1, Image.SCALE_SMOOTH);
            Logger.info(" image resized width");
        }else if(!widthBound && originalHeight > size){
            smallImage = image.getScaledInstance(-1, size, Image.SCALE_SMOOTH);
            Logger.info(" image resized height");
        }
        if(smallImage != null){
            image = renderImage(smallImage);
            Logger.info(" scaled image transformed");
        }else if(image.getType() != BufferedImage.TYPE_INT_RGB
                && image.getType() != BufferedImage.TYPE_INT_BGR){
            // must render it if the original image is ARGB because trying to save an ARGB jpeg fails
            // so this will turn it into an RGB image
            image = renderImage(image);
        }
        return image;
    }

    private static BufferedImage renderImage(Image image){
        BufferedImage newImage = new BufferedImage(image.getWidth(null),
                image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = newImage.getGraphics();
        graphics.setColor(Color.white);
        graphics.fillRect(0,0,newImage.getWidth(),newImage.getHeight());
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();
        return newImage;
    }

    public static void writeImage(BufferedImage image, ImageOutputStream output) throws IOException {
        ImageWriter imageWriter = ImageIO.getImageWritersByFormatName("jpeg").next();
        ImageWriteParam iwp = imageWriter.getDefaultWriteParam();
        iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        iwp.setCompressionQuality(0.85f);
        IIOImage iioImage = new IIOImage(image, null, null);
        imageWriter.setOutput(output);
        imageWriter.write(null, iioImage, iwp);
        imageWriter.dispose();
        output.close();
    }
}
