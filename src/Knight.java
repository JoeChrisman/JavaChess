import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Knight extends Piece
{
    private final ImageIcon knightIcon = new ImageIcon(new ImageIcon("Images/knight_" + (super.isWhite() ? "w" : "b") + ".png").getImage()
            .getScaledInstance(Constants.SQUARE_SIZE, Constants.SQUARE_SIZE, Image.SCALE_DEFAULT));

    public Knight(Square square, boolean isWhite)
    {
        super(square, isWhite);
    }



    @Override
    public ImageIcon getImage() {
        return knightIcon;
    }

    @Override
    public ArrayList<Square> getLegalMoves(Board board) {
        int row = getRow();
        int col = getCol();

        Square[][] squares = board.getSquares();
        ArrayList<Square> legalMoves = new ArrayList<>();

        // offsets for each knight move in x,y format
        byte[][] offsets = new byte[][] {
                {-1, -2},
                {1, -2},
                {-2, -1},
                {2, -1},
                {-2, 1},
                {2, 1},
                {-1, 2},
                {1, 2}
        };

        for (byte[] offset : offsets)
        {
            Square targetSquare;
            try
            {
                targetSquare = squares[row + offset[1]][col + offset[0]];
            }
            catch (IndexOutOfBoundsException exc)
            {
                // the square we wanted to go to is off the board, so skip and check the next one
                continue;
            }
            assert targetSquare != null;
            Piece pieceOnTargetSquare = targetSquare.getPiece();
            if (pieceOnTargetSquare == null)
            {
                legalMoves.add(targetSquare);
            }
            else if (pieceOnTargetSquare.isWhite() != isWhite())
            {
                legalMoves.add(targetSquare);
            }
        }
        return legalMoves;
    }
}
