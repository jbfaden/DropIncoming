
package dropincoming;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * DropPanel is a JPanel component which allows file references to be 
 * dropped on it.
 * @author jbf
 */
public final class DropPanel extends javax.swing.JPanel {

    Color color0;
    File currentDirectory=null;
    File currentFile=null;
    String pathDelim;
    boolean dropActive=false;
    
    /**
     * Creates new form DropPanel
     */
    public DropPanel() {
        initComponents();
        addDropTarget();
        color0= this.getBackground();
        pathDelim= FileSystems.getDefault().getSeparator();
    }

    @Override
    protected void paintComponent(Graphics g1) {
        super.paintComponent(g1);
        if ( dropActive ) {
            Graphics2D g= (Graphics2D)g1;
            Stroke stroke0= g.getStroke();
            Color color0= g.getColor();
            g.setColor( new Color(128,128,128) );
            g.setStroke( new BasicStroke(10f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{60, 20}, 0) );
            g.drawRoundRect( 30, 30, getWidth()-60, getHeight()-60, 30, 30 );
            g.setStroke(stroke0);
            g.setColor(color0);
        }
    }

    
    void addDropTarget() {
        DropTargetListener listener= new DropTargetListener() {
            @Override
            public void dragEnter(DropTargetDragEvent dtde) {
                DropPanel.this.dropActive= true;
                repaint();
            }

            @Override
            public void dragOver(DropTargetDragEvent dtde) {
                
            }

            @Override
            public void dropActionChanged(DropTargetDragEvent dtde) {
                
            }

            @Override
            public void dragExit(DropTargetEvent dte) {
                DropPanel.this.dropActive= false;
                repaint();
            }

            @Override
            public void drop(DropTargetDropEvent dtde) {
                DropPanel.this.dropActive= false;
                DataFlavor[] ff= dtde.getCurrentDataFlavors();
                for ( DataFlavor f : ff ) {
                    if ( f.getMimeType().startsWith("application/x-java-file-list" ) ) {
                        dtde.acceptDrop( DnDConstants.ACTION_COPY );
                        try {
                            Object o = dtde.getTransferable().getTransferData(f);
                            if ( o instanceof java.util.List ) {
                                List<File> l= (List<File>)o;
                                if ( l.size()>0 ) {
                                    final File file= l.get(0);
                                    if ( currentDirectory!=null ) {
                                        Runnable run= new Runnable() {
                                            public void run() {
                                                try {
                                                    dropWithCurrentDirectory(file);
                                                } catch (IOException ex) {
                                                    Logger.getLogger(DropPanel.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                            }
                                        };
                                        SwingUtilities.invokeLater(run);
                                        
                                    } else {
                                        statusLabel.setText( "file in temporary area");
                                        if ( file.isDirectory() ) {
                                            currentDirectory= file;
                                            currentFile=null;
                                        } else {
                                            currentFile= file;
                                        }
                                    }
                                    if ( currentFile==null ) {
                                        currentFileLabel.setText(MSG_NO_CURRENT_FILE);
                                    } else {
                                        currentFileLabel.setText( currentFile.toString() );
                                    }
                                    if ( currentDirectory==null ) {
                                        currentDirectoryLabel.setText(MSG_NO_CURRENT_DIRECTORY);
                                    } else {
                                        currentDirectoryLabel.setText( currentDirectory.toString() );
                                    }
                                    
                                }
                            }
                        } catch (UnsupportedFlavorException | IOException ex) {
                            Logger.getLogger(DropPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else if ( f.getMimeType().startsWith("application/x-moz-custom-clipdata" ) ) {
                        dtde.acceptDrop( DnDConstants.ACTION_COPY );
                        try {
                            Object o = dtde.getTransferable().getTransferData(f);
                            ByteArrayInputStream in= (ByteArrayInputStream)o;
                            File ftmp= new File( "/tmp/dropPanel.dat" );
                            Files.copy( in, ftmp.toPath() );
                            currentFile= ftmp;
                            currentFileLabel.setText( currentFile.toString() );
                        } catch (UnsupportedFlavorException | IOException ex) {
                            Logger.getLogger(DropPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        System.err.println("ignore "+f.getMimeType());
                    } else {
                        System.err.println("ignore "+f.getMimeType());
                    }
//                    Object ins;
//                    try {
//                        ins = dtde.getTransferable().getTransferData(f);
//                        System.err.println(ins);
//                    } catch (UnsupportedFlavorException | IOException ex) {
//                        Logger.getLogger(DropPanel.class.getName()).log(Level.SEVERE, null, ex);
//                    }
                }
                DropPanel.this.repaint();
                
            }


        };
        new DropTarget(this,DnDConstants.ACTION_COPY,listener,true);
    }
    public static final String MSG_NO_CURRENT_FILE = "(no current file)";
    public static final String MSG_NO_CURRENT_DIRECTORY = "(no current directory)";
    
        public void dropWithCurrentDirectory(File file) throws IOException {
            int opt= JOptionPane.showConfirmDialog( DropPanel.this,
                    "drop into "+currentDirectory + "?",
                    "Drop here",
                    JOptionPane.YES_NO_CANCEL_OPTION );
            if ( JOptionPane.OK_OPTION==opt ) {
                currentFile= new File( currentDirectory, file.getName() );
                Files.copy( file.toPath(), currentFile.toPath() );
                file= currentFile;
                statusLabel.setText( "file copied to current");
            } else if ( JOptionPane.NO_OPTION==opt ) {
                currentDirectory=null;
                currentDirectoryLabel.setText("");
                statusLabel.setText( "file in temporary area");
                currentFile= file;
            } else {
                statusLabel.setText( "cancelled");
            }
        }
            
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of
     * this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        currentFileLabel = new javax.swing.JLabel();
        saveToButton = new javax.swing.JButton();
        copyDirectoryButton = new javax.swing.JButton();
        currentDirectoryLabel = new javax.swing.JLabel();
        statusLabel = new javax.swing.JLabel();

        jButton1.setText("Copy");
        jButton1.setToolTipText("Copy Filename to Clipboard");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        currentFileLabel.setText("(no current file)");

        saveToButton.setText("Save to...");
        saveToButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveToButtonActionPerformed(evt);
            }
        });

        copyDirectoryButton.setText("Copy Directory");
        copyDirectoryButton.setToolTipText("Copy directory name to clipboard");
        copyDirectoryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyDirectoryButtonActionPerformed(evt);
            }
        });

        currentDirectoryLabel.setText("(no current directory)");

        statusLabel.setText("ready for drops");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(currentDirectoryLabel)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(currentFileLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 639, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(statusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(saveToButton))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton1)
                                    .addComponent(copyDirectoryButton))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(currentFileLabel)
                .addGap(4, 4, 4)
                .addComponent(copyDirectoryButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(currentDirectoryLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 336, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(saveToButton)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(statusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void saveToButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveToButtonActionPerformed
        JFileChooser chooser= new JFileChooser();
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        final ShortCutsPanel p= new ShortCutsPanel();
        chooser.setAccessory( p );
        p.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File f= chooser.getCurrentDirectory();
                try {
                    chooser.setCurrentDirectory( p.doAction( e.getActionCommand(), f ) );
                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }
            }
        });
        
