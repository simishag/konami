package konami.sampleJB;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import org.apache.log4j.Logger;

public class ClientApp {
    private static final Logger logger = Logger.getLogger(Server.class);

    public static void main(String[] args) {
        if (args.length != 3) {
            String msg = "Command line arguments are invalid";
            logger.fatal(msg);
            System.out.println(msg);
            System.exit(1);
        }

        String ipAddress = args[0];
        int tcpPort = Integer.valueOf(args[1]);
        String xmlFilename = args[2];
        String xmlData = null;
        try {
            xmlData = new Scanner(new File(xmlFilename)).useDelimiter("\\Z").next();
            logger.debug("xmlFile: " + xmlData);
        } catch (FileNotFoundException e) {
            String msg = "File not found: " + xmlFilename;
            logger.fatal(msg);
            System.out.println(msg);
            System.exit(2);
        }

        try {
            Socket socket = new Socket(ipAddress, tcpPort);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(xmlData);
            out.flush();
            
            logger.debug("here");
            
            String response = in.readLine();
            System.out.println("Response: " + response);
            
        } catch (UnknownHostException e) {
            String msg = "Host unknown: " + ipAddress;
            logger.fatal(msg);
            System.out.println(msg);
            System.exit(3);
        } catch (IOException e) {
            String msg = "Could not connect to server: " + ipAddress + ":" + tcpPort;
            logger.fatal(msg);
            System.out.println(msg);
            System.exit(4);
        }
    }
}