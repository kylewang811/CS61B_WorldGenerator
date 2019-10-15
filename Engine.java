package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.io.*;

public class Engine implements Serializable {
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    String typed = "";
    String seed = "";
    MapGenerator map;
    TETile[][] world;
    MapGenerator.Avatar player;
    MapGenerator.Monster enemy;
    MapGenerator.Monster enemy1;
    MapGenerator.Monster enemy2;
    MapGenerator.Monster enemy3;
    Menu menu;
    Color sColor = Color.RED;
    boolean quit;
    double curX;
    double curY;
    TERenderer ter = new TERenderer();

    public void interactWithKeyboard() {
        menu = new Menu();
        menu.beginGame();
        menu.startScreen();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                Character x = Character.toLowerCase(StdDraw.nextKeyTyped());
                switch (x) {
                    case 'r':
                        //String savedState = getSaved();
                        String savedState = getWorld();
                        typed = savedState;
                        int position = 0;
                        int n = savedState.indexOf('n');
                        int s = savedState.indexOf('s');
                        menu.colorScreen();
                        Color rColor = getColor();
                        ter.initialize(80, 30 + 3);
                        String lenSeed = savedState.substring(n + 1, s);
                        Long saveSeed = Long.parseLong(lenSeed);
                        map = new MapGenerator(saveSeed, rColor);
                        position += lenSeed.length();
                        world = new TETile[WIDTH][HEIGHT];
                        map.draw(world);
                        player = map.createPlayer(rColor);
                        enemy = map.createEnemy();
                        enemy1 = map.createEnemy();
                        enemy2 = map.createEnemy();
                        enemy3 = map.createEnemy();
                        ter.renderFrame(world);
                        while (position < savedState.length()) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                System.out.println(e);
                            }
                            player.move(savedState.charAt(position));
                            enemy.move();
                            enemy1.move();
                            enemy2.move();
                            enemy3.move();
                            position += 1;
                            ter.renderFrame(world);
                        }
                        mouse();
                        while (!quit) {
                            movePlayer();
                        }
                        break;
                    case 'n':
                        typed = typed + "n";
                        menu.inputScreen();
                        getInput();
                        menu.colorScreen();
                        Color color = getColor();
                        //Draw world
                        ter.initialize(80, 30 + 3);
                        map = new MapGenerator(Long.parseLong(seed), color);
                        world = new TETile[WIDTH][HEIGHT];
                        map.draw(world);
                        ter.renderFrame(world);