        if ( currentFile!=null ) {
            chooser.setCurrentDirectory( currentFile.getParentFile() );
        }
        if ( currentDirectory!=null ) {
            chooser.setCurrentDirectory( currentDirectory );
        }
        int opt= chooser.showSaveDialog(this);
        if ( JFileChooser.APPROVE_OPTION==opt ) {
            currentDirectory= chooser.getSelectedFile();
            if ( currentFile!=null ) {
                File currentFileT= new File( currentDirectory, currentFile.getName() );
                try {   
                    Files.copy( currentFile.toPath(), currentFileT.toPath() );
                    statusLabel.setText( "file copied to current");
                    currentFileLabel.setText( currentFile.toString() );
                    currentFile= currentFileT;
                    currentFileLabel.setText(currentFile.toString());
                } catch (IOException ex) {
                    statusLabel.setText(ex.getMessage());
                }
            } else {
                statusLabel.setText("files will be saved here");
            }
            currentDirectoryLabel.setText(currentDirectory.toString());
        }
    }//GEN-LAST:event_saveToButtonActionPerformed

    /**
     * copy the string to the system clipboard.
     * @param text 
     */
    private void copyToClipBoard( String text ) {
        java.awt.Toolkit.getDefaultToolkit().getSystemClipboard()
            .setContents(new java.awt.datatransfer.StringSelection(text), null);
    }
    
    /**
     * show the message for the given number of milliseconds
     * @param message the message
     * @param timeout the timeout in milliseconds
     */
    public void setStatus( String message, int timeout ) {
        final String message0= statusLabel.getText();
        statusLabel.setText(message);
        Timer t= new Timer(timeout, (ActionEvent e) -> {
            statusLabel.setText(message0);
        });
        t.setRepeats(false);
        t.start();
    }
    
    private void copyDirectoryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyDirectoryButtonActionPerformed
        copyToClipBoard( currentDirectory.toString()+pathDelim );
    }//GEN-LAST:event_copyDirectoryButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        copyToClipBoard( currentFileLabel.getText() );
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton copyDirectoryButton;
    private javax.swing.JLabel currentDirectoryLabel;
    private javax.swing.JLabel currentFileLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton saveToButton;
    private javax.swing.JLabel statusLabel;
    // End of variables declaration//GEN-END:variables
}
