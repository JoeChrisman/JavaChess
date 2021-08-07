import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Main extends JPanel
{

    public static void main(String[] args)
    {
        new Main();
    }

    private ChessGame game = new ChessGame(true);
    private InputHandler inputHandler = new InputHandler();
    private static JFrame frame = new JFrame("Chess");

    public Main()
    {
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Constants.WINDOW_SIZE, Constants.WINDOW_SIZE + Constants.FRAME_INSET);
        frame.addKeyListener(inputHandler);
        frame.addMouseListener(inputHandler);
        frame.addMouseMotionListener(inputHandler);
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

        Piece pieceDragging = inputHandler.getPieceDragging();
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
                ArrayList<Square> allLegalMoves = board.getLegalMoves(squareForPiece);
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

    private class InputHandler implements MouseListener, MouseMotionListener, KeyListener
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
            // also offset for JFrame insets
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
                        legalMovesForPieceDragging = board.getLegalMoves(squareMovingFrom);

                        for (Square legalMove : legalMovesForPieceDragging)
                        {
                            legalMove.highlight(Constants.ColorPreset.HIGHLIGHT_LEGAL_MOVE);
                        }
                        pieceDragging.dragTo(getPieceLocationOffset(mouseLocation));
                        // temporarily remove the piece we are moving from it's square so it disappears while dragging
                        squareMovingFrom.setPiece(null);
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
                for (Square legalMove : legalMovesForPieceDragging)
                {
                    legalMove.removeHighlight();
                }
                pieceDragging.resetDrag();
                pieceDragging = null;

                if (squareMovingTo != null && legalMovesForPieceDragging.contains(squareMovingTo))
                {
                    game.movePiece(squareMovingFrom, squareMovingTo);
                } // else, cancel the move. already handled by piece restoration
                squareMovingFrom = null;
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

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            int keyCode = keyEvent.getKeyCode();
            switch(keyCode)
            {
                case KeyEvent.VK_LEFT: game.undo(); break;
                case KeyEvent.VK_RIGHT: game.redo(); break;
            }
            if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT)
            {
                if (pieceDragging != null)
                {
                    pieceDragging.resetDrag();

                }
                squareMovingFrom = null;
                pieceDragging = null;
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
        @Override
        public void keyTyped(KeyEvent e) {}
        @Override
        public void keyReleased(KeyEvent e) {}
    }
}
