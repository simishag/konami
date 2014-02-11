package konami.sampleJB;

import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class ServerTest {
    private static final Logger logger = Logger.getLogger(ServerTest.class);

    private static final String TEST_GOOD_XML = "src/test/resources/test-good.xml";

    private static final String TEST_IP_ADDRESS = "127.0.0.1";
    private static final int TEST_TCP_PORT = 9090;

    @Test
    public void testGood() throws IOException, ParserConfigurationException, SAXException, ServerException {
        FileInputStream fs = new FileInputStream(TEST_GOOD_XML);
        assertNotNull(fs);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(fs);

        Server server = new Server(TEST_IP_ADDRESS, TEST_TCP_PORT);
        ArrayList<RequestData> data = server.parseXML(doc);
        for (RequestData d : data) {
            logger.info("d: " + d.getDescription() + "/" + d.getValue());
        }

    }
}