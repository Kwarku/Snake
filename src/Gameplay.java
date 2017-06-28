import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.*;

/**
 * Created by Pawel on 22.06.2017.
 */
// rozszezenie o JPanel daje mozliwosc oblusgi okienka,
// implementacja odpwoaida za odbieranie z klwaiwtury ruchu postaci xD
public class Gameplay extends JPanel implements KeyListener,ActionListener {
    // wymiar węza
    private int[] snakexlength = new int[750];
    private int[] snakeylength = new int[750];

    //ruch węża
    private int move=0;
    private boolean play=true;


    // orientacja węża
    private boolean left =false;
    private boolean right =false;
    private boolean up =false;
    private boolean down =false;

    // zeby dodac grafike trzeba ja wpierdolic do projektu a nie do src
    private ImageIcon titleImage;
    // stworzenie zmeinnych dla wygladu głowy węża
    private ImageIcon rightmouth;
    private ImageIcon leftmouth;
    private ImageIcon upmouth;
    private ImageIcon downmouth;

    private ImageIcon snakeImage;
    private ImageIcon enemyImage;

    // mozeliwe pozycje wroga, beda wybierane losowo :D
    private int[] enemyxpos={25, 50, 75, 100, 125, 150, 175, 200, 225, 250, 275,
            300, 325, 350, 375, 400, 425, 450, 475, 500, 525, 550, 575, 600, 625,
            650, 675, 700, 725, 750, 775, 800, 825, 850};
    private int[] enemyypos={75, 100, 125, 150, 175, 200, 225, 250, 275,
            300, 325, 350, 375, 400, 425, 450, 475, 500, 525, 550, 575, 600, 625};

    private Random random = new Random();
    private int xpos=random.nextInt(enemyxpos.length); // tyle jest mozliwych pozycji w tej tablicy
    private int ypos=random.nextInt(enemyypos.length);

    private int lenghtofsnake=3;

    private int score=0;
    private int bestscore;

    private Timer timer;
    private int deley=100;  // szybkosc węża
    private int lvl=1;



    // główny konstruktor gry odpowiedzialny za wszystkie czynnosci
    public Gameplay()
    {
        addKeyListener(this); // teraz slucha sie klawiszy
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer =new Timer(deley,this);
        timer.start();

    }


   /* // staranie wydruku wyniku do pliku
    public void scorer(){
        if (!play && score>0){
            BufferedWriter writer = null;
            try
            {
                String filename= "score.txt";
                FileWriter fw = new FileWriter(filename,true); //the true will append the new data
                fw.write(score+"\n");//appends the string to the file
                fw.close();
            }
            catch(IOException ioe)
            {
                System.err.println("IOException: " + ioe.getMessage());
            }

        }*/



