import java.util.*;

public class PacmanGame {

    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {

        Board board = new Board(8, 8);
        Player p = new Player(board);
        Enemy e = new Enemy(board);
        Food f = new Food(board);
        gameLoop(board, p, e, f);
    }

    static void gameLoop(Board b, Player p, Enemy e, Food f) {
        while (p.lives > 0) {
            b.display(p, e, f);
            System.out.println("Score: " + p.score + " | Lives: " + p.lives);
            System.out.print("Move (W A S D): ");
            char move = input.next().toUpperCase().charAt(0);
            movePlayer(move, p, b);
            // Food check
            if (p.x == f.x && p.y == f.y) {
                p.score += 10;
                f.respawn(b);
            }
            // Enemy move
            e.move(b);
            // Collision with enemy
            if (p.x == e.x && p.y == e.y) {
                p.lives--;
                System.out.println("Ghost caught you!");
                p.reset(b);
                e.respawn(b);
            }
        }
        System.out.println("GAME OVER! Final Score: " + p.score);
    }
    static void movePlayer(char c, Player p, Board b) {

        int newX = p.x;
        int newY = p.y;

        if (c == 'W') newY--;
        else if (c == 'S') newY++;
        else if (c == 'A') newX--;
        else if (c == 'D') newX++;

        if (!b.isWall(newX, newY)) {
            p.x = newX;
            p.y = newY;
        } else {
            System.out.println("Cannot move into wall!");
        }
    }
}
// PLAYER CLASS
class Player {
    int x, y;
    int score = 0;
    int lives = 3;

    Player(Board b) {
        x = b.cols / 2;
        y = b.rows / 2;
    }
    void reset(Board b) {
        x = b.cols / 2;
        y = b.rows / 2;
    }
}

//  ENEMY CLASS
class Enemy {
    int x, y;
    Random r = new Random();
    Enemy(Board b) {
        respawn(b);
    }

    void move(Board b) {
        int dir = r.nextInt(4);
        int nx = x, ny = y;

        if (dir == 0) ny--;
        else if (dir == 1) ny++;
        else if (dir == 2) nx--;
        else nx++;

        if (!b.isWall(nx, ny)) {
            x = nx;
            y = ny;
        }
    }
    void respawn(Board b) {
        x = r.nextInt(b.cols - 2) + 1;
        y = r.nextInt(b.rows - 2) + 1;
    }
}
//  FOOD CLASS
class Food {
    int x, y;
    Random r = new Random();

    Food(Board b) {
        respawn(b);
    }
    void respawn(Board b) {
        x = r.nextInt(b.cols - 2) + 1;
        y = r.nextInt(b.rows - 2) + 1;
    }
}

//  BOARD CLASS
class Board {
    int rows, cols;
    char[][] grid;

    Board(int r, int c) {
        rows = r;
        cols = c;
        grid = new char[r][c];
    }
    void display(Player p, Enemy e, Food f) {

        // Reset board
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                if (i == 0 || j == 0 || i == rows - 1 || j == cols - 1)
                    grid[i][j] = '#';
                else
                    grid[i][j] = ' ';
            }
        }

        grid[p.y][p.x] = 'P';
        grid[e.y][e.x] = 'G';
        grid[f.y][f.x] = '*';
        // Print board
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }
    boolean isWall(int x, int y) {
        return (x <= 0 || y <= 0 || x >= cols - 1 || y >= rows - 1);
    }
}