

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class Game {
    ArrayList<Level> levels;
    Map map;
    private boolean isCamera=false;
    long pausedTime=0;
    public Game(ArrayList<Level> levels, Map map){
        this.levels=levels;
        this.map=map;
    }
    //deepCopy for copying coins array
    public static int[][] deepCopy(int[][] original) {
        if (original == null) return null;
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }
    public void updateCamera(Mario mario) {
        if (StdDraw.isKeyPressed(KeyEvent.VK_C)) {
            isCamera = !isCamera;
            //if player push c bad things will happen so pause after pressing c
            StdDraw.pause(100);
        }
        //standard camera mode
        if (!isCamera) {
            StdDraw.setXscale(0, 800);
            StdDraw.setYscale(0, 800);
        }
        //close camera mode
        else {
            //show the 200 unit distance from mario
            double zoomLevel = 200;
            //camera boundaries
            double minX = mario.x - zoomLevel;
            double maxX = mario.x + zoomLevel;
            double minY = mario.y - zoomLevel;
            double maxY = mario.y + zoomLevel;
            // preserve map boundaries
            if (minX < 0) {
                minX = 0; maxX = 400;
            }
            if (maxX > 800) {
                maxX = 800; minX = 400;
            }
            if (minY < 0) {
                minY = 0; maxY = 400;
            }
            if (maxY > 800) {
                maxY = 800; minY = 400;
            }
            StdDraw.setXscale(minX, maxX);
            StdDraw.setYscale(minY, maxY);
        }
    }
    public void run(Mario mario,int pause){
        //initially setting level time and frame
        int levelId=0;
        long start=System.currentTimeMillis();
        int frame=0;
        while (levelId<levels.size()){
            //getting current level, enemies and coins
            Level currentLevel=levels.get(levelId);
            ArrayList<Enemy> enemies=currentLevel.getEnemies();
            map.coins=deepCopy(currentLevel.getCoins());
            while (true){
                updateCamera(mario);
                StdDraw.clear(new Color(146,144,255));
                //checking around of mario
                boolean onGround=map.isOnGround(mario, mario.speedY);
                boolean isWallOnRight=map.isWallOnRight(mario.x, mario.y,mario.getSize()/2);
                boolean isWallOnLeft=map.isWallOnLeft(mario.x, mario.y, mario.getSize()/2);
                //Resetting the game when R is pressed
                if (StdDraw.isKeyPressed(KeyEvent.VK_R)){
                    levelId=0;
                    pausedTime=0;
                    mario.deathCount=0;
                    start=System.currentTimeMillis();
                    mario.respawn();
                    break;
                }
                //stopping mario when he hits a ceiling
                if (map.isOnCeiling(mario.x, mario.y, mario.getSize()/2)&&!mario.isDead){
                    mario.speedY=0;
                }
                //handling enemy
                if (enemies!=null){
                    for (Enemy enemy:enemies){
                        //default enemy movement
                        if (enemy.enemyType.equals("default")){
                            enemy.move();
                        }
                        //custom enemy movement
                        else {
                            enemy.customMove(mario.x, mario.y,mario.isDead);
                        }
                        enemy.draw(frame);
                        if (mario.checkEnemyCollision(enemy)){
                            mario.die();
                        }
                    }
                }
                mario.handleInput(onGround,isWallOnLeft,isWallOnRight, map.inPortal(mario));
                applyGravity(mario);
                //handling coins
                if (map.coins!=null){
                    map.collectCoin(mario,mario.isDead);
                }
                map.handleTeleport(mario,StdDraw.isKeyPressed(KeyEvent.VK_S));
                map.drawMap(frame,onGround);
                //while drawing HUD or End Screen ,scale should be normal
                StdDraw.setXscale(0,800);
                StdDraw.setYscale(0,800);
                drawBot(currentLevel,getTime(start),mario.getDeathCount());
                frame++;
                //handling exit situation
                if (map.isAtExit(mario)&&!mario.isDead){
                    //if there is coins in that level, mario cannot pass without collecting all coins
                    boolean isAllCoins=true;
                    if (levelId>=2){
                        for (int[] line:map.coins){
                            if (line[0]==-10&&line[1]==-10){
                                continue;
                            }
                            else isAllCoins=false;
                        }
                    }
                    //if all coins are collected mario can finish the game or pass the level
                    if (isAllCoins) {
                        levelId++;
                        mario.x = mario.spawnX;
                        mario.y = mario.spawnY;
                        //finishing the game
                        if (levelId == levels.size()) {
                            boolean isRestart = false;
                            endScreen(getTime(start), mario.getDeathCount(), isRestart, pause);
                            //if player chooses to play again, resetting the level,time and death
                            pausedTime=0;
                            start=System.currentTimeMillis();
                            mario.deathCount=0;
                            levelId = 0;
                            break;
                        }
                        //displaying level banner after passing the level
                        levelBanner(currentLevel.getId());
                        break;
                    }
                }
                //after drawing HUD or End Screen, restoring the camera
                updateCamera(mario);
                StdDraw.show();
                StdDraw.pause(pause);
            }
        }
    }
    public void drawBot(Level level,String time,int death){
        StdDraw.setPenColor(new Color(32,56,136));
        StdDraw.filledRectangle(400,70,400,70);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new Font("Arial",Font.PLAIN,25));
        StdDraw.text(100,100,"Move: [A] [D] [W]");
        StdDraw.text(100,70,"Restart: [R]");
        StdDraw.text(100,40,"Camera: [C]");
        StdDraw.text(700,100,"Level "+level.getId());
        StdDraw.text(700,70,time);
        StdDraw.setPenColor(new Color(255,162,0));
        StdDraw.text(400,70,level.getClue());
        StdDraw.text(700,40,"Deaths: "+death);
    }
    public void applyGravity(Mario mario){
        //if mario is falling to ground, having him stop
        if (map.isOnGround(mario, mario.speedY)&&!mario.isDead) {
            if (mario.speedY < 0) mario.speedY = 0;
        }
        //otherwise keep falling or rising with accelaration
        else {
            mario.speedY -= mario.gravity;
        }
        //finally add his speed to his location
        mario.y += mario.speedY;
    }
    public void startScreen(Mario mario,double fps){
        //ground situation for start screen
        if (mario.y==140){
            mario.speedY=0;
        }
        //drawing the caption and user guide
        StdDraw.clear(new Color(146,144,255));
        StdDraw.setPenColor(new Color(153,78,0));
        StdDraw.filledRectangle(400,600,300,100);
        StdDraw.setPenColor(new Color(100,50,0));
        StdDraw.rectangle(400,600,300,100);
        StdDraw.setPenColor(new Color(255,204,197));
        StdDraw.setFont(new Font("Arial",Font.PLAIN,40));
        StdDraw.text(400,630,"Super");
        StdDraw.setFont(new Font("Arial",Font.PLAIN,80));
        StdDraw.text(400,560,"MARİO BROS.");
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont(new Font("Arial",Font.PLAIN,20));
        StdDraw.text(400,460,"Press [Space] to start.");
        StdDraw.setFont(new Font("Arial",Font.ITALIC,15));
        StdDraw.text(400,435,"Move:  [A]  [D]  [W]");
        StdDraw.text(400,415,"FPS:  ∽"+fps+"    Adjust:   [←]   [→]");
        //drawing blocks
        for (int i=0;i<60;i++) {
            int obsX=map.obstacles[i][0];
            int obsY=map.obstacles[i][1];
            int obsSizeX=2*map.obstacles[i][2];
            int obsSizeY=2*map.obstacles[i][3];
            StdDraw.picture(obsX,obsY,"assets/block.png",obsSizeX,obsSizeY);
        }
    }
    public void levelBanner(int id){
        //when lever banner displays, keep the current time in memory
        long pauseStart=System.currentTimeMillis();
        while (true) {
            //drawing level banner
            StdDraw.setPenColor(new Color(32, 56, 136));
            StdDraw.filledRectangle(400, 400, 400, 100);
            StdDraw.setPenColor(new Color(255, 162, 0));
            StdDraw.setFont(new Font("Arial", Font.PLAIN, 50));
            StdDraw.text(400, 450, "Level " + id + " Complete!");
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.text(400, 350, "Press [Space] to Continue");
            //when player presses space game must go on
            if (StdDraw.isKeyPressed(KeyEvent.VK_SPACE)){
                //subtracting the time that is spent in level banner from total time
                long pauseEnd=System.currentTimeMillis();
                pausedTime+=pauseEnd-pauseStart;
                break;
            }
            StdDraw.show();
        }
    }
    public void endScreen(String elapsedTime,int deathCount,boolean isRestart,int pause){
        while (true) {
            //drawing the end screen
            StdDraw.clear(new Color(32, 56, 136));
            StdDraw.setFont(new Font("Arial", Font.PLAIN, 20));
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.text(400, 300, "Time: " + elapsedTime);
            StdDraw.text(400, 260, "Deaths: " + deathCount);
            StdDraw.setFont(new Font("Arial", Font.PLAIN, 60));
            StdDraw.setPenColor(new Color(255, 183, 0));
            StdDraw.text(400, 620, "You Won!");
            //when cursor is on the exit
            if (!isRestart) {
                StdDraw.setPenColor(new Color(255, 183, 0));
                StdDraw.setFont(new Font("Arial", Font.PLAIN, 30));
                StdDraw.text(400, 470, "> Exit <");
                StdDraw.setPenColor(new Color(10, 86, 180));
                StdDraw.text(400, 430, "Restart");
            }
            //when cursor is on the restart
            else {
                StdDraw.setPenColor(new Color(10, 86, 180));
                StdDraw.setFont(new Font("Arial", Font.PLAIN, 30));
                StdDraw.text(400, 470, "Exit");
                StdDraw.setPenColor(new Color(255, 183, 0));
                StdDraw.text(400, 430, "> Restart <");
            }
            //moving cursor
            if (StdDraw.isKeyPressed(KeyEvent.VK_UP)){
                isRestart=false;
            }
            if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN)){
                isRestart=true;
            }
            //when space is pressed, apply the player's choice
            if (StdDraw.isKeyPressed(KeyEvent.VK_SPACE)){
                if (isRestart){
                    break;
                }
                else {
                    System.exit(0);
                }
            }
            StdDraw.show();
            StdDraw.pause(pause);
        }
    }
    public String getTime(long startingTime){
        long diff=System.currentTimeMillis()-startingTime-pausedTime;
        long min=(diff/1000)/60;
        long sec=(diff/1000)%60;
        long mils=(diff%1000)/10;
        return String.format("%02d:%02d:%02d",min,sec,mils);
    }
}