    // rysowanie obiektów, dobra praktyka jest tak to nazywac czyli
    // paint(obsluguje Graphics i nazwa tego g)
    public  void paint(Graphics g)
    {
        if (play){

        // jezeli ruch jest zerowy tzn kiedy gra jeszcze sie nie zaczela to gdizes trzeba umiescic węża
        if (move==0)
        {
            snakexlength[2]=50;
            snakexlength[1]=75;
            snakexlength[0]=100;

            snakeylength[2]=100;
            snakeylength[1]=100;
            snakeylength[0]=100;
        }


        // granica tytulu (górna ramke)
        g.setColor(Color.WHITE);
        g.drawRect(24,10,851,55);

        //  wyglad tytulu
        titleImage = new ImageIcon("image/snaketitle.jpg"); // wczytanie obiektu
        titleImage.paintIcon(this,g,25,11);         // rysowanie obiektu


        // granica gry
        g.setColor(Color.WHITE);
        g.drawRect(24,74,851,577);

        //tło gry
        g.setColor(Color.BLACK);
        g.fillRect(25,75,850,575);  //wypelnia prostokat

        //wynik
        g.setColor(Color.WHITE);
        g.setFont(new Font("arial",Font.PLAIN,15));
        g.drawString("Wynik: "+ score,660,45);

        //dlugosc weza
        g.setColor(Color.WHITE);
        g.setFont(new Font("arial",Font.PLAIN,15));
        g.drawString("Dlugosc: "+ lenghtofsnake,780,45);

        //rysowanie węża
        rightmouth = new ImageIcon("image/rightmouth.png");
        rightmouth.paintIcon(this,g,snakexlength[0],snakeylength[0]);


        // wykrycie kierunku poruszania sie węża
        for (int a=0;a<lenghtofsnake; a++)
        {
            // a== 0 bo pierwszy element to głowa przeca
            if (a==0 && right){
                rightmouth = new ImageIcon("image/rightmouth.png");
                rightmouth.paintIcon(this,g,snakexlength[a],snakeylength[a]);
            }
            if (a==0 && left){
                leftmouth = new ImageIcon("image/leftmouth.png");
                leftmouth.paintIcon(this,g,snakexlength[a],snakeylength[a]);
            }
            if (a==0 && down){
                downmouth = new ImageIcon("image/downmouth.png");
                downmouth.paintIcon(this,g,snakexlength[a],snakeylength[a]);
            }
            if (a==0 && up){
                upmouth = new ImageIcon("image/upmouth.png");
                upmouth.paintIcon(this,g,snakexlength[a],snakeylength[a]);
            }
            // ciało węża
            if (a!=0){
                snakeImage = new ImageIcon("image/snakeImage.png");
                snakeImage.paintIcon(this,g,snakexlength[a],snakeylength[a]);
            }

        }

        //wrog
        enemyImage = new ImageIcon("image/enemy.png");
        enemyImage.paintIcon(this,g,enemyxpos[xpos],enemyypos[ypos]);
        //sprawdzenie czy nie koliduje z glowa weza
        if (enemyxpos[xpos]==snakexlength[0] && enemyypos[ypos]==snakeylength[0]){
         lenghtofsnake++;
         xpos=random.nextInt(enemyxpos.length);
         ypos=random.nextInt(enemyypos.length);
         score+=10;
        }


        // kolizja
        for (int b=1;b<lenghtofsnake;b++){
            if (snakexlength[b]==snakexlength[0] && snakeylength[b]==snakeylength[0]){
                right=false;
                left=false;
                down=false;
                up=false;
                play=false;




                //napis przy kolizji
                g.setColor(Color.RED);
                g.setFont(new Font("arial",Font.BOLD,50));
                g.drawString("Game Over",330,350);
                g.setColor(Color.white);
                g.setFont(new Font("arial",Font.BOLD,20));
                g.drawString("Kliknij Spacje zeby zrestartowac",315,380);


                // zapisywanie wyniku w pliku na koncu listy
                try
                {
                    String filename= "score.txt";
                    FileWriter fw = new FileWriter(filename,true); //the true will append the new data
                    fw.write(score+", \n");//appends the string to the file
                    fw.close();
                }
                catch(IOException ioe)
                {
                    System.err.println("IOException: " + ioe.getMessage());
                }

            }
        }

            /*
            //wyswietlanie najwiekszego wyniku
            g.setColor(Color.white);
            g.setFont(new Font("arial",Font.PLAIN,15));
            g.drawString("Najlepszy wynik: "+ bestscore,100,45);
            */

            // menu opcji
            if (move==0){
                g.setColor(Color.WHITE);
                g.setFont(new Font("arial",Font.BOLD,20));
                g.drawString("Wybierz poziom trudnosci: ",330,330);

                g.setFont(new Font("arial",Font.PLAIN,18));
                g.drawString("Latwy, kliknij 1 ",330,350);
                g.drawString("Sredni, klinkij 2 ",329,370);
                g.drawString("Trudny, kliknij 3  ",328,390);
                g.drawString("Bardzo trudny, kliknij 4  ",329,410);

            }

    }
    g.dispose();
    }





    //akcja w grze
    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        //play=true;
        /* zeby waz poruszal sie prawidlowo to: kazdy kolejny wyraz tablicy
        * musi przemieszczac sie w miejsce poprzedniego n-1 idzie w miejsce n-tego wyrazu
        * */

