package ua.com.foxminded.univesity.exception;

public class ExceptionMessage {
    
    public static final String DATABASE_FAILURE = 
            "Getting data from the database was failure.";
    public static final String FAILURE_MAPPING_OPERATION = 
            "Error erised during mapping operation.";
    public static final String INCORRECT_MODELMAPPER_CONFIGURATION = 
            "Configuration of the ModelMapper is invalid.";
    public static final String ILLEGAL_MODELMAPPER_ARGUMENT = 
            "ModelMapper recived inappropriate argument.";
    
    private ExceptionMessage() {
    }
}
