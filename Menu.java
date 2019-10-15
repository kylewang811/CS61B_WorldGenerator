package byow.Core;
import edu.princeton.cs.introcs.StdDraw;
import java.awt.*;

public class Menu {

    String seed;

    public Menu() {
        seed = "";
    }

    public void beginGame() {
        StdDraw.setCanvasSize(1000, 750);
        Font font = new Font("Arial", Font.BOLD, 48);
        StdDraw.setFont(font);
        StdDraw.clear(Color.BLUE);
        StdDraw.enableDoubleBuffering();
    }

    public void startScreen() {
        StdDraw.setCanvasSize(1000, 750);
        Font font = new Font("Arial", Font.BOLD, 48);
        StdDraw.setFont(font);
        StdDraw.clear(Color.BLUE);
        StdDraw.setPenColor(Color.WHITE);

        String text = "CS61B: THE GAME";
        StdDraw.text(0.5, 0.8, text);

        Font font1 = new Font("Arial", Font.PLAIN, 24);
        StdDraw.setFont(font1);
        String text1 = "New Game (N)";
        String text2 = "Load Game (L)";
        String text4 = "Replay (R)";
        String text3 = "Quit (Q)";
        StdDraw.text(0.5, 0.5, text1);
        StdDraw.text(0.5, 0.45, text2);
        StdDraw.text(0.5, 0.4, text4);
        StdDraw.text(0.5, 0.35, text3);
        StdDraw.show();
    }

    public void inputScreen() {

        StdDraw.clear(Color.BLUE);
        Font font = new Font("Arial", Font.BOLD, 48);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);

        StdDraw.text(0.5, 0.5, "Input Seed followed by s: ");
        StdDraw.text(0.5, 0.35, seed);
        StdDraw.show();
    }

    public void colorScreen() {
        StdDraw.clear(Color.BLUE);
        Font font = new Font("Arial", Font.PLAIN, 48);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(0.5, 0.75, "Pick an Avatar color:");

        Font font1 = new Font("Arial", Font.PLAIN, 30);
        StdDraw.setFont(font1);
        StdDraw.text(0.5, 0.5, "Red: R");
        StdDraw.text(0.5, 0.4, "Blue: B");
        StdDraw.text(0.5, 0.3, "Yellow: Y");
        StdDraw.text(0.5, 0.2, "Green: G");
        StdDraw.show();
    }

    //Gold Points
    public void winScreen() {
        StdDraw.setCanvasSize(1000, 750);
        StdDraw.clear(Color.BLUE);
        Font font = new Font("Arial", Font.BOLD, 48);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(0.5, 0.5, "You Win!!!");
        StdDraw.show();
    }
    /*public void rulesScreen() {
        StdDraw.setCanvasSize(1000, 750);
        StdDraw.clear(Color.BLUE);
        Font font = new Font("Arial", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(0.5, 0.3, "Creative Aspect 1: Locked door");
        StdDraw.text(0.5, 0.4, "Creative Aspect 2: Health points");
        StdDraw.text(0.5, 0.5, "Creative Aspect 3: Enemy players");
        StdDraw.show();
    }*/

    public void loseScreen() {
        StdDraw.setCanvasSize(1000, 750);
        StdDraw.clear(Color.BLUE);
        Font font = new Font("Arial", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(0.5, 0.5, "Better luck next time.");
        StdDraw.show();
    }
}
