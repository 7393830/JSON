package ru.evgenii;

import java.io.Reader;

public interface StreamingJsonParser {
    JSONElement parse(Reader reader);
}
