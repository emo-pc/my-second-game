

import java.awt.*;
import java.awt.event.KeyEvent;

public class Map {
    public int[][] obstacles;
    public int[][] pipes;
    public int[][] portals;
    public int[][] coins;
    public boolean isPortal;
    public Mario mario;
    private int teleportTimer=0;
    public Map(int[][] obstacles,int[][] pipes,int[][] portals,Mario mario){
        this.obstacles=obstacles;
        this.pipes=pipes;
        this.portals=portals;
        this.mario=mario;
        this.isPortal=false;
    }
    public boolean isOnGround(Mario mario,double speed){
        //if mario is dead there is no importance of checking the ground situation
        if (mario.isDead){return false;}
        //checking whether mario is colliding with any obstacle
        for (int i=60;i<obstacles.length;i++){
            int obsX=obstacles[i][0];
            int obsY=obstacles[i][1];
            int obsSizeX=obstacles[i][2];
            int obsSizeY=obstacles[i][3];
            boolean xOnGround=Math.abs(obsX-mario.x)<mario.getSize()/2+obsSizeX;
            boolean yOnGround=mario.y-obsY<=mario.getSize()/2+obsSizeY+2&&mario.y-obsY>0;
            //if there is collision with block and mario,teleport mario to the top of that block
            if (xOnGround&&yOnGround){
                mario.y=obsY+obsSizeY+mario.getSize()/2;
                return true;
            }
        }
        //for portal mechanism checking whether mario is colliding with the bottom portal
        int portalX=portals[3][0];
        int portalY=portals[3][1];
        int portalSizeX=portals[3][2];
        int portalSizeY=portals[3][3];
        if (Math.abs(mario.x-portalX)<portalSizeX+ mario.getSize()/2&&Math.abs(mario.y-portalY)<2*portalSizeY+ mario.getSize()/2){
            //if s is not pressed all entrance of portal is acts like obstacle
            if (!StdDraw.isKeyPressed(KeyEvent.VK_S)){
                if (mario.speedY <= 0) {
                    mario.y = portalY + portalSizeY + mario.getSize() / 2;
                }
                return true;
            }
            //if s is pressed and mario is between the appropriate x coordinate return false to enable portal mechanism
            else {
                if (Math.abs(mario.x-portalX)<5){
                    return false;
                }
                else return true;
            }
        }
        //bottom pipe ground situation part1
        int pipeX1=pipes[2][0];
        int pipeY1=pipes[2][1];
        int pipeSizeX1=pipes[2][2];
        int pipeSizeY1=pipes[2][3];
        if (Math.abs(mario.x-pipeX1)<mario.getSize()/2+pipeSizeX1&&mario.y>pipeSizeY1+pipeY1&&mario.y-(pipeSizeY1+pipeY1)<2+ mario.getSize()/2){
            return true;
        }
        //bottom pipe ground situation part2
        int pipeX2=pipes[3][0];
        int pipeY2=pipes[3][1];
        int pipeSizeX2=pipes[3][2];
        int pipeSizeY2=pipes[3][3];
        if (Math.abs(mario.x-pipeX2)< mario.getSize()/2+pipeSizeX2&&mario.y>pipeSizeY2+pipeY2&&mario.y-(pipeSizeY2+pipeY2)< mario.getSize()/2+2){
            return true;
        }
        return false;
    }
    //a function which just checks whether mario is in the entrance of any portal
    public boolean inPortal(Mario mario){
        int portalX=portals[3][0];
        int portalY=portals[3][1];
        int portalSizeX=portals[3][2];
        int portalSizeY=portals[3][3];
        boolean isPortal1=Math.abs(mario.x-portalX)<5&&Math.abs(mario.y-portalY)<portalSizeY+ mario.getSize()/2;
        int portalX2=portals[2][0];
        int portalY2=portals[2][1];
        int portalSizeX2=portals[2][2];
        int portalSizeY2=portals[2][3];
        boolean isPortal2=Math.abs(mario.x-portalX2)<5&&portalY2-mario.y<portalSizeY2+ mario.getSize()/2;
        return isPortal1||isPortal2;
    }
    public boolean isOnCeiling(int x,int y,double halfSize){
        if (mario.isDead){return false;}
        for (int i=60;i<obstacles.length;i++){
            int obsX=obstacles[i][0];
            int obsY=obstacles[i][1];
            int obsSizeX=obstacles[i][2];
            int obsSizeY=obstacles[i][3];
            boolean xOnGround=Math.abs(obsX-x)<halfSize+obsSizeX;
            boolean yOnGround=obsY-y<halfSize+obsSizeY+2&&obsY-y>0;
            if (xOnGround&&yOnGround){
                return true;
            }
            //handling the upper portal
            int portalX=portals[1][0];
            int portalY=portals[1][1];
            int portalSizeX=portals[1][2];
            int portalSizeY=portals[1][3];
            //if mario is colliding with upper portal but not entrance of portal then return true otherwise return false
            if (Math.abs(mario.x-portalX)>5&& Math.abs(mario.x-portalX)<portalSizeX+ mario.getSize()/2&&Math.abs(mario.y-portalY)<portalSizeY+ mario.getSize()/2){
                //having mario stop
                if (mario.speedY>0) {
                    mario.y = portalY -portalSizeY - mario.getSize() / 2;
                }
                return true;
            }
            //bottom pipe ceiling situation
            int pipeX1=pipes[2][0];
            int pipeY1=pipes[2][1];
            int pipeSizeX1=pipes[2][2];
            int pipeSizeY1=pipes[2][3];
            if (Math.abs(mario.x-pipeX1)< 20+mario.getSize()/2+pipeSizeX1&&mario.y<pipeY1+pipeSizeY1&&mario.y>pipeY1-pipeSizeY1){
                return true;
            }
            //top pipe ceiling situation
            int pipeX2=pipes[1][0];
            int pipeY2=pipes[1][1];
            int pipeSizeX2=pipes[1][2];
            int pipeSizeY2=pipes[1][3];
            if (Math.abs(mario.x-pipeX2)<mario.getSize()/2+pipeSizeX2&&(mario.x>pipeX2+20-mario.getSize()/2||mario.x<pipeX2-20+mario.getSize()/2)&&pipeY2-pipeSizeY2-mario.y<mario.getSize()/2&&pipeY2-pipeSizeY2> mario.y){
                return true;
            }
        }
        return false;
    }
    public boolean isWallOnRight(double x,double y,double halfSize){
        if (mario.isDead){return false;}
        //checking whether is there a block at the right of mario
        for (int i=60;i<obstacles.length;i++){
            int obsX=obstacles[i][0];
            int obsY=obstacles[i][1];
            int obsSizeX=obstacles[i][2];
            int obsSizeY=obstacles[i][3];
            boolean wallRight=obsX-x==halfSize+obsSizeX&&obsX-x>0;
            boolean yOnGround=Math.abs(y-obsY)<halfSize+obsSizeY-2;
            if (wallRight&&yOnGround){
                return true;
            }
        }
        //if mario is in portal right side of portal should act like an obstacle
        int portalX1=portals[0][0];
        int portalY1=portals[0][1];
        int portalSizeX1=portals[0][2];
        int portalSizeY1=portals[0][3];
        if (Math.abs(y-portalY1)<portalSizeY1+ mario.getSize()/2&&portalX1+portalSizeX1- mario.x< mario.getSize()/2&&portalX1+portalSizeX1>mario.x){
            return true;
        }
        int portalX2=portals[2][0];
        int portalY2=portals[2][1];
        int portalSizeX2=portals[2][2];
        int portalSizeY2=portals[2][3];
        if (Math.abs(y-portalY2)<2*portalSizeY2+ mario.getSize()/2&&portalX2+portalSizeX2-mario.x< mario.getSize()/2&&portalX2+portalSizeX2>mario.x){
            return true;
        }
        //bottom pipe situation
        int pipeX1=pipes[3][0];
        int pipeY1=pipes[3][1];
        int pipeSizeX1=pipes[3][2];
        int pipeSizeY1=pipes[3][3];
        if (pipeX1-mario.x<mario.getSize()/2+pipeSizeX1&&pipeX1>mario.x&&mario.y>455&&mario.y<465){
            return true;
        }
        //inside top pipe situation
        int pipeX2=pipes[0][0];
        int pipeY2=pipes[0][1];
        int pipeSizeX2=pipes[0][2];
        int pipeSizeY2=pipes[0][3];
        if (pipeX2+pipeSizeX2-mario.x<mario.getSize()/2+2&&pipeX2+pipeSizeX2>mario.x&&Math.abs(mario.y-pipeY2)<mario.getSize()/2+pipeSizeY2){
            return true;
        }
        return false;
    }
    public boolean isWallOnLeft(int x,int y,double halfSize){
        if (mario.isDead){return false;}
        //checking whether is there a block at the left of mario
        for (int i=60;i<obstacles.length;i++){
            int obsX=obstacles[i][0];
            int obsY=obstacles[i][1];
            int obsSizeX=obstacles[i][2];
            int obsSizeY=obstacles[i][3];
            boolean wallLeft=x-obsX==halfSize+obsSizeX&&x-obsX>0;
            boolean yOnGround=Math.abs(obsY-y)<halfSize+obsSizeY-2;
            if (wallLeft&&yOnGround){
                return true;
            }
        }
        //if mario is in portal ,left side of portal should act like an obstacle
        int portalX1=portals[0][0];
        int portalY1=portals[0][1];
        int portalSizeX1=portals[0][2];
        int portalSizeY1=portals[0][3];
        if (Math.abs(y-portalY1)<2*portalSizeY1+ mario.getSize()/2&&mario.x-(portalX1-portalSizeX1)< mario.getSize()/2&&mario.x>portalX1-portalSizeX1){
            return true;
        }
        int portalX2=portals[2][0];
        int portalY2=portals[2][1];
        int portalSizeX2=portals[2][2];
        int portalSizeY2=portals[2][3];
        if (Math.abs(y-portalY2)<2*portalSizeY2+ mario.getSize()/2&&mario.x-(portalX2-portalSizeX2)< mario.getSize()/2&&mario.x>portalX2-portalSizeX2){
            return true;
        }
        //inside top pipe situation
        int pipeX1=pipes[0][0];
        int pipeY1=pipes[0][1];
        int pipeSizeX1=pipes[0][2];
        int pipeSizeY1=pipes[0][3];
        if (Math.abs(mario.y-pipeY1)<mario.getSize()/2+pipeSizeY1&&mario.x-(pipeX1-pipeSizeX1)<mario.getSize()/2+2&&mario.x>pipeX1-pipeSizeX1){
            return true;
        }
        //top pipe situation
        int pipeX2=pipes[1][0];
        int pipeY2=pipes[1][1];
        int pipeSizeX2=pipes[1][2];
        int pipeSizeY2=pipes[1][3];
        if (mario.x-pipeX2<mario.getSize()/2+pipeSizeX2+2&&mario.x>pipeX2&&mario.y>720){
            return true;
        }
        //bot pipe situation
        int pipeX3=pipes[3][0];
        int pipeY3=pipes[3][1];
        int pipeSizeX3=pipes[3][2];
        int pipeSizeY3=pipes[3][3];
        if (mario.x-(pipeX3+pipeSizeX3)<2+mario.getSize()/2&&mario.x>pipeX3+pipeSizeX3&&Math.abs(mario.y-(pipeY3+25))<mario.getSize()/2-5){
            return true;
        }
        return false;
    }
    public void collectCoin(Mario mario,boolean isDead){
        for(int i=0;i<coins.length;i++){
            //if coins is already collected pass it
            if (coins[i][0]==-10){
                continue;
            }
            int coinX=coins[i][0];
            int coinY=coins[i][1];
            int coinSizeX=coins[i][2];
            int coinSizeY=coins[i][3];
            StdDraw.picture(coinX,coinY,"assets/coin.png",2*coinSizeX,2*coinSizeY);
            //checking mario-coin collision
            if (!isDead) {
                if (Math.abs(mario.x - coinX) <= mario.getSize() / 2 + coinSizeX && Math.abs(mario.y - coinY) <= mario.getSize() / 2 + coinSizeY) {
                    //setting coin's coordinates in order to make it disappear
                    coins[i][0] = -10;
                    coins[i][1] = -10;
                }
            }
        }
    }
    public void handleTeleport(Mario mario,boolean isPressed){
        //handling the upper portal
        if (checkCollision(mario.x, mario.y, mario.getSize() / 2, new int[][]{portals[0]})) {
            if (!isPortal) {
                mario.x = portals[3][0];
                mario.y = portals[3][1] + mario.getSize() / 2;
                mario.speedY = 5;
                //flag that prevents mario from oscillating
                isPortal=true;
            }
        }
        //handling the bottom portal
        else if (checkCollision(mario.x, mario.y, mario.getSize()/2,new int[][]{portals[2]})) {
            //enabling crouch animation
            if (isPressed && !isPortal && teleportTimer == 0) {
                teleportTimer = 17;
            }
            if (teleportTimer > 0) {
                teleportTimer--;
                mario.y -= 1.5;
                mario.speedY = 0;
                //reset the timer
                if (teleportTimer == 1) {
                    mario.x = portals[1][0];
                    mario.y = portals[1][1] + mario.getSize() / 2 + 10;
                    mario.speedY =0;
                    isPortal = true;
                }
            }
        }
        //after conducting the teleport mario can teleport again
        else isPortal=false;
    }
    public boolean isAtExit(Mario mario){
        if (Math.abs(mario.x-pipes[2][0])<2&&mario.y==pipes[2][1]){
            return true;
        }
        return false;
    }
    //collision checker for portal and pipe collisions
    public boolean checkCollision(double nextX,double nextY,double playerHalfSize,int[][] array){
        for (int[] line:array){
            int obsX=line[0];
            int obsY=line[1];
            int obsSizeX=line[2];
            int obsSizeY=line[3];
            if (Math.abs(nextX-obsX)<=playerHalfSize-5&&Math.abs(nextY-obsY)<=playerHalfSize+obsSizeY){
                return true;
            }
        }
        return false;
    }
    public void drawMap(int frame,boolean onGround){
        //if mario is not dead first draw mario then obstacles etc.
        if (!mario.isDead){
            mario.draw(frame,onGround);
        }
        //drawing obstacles
        for (int i=60;i<obstacles.length;i++){
            int obsX=obstacles[i][0];
            int obsY=obstacles[i][1];
            int obsSizeX=2*obstacles[i][2];
            int obsSizeY=2*obstacles[i][3];
            StdDraw.picture(obsX,obsY,"assets/block.png",obsSizeX,obsSizeY);
        }
        //drawing pipes
        for (int[] line:pipes){
            int pipeX=line[0];
            int pipeY=line[1];
            int pipeSizeX=line[2];
            int pipeSizeY=line[3];
            StdDraw.setPenColor(new Color(255,183,0));
            StdDraw.filledRectangle(pipeX,pipeY,pipeSizeX,pipeSizeY);
        }
        //drawing portals
        for (int[] line:portals){
            int portalX=line[0];
            int portalY=line[1];
            int portalSizeX=line[2];
            int portalSizeY=line[3];
            StdDraw.setPenColor(new Color(228,0,18));
            StdDraw.filledRectangle(portalX,portalY,portalSizeX,portalSizeY);
        }
        //if mario is dead last draw mario
        if (mario.isDead){
            mario.draw(frame,onGround);
        }

    }
}
