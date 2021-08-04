import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Pawn extends Piece {

    private final ImageIcon pawnIcon = new ImageIcon(new ImageIcon("Images/pawn_" + (super.isWhite() ? "w" : "b") + ".png").getImage()
            .getScaledInstance(Constants.SQUARE_SIZE, Constants.SQUARE_SIZE, Image.SCALE_DEFAULT));

    public Pawn(Square square, boolean isWhite)
    {
        super(square, isWhite);
    }

    @Override
    public ImageIcon getImage()
    {
        return pawnIcon;
    }

    @Override
    public ArrayList<Square> getLegalMoves(Board board)
    {
        ArrayList<Square> legalMoves = new ArrayList<>();

        int row = getRow();
        int col = getCol();

        Square[][] squares = board.getSquares();
        boolean isFromWhitesPerspective = board.isFromWhitesPerspective();

        // which direction our pawn is going
        int verticalOffset = isWhite() == isFromWhitesPerspective ? -1 : 1;

        // add forward pawn moves
        if (isWhite() && row > 0 || !isWhite() && row < 7)
        {
            Square oneSquareAhead = squares[row + verticalOffset][col];
            if (oneSquareAhead.getPiece() == null)
            {
                legalMoves.add(oneSquareAhead);
                // if pawn has not moved it can move two squares
                if (getTimesMoved() == 0)
                {
                    Square twoSquaresAhead = squares[row + 2 * verticalOffset][col];
                    if (twoSquaresAhead.getPiece() == null)
                    {
                        legalMoves.add(twoSquaresAhead);
                    }
                }
            }
        }
        // capturing right with pawn
        if (col < 7)
        {
            Square rightCapture = squares[row + verticalOffset][col + 1];
            Piece pieceToCapture = rightCapture.getPiece();
            if (pieceToCapture != null && pieceToCapture.isWhite() != isWhite())
            {
                legalMoves.add(rightCapture);
            }
            // capturing right by en passant
            if (row == (isFromWhitesPerspective == isWhite() ? 3 : 4)) {
                pieceToCapture = squares[row][col + 1].getPiece();
                if (pieceToCapture instanceof Pawn && pieceToCapture.isWhite() != isWhite() &&
                    board.getMostRecentPieceMoved().equals(pieceToCapture) && pieceToCapture.getTimesMoved() == 1)
                {
                    legalMoves.add(squares[row + verticalOffset][col + 1]);
                }
            }
        }
        // capturing left with pawn
        if (col > 0)
        {
            Square leftCapture = squares[row + verticalOffset][col - 1];
            Piece pieceToCapture = leftCapture.getPiece();
            if (pieceToCapture != null && pieceToCapture.isWhite() != isWhite())
            {
                legalMoves.add(leftCapture);
            }
            // capturing left by en passant
            if (row == (isFromWhitesPerspective == isWhite() ? 3 : 4)) {
                pieceToCapture = squares[row][col - 1].getPiece();
                if (pieceToCapture instanceof Pawn && pieceToCapture.isWhite() != isWhite() &&
                    board.getMostRecentPieceMoved().equals(pieceToCapture) && pieceToCapture.getTimesMoved() == 1)
                {
                    legalMoves.add(squares[row + verticalOffset][col - 1]);
                }
            }
        }
        return legalMoves;
    }
}
