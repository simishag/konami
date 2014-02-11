package konami.sampleJB;

import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * Launcher class (main) for sample app.
 * 
 * @author judd
 *
 */
public class ServerApp {
    private static final Logger logger = Logger.getLogger(Server.class);
    
    public static void main(String[] args) {
        if (args.length != 2) {
            logger.fatal("Command line arguments are invalid");
            System.exit(1);            
        }

        String ipAddress = args[0];
        int tcpPort = Integer.valueOf(args[1]);
        Server server = null;
        try {
            server = new Server(ipAddress, tcpPort);
        } catch (IOException e) {
            logger.fatal("Invalid IP address and/or TCP port", e);
            e.printStackTrace();
            System.exit(2);
        }
        
        if (server == null) {
            logger.fatal("Server failed to initialize");
            System.exit(3);
        }
        
        System.out.println("Listening on " + ipAddress + ":" + tcpPort);
        try {
            server.listen();
        } catch (IOException e) {
            logger.fatal("Server failed during operation", e);
            e.printStackTrace();
            System.exit(4);
        }
    }
}
