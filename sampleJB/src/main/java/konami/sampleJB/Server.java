package konami.sampleJB;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
            // process input
            InputStream inputStream = socket.getInputStream();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(inputStream);
            ArrayList<RequestData> data = parseXML(doc);
            logger.info("data rows: " + data.size());

            // input is valid, send response
            Date date = new Date();
            String response = "Message Received at " + date;
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(response.getBytes());
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

        NodeList mainList = doc.getElementsByTagName("Message");
        if (mainList == null) {
            throw new ServerException("XML request is missing required element: Message");
        } else if (mainList.getLength() > 1) {
            throw new ServerException("XML request can have only 1 element of type: Message");
        }

        Element message = (Element) mainList.item(0);
        NodeList commandList = message.getElementsByTagName("Command");
        if (commandList == null || commandList.getLength() == 0) {
            throw new ServerException("XML request is missing required element: Command");
        }
        Element command = (Element) commandList.item(0);
        NodeList dataList = message.getElementsByTagName("Data");
        if (dataList == null || dataList.getLength() == 0) {
            throw new ServerException("XML request is missing required element: Data");
        }
        Element data = (Element) dataList.item(0);

        // This tells us what to do with the data
        String commandValue = command.getNodeValue();

        NodeList rowsList = data.getElementsByTagName("Row");
        if (rowsList == null || rowsList.getLength() == 0) {
            throw new ServerException("No rows to process");
        }

        int rowCount = rowsList.getLength();
        for (int i = 0; i < rowCount; i++) {
            Element row = (Element) rowsList.item(i);

            NodeList descriptionList = row.getElementsByTagName("Description");
            if (descriptionList == null || descriptionList.getLength() != 1) {
                throw new ServerException("Invalid row found in data section: missing Description");
            }
            Element descriptionNode = (Element) descriptionList.item(0);
            String description = descriptionNode.getTextContent();

            NodeList valueList = row.getElementsByTagName("Value");
            if (valueList == null || valueList.getLength() != 1) {
                throw new ServerException("Invalid row found in data section: missing Value");
            }
            Element valueNode = (Element) valueList.item(0);
            String value = valueNode.getTextContent();

            list.add(new RequestData(description, value));
        }

        return list;
    }
}