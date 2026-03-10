package game.engine.exceptions;

import java.io.IOException;

public class InvalidCSVFormat extends IOException {
    static final String MSG="Invalid input detected while reading csv file, input = \n";
    String inputLine;
    InvalidCSVFormat(String inputLine){
        super(MSG +  inputLine);
        this.inputLine=inputLine;
    }
    InvalidCSVFormat(String message, String inputLine){
        super(message+inputLine);
        this.inputLine=inputLine;
    }
}
