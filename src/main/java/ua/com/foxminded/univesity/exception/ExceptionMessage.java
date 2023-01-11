package ua.com.foxminded.univesity.exception;

public class ExceptionMessage {
    
    public static final String REPOSITORY_FAILURE = 
            "Getting data from the database was failure.";
    public static final String MAPPING_FAILURE_OPERATION = 
            "Error erised during mapping operation.";
    public static final String CONFIGURATION_MODELMAPPER_EXCEPTION = 
            "Configuration of the ModelMapper is invalid.";
    public static final String ILLEGAL_MODELMAPPER_ARGUMENT = 
            "ModelMapper recived inappropriate argument.";
    
    private ExceptionMessage() {
    }
}
