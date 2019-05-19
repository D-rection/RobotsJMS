import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;

abstract class GameObject {
    double X_Position;
    double Y_Position;
    double Direction = 0;
    final String QueueName;
    final double Size;
    ImageView Picture;

    GameObject(double x, double y, String path, double size, String queueName)
    {
        Size = size;
        loadImage(path, size, size);
        X_Position = x;
        Y_Position = y;
        QueueName = queueName;
    }

    private void loadImage(String fileName, double width, double height){
        File file = new File(fileName);
        String localUrl = file.toURI().toString();
        Image image = new Image(localUrl, width, height, false, true);
        Picture = new ImageView(image);
    }

    void draw()
    {
        double drawX = X_Position - Size / 2;
        double drawY = Y_Position - Size / 2;
        Picture.setX(drawX);
        Picture.setY(drawY);
        Picture.setRotate(90 + Direction * 180 / Math.PI);
    }
}
