import java.util.ArrayList;

public class Queen extends Piece
{

    public Queen(Square square, boolean isWhite)
    {
        super(square, isWhite);
        loadIcon("Queen");
    }

    @Override
    public ArrayList<Square> getPseudoLegalMoves(Board board) {
        int row = getRow();
        int col = getCol();

        Square[][] squares = board.getSquares();
        ArrayList<Square> legalMoves = new ArrayList<>();

        for (Constants.Direction direction : Constants.Direction.values())
        {
            for (int distanceFromQueen = 1; distanceFromQueen < 8; distanceFromQueen++)
            {
                Square targetSquare = null;
                try {
                    switch (direction)
                    {
                        case NORTH: targetSquare = squares[row - distanceFromQueen][col]; break;
                        case NORTH_EAST: targetSquare = squares[row - distanceFromQueen][col + distanceFromQueen]; break;
                        case EAST: targetSquare = squares[row][col + distanceFromQueen]; break;
                        case SOUTH_EAST: targetSquare = squares[row + distanceFromQueen][col + distanceFromQueen]; break;
                        case SOUTH: targetSquare = squares[row + distanceFromQueen][col]; break;
                        case SOUTH_WEST: targetSquare = squares[row + distanceFromQueen][col - distanceFromQueen]; break;
                        case WEST: targetSquare = squares[row][col - distanceFromQueen]; break;
                        case NORTH_WEST: targetSquare = squares[row - distanceFromQueen][col - distanceFromQueen]; break;
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
