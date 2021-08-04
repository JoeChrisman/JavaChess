import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public abstract class Piece
{

    private int row;
    private int col;
    private boolean isWhite;

    private int timesMoved;

    // when a user is dragging a piece, it is not snapped to grid
    private Point positionWhileDragging;

    public Piece(Square square, boolean isWhite)
    {
        this.isWhite = isWhite;
        timesMoved = -1;
        moveTo(square);
    }

    public void moveTo(Square square)
    {
        row = square.getRow();
        col = square.getCol();
        timesMoved++;

        square.setPiece(this);
    }

    public void resetDrag()
    {
        positionWhileDragging = null;
    }

    public void dragTo(Point location)
    {
        this.positionWhileDragging = location;
    }

    public int getTimesMoved()
    {
        return timesMoved;
    }

    public int getRow()
    {
        return row;
    }

    public int getCol()
    {
        return col;
    }

    public boolean isWhite()
    {
        return isWhite;
    }

    public void render(Graphics graphics)
    {
        if (positionWhileDragging != null)
        {
            getImage().paintIcon(Main.getFrame(), graphics, positionWhileDragging.x, positionWhileDragging.y);
        }
        else {
            getImage().paintIcon(Main.getFrame(), graphics, col * Constants.SQUARE_SIZE, row * Constants.SQUARE_SIZE);

        }
    }

    public abstract ImageIcon getImage();
    public abstract ArrayList<Square> getLegalMoves(Board board);
}
