import java.awt.*;

public class Square extends Rectangle
{

    private final int row;
    private final int col;
    private final boolean isWhite;

    private Piece piece;

    public Square(int row, int col)
    {
        super(col * Constants.SQUARE_SIZE,
              row * Constants.SQUARE_SIZE,
                 Constants.SQUARE_SIZE,
                 Constants.SQUARE_SIZE);
        this.row = row;
        this.col = col;

        isWhite = row % 2 == col % 2;

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

    public Piece getPiece()
    {
        return piece;
    };

    public void render(Graphics graphics)
    {
        graphics.setColor(isWhite ? Color.white : Color.black);
        graphics.fillRect(x, y, width, height);


        if (piece != null)
        {
            piece.render(graphics);
        }

    }
}
