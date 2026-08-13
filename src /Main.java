//Your Name Emre Ezgu
//Student ID 2024400144

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    //reader function
    public static int[][] readData(String fileName){
        ArrayList<int[]> infos=new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(fileName));
            while (scanner.hasNextLine()) {
                int[] info=new int[4];
                String line = scanner.nextLine().trim();
                String[] lineSplit = line.split(",");
                int xCoord = Integer.parseInt(lineSplit[0]);
                info[0]=xCoord;
                int yCoord =Integer.parseInt(lineSplit[1]);
                info[1]=yCoord;
                int xSize = Integer.parseInt(lineSplit[2]);
                info[2]=xSize;
                int ySize=Integer.parseInt(lineSplit[3]);
                info[3]=ySize;
                infos.add(info);
            }
            scanner.close();
        }
        catch (FileNotFoundException e){
            System.out.println("file not found");
        }
        return infos.toArray(new int[0][]);
    }
    public static void main(String[] args) {
        final int width = 800;
        final int height = 800;

        final int cameraModeW = 400;
        final int cameraModeH = 400;

        StdDraw.setCanvasSize(width, height);
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);
        StdDraw.setTitle("Super Mario Bros.");
        StdDraw.enableDoubleBuffering();
        //creating enemies
        Enemy enemy0 = new Enemy(340, 580, 40, -1, 260, 420,"default"); //new Enemy(x, y, size, direction, x1, x2)
        Enemy enemy1 = new Enemy(440, 300, 40, 1, 380, 500,"default"); //new Enemy(x, y, size, direction, x1, x2)
        Enemy customEnemy=new Enemy(520,613,80,50,1,465,575,"custom");
        String clue0 = "Go to the exit pipe!";
        String clue1 = "Do not mess with mushrooms!";
        String clue2 = "Do not forget to collect all the coins!";
        String clue3="Hedgehog can follow you!";

        Color skyColor = new Color(146, 144, 255);
        Color mainMenuColor0 = new Color(153, 78, 0);
        Color mainMenuColor1 = new Color(100, 50, 0);
        Color mainMenuColor2 = new Color(255, 204, 197);
        Color marioDarkBlue = new Color(32, 56, 136);
        Color marioRed = new Color(228, 0, 18);
        Color marioGold = new Color(255, 183, 0);
        Color marioLightBlue = new Color(107, 140, 255);
        //creating class objects
        Mario mario=new Mario();
        Map map=new Map(readData("data/obstacles.txt"),readData("data/pipes.txt"),readData("data/portals.txt"),mario);
        Level level1=new Level(1,clue0);
        ArrayList<Enemy> enemies=new ArrayList<>();
        ArrayList<Enemy> customEnemies=new ArrayList<>();
        customEnemies.add(enemy0);
        enemies.add(enemy0);
        customEnemies.add(enemy1);
        enemies.add(enemy1);
        customEnemies.add(customEnemy);
        Level level2=new Level(2,enemies,clue1);
        Level level3=new Level(3,enemies,clue2,readData("data/coins.txt"));
        Level customLevel=new Level(4,customEnemies,clue3,readData("data/coins.txt"));
        ArrayList<Level> levels=new ArrayList<>();
        levels.add(level1);
        levels.add(level2);
        levels.add(level3);
        levels.add(customLevel);
        Game game=new Game(levels,map);

        mario.x=400;
        mario.y=140;
        int frame=0;
        boolean isGame=false;
        int pause=10 ;
        int fps=100;
        while (!isGame) {
            //changing fps according to player's input
            if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)&&fps<300){
                pause-=1;
            }
            if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT)&&fps>30){
                pause+=1;
            }
            //drawing start screen and other things
            game.startScreen(mario, fps);
            fps= Math.round(1000/pause);
            mario.handleInput(mario.y == 140,false,false,false);
            //start screen apply gravity
            mario.y += mario.speedY;
            if (mario.y > 140) {
                mario.speedY -= mario.gravity;
            } else {
                mario.speedY = 0;
            }
            mario.draw(frame, mario.y == 140);
            //when space is pressed pass to the main game
            if (StdDraw.isKeyPressed(KeyEvent.VK_SPACE)) {
                mario.x = mario.spawnX;
                mario.y = mario.spawnY;
                isGame = true;
            }
            frame++;
            StdDraw.show();
            StdDraw.pause(pause);
        }
        //running the main game
        game.run(mario,pause);
    }

}