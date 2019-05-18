package gui;

public class Bug extends GameObject {

    public final double maxVelocity = 0.1;
    public final double maxAngularVelocity = 0.001;

    private double duration = 10;

    public Bug(double x, double y)
    {
        super(x, y, "bug_1.png", FieldCell.translateFactor);
    }

    private double asNormalizedRadians(double angle) {
        while (angle < 0) {
            angle += 2 * Math.PI;
        }
        while (angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }
        return angle;
    }

    private double applyLimits(double value, double min, double max) {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    private double distance(double x1, double y1, double x2, double y2) {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private double angleTo(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;
        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    private void move(double angularVelocity) {
        double velocity = applyLimits(maxVelocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newX = X_Position + velocity / angularVelocity *
                (Math.sin(Direction + angularVelocity * duration) -
                        Math.sin(Direction));
        if (!Double.isFinite(newX)) {
            newX = X_Position + velocity * duration * Math.cos(Direction);
        }
        double newY = Y_Position - velocity / angularVelocity *
                (Math.cos(Direction + angularVelocity * duration) -
                        Math.cos(Direction));
        if (!Double.isFinite(newY)) {
            newY = Y_Position + velocity * duration * Math.sin(Direction);
        }
        X_Position = newX;
        Y_Position = newY;
        Direction = asNormalizedRadians(Direction + angularVelocity * duration);
    }

    public void onModelUpdateEvent(double targetX, double targetY) {
        double dist = distance(targetX, targetY, X_Position, Y_Position);
        if (dist < 0.5) {
            return;
        }
        double angleToTarget = angleTo(X_Position, Y_Position, targetX, targetY);
        double angularVelocity = 0;
        if (angleToTarget > Direction) {
            angularVelocity = maxAngularVelocity;
        }
        if (angleToTarget < Direction) {
            angularVelocity = - maxAngularVelocity;
        }
        move(angularVelocity);
    }
}
