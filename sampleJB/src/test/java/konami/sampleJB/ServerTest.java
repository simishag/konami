package konami.sampleJB;

import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class ServerTest {
    private static final Logger logger = Logger.getLogger(ServerTest.class);

    private static final String TEST_GOOD_XML = "src/test/resources/test-good.xml";
    private static final String TEST_BAD1_XML = "src/test/resources/test-bad1.xml";
    private static final String TEST_BAD2_XML = "src/test/resources/test-bad2.xml";
    private static final String TEST_BAD3_XML = "src/test/resources/test-bad3.xml";
    private static final String TEST_INVALID_XML = "src/test/resources/test-invalid.xml";

    private static final String TEST_IP_ADDRESS = "127.0.0.1";
    private static final int TEST_TCP_PORT = 9090;

    private static Server server;

    @BeforeClass
    public static void setup() throws Exception {
        server = new Server(TEST_IP_ADDRESS, TEST_TCP_PORT);
        assertNotNull(server);
    }

    protected Document createDocument(String filename) throws Exception {
        FileInputStream fs = new FileInputStream(filename);
        assertNotNull(fs);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(fs);

        return doc;
    }

    @Test
    public void testGood() throws Exception {
        Document doc = createDocument(TEST_GOOD_XML);
        assertNotNull(doc);

        assertNotNull(server);
        Request request = server.processXML(doc);
        for (RequestData d : request.getData()) {
            logger.info("d: " + d.getDescription() + "/" + d.getValue());
        }

    }

    @Test(expected = ServerException.class)
    public void testBad1() throws Exception {
        Document doc = createDocument(TEST_BAD1_XML);
        assertNotNull(doc);

        Request request = server.processXML(doc);
    }

    @Test(expected = ServerException.class)
    public void testBad2() throws Exception {
        Document doc = createDocument(TEST_BAD2_XML);
        assertNotNull(doc);

        Request request = server.processXML(doc);
    }

    @Test(expected = ServerException.class)
    public void testBad3() throws Exception {
        Document doc = createDocument(TEST_BAD3_XML);
        assertNotNull(doc);

        Request request = server.processXML(doc);
    }

    @Test(expected = SAXException.class)
    public void testInvalid() throws Exception {
        Document doc = createDocument(TEST_INVALID_XML);
        assertNotNull(doc);

        Request request = server.processXML(doc);
    }
}