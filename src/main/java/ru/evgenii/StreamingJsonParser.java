package ru.evgenii;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Created by e.kostyukovskiy on 31.03.2017.
 */
public interface StreamingJsonParser {
    //JSONElement parse(StringReader stringReader);

    JSONElement parse(Reader reader);
}
