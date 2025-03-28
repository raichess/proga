package managers;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Zoned date time преобразователь
 */
public class ZonedDateTimeConverter implements Converter {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS z");

    @Override
    public boolean canConvert(Class type) {
        return ZonedDateTime.class.isAssignableFrom(type);
    }

    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        ZonedDateTime dateTime = (ZonedDateTime) source;
        writer.setValue(FORMATTER.format(dateTime));
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        String dateTimeString = reader.getValue();
        return ZonedDateTime.parse(dateTimeString, FORMATTER);
    }
}
