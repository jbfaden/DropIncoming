
package dropincoming;

import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author jbf
 */
public final class DropPanel extends javax.swing.JPanel {

    Color color0;
    File currentDirectory=null;
    File currentFile=null;
    
    /**
     * Creates new form DropPanel
     */
    public DropPanel() {
        initComponents();
        addDropTarget();
        color0= this.getBackground();
    }

    void addDropTarget() {
        DropTargetListener listener= new DropTargetListener() {
            @Override
            public void dragEnter(DropTargetDragEvent dtde) {
                DropPanel.this.setBackground( Color.blue.brighter().brighter() );
            }

            @Override
            public void dragOver(DropTargetDragEvent dtde) {
                
            }

            @Override
            public void dropActionChanged(DropTargetDragEvent dtde) {
                
            }

            @Override
            public void dragExit(DropTargetEvent dte) {
                DropPanel.this.setBackground( DropPanel.this.color0 );
            }

            @Override
            public void drop(DropTargetDropEvent dtde) {
                DataFlavor[] ff= dtde.getCurrentDataFlavors();
                for ( DataFlavor f : ff ) {
                    if ( f.getMimeType().startsWith("application/x-java-file-list" ) ) {
                        dtde.acceptDrop( DnDConstants.ACTION_COPY );
                        try {
                            Object o = dtde.getTransferable().getTransferData(f);
                            if ( o instanceof java.util.List ) {
                                List<File> l= (List<File>)o;
                                if ( l.size()>0 ) {
                                    File file= l.get(0);
                                    if ( currentDirectory!=null ) {
                                        int opt= JOptionPane.showInternalConfirmDialog( DropPanel.this, 
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
                                        
                                    } else {
                                        statusLabel.setText( "file in temporary area");
                                        currentFile= file;
                                    }
                                    currentFileLabel.setText( currentFile.toString() );
                                }
                            }
                        } catch (UnsupportedFlavorException ex) {
                            Logger.getLogger(DropPanel.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
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
                DropPanel.this.setBackground( DropPanel.this.color0 );
            }
        };
        new DropTarget(this,DnDConstants.ACTION_COPY,listener,true);
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
    
    private void copyDirectoryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyDirectoryButtonActionPerformed
        copyToClipBoard( currentDirectory.toString() );
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
