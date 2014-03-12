Konami Java Sample Program

The project was created using Maven and Eclipse.

To run the server using Maven:
  maven exec:java -Dexec.mainClass=konami.sampleJB.ServerApp -Dexec.args="127.0.0.1 9090"
Arguments are the IP address and TCP port to listen on.

To run the client using Maven:
  maven exec:java -Dexec.mainClass=konami.sampleJB.ClientApp -Dexec.args="127.0.0.1 9090 sample.xml"
Arguments are the IP address and TCP port to connect to, followed by the XML data to be sent.

Time spent: (hours:minutes)
====
0:30 Analysis & design
1:00 Development - XML data parsing
1:00 Development - Server-client communication
1:00 Testing & debugging
0:30 Documentation & packaging
====
4:00 Total

The design is fairly straightforward. The specifications were clear and concise.

Classes include:
ClientApp - main() class for running client
ServerApp - main() class for running server
Server - all XML processing logic is contained here
ServerException - used to wrap exceptions in a single class for this project
Request - domain object representing a complete XML request
RequestData - domain object representing a single Data row in a request

Test classes include:
ServerTest - tests Server with various good & bad XML files

The project relies solely on standard Java APIs. No external libraries were
used, with the exception of JUnit for unit testing and log4j for logging.

Test message

Test message 2

Test message 3

Test message 4

Test 5

Test 6

Test 7

Test 8

Test 9

Test 10