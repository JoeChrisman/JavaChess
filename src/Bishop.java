import java.util.ArrayList;

public class Bishop extends Piece
{

    public Bishop(Square square, boolean isWhite)
    {
        super(square, isWhite);
        loadIcon("Bishop");
    }

    @Override
    public ArrayList<Square> getPseudoLegalMoves(Board board)
    {
        int row = getRow();
        int col = getCol();

        Square[][] squares = board.getSquares();
        ArrayList<Square> legalMoves = new ArrayList<>();

        for (Constants.Direction ordinalDirection : Constants.Direction.getOrdinalDirections())
        {
            for (int distanceFromBishop = 1; distanceFromBishop < 8; distanceFromBishop++)
            {
                Square targetSquare = null;
                try
                {
                    switch (ordinalDirection)
                    {
                        case NORTH_WEST: targetSquare = squares[row - distanceFromBishop][col - distanceFromBishop]; break;
                        case SOUTH_WEST: targetSquare = squares[row + distanceFromBishop][col - distanceFromBishop]; break;
                        case SOUTH_EAST: targetSquare = squares[row + distanceFromBishop][col + distanceFromBishop]; break;
                        case NORTH_EAST: targetSquare = squares[row - distanceFromBishop][col + distanceFromBishop]; break;
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
