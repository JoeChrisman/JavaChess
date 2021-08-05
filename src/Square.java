import java.awt.*;

public class Square extends Rectangle
{

    private final int row;
    private final int col;
    private final boolean isWhite;

    private Piece piece;

    private Constants.ColorPreset highlightColor;
    private Constants.ColorPreset squareColor;

    public Square(int row, int col)
    {
        super(col * Constants.SQUARE_SIZE,
              row * Constants.SQUARE_SIZE,
                 Constants.SQUARE_SIZE,
                 Constants.SQUARE_SIZE);
        this.row = row;
        this.col = col;

        isWhite = row % 2 == col % 2;
        squareColor = isWhite ? Constants.ColorPreset.LIGHT_SQUARE : Constants.ColorPreset.DARK_SQUARE;

    }

    public int getRow()
    {
        return row;
    }

    public int getCol()
    {
        return col;
    }

    public void setPiece(Piece piece)
    {
        this.piece = piece;
    }

    public void removePiece()
    {
        piece = null;
    }

    public void highlight(Constants.ColorPreset highlightColor)
    {
        this.highlightColor = highlightColor;
    }

    public void removeHighlight()
    {
        highlightColor = null;
    }

    public Piece getPiece()
    {
        return piece;
    };

    public void render(Graphics graphics)
    {
        if (highlightColor == null) {
            graphics.setColor(squareColor.getColor());
        }
        else
        {
            graphics.setColor(highlightColor.getColor());
        }

        graphics.fillRect(x, y, width, height);

        if (piece != null)
        {
            piece.render(graphics);
        }

    }
}
