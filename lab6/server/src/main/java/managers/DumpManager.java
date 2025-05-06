package managers;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.ErrorWritingException;
import com.thoughtworks.xstream.converters.time.ZonedDateTimeConverter;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;
import models.*;
import utility.Validator;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Vector;

/**
 * Класс, отвечающий за сериализацию и десериализацию в XML и обратно в коллекцию
 *
 * @author raichess
 * @since 1.0
 */
public class DumpManager {
    private static DumpManager instance;
    private static String filepath;
    private final XStream xstream;

    /**
     * Базовый конструктор с настройкой тэгов для XML файла и настройками безопасности
     *
     * @param filepath путь к файлу
     */
    public DumpManager(String filepath) {
        this.filepath = filepath;
        xstream = new XStream(new DomDriver());
        xstream.registerConverter(new ZonedDateTimeConverter());

        xstream.alias("LabWork", LabWork.class);
        xstream.alias("Coordinates", Coordinates.class);
        xstream.alias("Person", Person.class);
        xstream.alias("Difficulty", Difficulty.class);
        xstream.alias("Country", Country.class);
        xstream.alias("labWorks", Vector.class);

        xstream.setMode(XStream.NO_REFERENCES);
        xstream.addPermission(NoTypePermission.NONE);
        xstream.addPermission(NullPermission.NULL);
        xstream.addPermission(PrimitiveTypePermission.PRIMITIVES);
        xstream.allowTypeHierarchy(Vector.class);
        xstream.allowTypeHierarchy(String.class);
        xstream.ignoreUnknownElements();
    }

    public static DumpManager getInstance(String filepath) {
        if (instance == null) {
            instance = new DumpManager(filepath);
        }
        return instance;
    }

    /**
     * Метод для чтения данных из файла, проверки их валидности и записи в коллекцию
     *
     * @return the vector
     */
    public Vector<LabWork> readLabWorks() {
        if (!filepath.isEmpty()) {
            try (FileInputStream input = new FileInputStream(filepath);
                 InputStreamReader reader = new InputStreamReader(input)) {


                xstream.setMode(XStream.NO_REFERENCES);
                xstream.addPermission(NoTypePermission.NONE);
                xstream.addPermission(NullPermission.NULL);
                xstream.addPermission(PrimitiveTypePermission.PRIMITIVES);
                xstream.allowTypeHierarchy(Vector.class);
                xstream.allowTypeHierarchy(String.class);
                xstream.ignoreUnknownElements();
                xstream.registerConverter(new ZonedDateTimeConverter());
                xstream.allowTypes(new Class[]{LabWork.class, Vector.class});
                StringBuilder xml = new StringBuilder();
                char[] buffer = new char[1024];
                int bytesRead;
                while ((bytesRead = reader.read(buffer)) != -1) {
                    xml.append(buffer, 0, bytesRead);
                }
                Vector<LabWork> labWorkStart = (Vector<LabWork>) xstream.fromXML(xml.toString());
                Validator validator = new Validator(labWorkStart);
                Vector<LabWork> labWorkVector = new Vector<>();
                labWorkVector.addAll(validator.validateFile());
                return labWorkVector;
            } catch (StreamException e) {
                System.out.println("Error reading file:");
                System.exit(1);
            } catch (ConversionException e) {
                if (e.getCause() instanceof NumberFormatException) {
                    System.out.println("Error: String instead of number.");
                    System.out.println("Collection from file not loaded, check the source file");
                    System.exit(1);
                }
                if (e.getCause() instanceof IllegalArgumentException) {
                    System.out.println("Error: incorrect value in Difficulty or Country.");
                    System.exit(1);
                }
                if (e.getCause() instanceof ErrorWritingException) {
                    System.out.println("Error: Incorrect date in element");
                    System.exit(1);
                }
            } catch (FileNotFoundException exception) {
                System.out.println("File was not found");
                System.exit(1);
            } catch (IllegalStateException e) {
                System.out.println("Unexpected error");
                System.exit(1);
            } catch (NullPointerException e) {
                System.out.println("Can't find a collection in file");
                System.exit(1);
            } catch (NoSuchElementException e) {
                System.out.println("File is empty");
                System.exit(1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else System.out.println("Filename is wrong");
        return new Vector<>();
    }

    /**
     * Метод для записи коллекции в XML файл
     *
     * @param labWork элемент коллекции
     */
    public void writeLabWorks(Vector<LabWork> labWork) {
        if (!filepath.isEmpty()) {
            try (FileOutputStream output = new FileOutputStream(filepath);
                 BufferedOutputStream bufferedOutput = new BufferedOutputStream(output)) {
                String xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n";
                bufferedOutput.write(xmlHeader.getBytes(StandardCharsets.UTF_8));

                String xml = xstream.toXML(new Vector<>(labWork));
                bufferedOutput.write(xml.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                System.out.println("Error writing file:");
            }
        } else System.out.println("Filename is wrong");
    }
}
