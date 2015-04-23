package pong;

import static jalse.JALSEBuilder.buildManualJALSE;
import static jalse.attributes.Attributes.newTypeOf;
import jalse.JALSE;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;
import javax.swing.Timer;

import pong.actions.MoveElements;
import pong.entities.Ball;
import pong.entities.Paddle;
import pong.entities.ScoreBoard;
import pong.entities.Table;
import pong.entities.TableElement;
import pong.listeners.BounceBall;

@SuppressWarnings("serial")
public class PongPanel extends JPanel implements ActionListener, KeyListener {

    private static final Font PAUSED_FONT = new Font("Dialog", Font.BOLD, 32);

    private static final Font SCORE_FONT = new Font("Dialog", Font.BOLD, 64);

    private static void drawCentredString(final Graphics g, final String str, final int x, final int y) {
	final int xOffset = g.getFontMetrics().stringWidth(str) / 2;
	g.drawString(str, x - xOffset, y);
    }

    private static void drawElement(final Graphics g, final TableElement element) {
	final Point position = element.getPosition();
	final Dimension size = element.getSize();
	g.fillRect(position.x, position.y, size.width, size.height);
    }

    private final JALSE jalse;
    private boolean paused;
    private boolean newGame;

    public PongPanel() {
	paused = true;
	newGame = true;
	// Manually ticked JALSE
	jalse = buildManualJALSE();
	// Create data model
	createEntities();
	// Size to table size
	setPreferredSize(getTable().getSize());
	// Set black background
	setBackground(Color.BLACK);
	// Listener for key events
	setFocusable(true);
	addKeyListener(this);
	// Start ticking and rendering (60 FPS)
	new Timer(1000 / 60, this).start();
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
	// Reset for new game
	if (newGame) {
	    resetLeftPaddle();
	    resetRightPaddle();
	    resetBall();
	    newGame = false;
	}

	// Check paused
	if (!paused) {
	    // Tick manual engine
	    jalse.resume();

	    // Check winner
	    final Ball ball = getTable().getBall();
	    final Point ballPos = ball.getPosition();
	    final Dimension ballSize = ball.getSize();
	    if (ballPos.getX() <= 0) {
		newGame = true;
		getScoreBoard().rightWins();
	    } else if (ballPos.getX() + ballSize.getWidth() >= getTable().getSize().getWidth()) {
		newGame = true;
		getScoreBoard().leftWins();
	    }
	}

	// Request repaint
	repaint();
    }

    private void createEntities() {
	// Create empty score board
	final ScoreBoard board = jalse.newEntity(ScoreBoard.ID, ScoreBoard.class);
	board.setLeftScore(0);
	board.setRightScore(0);
	board.setLastWinner(ThreadLocalRandom.current().nextBoolean() ? Paddle.LEFT_ID : Paddle.RIGHT_ID);

	// Create table
	final Table table = jalse.newEntity(Table.ID, Table.class);
	table.setSize(new Dimension(700, 500));
	// Move the elements
	table.scheduleForActor(new MoveElements(), 0, 1, TimeUnit.MILLISECONDS);

	// Create paddles
	final Dimension paddleSize = new Dimension(15, 80);
	final int paddleSpeed = 20;
	final Paddle left = table.newEntity(Paddle.LEFT_ID, Paddle.class);
	left.setSize(paddleSize);
	left.setSpeed(paddleSpeed);
	left.stopMoving();
	final Paddle right = table.newEntity(Paddle.RIGHT_ID, Paddle.class);
	right.setSize(paddleSize);
	right.setSpeed(paddleSpeed);
	right.stopMoving();

	// Create ball
	final Ball ball = table.newEntity(Ball.ID, Ball.class);
	ball.setSize(new Dimension(20, 20));
	ball.setSpeed(5);
	ball.stopMoving();
	ball.addAttributeListener("position", newTypeOf(Point.class), new BounceBall());
    }

    private ScoreBoard getScoreBoard() {
	return jalse.getEntityAsType(ScoreBoard.ID, ScoreBoard.class);
    }

