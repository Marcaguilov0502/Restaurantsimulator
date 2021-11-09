import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Sprite {


    //Attributes


    public BufferedImage image;


    //Constructor


    public Sprite(BufferedImage image) {
        this.image = image;
    }

    public Sprite(String fileName) throws IOException {
        image = importImage(fileName);
    }


    //Methods


    public BufferedImage importImage(String fileName) throws IOException {
        String path = "src\\main\\resources\\" + fileName;
        BufferedImage img = ImageIO.read(new File(path));
        return img;
    }

    public void resize(int width, int height) {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
        Graphics2D g2d = (Graphics2D) bi.createGraphics();
        g2d.addRenderingHints(
                new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g2d.drawImage(image, 0, 0, width, height, null);
        g2d.dispose();
        image = bi;
    }

    public void rotate() {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage rotatedImage = new BufferedImage(height, width, image.getType());
        for(int i=0; i<width; i++)
            for(int j=0; j<height; j++)
                rotatedImage.setRGB(height-1-j, width-1-i, image.getRGB(i, j));

        image = rotatedImage;
    }

}
