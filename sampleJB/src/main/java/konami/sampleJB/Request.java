package konami.sampleJB;

import java.util.ArrayList;

/**
 * Domain object for XML request.
 * 
 * @author judd
 * 
 */
public class Request {
    private String command;
    private ArrayList<RequestData> data;
    public String getCommand() {
        return command;
    }
    public void setCommand(String command) {
        this.command = command;
    }
    public ArrayList<RequestData> getData() {
        return data;
    }
    public void setData(ArrayList<RequestData> data) {
        this.data = data;
    }
    
}