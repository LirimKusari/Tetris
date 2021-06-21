/**
 * Klasa Shape modelon njeren nga format e lojes
 */

import java.awt.Color;
import java.awt.Graphics;

public class Shape {
    private Color color; //ngjyra te cilen do ta kete forma
    private int x, y;  // pozita x dhe y
    private long time, lastTime; // koha se sa do te qendroje figura ne nje pozite dhe pastaj te levize poshte
    private int normal = 600, fast = 50; // shpejtesia e renies se figures
    private int delay; // pritja qe ben figura derisa te levize per nje katror poshte
    private int[][] coords;// blloqet qe i vizatojme (nje katror)
    private int deltaX;//shpejtesia e levizjes horizontale
    private Board board;//paneli ku vizatohet forma
    private boolean collision = false, moveX = false;//detektimi i perplasjeve

    //Konstruktori qe inicializon variblat private te klases Shape
    public Shape(int[][] coords, Board board, Color color) {
        this.coords = coords;//inicializimi i blloqeve
        this.board = board;//inicializimi i panelit
        this.color = color;//inicializimi i ngjyres;
        deltaX = 0;//inicializimi i shpejtesise horizontale
        x = 2;//inicializimi i pozites x
        y = 0;//inicializimi i pozites y
        delay = normal;//inicializimi i shpejtesise vertikale
        time = 0;//inicializimi i kohes se ardhshme per paraqitje
        lastTime = System.currentTimeMillis();//inicializimi i kohes se tanishme per paraqitje
    }

    //metoda update() e cila rifreskon poziten e nje forme ne tabele
    public void update() {
        moveX = true; // tregon qe eshte duke levizur figura
        // ndryshon kohen
        time += System.currentTimeMillis() - lastTime;
        lastTime = System.currentTimeMillis();

        if (collision) {
//       ruan dhe vizton guren ne tabele
            for (int row = 0; row < coords.length; row++) {
                for (int col = 0; col < coords[0].length; col++) {
                    if (coords[row][col] != 0)
                        board.getBoard()[y + row][x + col] = color.getRGB(); // vizaton figurat poshte ne tabele
                }
            }

            // kontrollon nese nje rend i tables eshte i mbushur plotesisht, nese po heq tere guret nga rendi
            checkLine();

            board.addScore();
            board.setCurrentShape();
        }


//    perderisa jemi brenda kornizave atehere leviz
        // coords[0].length paraqet gjatesine e rrestit
        if (!(x + deltaX + coords[0].length > board.getBoardWidth()) && !(x + deltaX < 0)) {
            x += deltaX;
        }

//
        if (!(y + 1 + coords.length > board.getBoardHeight())) {
//          tregon nese figura qe eshte duke levizur poshte hase ne figura tjera
            for (int row = 0; row < coords.length; row++) {
                for (int col = 0; col < coords[row].length; col++) {
                    if (coords[row][col] != 0) {
                        if (board.getBoard()[y + 1 + row][x + col] != 0) {
                            collision = true;
                        }
                    }
                }
            }

//       nese ka kaluar koha atehere duhet te levize per nje katror poshte
            if (time > delay) {
                y++;
                time = 0;
            }
        } else {
            collision = true;
        }

        deltaX = 0;
    }

    // vizaton guret kur jane duke rene ne tabele
    public void render(Graphics g) {

        for (int row = 0; row < coords.length; row++) {
            for (int col = 0; col < coords[0].length; col++) {
                if (coords[row][col] != 0) {
                    g.setColor(color);
                    g.fillRect(col * 30 + x * 30, row * 30 + y * 30, 30, 30);
                }
            }
        }
    }

    //checkLine kontrollon nese ka ndonje rresht qe i ka te gjitha hapesirat e mbushura qe ta fshije
    private void checkLine() {
        int size = board.getBoard().length - 1; //madhesia e tabeles

        for (int i = board.getBoard().length - 1; i > 0; i--) {
            int count = 0;//mban numrin e katroreve qe mbushin nje rresht per secilin rresht
            for (int j = 0; j < board.getBoard()[0].length; j++) {
                if (board.getBoard()[i][j] != 0)
                    count++;

                board.getBoard()[size][j] = board.getBoard()[i][j];
            }
            if (count < board.getBoard()[0].length)
                size--;//largon rreshtin nese ai eshte mbushur
        }
    }

    //ben rrotullimin 90 shkalle te formes
    public void rotateShape() {
        int[][] rotatedShape = null;
        rotatedShape = transposeMatrix(coords); //rrotullon formen
        //kontrollon nese forma e rrotulluar kalon gjeresine e tabeles
        if ((x + rotatedShape[0].length > board.getBoardWidth()) || (y + rotatedShape.length > board.getBoardHeight())) {
            return;//forma nuk mund te rrotullohet
        }
        //kontrollon nese forma e rrotulluar prek ndonje forme tjeter
        for (int row = 0; row < rotatedShape.length; row++) {
            for (int col = 0; col < rotatedShape[row].length; col++) {
                if (rotatedShape[row][col] != 0) {
                    if (board.getBoard()[y + row][x + col] != 0) {
                        return; //nuk mund te rrotullohet
                    }
                }
            }
        }
        coords = rotatedShape;//kthen formen e rrotulluar
    }

    //metoda transposeMatrix ben rrotullimin 90 shkalle te formes
    private int[][] transposeMatrix(int[][] matrix) {
        int[][] temp = new int[matrix[0].length][matrix.length];
        //behet transponimi i blloqeve ku rreshtat behen shtylla dhe anasjelltas
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++)
                temp[j][i] = matrix[i][j];
        return temp;
    }

    public Color getColor() {
        return color;//kthen ngjyren e formes
    }

    public void setDeltaX(int deltaX) {
        this.deltaX = deltaX;//percakton shpejtesine e levizjes horizontale
    }

    public void speedUp() {
        delay = fast;//percakton shpejtesine e levizjes vertikale kur preket tasti DOWN
    }

    public void speedDown() {
        delay = normal;//percakton shpejtesine normale te levizjes vertikale;
    }

    public int getBlock() {
        return color.getRGB();//kthen ngjyren e formes si vlere numerike
    }

    public int[][] getCoords() {
        return coords;//kthen blloqet e formes
    }

    public int getX() {
        return x;//kthen poziten x te formes
    }

    public int getY() {
        return y;//kthen poziten y te formes
    }

    public void setNormalSpeed() {
        if (normal > 50) normal -= 50;//ben rritjen e shpejtesise kur rriten piket
    }
}