package gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;

public abstract class GameObject {
    public volatile double X_Position;
    public volatile double Y_Position;
    public volatile double Direction = 0;
    public final double Size;
    public ImageView Picture;

    public GameObject(double x, double y, String path, double size)
    {
        Size = size;
        loadImage(path, size, size);
        X_Position = x;
        Y_Position = y;
    }

    private void loadImage(String fileName, double width, double height){
        File file = new File(fileName);
        String localUrl = file.toURI().toString();
        Image image = new Image(localUrl, width, height, false, true);
        Picture = new ImageView(image);
    }

    public void draw()
    {
        double drawX = X_Position - Size / 2;
        double drawY = Y_Position - Size / 2;
        Picture.setX(drawX);
        Picture.setY(drawY);
        Picture.setRotate(90 + Direction * 180 / Math.PI);
    }
}
