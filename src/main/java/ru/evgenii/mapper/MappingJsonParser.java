package ru.evgenii.mapper;

import java.io.Reader;

public abstract class MappingJsonParser{
    public abstract <T> T parse(Reader r, Mapper<T> mapper);
}
