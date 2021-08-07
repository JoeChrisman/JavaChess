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

        // search in a grid around the king
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

        // check for castling moves
        if (getTimesMoved() == 0)
        {
            // left/right does not always correspond to kingside/queenside it could also be queenside/kingside
            Piece leftRook = squares[row][0].getPiece();
            Piece rightRook = squares[row][7].getPiece();

            // first make sure rooks are there and have not moved
            boolean canCastleLeftOfKing = leftRook instanceof Rook && leftRook.getTimesMoved() == 0;
            boolean canCastleRightOfKing = rightRook instanceof Rook && rightRook.getTimesMoved() == 0;

            // see if we can castle left
            for (int backRankCol = 1; backRankCol < col && canCastleLeftOfKing; backRankCol++) {
                // dont castle through pieces
                if (squares[row][backRankCol].getPiece() != null)
                {
                    canCastleLeftOfKing = false;
                }
            }
            // see if we can castle right
            for (int backRankCol = col + 1; backRankCol < 7 && canCastleRightOfKing; backRankCol++) {
                // dont castle through pieces
                if (squares[row][backRankCol].getPiece() != null)
                {
                    canCastleRightOfKing = false;
                }
            }

            if (canCastleLeftOfKing)
            {
                legalMoves.add(squares[row][col - 2]);
            }
            if (canCastleRightOfKing)
            {
                legalMoves.add(squares[row][col + 2]);
            }
        }


        return legalMoves;
    }

}
