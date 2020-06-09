package Estilos;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class RenderTablaConsultaPromotoras extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        Color colorFondo = null;
        Color colorFondoPorDefecto = new Color(192, 1, 192);
        Color colorFondoSeleccion = new Color(116, 117, 115);

        if (isSelected) {
            this.setBackground(colorFondoSeleccion);
        } else {
            this.setBackground(Color.WHITE);
        }

        this.setHorizontalAlignment(JLabel.CENTER);
        this.setFont(new Font("Arial", 1, 12));

        this.setText((String) value);

        return this;

    }

}
