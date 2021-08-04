import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Main extends JPanel
{

    public static void main(String[] args)
    {
        new Main();
    }

    private static Board board = new Board(true);
    private static JFrame frame = new JFrame("Chess");
    private MouseInputHandler mouseHandler = new MouseInputHandler();

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
        board.render(graphics);

        Piece pieceDragging = mouseHandler.getPieceDragging();
        if (pieceDragging != null)
        {
            pieceDragging.render(graphics);
        }

    }

    private class MouseInputHandler implements MouseListener, MouseMotionListener
    {

        private Piece pieceDragging;
        private Square squareMovingFrom;

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
                    pieceDragging.dragTo(getPieceLocationOffset(mouseLocation));
                    // temporarily remove the piece we are moving from it's square so it disappears while dragging
                    squareMovingFrom.setPiece(null);
                    repaint();
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
                if (squareMovingTo != null && pieceDragging.getLegalMoves(board).contains(squareMovingTo))
                {
                    board.movePiece(squareMovingFrom, squareMovingTo);
                } // else, cancel the move. already handled by piece restoration
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
            return board.getSquareClicked(getCoordinateAdjustedForFrameInsets(mouseLocation));
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
