package byow.Core;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.awt.Color;

public class MapGenerator {

    private class Point {
        int typeOfTile;
        int xPos;
        int yPos;
        Point() {
            typeOfTile = 0;
            xPos = 0;
            yPos = 0;
        }
        Point(int tile, int x, int y) {
            typeOfTile = tile;
            xPos = x;
            yPos = y;
        }
        private Point getPoint() {
            return this;
        }
    }

    public class Avatar {
        int xPos;
        int yPos;
        Color color;
        TETile tile;

        public Avatar(Color c) {
            Point start = storage.get(random.nextInt(storage.size()));
            xPos = start.xPos;
            yPos = start.yPos;
            this.tile = new TETile('@', c, color.BLACK, "player");
        }

        public void move(Character c) {
            int x = player.xPos;
            int y = player.yPos;

            switch (c) {
                case 'a':
                    if (check(x - 1, y)) {
                        player.xPos -= 1;
                        world[x - 1][y] = new TETile('@', col, Color.black, "player");
                        world[x][y] = Tileset.FLOOR;
                    }
                    break;
                case 'd':
                    if (check(x + 1, y)) {
                        player.xPos += 1;
                        world[x + 1][y] = new TETile('@', col, Color.black, "player");
                        world[x][y] = Tileset.FLOOR;
                    }
                    break;
                case 'w':
                    if (check(x, y + 1)) {
                        player.yPos += 1;
                        world[x][y + 1] = new TETile('@', col, Color.black, "player");
                        world[x][y] = Tileset.FLOOR;
                    }
                    break;
                case 's':
                    if (check(x, y - 1)) {
                        player.yPos -= 1;
                        world[x][y - 1] = new TETile('@', col, Color.black, "player");
                        world[x][y] = Tileset.FLOOR;
                    }
                    break;
                default:
                    break;
            }
        }

