package konami.sampleJB;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Server {
    private static final Logger logger = Logger.getLogger(Server.class);
    private static final int SOCKET_BACKLOG = 20;

    private ServerSocket listener;

    /*
     * Validate parameters
     */
    public Server(String ipAddress, int tcpPort) throws IOException {
        InetAddress addr = InetAddress.getByName(ipAddress);
        listener = new ServerSocket(tcpPort, SOCKET_BACKLOG, addr);
    }

    /*
     * Main socket loop
     */
    protected void listen() throws IOException {
        try {
            while (true) {
                Socket socket = listener.accept();
                try {
                    handleRequest(socket);
                } catch (ServerException e) {
                    logger.warn("Request handler failed", e);
                    Date date = new Date();
                    System.out.println("Unknown Command Received at " + date);
                }
            }
        } finally {
            listener.close();
        }

        // any cleanup would occur here
    }

    /*
     * Handle a single request
     */
    protected void handleRequest(Socket socket) throws ServerException {
        String errorMsg;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            InputStream inputStream = socket.getInputStream();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(inputStream);
            
            

        } catch (SAXException e) {
            errorMsg = "Invalid XML";
            logger.warn(errorMsg, e);
            throw new ServerException(errorMsg, e);
        } catch (IOException e) {
            errorMsg = "Socket error";
            logger.warn(errorMsg, e);
            throw new ServerException(errorMsg, e);
        } catch (ParserConfigurationException e) {
            errorMsg = "XML parser configuration error";
            logger.warn(errorMsg, e);
            throw new ServerException(errorMsg, e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }

    /*
     * Parse & validate the XML input request
     */
    protected ArrayList<RequestData> parseXML(Document doc) throws ServerException {
        ArrayList<RequestData> list = new ArrayList<RequestData>();

        Element message = doc.getElementById("Message");
        if (message == null) {
            throw new ServerException("XML request is missing required element: Message");
        }
        
        NodeList commandList = message.getElementsByTagName("Command");
        if (commandList == null) {
            throw new ServerException("XML request is missing required element: Command");
        } else if (commandList.getLength() > 1) {
            throw new ServerException("XML request can have only 1 element of type: Command");
        }
        Node commandNode = commandList.item(0);
        // This tells us what to do with the data
        String commandValue = commandNode.getNodeValue();
        
        NodeList dataList = message.getElementsByTagName("Data");
        if (dataList == null) {
            throw new ServerException("XML request is missing required element: Data");
        } else if (dataList.getLength() > 1) {
            throw new ServerException("XML request can have only 1 element of type: Data");
        } 
        Node data = dataList.item(0);
        NodeList rowsList = data.getChildNodes();
        if (rowsList == null || rowsList.getLength() == 0) {
            throw new ServerException("No rows to process");
        }
        
        int rowCount = rowsList.getLength();
        for (int i = 0; i < rowCount; i++) {
            Node row = rowsList.item(i);
            if (!row.getNodeName().equals("Row")) {
                throw new ServerException("Invalid row found in data section: invalid node name");
            }
            
            // We can only do this here because we know the request has only 
            // 2 fields. Otherwise we would have to parse lists as above.
            Node descriptionNode = row.getFirstChild();
            if (descriptionNode == null || !descriptionNode.getNodeName().equals("Description")) {
                throw new ServerException("Invalid row found in data section: missing Description");
            }
            Node valueNode = row.getLastChild();
            if (valueNode == null || !valueNode.getNodeName().equals("Value")) {
                throw new ServerException("Invalid row found in data section: missing Value");
            }
            
            // XML should be valid here so we add the data row
            String description = descriptionNode.getNodeValue();
            String value = valueNode.getNodeValue();
            
            list.add(new RequestData(description, value));
        }
        
        return list;
    }
}