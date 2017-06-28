import javax.swing.JFrame;
import java.awt.*;

/**
 * Created by Pawel on 22.06.2017.
 */
public class Main {
    public static void main(String[] args) {
        JFrame obj= new JFrame();
        Gameplay gameplay = new Gameplay(); //tworzymy klase Gameplay rozszezamy od JPanel i od razu to dajemy

        obj.setBounds(10,10,905,700); // rozmiar
        obj.setBackground(Color.DARK_GRAY);  // kolor tła
        obj.setResizable(false);  // brak mozliwosci zmiany rozmiaru
        obj.setVisible(true);  // widocznosc
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // co wyłączy nasz program
        obj.add(gameplay); // dodajemy obiekt z klasy Gameplay czyli wszystko xD
        obj.setTitle("Snake");




    }
}
