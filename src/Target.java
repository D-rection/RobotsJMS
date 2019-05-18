public class Target extends GameObject {

    public void setTargetPosition(double x, double y) {
        X_Position = x;
        Y_Position = y;
    }

    Target(double x, double y)
    {
        super(x, y, "apple.png", 30);
        Direction = - Math.PI / 2;
    }
}
