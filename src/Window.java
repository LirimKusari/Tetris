/** Klasa Windows paraqet dritaren grafike ku do te luhet loja tetris */
import javax.swing.JFrame;
public class Window{
	public static final int WIDTH = 360, HEIGHT = 400; // Konstante qe permbajne gjatesine dhe gjeresine e ekranit
	private Board board; // paneli i cili vizaton lojen
	private JFrame window; // referenca e dritares grafike ku futet paneli i lojes
	
   // Konstruktori i cili inicializon dritaren grafike dhe variablat e klases
	public Window(){
		window = new JFrame("OOP GUI");//inicializimi i dritares
		window.setSize(WIDTH, HEIGHT);//percaktimi i madhesise se dritares
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//mbyllja e aplikacionit kur mbyllet dritarja
		window.setLocationRelativeTo(null);//centrimi i dritares
		window.setResizable(false);//e ben dritaren qe mos te mund te rritet a zvogelohet
		board = new Board(); //inicializimi i panelit
		window.addKeyListener(board);//shtimi i degjusit te tasteve-levizjeve
		window.setVisible(true);//paraqitja e dritares
	}
   
   // metoda e cila ben fillimin e lojes
	public void startTetris(){
		window.add(board); //futja e panelit ne dritare
		board.startGame(); //fillimi i lojes tetris
		window.revalidate(); //rifreskimi i te dhenave ne dritare
	}
	
   //metoda kryesore(main) qe fillon lojen
	public static void main(String[] args) {

		new Window().startTetris();;
	}

}