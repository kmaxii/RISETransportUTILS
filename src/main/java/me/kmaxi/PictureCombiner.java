package me.kmaxi;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PictureCombiner {

    private static final String pictureFolder = "mapPicutres/";

    public static void main(String[] args) throws IOException {
        int tileSize = 512; // Each tile is 512x512 pixels
        int gridSize = 21;  // 21x21 grid
        BufferedImage combined = new BufferedImage(tileSize * gridSize, tileSize * gridSize, BufferedImage.TYPE_INT_ARGB);

        // Start with image 0 at the center
        BufferedImage centerImage = ImageIO.read(new File(pictureFolder + "0.png"));
        combined.getGraphics().drawImage(centerImage, 10 * tileSize, 10 * tileSize, null);

        int currentImage = 440;

        int startX = gridSize - 1;
        int startY = gridSize - 1;

        for (int x = startX; x >= 0; x--) {
            for (int y = startY; y >= 0; y--) {
                if (x != 10 || y != 10) { // Skip the center position
                    BufferedImage tile = ImageIO.read(new File(pictureFolder + currentImage + ".png"));
                    combined.getGraphics().drawImage(tile, x * tileSize, y * tileSize, null);
                    currentImage--;
                }
            }
        }


        ImageIO.write(combined, "PNG", new File("combined_image.png"));
    }
}

