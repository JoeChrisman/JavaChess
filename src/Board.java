import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class Board implements Serializable {

    private Pieces pieces;
    private Square[][] squares;
    private Piece mostRecentPieceMoved;

    private boolean isFromWhitesPerspective;

    public Board(boolean isFromWhitesPerspective) {
        this.isFromWhitesPerspective = isFromWhitesPerspective;
        squares = new Square[Constants.SQUARES_WIDE][Constants.SQUARES_WIDE];
        pieces = new Pieces();

        for (int row = 0; row < squares.length; row++) {
            for (int col = 0; col < squares.length; col++) {
                squares[row][col] = new Square(row, col);
                if (row == 1) {
                    addPiece(Constants.PieceType.PAWN, !isFromWhitesPerspective, row, col);
                }
                if (row == 6) {
                    addPiece(Constants.PieceType.PAWN, isFromWhitesPerspective, row, col);
                }
            }
        }

        for (int row = 0; row < squares.length; row += 7)
        {
            boolean pieceIsWhite = isFromWhitesPerspective == (row == 7);

            addPiece(Constants.PieceType.ROOK, pieceIsWhite, row, 0);
            addPiece(Constants.PieceType.KNIGHT, pieceIsWhite, row, 1);
            addPiece(Constants.PieceType.BISHOP, pieceIsWhite, row, 2);
            addPiece(Constants.PieceType.KING, pieceIsWhite, row, isFromWhitesPerspective ? 4 : 3);
            addPiece(Constants.PieceType.QUEEN, pieceIsWhite, row, isFromWhitesPerspective ? 3 : 4);
            addPiece(Constants.PieceType.BISHOP, pieceIsWhite, row, 5);
            addPiece(Constants.PieceType.KNIGHT, pieceIsWhite, row, 6);
            addPiece(Constants.PieceType.ROOK, pieceIsWhite, row, 7);
        }
    }

    public Board deepClone()
    {
        try
        {
            ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
            ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput);
            objectOutput.writeObject(this);

            ByteArrayInputStream byteInput = new ByteArrayInputStream(byteOutput.toByteArray());
            ObjectInputStream objectInput = new ObjectInputStream(byteInput);
            return (Board) objectInput.readObject();

        } catch (IOException | ClassNotFoundException exc)
        {
            assert false;
        }
        return null;
    }

    // returns piece captured which may be null
    public Piece movePiece(Square squareMovingFrom, Square squareMovingTo)
    {
        Piece pieceMoving = squareMovingFrom.getPiece();
        Piece pieceCaptured = squareMovingTo.getPiece();

        // move piece to target square
        pieceMoving.moveTo(squareMovingTo);
        setMostRecentPieceMoved(pieceMoving);
        squareMovingFrom.setPiece(null);

        // if a pawn is capturing using en passant
        if (pieceMoving instanceof Pawn && pieceCaptured == null && squareMovingFrom.getCol() != squareMovingTo.getCol())
        {
            // find the pawn captured by en passant and remove it
            Square enPassantCaptureSquare = squares[squareMovingFrom.getRow()][squareMovingTo.getCol()];
            // store piece captured
            pieceCaptured = enPassantCaptureSquare.getPiece();
            enPassantCaptureSquare.setPiece(null);
        }


        // if a king is castling
        if (pieceMoving instanceof King && Math.abs(squareMovingFrom.getCol() - squareMovingTo.getCol()) > 1)
        {
            // if castling left
            if (squareMovingTo.getCol() < 3)
            {
                squares[pieceMoving.getRow()][0].getPiece().moveTo(squares[pieceMoving.getRow()][pieceMoving.getCol() + 1]);
            }
            // if castling right
            if (squareMovingTo.getCol() > 4)
            {
                squares[pieceMoving.getRow()][7].getPiece().moveTo(squares[pieceMoving.getRow()][pieceMoving.getCol() - 1]);
            }
        }

        // remove piece captured
        if (pieceCaptured != null)
        {
            pieces.removePiece(pieceCaptured);
        }
        return pieceCaptured;
    }

    public void movePieceBack(Square squareMovedFrom, Square squareMovedTo, Piece pieceCaptured, Piece pieceMovedBefore)
    {
        mostRecentPieceMoved = pieceMovedBefore;

        Piece pieceMoved = squareMovedTo.getPiece();

        // move piece back to where it came from
        pieceMoved.moveTo(squareMovedFrom);
        pieceMoved.setTimesMoved(pieceMoved.getTimesMoved() - 2);

        // if a king was castled
        if (pieceMoved instanceof King && Math.abs(squareMovedFrom.getCol() - squareMovedTo.getCol()) > 1)
        {
            // if castled left, move rook back
            if (squareMovedTo.getCol() < 3)
            {
                Square squareWithRook = squares[pieceMoved.getRow()][squareMovedTo.getCol() + 1];
                Piece leftRook = squareWithRook.getPiece();
                leftRook.moveTo(squares[pieceMoved.getRow()][0]);
                leftRook.setTimesMoved(leftRook.getTimesMoved() - 2);
                squareWithRook.setPiece(null);
            }
            // if castled right, move rook back
            if (squareMovedTo.getCol() > 4)
            {
                Square squareWithRook = squares[pieceMoved.getRow()][squareMovedTo.getCol() - 1];
                Piece rightRook = squareWithRook.getPiece();
                rightRook.moveTo(squares[pieceMoved.getRow()][7]);
                rightRook.setTimesMoved(rightRook.getTimesMoved() - 2);
                squareWithRook.setPiece(null);
            }
        }

        // if a pawn was captured by en passant
        if (pieceCaptured != null && pieceMoved instanceof Pawn && pieceCaptured.getRow() == squareMovedFrom.getRow())
        {
            // replace captured pawn
            squares[pieceCaptured.getRow()][pieceCaptured.getCol()].setPiece(pieceCaptured);
            // in en passant the captured piece is not from where we moved the piece to
            squareMovedTo.setPiece(null);
        }
        else
        {
            // but in all other cases the captured piece is where we moved the piece to
            squareMovedTo.setPiece(pieceCaptured);
        }

        if (pieceCaptured != null)
        {
            pieces.addPiece(pieceCaptured);

        }

    }

    public ArrayList<Square> getLegalMoves(Square squareMovingFrom)
    {
        Piece pieceMoving = squareMovingFrom.getPiece();
        ArrayList<Square> pseudoLegalMoves = squareMovingFrom.getPiece().getPseudoLegalMoves(this);
        ArrayList<Square> legalMoves = new ArrayList<>();

        for (Square pseudoLegalMove : pseudoLegalMoves)
        {
            Piece lastPieceMovedBefore = mostRecentPieceMoved;

            Piece pieceCaptured = movePiece(squareMovingFrom, pseudoLegalMove);

            // dont make a move that puts our own king in check (including castling)
            if (!isKingInCheck(pieceMoving.isWhite()))
            {
                // make sure we dont castle over check
                if (pieceMoving instanceof King && Math.abs(pseudoLegalMove.getCol() - squareMovingFrom.getCol()) > 1)
                {
                    boolean canCastle = true;
                    int backRankCol = Math.min(squareMovingFrom.getCol(), pseudoLegalMove.getCol() + 1);
                    int maxCol = Math.max(squareMovingFrom.getCol(), pseudoLegalMove.getCol() - 1);
                    for (; backRankCol < maxCol; backRankCol++)
                    {
                        if (isSquareInCheck(squares[pieceMoving.getRow()][backRankCol], pieceMoving.isWhite()))
                        {
                            canCastle = false;
                            break;
                        }
                    }
                    if (canCastle)
                    {
                        legalMoves.add(pseudoLegalMove);
                    }
                }
                else
                {
                    legalMoves.add(pseudoLegalMove);
                }
            }

            movePieceBack(squareMovingFrom, pseudoLegalMove, pieceCaptured, lastPieceMovedBefore);

        }
        return legalMoves;
    }

    public boolean isKingInCheck(boolean isKingWhite)
    {
        Piece king = pieces.getKing(isKingWhite);
        return isSquareInCheck(squares[king.getRow()][king.getCol()], isKingWhite);
    }

    public boolean isSquareInCheck(Square squareInCheck, boolean isKingWhite)
    {
        ArrayList<Piece> attackingPieces = pieces.getPieces(!isKingWhite);
        for (Piece attackingPiece : attackingPieces)
        {
            for (Square attackedSquare : attackingPiece.getPseudoLegalMoves(this))
            {
                if (attackedSquare.getRow() == squareInCheck.getRow() && attackedSquare.getCol() == squareInCheck.getCol())
                {
                    return true;
                }
            }
        }
        return false;
    }

    public void addPiece(Constants.PieceType pieceType, boolean isWhite, int row, int col) {
        Square square = squares[row][col];
        Piece pieceToAdd = null;
        switch(pieceType) {
            case KING: pieceToAdd = new King(square, isWhite); break;
            case PAWN: pieceToAdd = new Pawn(square, isWhite); break;
            case ROOK: pieceToAdd = new Rook(square, isWhite); break;
            case BISHOP: pieceToAdd = new Bishop(square, isWhite); break;
            case KNIGHT: pieceToAdd = new Knight(square, isWhite); break;
            case QUEEN: pieceToAdd = new Queen(square, isWhite); break;
        }
        assert pieceToAdd != null;
        pieces.addPiece(pieceToAdd);
    }

    public Square[][] getSquares()
    {
        return squares;
    }

    public Pieces getPieces()
    {
        return pieces;
    }

    public boolean isFromWhitesPerspective() {
        return isFromWhitesPerspective;
    }

    public void render(Graphics graphics) {
        for (Square[] row : squares) {
            for (Square square : row) {
                square.render(graphics);
            }
        }
    }

    public Square getSquareClicked(Point mouseLocation) {
        for (Square[] row : squares) {
            for (Square square : row) {
                if (square.contains(mouseLocation)) {
                    return square;
                }
            }
        }
        return null;
    }

    public void setMostRecentPieceMoved(Piece pieceMoved)
    {
        mostRecentPieceMoved = pieceMoved;
    }

    public Piece getMostRecentPieceMoved()
    {
        return mostRecentPieceMoved;
    }
}
