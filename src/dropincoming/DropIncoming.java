
package dropincoming;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author jbf
 */
public class DropIncoming {

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        DropPanelFrame app= new DropPanelFrame();
        if ( args.length==1 ) {
            if ( args[0].equals("--help") ) {
                printHelp();
                System.exit(1);
            } else {
                File config= new File( args[0] );
                app.loadConfig(config);
            }
        } else if ( args.length>1 ) {
            printHelp();
            System.exit(1);
        }
        app.setVisible(true);
    }

    public static void printHelp() {
        System.err.println("DropIncoming [config.dropj]");
        System.err.println("   config.dropj - config file containing preferred locations.  These will be name-value pairs only containing background");
    }
    
}
