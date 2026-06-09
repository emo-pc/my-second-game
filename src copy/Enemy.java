//Your Name Emre Ezgu
//Student ID 2024400144

public class Enemy {
    private int x;
    private int y;
    private int size;
    public int sizeX;
    public int sizeY;
    int direction;
    int x1;
    int x2;
    boolean isRight;
    String enemyType;
    public Enemy(int x,int y,int size,int direction,int x1,int x2,String enemyType){
        this.x=x;
        this.y=y;
        this.size=size;
        this.x1=x1;
        this.x2=x2;
        this.direction=direction;
        this.enemyType=enemyType;

    }
    public Enemy(int x,int y,int sizeX,int sizeY,int direction,int x1,int x2,String enemyType){
        this.x=x;
        this.y=y;
        this.sizeX=sizeX;
        this.sizeY=sizeY;
        this.x1=x1;
        this.x2=x2;
        this.direction=direction;
        this.enemyType=enemyType;

    }
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
    public int getSize(){
        return this.size;
    }
    public void move(){
        x+=direction*3;
        if (x>x2||x<x1){
            direction*=-1;
        }
    }
    //movement mechanism of custom enemy
    public void customMove(int x,int y,boolean isDead){
        //if mario is in the enemys eyesight enemy go forward to player
        if (x>420&&x<620&&y>619&&!isDead){
            if (x>this.x){
                direction=1;
                isRight=true;
            }
            else if (x==this.x){
                direction=0;
            }
            else {
                direction=-1;
                isRight=false;
            }
        }
        //otherwise moves like a default enemy
        else {
            if (direction==0){
                if (isRight){
                    direction=1;
                }
                else direction=-1;
            }
            if (this.x>x2||this.x<x1){
                direction*=-1;
            }
            if (direction==1){
                isRight=true;
            }
            else {
                isRight=false;
            }
        }
        this.x+=direction;
    }
    public void draw(int frame){
        if (this.enemyType.equals("default"))
            StdDraw.picture(x,y,"assets/mushroom.png",size,size);
        //drawing the hedgehog
        else {
            //user can add movement mechanism by changing png files, i did not because i could not create suitable movement images
            if (isRight){
                if ((frame/8)%2==0) {
                    StdDraw.picture(x, y, "assets/sagciKirpi2.png", sizeX, sizeY);
                }
                else {
                    StdDraw.picture(x,y,"assets/sagciKirpi2.png",sizeX,sizeY);
                }
            }
            else {
                if ((frame/8)%2==0) {
                    StdDraw.picture(x, y, "assets/solcuKirpi1.png", sizeX, sizeY);
                }
                else {
                    StdDraw.picture(x,y,"assets/solcuKirpi1.png",sizeX,sizeY);
                }
            }
        }
    }

}
