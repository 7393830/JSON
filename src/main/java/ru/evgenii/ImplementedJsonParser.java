package ru.evgenii;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Created by e.kostyukovskiy on 31.03.2017.
 */
public class ImplementedJsonParser implements StreamingJsonParser {


    @Override
    public JSONElement parse(Reader stringReader)  {
        try {
            JSONElement retJson = new JSONParser(stringReader).jsonFactory();
            int c;
            while ((c = stringReader.read()) != -1) {
                if (!Character.isWhitespace(c)) {
                    throw new IllegalArgumentException("error! ImplementedJsonParser.parse");
                }
            }
            return retJson;
        } catch (IOException ex) {
            throw new IllegalArgumentException("error! ImplementedJsonParser.parse");
        } finally {
            try {
                stringReader.close();
            } catch (IOException e) {
                throw new IllegalStateException("error! ImplementedJsonParser.parse");
            }
        }
    }

   /* @Override
    public JSONElement parse(Reader reader) {
        return parse((StringReader) reader);
    }*/
}
