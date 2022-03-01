import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Utility
{

    private  static ObjectMapper _mapper = new ObjectMapper();

  // Map<String,Object> map = mapper.readValue(json, MessageRequest.class);

    private static final JSONParser PARSER = new JSONParser();

    private Utility()
    {
    }

    public static JSONObject parseJSON(String json) {

        try {

            return (JSONObject) PARSER.parse(json);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String parseObjectToJSON(MessageRequest messageRequest)
            throws JsonProcessingException
    {

        try {
            // convert user object to json string and return it
           return  _mapper.writeValueAsString(messageRequest);
        }
        catch (JsonGenerationException | JsonMappingException e) {
            // catch various errors
            e.printStackTrace();
        }
        return Common.FAULTCONVERTTOJSON;

    }


    public static long getCRC32Checksum(byte[] bytes) {
        Checksum crc32 = new CRC32();
        crc32.update(bytes, 0, bytes.length);
        return crc32.getValue();
    }


    public static String saveHashmapToFile(Map<Integer, String> hashmap )
    {
        if(!hashmap.isEmpty()){
            try
            {
                Properties properties = new Properties();
                for (Map.Entry<Integer,String> entry : hashmap.entrySet()) {
                    properties.put(entry.getKey(), entry.getValue());
                }
                properties.store(new FileOutputStream("data.properties"), null);
            }
            catch (IOException e){
                System.out.printf("Fault to save the file : %s%n",e.getMessage());
                return Common.FAULT;
            }

        }
        return Common.OK;
    }
    public static HashMap<Integer, String> loadHashmapFromFile(){
        // Load the map
        HashMap<Integer, String> map_from_file = new HashMap<>();
        Properties properties = new Properties();
        try
        {
            properties.load(new FileInputStream("data.properties"));
            for (String key: properties.stringPropertyNames()) {
                map_from_file.put(Integer.valueOf(key), properties.get(key).toString());
            }
            return map_from_file;
        }
        catch (IOException e){
            System.out.printf("Fault to load the file : %s%n",e.getMessage());
            return map_from_file;
        }
    }
}
