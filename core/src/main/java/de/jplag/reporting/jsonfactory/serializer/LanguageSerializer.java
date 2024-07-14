package de.jplag.reporting.jsonfactory.serializer;

import java.io.IOException;
import java.io.Serial;
import java.util.List;

import de.jplag.Language;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class LanguageSerializer extends StdSerializer<List<Language>> {

    @Serial
    private static final long serialVersionUID = 5944655736767387268L; // generated

    /**
     * Constructor used by the fasterxml.jackson
     */
    public LanguageSerializer() {
        this(null);
    }

    public LanguageSerializer(Class<List<Language>> languageClass) {
        super(languageClass);
    }

    @Override
    public void serialize(List<Language> languages, JsonGenerator generator, SerializerProvider provider) throws IOException {
        List<String> languageNames = languages.stream().map(Language::getName).toList();
        generator.writeString(String.join(", ", languageNames));
    }
}
