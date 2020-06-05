package wally;

import Vistas.MenuPrincipal;
import javax.swing.JFrame;
import javax.swing.text.StyleConstants;

public class Wally {

    public static void main(String[] args) {
        MenuPrincipal mp = new MenuPrincipal();
        mp.setVisible(true);
        mp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
