package gui;

public class FieldCell {
    public int X;
    public int Y;

    public FieldCell(int x, int y)
    {
        X = x;
        Y = y;
    }

    public final static double translateFactor = 50;

    public static FieldCell getCell(double x, double y)
    {
        int cellX = (int)Math.round(x / translateFactor);
        int cellY = (int)Math.round(y / translateFactor);
        return new FieldCell(cellX, cellY);
    }

    @Override
    public int hashCode() {
        return X * 3559 + Y * Y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FieldCell)
        {
            FieldCell otherCell = (FieldCell) obj;
            if (X == otherCell.X && Y == otherCell.Y)
                return true;
        }
        return false;
    }
}
