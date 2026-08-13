//Your Name Emre Ezgu
//Student ID 2024400144

import java.awt.event.KeyEvent;

public class Mario {

    final int spawnX = 60;
    final int spawnY = 770;
    private final int size = 40;
    public int x;
    public int y;
    public double speedY=0;
    public double gravity=0.5;
    public int deathCount=0;
    boolean isDead=false;
    private boolean isRight=true;
    public int state=0;

    public Mario(){
        this.x=spawnX;
        this.y=spawnY;
        this.deathCount=0;
    }
    public int getSize(){
        return this.size;
    }
    public int getDeathCount(){
        return this.deathCount;
    }
    public int handleInput(boolean onGround,boolean wallOnLeft,boolean wallOnRight,boolean inPortal){
        //if mario dead no input is taken
        if (isDead){
            //after dead animation respawn mario
            if (this.y<0){
                respawn();
            }
            return 0;
        }
        //if there is no input state stand
        state=0;
        if (StdDraw.isKeyPressed(KeyEvent.VK_D)){
            //if right is empty and not out of the map walk right
            if(!wallOnRight) {
                if (this.x < 780) {
                    this.x += 5.0;
                    this.isRight = true;
                    state = 1;
                }
            }
        }
        else if (StdDraw.isKeyPressed(KeyEvent.VK_A)){
            //if left is empty and not out of the map walk left
            if (!wallOnLeft) {
                if (this.x > 20) {
                    this.x -= 5.0;
                    this.isRight = false;
                    state = -1;
                }
            }
        }
        //portal mechanism: if mario at the middle of the bottom portal and s is pressed move downward with crouch image
        if (StdDraw.isKeyPressed(KeyEvent.VK_S)&&inPortal&&!onGround){
            //this.y-=2;
            state=2;
        }
        //if mario on ground and w is pressed jump
        if (onGround&&StdDraw.isKeyPressed(KeyEvent.VK_W)){
            jump();
        }
        //according to state mario image will be chosen
        return state;
    }
    public void jump(){
        this.speedY=10;
    }
    public boolean checkEnemyCollision(Enemy enemy){
        if (enemy.enemyType.equals("default")) {
            if (Math.abs(enemy.getX() - x) < (enemy.getSize() + size) / 2 && Math.abs(enemy.getY() - y) < (enemy.getSize() + size) / 2) {
                return true;
            }
        }
        else {
            if (Math.abs(enemy.getX()-x)<(enemy.sizeX-35+size)/2&&Math.abs(enemy.getY()-y)<(enemy.sizeY+size)/2){
                return true;
            }
        }
        return false;
    }
    public void die(){
        if (!isDead) {
            isDead=true;
            deathCount++;
            speedY=15.0;
        }
    }
    public void respawn(){
        x=spawnX;
        y=spawnY;
        speedY=0;
        isDead=false;
    }
    public void draw(int frame,boolean onGround){
        String img="";
        if (isDead){
            img="assets/dead.png";
        }
        else if (!onGround){
            if (isRight){
                img="assets/jumpRight.png";
            }
            else{
                img="assets/jumpLeft.png";
            }
            if (state==2){
                img="assets/crouch.png";
            }
        }
        else{
            if (state==0){
                if (isRight){
                    img="assets/standRight.png";
                }
                else {
                    img="assets/standLeft.png";
                }
            }
            else if (state==1) {
                //changing img according to frame in order to enable walking animation
                if ((frame / 8) % 2 == 0) {
                    img = "assets/walkRight1.png";
                } else {
                    img = "assets/walkRight2.png";
                }
            }
            else if (state==-1){
                if ((frame/8)%2==0){
                    img="assets/walkLeft1.png";
                }
                else{
                    img="assets/walkLeft2.png";
                }
            }
        }
        if (!img.isEmpty()){
            StdDraw.picture(x, y, img, size, size);
        }
    }













}