    private Table getTable() {
	return jalse.getEntityAsType(Table.ID, Table.class);
    }

    @Override
    public void keyPressed(final KeyEvent e) {
	final Table t = getTable();
	switch (e.getKeyCode()) {
	case KeyEvent.VK_UP:
	    t.getRightPaddle().moveUp();
	    break;
	case KeyEvent.VK_DOWN:
	    t.getRightPaddle().moveDown();
	    break;
	case KeyEvent.VK_W:
	    t.getLeftPaddle().moveUp();
	    break;
	case KeyEvent.VK_S:
	    t.getLeftPaddle().moveDown();
	    break;
	}
    }

    @Override
    public void keyReleased(final KeyEvent e) {
	final Table t = getTable();
	switch (e.getKeyCode()) {
	case KeyEvent.VK_UP:
	    t.getRightPaddle().stopMoving();
	    break;
	case KeyEvent.VK_DOWN:
	    t.getRightPaddle().stopMoving();
	    break;
	case KeyEvent.VK_W:
	    t.getLeftPaddle().stopMoving();
	    break;
	case KeyEvent.VK_S:
	    t.getLeftPaddle().stopMoving();
	    break;
	case KeyEvent.VK_P:
	    paused = !paused;
	    break;
	case KeyEvent.VK_SPACE:
	    newGame = true;
	    break;
	}
    }

    @Override
    public void keyTyped(final KeyEvent e) {}

    @Override
    protected void paintComponent(final Graphics g) {
	// Draw component as before
	super.paintComponent(g);

	// Check game has started
	if (newGame) {
	    return;
	}

	// White on black colour scheme
	g.setColor(Color.WHITE);

	final Dimension tableSize = getTable().getSize();

	// Draw half-way line
	for (int y = 0; y < tableSize.height; y += tableSize.height / 20) {
	    g.drawLine(tableSize.width / 2, y, tableSize.width / 2, y += tableSize.height / 10);
	}

	// Draw scores
	g.setFont(SCORE_FONT);
	final int scoreXOffSet = tableSize.width / 8;
	final int scoreYOff = tableSize.height / 8;
	drawCentredString(g, getScoreBoard().getLeftScore().toString(), tableSize.width / 2 - scoreXOffSet, scoreYOff);
	drawCentredString(g, getScoreBoard().getRightScore().toString(), tableSize.width / 2 + scoreXOffSet, scoreYOff);

	// Draw paddles
	drawElement(g, getTable().getLeftPaddle());
	drawElement(g, getTable().getRightPaddle());

	// Draw ball
	drawElement(g, getTable().getBall());

	// Draw paused
	if (paused) {
	    g.setFont(PAUSED_FONT);
	    drawCentredString(g, "PAUSED", tableSize.width / 2, tableSize.height / 4);
	}

	// Clean up
	g.dispose();
    }

    private void resetBall() {
	final Ball ball = getTable().getBall();
	final Dimension tableSize = getTable().getSize();
	final Dimension ballSize = ball.getSize();
	ball.setPosition(new Point(tableSize.width / 2 - ballSize.width / 2, tableSize.height / 2 - ballSize.height / 2));
	if (Paddle.LEFT_ID.equals(getScoreBoard().getLastWinner())) {
	    ball.randomMoveLeft();
	} else {
	    ball.randomMoveRight();
	}
    }

    private void resetLeftPaddle() {
	final Paddle left = getTable().getLeftPaddle();
	final Dimension tableSize = getTable().getSize();
	left.setPosition(new Point(0, tableSize.height / 2 - left.getSize().height / 2));
    }

    private void resetRightPaddle() {
	final Paddle right = getTable().getRightPaddle();
	final Dimension tableSize = getTable().getSize();
	final Dimension paddleSize = right.getSize();
	right.setPosition(new Point(tableSize.width - paddleSize.width, tableSize.height / 2 - paddleSize.height / 2));
    }
}
