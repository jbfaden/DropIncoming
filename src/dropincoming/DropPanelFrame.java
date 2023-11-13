
package dropincoming;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import javax.swing.ImageIcon;

/**
 *
 * @author jbf
 */
public class DropPanelFrame extends javax.swing.JFrame {

    /**
     * Creates new form DropPanelFrame
     */
    public DropPanelFrame() {
        initComponents();
        brandingLabel.setText( null );
        brandingLabel.setIcon( new ImageIcon( DropPanelFrame.class.getResource("2023-09-05T08-55-44.png")));
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of
     * this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dropPanel1 = new dropincoming.DropPanel();
        brandingLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        brandingLabel.setText("jLabel1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(brandingLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dropPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 790, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dropPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(brandingLabel)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DropPanelFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DropPanelFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DropPanelFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DropPanelFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DropPanelFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel brandingLabel;
    private dropincoming.DropPanel dropPanel1;
    // End of variables declaration//GEN-END:variables


    void loadConfig(File config) throws IOException {

        LinkedHashMap<String,String> map= new LinkedHashMap<>();
        
        try ( BufferedReader bfr = new BufferedReader(new FileReader(config)) ) {


            String line;
            while ((line = bfr.readLine()) != null) {
                if (!line.startsWith("#") && !line.isEmpty()) {
                    String[] pair = line.trim().split("=",2);
                    map.put(pair[0].trim(), pair[1].trim());
                }
            }
        }
        
        String background= map.getOrDefault( "background", "" );
        if ( background.length()>0 ) {
            dropPanel1.setBackground( Color.decode(background) );
        }
        
        dropPanel1.setStatus( "read config file "+config, 3000 );
        
    }
}
