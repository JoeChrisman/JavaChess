import java.util.ArrayList;

public class MoveValidator {

    public static ArrayList<Square> getLegalMoves(Board board, Square squareMovingFrom)
    {
        Piece pieceMoving = squareMovingFrom.getPiece();
        ArrayList<Square> pseudoLegalMoves = squareMovingFrom.getPiece().getPseudoLegalMoves(board);
        ArrayList<Square> legalMoves = new ArrayList<>();

        for (Square pseudoLegalMove : pseudoLegalMoves)
        {
            Piece pieceCaptured = pseudoLegalMove.getPiece();
            board.movePiece(squareMovingFrom, pseudoLegalMove, true);

            if (!isKingInCheck(board, pieceMoving.isWhite()))
            {
                legalMoves.add(pseudoLegalMove);
            }

            board.movePiece(pseudoLegalMove, squareMovingFrom, true);
            pseudoLegalMove.setPiece(pieceCaptured);
            if (pieceCaptured != null)
            {
                board.getPieces().addPiece(pieceCaptured);
            }
            pieceMoving.setTimesMoved(pieceMoving.getTimesMoved() - 2);

        }
        return legalMoves;
    }

    public static boolean isKingInCheck(Board board, boolean isKingWhite)
    {
        Pieces pieces = board.getPieces();
        ArrayList<Piece> attackingPieces = pieces.getPieces(!isKingWhite);
        Piece king = pieces.getKing(isKingWhite);

        for (Piece attackingPiece : attackingPieces)
        {
            for (Square attackedSquare : attackingPiece.getPseudoLegalMoves(board))
            {
                if (attackedSquare.getRow() == king.getRow() && attackedSquare.getCol() == king.getCol())
                {
                    return true;
                }
            }
        }
        return false;
    }
}