                        player = map.createPlayer(color);
                        enemy = map.createEnemy();
                        enemy1 = map.createEnemy();
                        enemy2 = map.createEnemy();
                        enemy3 = map.createEnemy();
                        mouse();
                        while (!quit) {
                            movePlayer();
                            if (!checkHealth()) {
                                menu.loseScreen();
                                break;
                            }
                            if (checkWin()) {
                                menu.winScreen();
                                break;
                            }
                        }
                        break;
                    case 'l':
                        //String loadState = getSaved();
                        String loadState = getWorld();
                        typed = loadState;
                        ter.initialize(80, 30 + 3);
                        TETile[][] newWorld = interactWithInputString(loadState);
                        ter.renderFrame(newWorld);
                        mouse();
                        while (!quit) {
                            movePlayer();
                        }
                        break;
                    case 'q':
                        System.exit(0);
                        break;
                    default:
                }
            }
        }
    }

    public TETile[][] interactWithInputString(String input) {
        if (input == null || input.length() == 0) {
            return new TETile[WIDTH][HEIGHT];
        }
        input = input.toLowerCase();
        String moves;
        if (input.indexOf('l') == 0) {
            String load = getWorld();
            typed = load;
            int n = load.indexOf('n');
            int s = load.indexOf('s');
            if (n == -1 || s == -1 || s < n || s - n == 1 || load.equals("")) {
                return MapGenerator.nothingWorld();
            }
            long oSeed = Long.parseLong(load.substring(n + 1, s));
            moves = load.substring(s + 1) + input.substring(1);
            map = new MapGenerator(oSeed, sColor);
            world = new TETile[WIDTH][HEIGHT];
            world = map.draw(world);
            player = map.createPlayer(sColor);
            enemy = map.createEnemy();
            enemy1 = map.createEnemy();
            enemy2 = map.createEnemy();
            enemy3 = map.createEnemy();
        } else {
            int n = input.indexOf('n');
            int s = input.indexOf('s');
            if (n == -1 || s == -1 || s < n || s - n == 1) {
                return MapGenerator.nothingWorld();
            }
            typed = input.substring(0, s + 1);
            long oSeed = Long.parseLong(input.substring(n + 1, s));
            moves = input.substring(s + 1);
            map = new MapGenerator(oSeed, sColor);
            world = new TETile[WIDTH][HEIGHT];
            world = map.draw(world);
            player = map.createPlayer(sColor);
            enemy = map.createEnemy();
            enemy1 = map.createEnemy();
            enemy2 = map.createEnemy();
            enemy3 = map.createEnemy();
        }
        int counter = 0;
        while (moves != null && counter < moves.length()) {
            player.move(moves.charAt(counter));
            if ((moves.charAt(counter) == 'w') || (moves.charAt(counter) == 'a')
                || (moves.charAt(counter) == 's') || moves.charAt(counter) == 'd') {
                typed += moves.charAt(counter);
            }
            counter += 1;
        }
        saveFile();
        return world;
    }

    private void getInput() {
        boolean temp = true;
        while (temp) {
            if (StdDraw.hasNextKeyTyped()) {
                Character x = Character.toLowerCase(StdDraw.nextKeyTyped());
                switch (x) {
                    case 's':
                        typed = typed + "s";
                        temp = false;
                        break;
                    default:
                        typed = typed + x;
                        seed = seed + x;
                        menu.seed = seed;
                }
            }
            menu.inputScreen();
        }
    }

    private Color getColor() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                Character x = Character.toLowerCase(StdDraw.nextKeyTyped());
                switch (x) {
                    case 'r':
                        sColor = Color.RED;
                        return Color.RED;
                    case 'b':
                        sColor = Color.BLUE;
                        return Color.BLUE;
                    case 'y':
                        sColor = Color.YELLOW;
                        return Color.YELLOW;
                    case 'g':
                        sColor = Color.GREEN;
                        return Color.GREEN;
                    default:
                        sColor = Color.WHITE;
                        return Color.WHITE;
                }
            }
        }
    }

    private void movePlayer() {
        if (checkMouseInput()) {
            mouse();
        }
        if (StdDraw.hasNextKeyTyped()) {
            Character x = Character.toLowerCase(StdDraw.nextKeyTyped());
            switch (x) {
                case ':':
                    if (quitGame()) {
                        saveFile();
                        System.exit(0);
                    }
                    break;
                default:
                    player.move(x);
                    enemy.move();
                    enemy1.move();
                    enemy2.move();
                    enemy3.move();
                    map.health -= 1;
                    ter.renderFrame(world);
                    break;
            }
            if (x != ':' || x != 'q') {
                typed = typed + x;
            }
        }
    }
    private boolean checkMouseInput() {
        double x = StdDraw.mouseX();
        double y = StdDraw.mouseY();
        if (x != curX || y != curY) {
            curX = x;
            curY = y;
            return true;
        }
        return false;
    }
    private boolean quitGame() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                Character x = Character.toLowerCase(StdDraw.nextKeyTyped());
                switch (x) {
                    case 'q':
                        quit = true;
                        return true;
                    default:
                        return false;
                }
            }
        }
    }

    private void saveFile() {
        FileWriter file = null;

        try {
            file = new FileWriter("./save_data.txt");
            PrintWriter print = new PrintWriter(file);
            print.print(typed);
            print.close();
        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    private String getWorld() {
        try {
            BufferedReader read = new BufferedReader(new FileReader("./save_data.txt"));
            String answer = read.readLine();
            if (answer == null) {
                return "";
            }
            return answer;
        } catch (IOException error) {
            System.out.println("error");
        }
        return null;
    }

    private void mouse() {

        int xPos = (int) StdDraw.mouseX();
        int yPos = (int) StdDraw.mouseY();
        if (xPos > -1 && xPos < WIDTH && yPos > -1 && yPos < HEIGHT) {
            TETile temp = world[xPos][yPos];
            String type = temp.description();
            String life = "Health: " + map.health;
            ter.renderFrame(world);
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.text(.05 * WIDTH, HEIGHT + 1, type);
            StdDraw.text(0.95 * WIDTH, HEIGHT + 1, life);
            StdDraw.show();
        }
    }

    private boolean checkHealth() {
        if (map.health > 0) {
            return true;
        }
        return false;
    }
    private boolean checkWin() {
        if (map.health > 0 && map.win) {
            return true;
        }
        return false;
    }
}
