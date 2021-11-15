package utils.data;

import com.google.gson.Gson;
import testdata.purchasing.ComputerDataObject;
import testdata.users.UserDataObject;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CommonData {

    public static UserDataObject buildUserDataObject(String dataFileLocation){
        return buildDataObjectFrom(dataFileLocation, UserDataObject.class);
    }
    private static <T> T buildDataObjectFrom(String dataFileLocation, Class<T> dataType) {
        String currentProjectLocation = System.getProperty("user.dir");
        try(
                Reader reader = Files.newBufferedReader(Paths.get(currentProjectLocation + dataFileLocation));
        ) {
            // create Gson instance
            Gson gson = new Gson();

            // Convert to array of Computer instances
            return gson.fromJson(reader, dataType);

        } catch (Exception e) {
            e.printStackTrace();
        }
    return  null;
    }


}
