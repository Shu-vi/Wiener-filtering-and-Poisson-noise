import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Poisson {

    public static void main(String[] args) throws Exception{

        try {
            File file = new File("input.jpg");
            BufferedImage source = ImageIO.read(file);
            BufferedImage result = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
            for (int x = 0; x < source.getWidth(); x++) {
                for (int y = 0; y < source.getHeight(); y++) {
                    Color color = new Color(source.getRGB(x, y));
                    int blue = color.getBlue();
                    int red = color.getRed();
                    int green = color.getGreen();
                    double brightness = 0.3*red + 0.59*green + 0.11*blue;
                    double newBrightness = poissonValue(brightness);
                    int newBlue=(int) ((newBrightness/brightness)*blue),
                            newRed=(int) ((newBrightness/brightness)*red),
                            newGreen=(int) ((newBrightness/brightness)*green);
                    if (newRed>255)
                        newRed=255;
                    if (newGreen>255)
                        newGreen=255;
                    if (newBlue>255)
                        newBlue=255;
                    Color newColor= new Color(newRed, newGreen, newBlue);
                    result.setRGB(x, y, newColor.getRGB());
                }
            }
            File output = new File("output.jpg");
            ImageIO.write(result, "jpg", output);
        } catch (IOException e) {
            System.out.println("Файл не найден или не удалось сохранить");
        }
    }

    static double poissonValue(double value){
        double L = Math.exp(-value);
        int k = 0;
        double p = 1;
        do
        {
            ++k;
            double tmp = Math.random();
            p *= tmp;
        } while (p > L);
        return (k - 1);
    }
}
