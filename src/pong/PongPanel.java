package pong;

import static jalse.JALSEBuilder.buildManualJALSE;
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
	    final Table table = getTable();
	    final Ball ball = table.getBall();
	    final Point ballPos = ball.getPosition();
	    if (ballPos.x <= 0) {
		newGame = true;
		getScoreBoard().rightWins();
	    } else if (ballPos.x + ball.getSize().width >= table.getSize().width) {
		newGame = true;
		getScoreBoard().leftWins();
	    }
	}

	// Request repaint
	repaint();
    }

    private void createEntities() {
	// Create empty score board
	jalse.newEntity(ScoreBoard.ID, ScoreBoard.class);
	resetScore();

	// Create table
	final Table table = jalse.newEntity(Table.ID, Table.class);
	table.setSize(new Dimension(700, 500));
	table.scheduleForActor(new MoveElements(), 0, 1, TimeUnit.MILLISECONDS);

	// Create paddles
	final Paddle left = table.newEntity(Paddle.LEFT_ID, Paddle.class);
	left.setSize(new Dimension(15, 80));
	left.setSpeedIncrement(20);
	left.stopMoving();
	final Paddle right = table.newEntity(Paddle.RIGHT_ID, Paddle.class);
	right.setSize(new Dimension(15, 80));
	right.setSpeedIncrement(20);
	right.stopMoving();

	// Create ball
	final Ball ball = table.newEntity(Ball.ID, Ball.class);
	ball.setSize(new Dimension(20, 20));
	ball.setSpeedIncrement(4);
	ball.setMaxSpeed(24);
	ball.stopMoving();
	ball.addAttributeListener(TableElement.POSITION_TYPE, new BounceBall());
    }

    private ScoreBoard getScoreBoard() {
	return jalse.getEntityAsType(ScoreBoard.ID, ScoreBoard.class);
    }

    private Table getTable() {
	return jalse.getEntityAsType(Table.ID, Table.class);
    }

    @Override
    public void keyPressed(final KeyEvent e) {
	switch (e.getKeyCode()) {
	case KeyEvent.VK_UP:
	    getTable().getRightPaddle().moveUp();
	    break;
	case KeyEvent.VK_DOWN:
	    getTable().getRightPaddle().moveDown();
	    break;
	case KeyEvent.VK_W:
	    getTable().getLeftPaddle().moveUp();
	    break;
	case KeyEvent.VK_S:
	    getTable().getLeftPaddle().moveDown();
	    break;
	}
    }

    @Override
    public void keyReleased(final KeyEvent e) {
	switch (e.getKeyCode()) {
	case KeyEvent.VK_UP:
	    getTable().getRightPaddle().stopMoving();
	    break;
	case KeyEvent.VK_DOWN:
	    getTable().getRightPaddle().stopMoving();
	    break;
	case KeyEvent.VK_W:
	    getTable().getLeftPaddle().stopMoving();
	    break;
	case KeyEvent.VK_S:
	    getTable().getLeftPaddle().stopMoving();
	    break;
	case KeyEvent.VK_P:
	    paused = !paused;
	    break;
	case KeyEvent.VK_SPACE:
	    newGame = true;
	    resetScore();
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

	// Table data
	final Table table = getTable();
	final Dimension tableSize = table.getSize();

	// White on black colour scheme
	g.setColor(Color.WHITE);

	// Draw half-way line
	for (int y = 0; y < tableSize.height; y += tableSize.height / 20) {
	    g.drawLine(tableSize.width / 2, y, tableSize.width / 2, y += tableSize.height / 10);
	}

	// Draw scores
	g.setFont(SCORE_FONT);
	final int scoreXOffSet = tableSize.width / 8;
	final int scoreYOff = tableSize.height / 8;
	final ScoreBoard board = getScoreBoard();
	drawCentredString(g, board.getLeftScore().toString(), tableSize.width / 2 - scoreXOffSet, scoreYOff);
	drawCentredString(g, board.getRightScore().toString(), tableSize.width / 2 + scoreXOffSet, scoreYOff);

	// Draw paddles
	drawElement(g, table.getLeftPaddle());
	drawElement(g, table.getRightPaddle());

	// Draw ball
	drawElement(g, table.getBall());

	// Draw paused
	if (paused) {
	    g.setFont(PAUSED_FONT);
	    drawCentredString(g, "PAUSED", tableSize.width / 2, tableSize.height / 4);
	}

	// Clean up
	g.dispose();
    }

    private void resetBall() {
	final Table table = getTable();
	final Ball ball = table.getBall();

	final Dimension tableSize = table.getSize();
	final Dimension ballSize = ball.getSize();

	// Centre ball
	ball.setPosition(new Point(tableSize.width / 2 - ballSize.width / 2, tableSize.height / 2 - ballSize.height / 2));

	// Work out what side to serve to
	if (Paddle.LEFT_ID.equals(getScoreBoard().getLastWinner())) {
	    ball.randomMoveLeft();
	} else {
	    ball.randomMoveRight();
	}
    }

    private void resetLeftPaddle() {
	final Table table = getTable();
	final Paddle left = table.getLeftPaddle();
	final Dimension tableSize = table.getSize();

	// Centre paddle
	left.setPosition(new Point(0, tableSize.height / 2 - left.getSize().height / 2));
    }

    private void resetRightPaddle() {
	final Table table = getTable();
	final Paddle right = table.getRightPaddle();

	final Dimension tableSize = table.getSize();
	final Dimension paddleSize = right.getSize();

	// Centre paddle
	right.setPosition(new Point(tableSize.width - paddleSize.width, tableSize.height / 2 - paddleSize.height / 2));
    }

    private void resetScore() {
	final ScoreBoard board = getScoreBoard();
	board.setLeftScore(0);
	board.setRightScore(0);
	// Pick a random last winner
	board.setLastWinner(ThreadLocalRandom.current().nextBoolean() ? Paddle.LEFT_ID : Paddle.RIGHT_ID);
    }
}
