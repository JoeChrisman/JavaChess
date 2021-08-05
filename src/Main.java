import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class Main extends JPanel
{

    public static void main(String[] args)
    {
        new Main();
    }

    private ChessGame game = new ChessGame(true);
    private MouseInputHandler mouseHandler = new MouseInputHandler();
    private static JFrame frame = new JFrame("Chess");


    public Main()
    {
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Constants.WINDOW_SIZE, Constants.WINDOW_SIZE + Constants.FRAME_INSET);
        frame.addMouseListener(mouseHandler);
        frame.addMouseMotionListener(mouseHandler);
        frame.add(this);

    }

    public static Frame getFrame()
    {
        return frame;
    }

    @Override
    public void paintComponent(Graphics graphics)
    {
        game.render(graphics);

        Piece pieceDragging = mouseHandler.getPieceDragging();
        if (pieceDragging != null)
        {
            pieceDragging.render(graphics);
        }

        /*
        // fun hacked together way to show all your legal moves
        graphics.setColor(Color.red);
        Board board = game.getBoard();
        for (Piece piece : board.getPieces().getPieces(game.isWhitesTurn()))
        {
            Square squareForPiece = board.getSquares()[piece.getRow()][piece.getCol()];
            if (squareForPiece.getPiece() != null)
            {
                ArrayList<Square> allLegalMoves = MoveValidator.getLegalMoves(board, squareForPiece);
                for (Square legalMove : allLegalMoves)
                {
                    graphics.drawLine(
                            (int)squareForPiece.getCenterX(),
                            (int)squareForPiece.getCenterY(),
                            (int)legalMove.getCenterX(),
                            (int)legalMove.getCenterY()
                    );
                }
            }
        }*/

    }

    private class MouseInputHandler implements MouseListener, MouseMotionListener
    {

        private Piece pieceDragging;
        private Square squareMovingFrom;
        private ArrayList<Square> legalMovesForPieceDragging;

        public Piece getPieceDragging()
        {
            return pieceDragging;
        }

        public Point getCoordinateAdjustedForFrameInsets(Point coordinate)
        {
            return new Point(coordinate.x, coordinate.y - Constants.FRAME_INSET);
        }

        public Point getPieceLocationOffset(Point pieceLocation)
        {
            // offset dragged piece to render from center instead of corner
            return getCoordinateAdjustedForFrameInsets(new Point(
                    pieceLocation.x - Constants.SQUARE_SIZE / 2,
                    pieceLocation.y - Constants.SQUARE_SIZE / 2
            ));
        }

        @Override
        public void mousePressed(MouseEvent mouseEvent)
        {
            Point mouseLocation = mouseEvent.getPoint();
            squareMovingFrom = getSquareClicked(mouseLocation);

            if (squareMovingFrom != null)
            {
                pieceDragging = squareMovingFrom.getPiece();
                if (pieceDragging != null)
                {
                    if (pieceDragging.isWhite() == game.isWhitesTurn())
                    {
                        Board board = game.getBoard();
                        legalMovesForPieceDragging = MoveValidator.getLegalMoves(board, squareMovingFrom);
                        for (Square legalMove : legalMovesForPieceDragging)
                        {
                            legalMove.highlight(Constants.ColorPreset.HIGHLIGHT_LEGAL_MOVE);
                        }
                        pieceDragging.dragTo(getPieceLocationOffset(mouseLocation));
                        // temporarily remove the piece we are moving from it's square so it disappears while dragging
                        squareMovingFrom.removePiece();
                        repaint();
                    }
                    else
                    {
                        pieceDragging = null;
                    }

                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent)
        {
            if (pieceDragging != null)
            {
                Square squareMovingTo = getSquareClicked(mouseEvent.getPoint());
                // restore piece we are moving back onto to its original square before calling board.movePiece()
                squareMovingFrom.setPiece(pieceDragging);
                if (squareMovingTo != null && legalMovesForPieceDragging.contains(squareMovingTo))
                {
                    game.movePiece(squareMovingFrom, squareMovingTo);
                } // else, cancel the move. already handled by piece restoration
                for (Square legalMove : legalMovesForPieceDragging)
                {
                    legalMove.removeHighlight();
                }
                pieceDragging.resetDrag();
                squareMovingFrom = null;
                pieceDragging = null;
                repaint();
            }
        }

        @Override
        public void mouseDragged(MouseEvent mouseEvent)
        {
            if (pieceDragging != null)
            {
                pieceDragging.dragTo(getPieceLocationOffset(mouseEvent.getPoint()));
                repaint();
            }
        }

        public Square getSquareClicked(Point mouseLocation)
        {
            return game.getBoard().getSquareClicked(getCoordinateAdjustedForFrameInsets(mouseLocation));
        }

        @Override
        public void mouseMoved(MouseEvent e) {}
        @Override
        public void mouseEntered(MouseEvent e) {}
        @Override
        public void mouseExited(MouseEvent e) {}
        @Override
        public void mouseClicked(MouseEvent e) {}
    }
}
