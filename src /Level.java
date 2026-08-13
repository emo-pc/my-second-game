//Your Name Emre Ezgu
//Student ID 2024400144

import java.util.ArrayList;

public class Level {
    private int id;
    private ArrayList<Enemy> enemies;
    private String clue;
    private int[][] coins;
    public Level(int id,ArrayList<Enemy> enemies,String clue,int[][] coins){
        this.id=id;
        this.enemies=enemies;
        this.clue=clue;
        this.coins=coins;
    }
    public Level(int id,String clue){
        this.id=id;
        this.clue=clue;
    }
    public Level(int id,ArrayList<Enemy> enemies,String clue){
        this.id=id;
        this.enemies=enemies;
        this.clue=clue;
    }
    public int getId(){
        return this.id;
    }
    public ArrayList<Enemy> getEnemies(){
        return this.enemies;
    }
    public String getClue(){
        return this.clue;
    }
    public int[][] getCoins(){
        return this.coins;
    }


}
