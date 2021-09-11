package co.com.pragma.apiphotos.configuration.constant;

import java.io.Serializable;
import java.util.function.Function;

public class MessageConstant implements Serializable {
   private static final long serialVersionUID = -6885382462055438626L;
   public static final Function<String, String> addSuccess = str -> String.format("The entity of the %s was successfully registered.", str);
   public static final Function<String, String> editSuccess = str -> String.format("The entity of the %s was updated successfully.", str);
   public static final Function<String, String> existSuccess = str -> String.format("The entity %s does exist.", str);
   public static final Function<String, String> addError = str -> String.format("The %s entity could not be registered.", str);
   public static final Function<String, String> editError = str -> String.format("Could not update %s entity.", str);
   public static final Function<String, String> existError = str -> String.format("The entity %s does not exist.", str);
   public static final Function<String, String> deleteSuccess = str -> String.format("The entity %s was removed.", str);
   public static final Function<String, String> deleteError = str -> String.format("The entity %s cannot be removed.", str);
   public static final Function<String, String> alreadyExistError = str -> String.format("The entity %s already exist.", str);
   public static final Function<String, String> fieldRequired = str -> String.format("The field %s is required.", str);
}