        public boolean check(int x, int y) {

            if (x == lockedDoor.xPos && y == lockedDoor.yPos) {
                win = true;
            }
            if (temp[x][y].typeOfTile == 5) {
                world[lockedDoor.xPos][lockedDoor.yPos] = Tileset.UNLOCKED_DOOR;
            }
            if (temp[x][y].typeOfTile == 7) {
                health += 5;
            }
            if (x == enemy.xPos && y == enemy.yPos) {
                health -= 25;
            }

            if (temp[x][y].typeOfTile == 2 || temp[x][y].typeOfTile == 3) {
                return false;
            }
            if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT) {
                return false;
            }
            return true;
        }
    }

    public class Monster {
        int xPos;
        int yPos;
        TETile tile;

        public Monster() {
            Point start1 = storage.get(random.nextInt(storage.size()));
            xPos = start1.xPos;
            yPos = start1.yPos;
            this.tile = new TETile('^', Color.WHITE,  Color.BLACK, "enemy");
        }

        public void move() {
            int x = this.xPos;
            int y = this.yPos;
            double random = Math.random();
            if (random < 0.25) {
                if (check(x - 1, y)) {
                    this.xPos -= 1;
                    world[x - 1][y] = new TETile('^', Color.WHITE, Color.black, "enemy");
                    world[x][y] = Tileset.FLOOR;
                }
            } else if (random >= 0.25 && random < 0.5) {
                if (check(x + 1, y)) {
                    this.xPos += 1;
                    world[x + 1][y] = new TETile('^', Color.WHITE, Color.black, "enemy");
                    world[x][y] = Tileset.FLOOR;
                }
            } else if (random >= 0.5 && random < 0.75) {
                if (check(x, y + 1)) {
                    this.yPos += 1;
                    world[x][y + 1] = new TETile('^', Color.WHITE, Color.black, "enemy");
                    world[x][y] = Tileset.FLOOR;
                }
            } else if (random >= 0.75) {
                if (check(x, y - 1)) {
                    this.yPos -= 1;
                    world[x][y - 1] = new TETile('^', Color.WHITE, Color.black, "enemy");
                    world[x][y] = Tileset.FLOOR;
                }
            }
        }

        public boolean check(int x, int y) {

            if (temp[x][y].typeOfTile == 2 || temp[x][y].typeOfTile == 3) {
                return false;
            }
            if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT) {
                return false;
            }
            return true;
        }
    }
    Point lockedDoor;
    int health;
    Point[][] temp;
    ArrayList<Point> storage;
    ArrayList<Point> walls;
    ArrayList<Point> hallwayEnds = new ArrayList<>();
    HashMap<Point, Double> hallwayDirection = new HashMap<>();
    Avatar player;
    Monster enemy;
    TETile[][] world;
    Boolean win;

    private static final int WIDTH = 80;
    private static final int HEIGHT = 30;
    private Random random;
    int numSpaces = 0;
    Color col;

    public MapGenerator(long seed, Color color) {

        random = new Random(seed);
        storage = new ArrayList<>();
        walls = new ArrayList<>();
        temp = new Point[WIDTH][HEIGHT];
        col = color;
        health = 100;
        win = false;
    }

    public TETile[][] draw(TETile[][] w) {

        this.world = w;
        Point[][] template = drawHallways(WIDTH, HEIGHT);
        player = new Avatar(col);
        enemy = new Monster();

        template[player.xPos][player.yPos].typeOfTile = 4;
        template[enemy.xPos][enemy.yPos].typeOfTile = 9;
        for (int i = 0; i < 20; i++) {
            Point tempP = storage.get(random.nextInt(storage.size()));
            template[tempP.xPos][tempP.yPos].typeOfTile = 7;
        }

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (template[i][j].typeOfTile == 1) {
                    this.world[i][j] = Tileset.FLOOR;
                } else if (template[i][j].typeOfTile == 2) {
                    this.world[i][j] = Tileset.WALL;
                } else if (template[i][j].typeOfTile == 3) {
                    this.world[i][j] = Tileset.LOCKED_DOOR;
                } else if (template[i][j].typeOfTile == 4) {
                    this.world[i][j] = new TETile('@', col, Color.black, "player");
                } else if (template[i][j].typeOfTile == 9) {
                    this.world[i][j] = new TETile('^', Color.WHITE, Color.black, "enemy");
                } else if (template[i][j].typeOfTile == 5) {
                    this.world[i][j] = Tileset.FLOWER;
                } else if (template[i][j].typeOfTile == 6) {
                    this.world[i][j] = Tileset.UNLOCKED_DOOR;
                } else if (template[i][j].typeOfTile == 7) {
                    this.world[i][j] = Tileset.SAND;
                } else if (template[i][j].typeOfTile == 8) {
                    this.world[i][j] = Tileset.MOUNTAIN;
                } else {
                    this.world[i][j] = Tileset.NOTHING;
                }
            }
        }
        return this.world;
    }
    public static TETile[][] nothingWorld() {
        TETile[][] nothing = new TETile[WIDTH][HEIGHT];
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                nothing[i][j] = Tileset.NOTHING;
            }
        }
        return nothing;
    }

    public Point[][] drawHallways(int dim1, int dim2) {

        for (int i = 0; i < dim1; i++) {
            for (int j = 0; j < dim2; j++) {
                temp[i][j] = new Point(0, i, j);
            }
        }

        int startx = random.nextInt(WIDTH - 3) + 1;
        int starty = random.nextInt(HEIGHT - 3) + 1;
        temp[startx][starty] = new Point(1, startx, starty);
        storage.add(temp[startx][starty]);
        numSpaces += 1;

        double direction = random.nextDouble();
        baseCase(startx, starty, direction, temp, storage, hallwayEnds, hallwayDirection);

        int i = 0;
        while (i < 250 && (numSpaces * 1.0 / 2400) < 0.25) {
            double dir = random.nextDouble();
            Point starter = storage.get(random.nextInt(storage.size()));

            if (i % 8 == 7) {
                generateRoom(hallwayEnds, hallwayDirection, storage, temp);
            } else {
                iterativeCase(starter, dir, temp, storage, hallwayEnds, hallwayDirection);
            }
            i += 1;
        }

        finalWalls(temp, walls);
        makeWalls(temp, storage, walls);
        lockedDoor = lockedDoor(temp, walls);
        Point unlocker = storage.get(random.nextInt(storage.size()));
        temp[unlocker.xPos][unlocker.yPos].typeOfTile = 5;

        return temp;
    }

    private void baseCase(int x, int y, double direction,
                          Point[][] tempA, ArrayList<Point> storageList, ArrayList<Point> hallways,
                          HashMap<Point, Double> hallwayDir) {
        if (direction < 0.25) {
            int length = Math.min(random.nextInt(x), 15);

            for (int i = 1; i < length; i++) {
                Point current = new Point(1, x - i, y);
                tempA[x - i][y] = current;
                storageList.add(tempA[x - i][y]);
                numSpaces += 1;
                if (i == length - 1) {
                    hallwayEnds.add(current);
                    hallwayDir.put(current, direction);
                }
            }
        } else if (direction >= .25 && direction < .5) {
            int length = Math.min(random.nextInt(HEIGHT - y - 1), 15);

            for (int i = 1; i < length; i++) {
                Point current = new Point(1, x, y + i);
                tempA[x][y + i] = current;
                storageList.add(tempA[x][y + i]);
                numSpaces += 1;
                if (i == length - 1) {
                    hallways.add(current);
                    hallwayDir.put(current, direction);
                }
            }
        } else if (direction >= .5 && direction < .75) {
            int length = Math.min(random.nextInt(WIDTH - x - 1), 15);

            for (int i = 1; i < length; i++) {
                Point current = new Point(1, x + i, y);
                tempA[x + i][y] = current;
                storageList.add(tempA[x + i][y]);
                numSpaces += 1;
                if (i == length - 1) {
                    hallways.add(current);
                    hallwayDir.put(current, direction);
                }
            }
        } else {
            int length = Math.min(random.nextInt(y), 15);

            for (int i = 1; i < length; i++) {
                Point current = new Point(1, x, y - i);
                tempA[x][y - i] = current;
                storageList.add(tempA[x][y - i]);
                numSpaces += 1;
                if (i == length - 1) {
                    hallways.add(current);
                    hallwayDir.put(current, direction);
                }
            }
        }
    }

    private void iterativeCase(Point starter, Double directionGenerator, Point[][]tempL,
                               ArrayList<Point> pointL, ArrayList<Point> hallways,
                               HashMap<Point, Double> hallwayDir) {
        int xPos = starter.xPos;
        int yPos = starter.yPos;
        int checkLength = checkAdj(directionGenerator, xPos, yPos, tempL);
        while (checkLength == 0) {
            directionGenerator = random.nextDouble();
            starter = pointL.get(random.nextInt(storage.size()));
            xPos = starter.xPos;
            yPos = starter.yPos;
            checkLength = checkAdj(directionGenerator, xPos, yPos, tempL);
        }
        if (directionGenerator < 0.25) {
            int length = checkLength;
            for (int x = 1; x < length; x++) {
                Point current = new Point(1, xPos - x, yPos);
                tempL[xPos - x][yPos] = current;
                pointL.add(tempL[xPos - x][yPos]);
                numSpaces += 1;
                if (x == length - 1) {
                    hallways.add(current);
                    hallwayDir.put(current, directionGenerator);
                }
            }
        } else if (directionGenerator >= .25 && directionGenerator < .5) {
            int length = checkLength;
            for (int x = 1; x < length; x++) {
                Point current = new Point(1, xPos, yPos + x);
                tempL[xPos][yPos + x] = current;
                pointL.add(tempL[xPos][yPos + x]);
                numSpaces += 1;
                if (x == length - 1) {
                    hallways.add(current);
                    hallwayDir.put(current, directionGenerator);
                }
            }
        } else if (directionGenerator >= .5 && directionGenerator < .75) {
            int length = checkLength;
            for (int x = 1; x < length; x++) {
                Point current = new Point(1, xPos + x, yPos);
                tempL[xPos + x][yPos] = current;
                pointL.add(tempL[xPos + x][yPos]);
                numSpaces += 1;
                if (x == length - 1) {
                    hallways.add(current);
                    hallwayDir.put(current, directionGenerator);
                }
            }
        } else {
            int length = checkLength;
            for (int x = 1; x < length; x++) {
                Point current = new Point(1, xPos, yPos - x);
                tempL[xPos][yPos - x] = current;
                pointL.add(tempL[xPos][yPos - x]);
                numSpaces += 1;
                if (x == length - 1) {
                    hallways.add(current);
                    hallwayDir.put(current, directionGenerator);
                }
            }
        }
    }

    private void generateRoom(ArrayList<Point> hallways, HashMap<Point,
            Double> hallwayDir, ArrayList<Point> pointL, Point[][] tempA) {
        Point starter = hallways.get(random.nextInt(hallways.size()));
        int xPos = starter.xPos;
        int yPos = starter.yPos;
        int xDir = random.nextInt(3) + 2;
        int yDir = random.nextInt(3) + 2;
        double direction = hallwayDir.get(starter);
        boolean checkRoom = checkRoomAdj(direction, xPos, yPos, tempA, xDir, yDir);
        while (!checkRoom) {
            starter = hallways.get(random.nextInt(hallways.size()));
            xPos = starter.xPos;
            yPos = starter.yPos;
            xDir = random.nextInt(3) + 2;
            yDir = random.nextInt(3) + 2;
            direction = hallwayDir.get(starter);
            checkRoom = checkRoomAdj(direction, xPos, yPos, tempA, xDir, yDir);
        }

        if (direction < 0.25) {
            for (int y = 0; y < xDir; y++) {
                for (int z = 0; z < yDir; z++) {
                    tempA[xPos - y][yPos - yDir / 2 + z] =
                            new Point(1, xPos - y, yPos - yDir / 2 + z);
                    pointL.add(tempA[xPos - y][yPos - yDir / 2 + z]);
                    numSpaces += 1;
                }
            }
        } else if (direction >= .25 && direction < .5) {
            for (int y = 0; y < xDir; y++) {
                for (int z = 0; z < yDir; z++) {
                    tempA[xPos - xDir / 2 + y][yPos + z] =
                            new Point(1, xPos - xDir / 2 + y, yPos + z);
                    pointL.add(tempA[xPos - xDir / 2 + y][yPos + z]);
                    numSpaces += 1;
                }
            }
        } else if (direction >= .5 && direction < .75) {
            for (int y = 0; y < xDir; y++) {
                for (int z = 0; z < yDir; z++) {
                    tempA[xPos + y][yPos - yDir / 2 + z] =
                            new Point(1, xPos + y, yPos - yDir / 2 + z);
                    pointL.add(tempA[xPos + y][yPos - yDir / 2 + z]);
                    numSpaces += 1;
                }
            }
        } else {
            for (int y = 0; y < xDir; y++) {
                for (int z = 0; z < yDir; z++) {
                    tempA[xPos - xDir / 2 + y][yPos - z] =
                            new Point(1, xPos - xDir / 2 + y, yPos - z);
                    pointL.add(tempA[xPos - xDir / 2 + y][yPos - z]);
                    numSpaces += 1;
                }
            }
        }
    }

    private void makeWalls(Point[][] file, ArrayList<Point> list, ArrayList<Point> wallL) {

        for (int i = 0; i < list.size(); i++) {

            Point wall = list.get(i);
            int xPos = wall.xPos;
            int yPos = wall.yPos;

            if (xPos > 0 && yPos > 0 && file[xPos - 1][yPos - 1].typeOfTile != 1) {
                file[xPos - 1][yPos - 1].typeOfTile = 2;
                wallL.add(new Point(2, xPos - 1, yPos - 1));
            }
            if (xPos > 0 && file[xPos - 1][yPos].typeOfTile != 1) {
                file[xPos - 1][yPos].typeOfTile = 2;
                wallL.add(new Point(2, xPos - 1, yPos));
            }
            if (xPos > 0 && yPos < HEIGHT - 1 && file[xPos - 1][yPos + 1].typeOfTile != 1) {
                file[xPos - 1][yPos + 1].typeOfTile = 2;
                wallL.add(new Point(2, xPos - 1, yPos + 1));
            }
            if (yPos > 0 && file[xPos][yPos - 1].typeOfTile != 1) {
                file[xPos][yPos - 1].typeOfTile = 2;
                wallL.add(new Point(2, xPos, yPos - 1));
            }
            if (yPos < HEIGHT - 1 && file[xPos][yPos + 1].typeOfTile != 1) {
                file[xPos][yPos + 1].typeOfTile = 2;
                wallL.add(new Point(2, xPos, yPos + 1));
            }
            if (xPos < WIDTH - 1 && yPos > 0 && file[xPos + 1][yPos - 1].typeOfTile != 1) {
                file[xPos + 1][yPos - 1].typeOfTile = 2;
                wallL.add(new Point(2, xPos + 1, yPos - 1));
            }
            if (xPos < WIDTH - 1 && file[xPos + 1][yPos].typeOfTile != 1) {
                file[xPos + 1][yPos].typeOfTile = 2;
                wallL.add(new Point(2, xPos + 1, yPos));
            }
            if (xPos < WIDTH - 1 && yPos < HEIGHT - 1 && file[xPos + 1][yPos + 1].typeOfTile != 1) {
                file[xPos + 1][yPos + 1].typeOfTile = 2;
                wallL.add(new Point(2, xPos + 1, yPos + 1));
            }
        }
    }

    private void finalWalls(Point[][]file, ArrayList<Point> wallL) {

        for (int i = 0; i < WIDTH; i++) {
            if (file[i][0].typeOfTile == 1) {
                file[i][0].typeOfTile = 2;
                wallL.add(file[i][0]);
            }
            if (file[i][HEIGHT - 1].typeOfTile == 1) {
                file[i][HEIGHT - 1].typeOfTile = 2;
                wallL.add(file[i][HEIGHT - 1]);
            }
        }
        for (int j = 0; j < HEIGHT; j++) {
            if (file[0][j].typeOfTile == 1) {
                file[0][j].typeOfTile = 2;
                wallL.add(file[0][j]);
            }
            if (file[WIDTH - 1][j].typeOfTile == 1) {
                file[WIDTH - 1][j].typeOfTile = 2;
                wallL.add(file[WIDTH - 1][j]);
            }
        }
    }

    private int generateLength(double directionGenerator, int xPos, int yPos) {
        int length;
        if (directionGenerator < 0.25) {
            if (xPos < 2) {
                length = 0;
            } else {
                length = Math.min(random.nextInt(xPos), 15);
            }
        } else if (directionGenerator >= .25 && directionGenerator < .5) {
            if (yPos > HEIGHT - 3) {
                length = 0;
            } else {
                length = Math.min(random.nextInt(HEIGHT - yPos - 1), 15);
            }
        } else if (directionGenerator >= .5 && directionGenerator < .75) {
            if (xPos > WIDTH - 3) {
                length = 0;
            } else {
                length = Math.min(random.nextInt(WIDTH - xPos - 1), 15);
            }
        } else {
            if (yPos < 2) {
                length = 0;
            } else {
                length = Math.min(random.nextInt(yPos), 15);
            }
        }
        return length;
    }

    private boolean checkRoomAdj(double direction, int xPos,
                                 int yPos, Point[][] tempA, int xDir, int yDir) {
        if (direction < 0.25) {
            if (xPos - xDir < 1 || yPos - yDir / 2 < 1 || yPos + yDir / 2 > HEIGHT - 2) {
                return false;
            }
            for (int y = 1; y < xDir; y++) {
                for (int z = 1; z < yDir; z++) {
                    if (tempA[xPos - y][yPos - yDir / 2 + z].typeOfTile == 1) {
                        return false;
                    }
                }
            }
        } else if (direction >= .25 && direction < .5) {
            if (xPos - xDir / 2 < 1 || yPos + yDir > HEIGHT - 2 || xPos + xDir / 2 > WIDTH - 2) {
                return false;
            }
            for (int y = 1; y < xDir; y++) {
                for (int z = 1; z < yDir; z++) {
                    if (tempA[xPos - xDir / 2 + y][yPos + z].typeOfTile == 1) {
                        return false;
                    }
                }
            }
        } else if (direction >= .5 && direction < .75) {
            if (xPos + xDir > WIDTH - 2 || yPos - yDir / 2 < 1 || yPos + yDir / 2 > HEIGHT - 2) {
                return false;
            }
            for (int y = 1; y < xDir; y++) {
                for (int z = 1; z < yDir; z++) {
                    if (tempA[xPos + y][yPos - yDir / 2 + z].typeOfTile == 1) {
                        return false;
                    }
                }
            }
        } else {
            if (xPos - xDir / 2 < 1 || yPos - yDir < 1 || xPos + xDir / 2 > WIDTH - 2) {
                return false;
            }
            for (int y = 1; y < xDir; y++) {
                for (int z = 1; z < yDir; z++) {
                    if (tempA[xPos - xDir / 2 + y][yPos - z].typeOfTile == 1) {
                        return false;
                    }
                }
            }
        }
        return true;

    }

    private int checkAdj(double direction, int xPos, int yPos, Point[][] tempA) {
        int length = generateLength(direction, xPos, yPos);
        if (direction < 0.25) {
            if (xPos - length < 1) {
                return 0;
            }
            for (int x = 1; x < length; x++) {
                if (tempA[xPos - x][Math.max(0, yPos - 1)].typeOfTile == 1
                        || tempA[xPos - x][Math.min(HEIGHT - 1, yPos + 1)].typeOfTile == 1) {
                    return 0;
                }
            }
        } else if (direction >= .25 && direction < .5) {
            if (yPos + length > HEIGHT - 2) {
                return 0;
            }
            for (int x = 1; x < length; x++) {
                if (tempA[Math.max(0, xPos - 1)][yPos + x].typeOfTile == 1
                        || tempA[Math.min(WIDTH - 1, xPos + 1)][yPos + x].typeOfTile == 1) {
                    return 0;
                }
            }
        } else if (direction >= .5 && direction < .75) {
            if (xPos + length > WIDTH - 2) {
                return 0;
            }
            for (int x = 1; x < length; x++) {
                if (tempA[xPos + x][Math.max(0, yPos - 1)].typeOfTile == 1
                        || tempA[xPos + x][Math.min(HEIGHT - 1, yPos + 1)].typeOfTile == 1) {
                    return 0;
                }
            }
        } else {
            if (yPos - length < 1) {
                return 0;
            }
            for (int x = 1; x < length; x++) {
                if (tempA[Math.max(0, xPos - 1)][yPos - x].typeOfTile == 1
                        || tempA[Math.min(WIDTH - 1, xPos + 1)][yPos - x].typeOfTile == 1) {
                    return 0;
                }
            }
        }
        return length;
    }

    private Point lockedDoor(Point[][] file, ArrayList<Point> list) {

        int counter = 0;
        Point answer;
        while (true) {
            Point rPoint = list.get(random.nextInt(list.size()));
            int x = rPoint.xPos;
            int y = rPoint.yPos;
            if (x >= 0 && y >= 0 && x <= WIDTH - 2 && y <= HEIGHT - 2) {
                if (file[x - 1][y].typeOfTile == 2 && file[x][y + 1].typeOfTile == 2) {
                    counter += 1;
                } else if (file[x - 1][y].typeOfTile == 2 && file[x][y - 1].typeOfTile == 2) {
                    counter += 1;
                } else if (file[x + 1][y].typeOfTile == 2 && file[x][y + 1].typeOfTile == 2) {
                    counter += 1;
                } else if (file[x + 1][y].typeOfTile == 2 && file[x][y - 1].typeOfTile == 2) {
                    counter += 1;
                } else {
                    file[x][y].typeOfTile = 3;
                    answer = rPoint;
                    break;
                }
            }
        }
        return answer;
    }

    public Avatar createPlayer(Color color) {
        return new Avatar(color);
    }

    public Monster createEnemy() {

        return new Monster();
    }

    public static void main(String[] args) {

        MapGenerator gen = new MapGenerator(1, Color.BLUE);
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        TERenderer ter = new TERenderer();
        gen.draw(world);
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                System.out.println(world[i][j].description());
            }
        }
    }
}
