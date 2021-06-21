/** Klasa Board paraqet panelin ku vizatohet loja*/
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.Timer;
public class Board extends JPanel implements KeyListener{
	private final int boardHeight = 12, boardWidth = 6;//nr i rreshtave dhe kolonave
	private final int blockSize = 30;//madhesia e nje blloku
	private int[][] board = new int[boardHeight][boardWidth];//tabela e lojes
   private Shape[] shapes = new Shape[3];//format e lojes
   private static Shape currentShape, nextShape;//forma e tanishme dhe e ardhshme
   private Timer looper; // Kohematesi 
	
	private int FPS = 60; // shpejtesia 
	private int delay = 1000/FPS; // koha per sa figura do te kaloje per nje rresht poshte
	
	private boolean gameOver = false;//a ka perfunduar loja
   private int score = 0;//piket e fituara
   
	//Konstruktori qe inicializon variablat private te klases
	public Board(){
		looper = new Timer(delay, new GameLooper());
      //inicializimi i formave
		shapes[0] = new Shape(new int[][]{
			{1, 1, 1, 1}   // forma I;
		},  this, Color.RED);
		shapes[1] = new Shape(new int[][]{
			{1, 1, 0},
			{0, 1, 1},   // forma Z;
		}, this, Color.BLUE);
		shapes[2] = new Shape(new int[][]{
			{1, 1},
			{1, 1},   // Forma O;
		}, this, Color.GREEN);
	}
   
	//kthen gjatesine e tabeles
	public int getBoardHeight() {
		return boardHeight;
	}
	//kthen gjeresine e tabeles
	public int getBoardWidth() {
		return boardWidth;
	}
	//metoda update ben rifreskimin e panelit
	private void update(){
		//kontrollon a ka perfunduar loja
		if(gameOver)
		{
			return; 
		}
		//nese loja nuk ka perfunduar rifreskon format (pozitat e tyre)
		currentShape.update();
	}
	
	// metoda paintComponent e cila vizaton lojen
	public void paintComponent(Graphics g){
		super.paintComponent(g);//therret konstruktorin e klases prind
		//vizaton gjendjen aktuale te tabeles
		for(int row = 0; row < board.length; row++)
		{
			for(int col = 0; col < board[row].length; col ++)
			{
				//vizaton figurat qe kane zene vend ne tabele
				if(board[row][col] != 0)
				{
					g.setColor(new Color(board[row][col]));
					g.fillRect(col*blockSize,row*blockSize,blockSize,blockSize);
				}

			}
		}
		
      //vizaton ne anen e djathte formen e radhes qe do te paraqitet
		for(int row = 0; row < nextShape.getCoords().length; row ++)
		{
			for(int col = 0; col < nextShape.getCoords()[0].length; col ++)
			{
				if(nextShape.getCoords()[row][col] != 0)
				{
					g.setColor(new Color(nextShape.getBlock()));
					g.fillRect(col*30 + 240, row*30 + 50, blockSize, blockSize);
				}
			}
		}

		//vizaton formen aktuale e cila eshte duke rene
		currentShape.render(g);
		
		//vizaton mesazhin e perfundumit te lojes nese plotesohet kushti
		if(gameOver)
		{
			String gameOverString = "GAME OVER";
			g.setColor(Color.BLACK);
			g.setFont(new Font("Georgia", Font.BOLD, 30));
			g.drawString(gameOverString, 50, Window.HEIGHT/2);
		}	
		
		//vizaton numrin e pikeve te fituara
		g.setColor(Color.BLACK);
		g.setFont(new Font("Georgia", Font.BOLD, 20));
		g.drawString("SCORE", Window.WIDTH - 125, Window.HEIGHT/2);
		g.drawString(score+"", Window.WIDTH - 125, Window.HEIGHT/2 + 30);
		
        //vizatimi i vijave kufizuese te tabeles me theks te veqante
		Graphics2D g2d = (Graphics2D)g;
		g2d.setStroke(new BasicStroke(2));
		g2d.setColor(new Color(0, 0, 0, 100));
		for(int i = 0; i <= boardHeight; i++)
		{	// vizatimi i vijave horizontale
			g2d.drawLine(0, i*blockSize, boardWidth*blockSize, i*blockSize);
		}
		for(int j = 0; j <= boardWidth; j++)
		{	// vizatimi i vijave vertikale
			g2d.drawLine(j*blockSize, 0, j*blockSize, boardHeight*30);
		}
	}
	
	//percakton formen e radhes ne menyre te rastesishme
	public void setNextShape(){
		int index = (int)(Math.random()*shapes.length);
		nextShape = new Shape(shapes[index].getCoords(),this, shapes[index].getColor());
	}
	
	// vendose figuren qe do te hyje ne tabele
	public void setCurrentShape(){
		currentShape = nextShape; //figura qe ishte duke pritur behet figura aktuale
		setNextShape(); // gjeneron figuren e ardhshme
		
		// kontrollon secilen hapesire te tabeles
		for(int row = 0; row < currentShape.getCoords().length; row ++)
		{
			for(int col = 0; col < currentShape.getCoords()[0].length; col ++)
			{	

//				kontrollon nese ka vend ne tabele per te vazhduar lojen
				if(currentShape.getCoords()[row][col] != 0)
				{
					if(board[currentShape.getY() + row][currentShape.getX() + col] != 0)
						gameOver = true;
				}
			}		
		}
		
	}
	
	
	public int[][] getBoard(){
		return board;//rikthen tabelen e lojes
	}
	
   //Implementimi i metodave qe kryjne levizjet me ane te tasteve
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_UP)
			currentShape.rotateShape();
		if(e.getKeyCode() == KeyEvent.VK_RIGHT)
			currentShape.setDeltaX(1);
		if(e.getKeyCode() == KeyEvent.VK_LEFT)
			currentShape.setDeltaX(-1);
		if(e.getKeyCode() == KeyEvent.VK_DOWN)
			currentShape.speedUp();
	}
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_DOWN)
			currentShape.speedDown();
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
   //metoda qe modelon fillimin e lojes;
	public void startGame(){
		stopGame();//kthen tabelen e lojes ne gjendje fillestare
		setNextShape();//percakton formen e radhes
		setCurrentShape();//percakton formen e tanishme
		gameOver = false;//tregon se loja ka filluar
		looper.start();//fillon oren e lojes
	}
   
   //metoda qe modelon fundin e lojes
	public void stopGame(){
		score = 0;
		//rikthen tabelen ne gjendjen fillestare
		for(int row = 0; row < board.length; row++)
		{
			for(int col = 0; col < board[row].length; col ++)
			{
				board[row][col] = 0;
			}
		}
		looper.stop();//ndalon oren e lojes
	}
	
   //Klasa e brendshme e cila ekzekuton ndryshimet ne varesi te kohematesit
	class GameLooper implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			update(); //rifreskon gjendjen e lojes / pra pozitat e gureve
			repaint();//rivizaton lojen - krijon animacionin
		}
		
	}
	
   //shton numrin e pikeve dhe rrite shpejtesine e lojes
	public void addScore(){
		nextShape.setNormalSpeed();//rrite shpejtesine
		score ++;//rrite rezultatin
	}
}