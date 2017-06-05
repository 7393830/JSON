package ru.evgenii.parser;

import java.io.Reader;

public interface StreamingJsonParser {
    JSONElement parse(Reader reader);
}
