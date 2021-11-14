import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static java.lang.Math.abs;

public class VinerFilter {
    public static void main(String[] args) {
        try {

            File fileInput = new File("output.jpg");

            BufferedImage imageSource = ImageIO.read(fileInput);
            BufferedImage imageResult = new BufferedImage(imageSource.getWidth(), imageSource.getHeight(), imageSource.getType());

            double dispersRedNoise = getDispersRN(imageSource);
            double dispersGreenNoise = getDispersGN(imageSource);
            double dispersBlueNoise = getDispersBN(imageSource);

            for (int x = 0;x < imageSource.getWidth();x++){

                for (int y = 0;y < imageSource.getHeight();y++){
                    Color color = new Color(imageSource.getRGB(x, y));

                    int red = color.getRed();
                    int green = color.getGreen();
                    int blue = color.getBlue();

                    double midRed =  midR(getNeighborhoodPoint(imageSource, x, y));
                    double midGreen = midG(getNeighborhoodPoint(imageSource, x, y));
                    double midBlue = midB(getNeighborhoodPoint(imageSource, x, y));

                    double dispersR = getDispersR(getNeighborhoodPoint(imageSource, x, y));
                    double dispersG = getDispersG(getNeighborhoodPoint(imageSource, x, y));
                    double dispersB = getDispersB(getNeighborhoodPoint(imageSource, x, y));

                    int newRed = (int) (midRed + (red - midRed) * (dispersR / (dispersR * dispersRedNoise)));
                    int newGreen = (int) (midGreen + (green - midGreen) * (dispersG / (dispersG * dispersGreenNoise)));
                    int newBlue = (int) (midBlue + (blue - midBlue) * (dispersB / (dispersB * dispersBlueNoise)));

                    if(newRed < 0){
                        newRed = abs(newRed);
                    }
                    if(newRed > 255){
                        newRed = 255;
                    }

                    if(newGreen < 0){
                        newGreen = abs(newGreen);
                    }
                    if(newGreen > 255){
                        newGreen = 255;
                    }

                    if(newBlue < 0){
                        newBlue = abs(newBlue);
                    }
                    if(newBlue > 255){
                        newBlue = 255;
                    }


                    Color newColor = new Color(newRed, newGreen, newBlue);

                    imageResult.setRGB(x, y, newColor.getRGB());

                }
            }

            File fileOutput = new File("output2.jpg");
            ImageIO.write(imageResult, "jpg", fileOutput);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double midR(Color[][] data){

        double midr;
        int summ = 0;


        for(int i = 0; i<3; i++){
            for (int j = 0; j<3; j++){
                summ = summ + data[i][j].getRed();

            }
        }

        midr = summ/9;

        return midr;
    }


    public static double midG(Color[][] data){
        double midg;

        int summa = 0;


        for(int i = 0; i<3; i++){
            for (int j = 0; j<3; j++){

                summa = summa + data[i][j].getGreen();

            }
        }

        midg = summa/9;

        return midg;
    }


    public static double midB(Color[][] data){
        double midb;

        int summa = 0;

        for(int i = 0; i<3; i++){
            for (int j = 0; j<3; j++){

                summa = summa + data[i][j].getBlue();
            }
        }

        midb = summa/9;

        return midb;
    }

    public static Color[][] getNeighborhoodPoint(BufferedImage image, int x, int y){
        Color[][] data = new Color[3][3];
        data[0][0] = new Color(image.getRGB(abs(x - 1), abs(y - 1)));
        data[1][0] = new Color(image.getRGB(x, abs(y - 1)));

        if(x + 1 >= image.getWidth()){
            data[2][0] = new Color(image.getRGB(x, abs(y - 1)));
        }else {
            data[2][0] = new Color(image.getRGB(x + 1, abs(y - 1)));
        }

        data[0][1] = new Color(image.getRGB(abs(x - 1), y));
        data[1][1] = new Color(image.getRGB(x, y));

        if(x + 1 >= image.getWidth()){
            data[2][1] = new Color(image.getRGB(x, y));
        }else {
            data[2][1] = new Color(image.getRGB(x + 1, y));
        }

        if(y + 1 >= image.getHeight()){
            data[0][2] = new Color(image.getRGB(abs(x - 1), y));
            data[1][2] = new Color(image.getRGB(x, y));

            if(x + 1 >= image.getWidth()){
                data[2][2] = new Color(image.getRGB(x, y));
            }else {
                data[2][2] = new Color(image.getRGB(x + 1, y));
            }

        }else {
            data[0][2] = new Color(image.getRGB(abs(x - 1), y+1));
            data[1][2] = new Color(image.getRGB(x, y+1));

            if(x + 1 >= image.getWidth()){
                data[2][2] = new Color(image.getRGB(x, y+1));
            }else {
                data[2][2] = new Color(image.getRGB(x + 1, y+1));
            }
        }

        return data;
    }


    static double getDispersR(Color[][] data) {
        double mid = midR(data);
        double deviation = 0;
        for(int i = 0; i<3; i++){
            for (int j = 0; j<3; j++){
                int a = data[i][j].getRed();
                deviation += (a-mid)*(a-mid);
            }
        }
        return deviation/(8);
    }


    static double getDispersG(Color[][] data) {
        double mid = midG(data);
        double deviation = 0;
        for(int i = 0; i<3; i++){
            for (int j = 0; j<3; j++){
                int a = data[i][j].getGreen();
                deviation += (a-mid)*(a-mid);
            }
        }
        return deviation/(8);
    }


    static double getDispersB(Color[][] data) {
        double mid = midB(data);
        double deviation = 0;
        for(int i = 0; i<3; i++){
            for (int j = 0; j<3; j++){
                int a = data[i][j].getBlue();
                deviation += (a-mid)*(a-mid);
            }
        }
        return deviation/(8);
    }


    static double getDispersRN(BufferedImage image) {
        double mid;
        int sum = 0;
        double deviation = 0;

        for (int x = 0 ; x < image.getWidth(); x++){
            for (int y = 0 ; y < image.getHeight(); y++) {
                Color color = new Color(image.getRGB(x, y));
                int red = color.getRed();
                sum += red;
            }
        }
        mid = sum / (image.getHeight() * image.getWidth());

        for (int x = 0 ; x < image.getWidth(); x++){
            for (int y = 0 ; y < image.getHeight(); y++) {
                Color color = new Color(image.getRGB(x, y));
                int red = color.getRed();
                deviation += (red-mid) * (red-mid);

            }
        }
        return deviation / (image.getHeight() * image.getWidth());
    }


    static double getDispersGN(BufferedImage image) {
        double mid;
        int sum = 0;
        double deviation = 0;

        for (int x = 0 ; x < image.getWidth(); x++){
            for (int y = 0 ; y < image.getHeight(); y++) {
                Color color = new Color(image.getRGB(x, y));
                int green = color.getGreen();
                sum += green;
            }
        }
        mid = sum / (image.getHeight()*image.getWidth());

        for (int x = 0 ; x<image.getWidth(); x++){
            for (int y = 0 ; y< image.getHeight(); y++) {
                Color color = new Color(image.getRGB(x, y));
                int green = color.getGreen();
                deviation += (green - mid)*(green - mid);

            }
        }
        return deviation / (image.getHeight() * image.getWidth());
    }


    static double getDispersBN(BufferedImage image) {
        double mid;
        int sum = 0;
        double deviation = 0;

        for (int x = 0 ; x < image.getWidth(); x++){
            for (int y = 0 ; y < image.getHeight(); y++) {
                Color color = new Color(image.getRGB(x, y));
                int blue = color.getBlue();
                sum += blue;
            }
        }
        mid = sum / (image.getHeight()*image.getWidth());

        for (int x = 0 ; x < image.getWidth(); x++){
            for (int y = 0 ; y < image.getHeight(); y++) {
                Color color = new Color(image.getRGB(x, y));
                int blue = color.getBlue();
                deviation += (blue - mid) * (blue - mid);

            }
        }
        return deviation / (image.getHeight() * image.getWidth());
    }
}
