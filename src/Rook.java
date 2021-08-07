
import java.util.ArrayList;

public class Rook extends Piece
{

    public Rook(Square square,  boolean isWhite)
    {
        super(square, isWhite);
        loadIcon("Rook");
    }

    @Override
    public ArrayList<Square> getPseudoLegalMoves(Board board) {
        int row = getRow();
        int col = getCol();

        Square[][] squares = board.getSquares();
        ArrayList<Square> legalMoves = new ArrayList<>();

        for (Constants.Direction cardinalDirection : Constants.Direction.getCardinalDirections())
        {
            for (int distanceFromRook = 1; distanceFromRook < 8; distanceFromRook++)
            {
                Square targetSquare = null;
                try
                {
                    switch(cardinalDirection) {
                        case NORTH: targetSquare = squares[row - distanceFromRook][col]; break;
                        case EAST: targetSquare = squares[row][col + distanceFromRook]; break;
                        case SOUTH: targetSquare = squares[row + distanceFromRook][col]; break;
                        case WEST: targetSquare = squares[row][col - distanceFromRook]; break;
                    }
                }
                catch (IndexOutOfBoundsException exc)
                {
                    // we went off the board for this direction, so stop and go try next direction
                    break;
                }
                assert targetSquare != null;
                Piece pieceOnTargetSquare = targetSquare.getPiece();
                if (pieceOnTargetSquare == null)
                {
                    legalMoves.add(targetSquare);
                }
                else
                {
                    // add possible capture move
                    if (pieceOnTargetSquare.isWhite() != isWhite())
                    {
                        legalMoves.add(targetSquare);
                    }
                    // dont jump over pieces
                    break;
                }
            }
        }

        return legalMoves;
    }

}
