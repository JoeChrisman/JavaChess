import java.util.ArrayList;

public class King extends Piece
{

    public King(Square square,  boolean isWhite)
    {
        super(square, isWhite);
        loadIcon("King");
    }

    @Override
    public ArrayList<Square> getPseudoLegalMoves(Board board)
    {
        int row = getRow();
        int col = getCol();

        Square[][] squares = board.getSquares();
        ArrayList<Square> legalMoves = new ArrayList<>();

        // set up loop to avoid going off the board
        int minRowOffset = row > 0 ? -1 : 0;
        int maxRowOffset = row < 7 ? 1 : 0;
        int minColOffset = col > 0 ? -1 : 0;
        int maxColOffset = col < 7 ? 1 : 0;

        for (int rowOffset = minRowOffset; rowOffset <= maxRowOffset; rowOffset++)
        {
            for (int colOffset = minColOffset; colOffset <= maxColOffset; colOffset++)
            {
                // king moving to it's own square is not a legal move
                if (rowOffset == 0 && colOffset == 0)
                {
                    continue;
                }
                Square targetSquare = squares[row + rowOffset][col + colOffset];

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
        }

        return legalMoves;
    }

}