        if (right){
            for (int r=lenghtofsnake-1;r>=0;r--){
                snakeylength[r+1]=snakeylength[r]; }
            for (int r = lenghtofsnake; r>=0;r--){
                if (r==0){ snakexlength[r]=snakexlength[r]+25;}
                else{ snakexlength[r]=snakexlength[r-1]; }
                if (snakexlength[r]>850){ //jezeli wychodzi poza ekran
                    snakexlength[r]=25; }    // to wraca w lewej strony
            }
            repaint();
        }
        if (left){
            for (int r=lenghtofsnake-1;r>=0;r--){
                snakeylength[r+1]=snakeylength[r]; }
            for (int r = lenghtofsnake; r>=0;r--){
                if (r==0){ snakexlength[r]=snakexlength[r]-25;}
                else{ snakexlength[r]=snakexlength[r-1]; }
                if (snakexlength[r]<25){
                    snakexlength[r]=850; }
            }
            repaint();

        }
         if (up){
             for (int r=lenghtofsnake-1;r>=0;r--){
                 snakexlength[r+1]=snakexlength[r]; }
             for (int r = lenghtofsnake; r>=0;r--){
                 if (r==0){ snakeylength[r]=snakeylength[r]-25;}
                 else{ snakeylength[r]=snakeylength[r-1]; }
                 if (snakeylength[r]<75){
                     snakeylength[r]=625; }
             }
             repaint();

        }
        if (down){
            for (int r=lenghtofsnake-1;r>=0;r--){
                snakexlength[r+1]=snakexlength[r]; }
            for (int r = lenghtofsnake; r>=0;r--){
                if (r==0){ snakeylength[r]=snakeylength[r]+25;}
                else{ snakeylength[r]=snakeylength[r-1]; }
                if (snakeylength[r]>625){
                    snakeylength[r]=75; }
            }
            repaint();


        }

    }


    @Override
    public void keyTyped(KeyEvent e) {}

    // wcisniecie klawisza
    @Override
    public void keyPressed(KeyEvent e) {
        /*Jezeli klikamy w prawo to automatycznie musi zostac zablokowany kazdy inny kierunek
        zeby nasz kod w Graphics mogl wczytac odpowieni wyglad głowy
        * */
        if (e.getKeyCode()==KeyEvent.VK_RIGHT || e.getKeyCode()==KeyEvent.VK_D)
        {
            move++;  // musimy zmienic z 0 bo inaczej nie ruszyl by sie przez wczesniejszy kod
            right=true;
            if (!left){      // jezeli waz idzie w prawo to nie moze isc od razu w lewo
                right=true;   // tak samo analogicznie
            } else{
                   right=false; // jezeli idize w prawo to nie moze isc w lewo
                   left=true;
                }
            down=false;
            up=false;
        }

        if (e.getKeyCode()==KeyEvent.VK_LEFT || e.getKeyCode()==KeyEvent.VK_A)
        {
            move++;
            left=true;
            if (!right){
                left=true;
            } else{
                right=true;
                left=false;
            }
            down=false;
            up=false;
        }

        if (e.getKeyCode()==KeyEvent.VK_UP || e.getKeyCode()==KeyEvent.VK_W)
        {
            move++;
            up=true;
            if (!down){
                up=true;
            } else{
                up=false;
                down=true;
            }
            left=false;
            right=false;
        }

        if (e.getKeyCode()==KeyEvent.VK_DOWN || e.getKeyCode()==KeyEvent.VK_S)
        {
            move++;
            down=true;
            if (!up){
                down=true;
            } else{
                down=false;
                up=true;
            }
            right=false;
            left=false;
        }

        // wylaczenie gry
        if (e.getKeyCode()==KeyEvent.VK_ESCAPE){System.exit(0);}

        // uruchomienie po smierci
        if (e.getKeyCode()==KeyEvent.VK_SPACE && play==false){
            right=true;
            left=false;
            up=false;
            down=false;
            move=0;
            score=0;
            lenghtofsnake=3;
            repaint();
            play=true;
        }

        //pauza
        if (e.getKeyCode()==KeyEvent.VK_SPACE){
            right=false;
            left=false;
            up=false;
            down=false;
        }

        //nie dziala -.-   nie wiem jak zmienic deley :(
        //poziom trudnosci
        if (e.getKeyCode()==KeyEvent.VK_1 && lvl!=1 && move==0){
            deley=(int)(100);
            timer.setDelay((int)(deley));
            lvl=1;

        }
        if (e.getKeyCode()==KeyEvent.VK_2 && lvl!=2 &&  move==0){
            deley=(int)(70);
            timer.setDelay((int)(deley));

            lvl=2;
        }
        if (e.getKeyCode()==KeyEvent.VK_3 && lvl!=3 &&  move==0){
            deley=(int)(45);
            timer.setDelay((int)(deley));
            lvl=3;
        }
        if (e.getKeyCode()==KeyEvent.VK_4 && lvl!=4 &&  move==0){
            deley=(int)(30);
            timer.setDelay((int)(deley));
            lvl=4;
        }


    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
