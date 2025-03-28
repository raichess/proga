package commands;


import exceptions.*;
import managers.CollectionManager;
import managers.CommandManager;
import models.*;
import utility.IdGenerator;
import utility.Validator;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EmptyStackException;
import java.util.Scanner;
import java.util.Stack;
import java.util.Vector;

/**
 * Данная команда выполняет скрипт файл
 *
 * @see Command
 * @author raichess
 * @since 1.0
 */
public class ExecuteScriptCommand implements Command {
    private static Stack<File> stack = new Stack<>();
    @Override
    public void execute(String[] args) throws Exception {
        CollectionManager collectionManager = CollectionManager.getInstance();
        if (args.length > 2) {
            throw new WrongArgumentException(args[2]);
        }
        if (args.length < 2) {
            throw new NoArgumentException("script name");
        }
        File file = new File(args[1]);

        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    throw new RuntimeException("Unable to create new file: " + file.getName());
                }
                System.out.println("New file was created: " + file.getName());
            } catch (Exception e) {
                throw new RuntimeException("Error with creating file " + e.getMessage());
            }
        }

        try {
            if (!file.canRead()) {
                throw new RootException();
            }
            if (stack.isEmpty()) {
                stack.add(file);
            } else if (stack.contains(file)) {
                throw new RecursionException();
            }
            stack.add(file);
        } catch (RecursionException ex) {
            stack.pop();
            System.out.println(ex.getMessage());
            System.out.println("Script Executing finished with error");
            if (!stack.isEmpty()) {
                stack.pop();
            }
            return;
        }

        String path = args[1];
        Scanner scanner = new Scanner(new File(path), StandardCharsets.UTF_8.name());
        String[] labworkD = new String[7];
        try {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if ((line.split(" ")[0].equals("add"))) {
                    try {
                        String[] parts = line.split(" ");
                        if (parts.length > 1) {
                            throw new WrongArgumentException(parts[2]);
                        }
                        for (int n = 0; n < 7 && scanner.hasNextLine(); n++) {
                            labworkD[n] = scanner.nextLine();
                        }
                        LabWork labwork1 = new LabWork();
                        Validator.inputNotEmpty(labworkD[0], "Name");
                        Validator.inputNotEmpty(labworkD[1], "x");
                        Validator.xGood(labworkD[1]);
                        Validator.inputNotEmpty(labworkD[2], "y");
                        Validator.yGood(labworkD[2]);
                        Validator.inputNotEmpty(labworkD[3], "minimal point");
                        Validator.minimalPointGood(labworkD[3]);
                        Validator.inputNotEmpty(labworkD[4], "description");
                        Validator.inputNotEmpty(labworkD[5], "difficulty");
                        Validator.difficultyGood(labworkD[5]);

                        Coordinates coordinates = new Coordinates(Integer.parseInt(labworkD[1]), Double.parseDouble(labworkD[2]));

                        String[] personData = labworkD[6].split(",");
                        if (personData.length != 4) {
                            throw new WrongArgumentException("Person data must be in format: name,birthday,height,nationality");
                        }

                        String name = personData[0].trim();
                        LocalDate birthday = LocalDate.parse(personData[1].trim(), DateTimeFormatter.ISO_LOCAL_DATE);
                        Double height = personData[2].trim().isEmpty() ? null : Double.parseDouble(personData[2].trim());
                        Country nationality = personData[3].trim().isEmpty() ? null : Country.valueOf(personData[3].trim().toUpperCase());

                        Person author = new Person(name, birthday, height, nationality);

                        labwork1.setId(IdGenerator.generateId());
                        labwork1.setName(labworkD[0]);
                        labwork1.setCoordinates(coordinates);
                        labwork1.setCreationDate(ZonedDateTime.now());
                        labwork1.setMinimalPoint(Long.parseLong(labworkD[3]));
                        labwork1.setDescription(labworkD[4]);
                        labwork1.setDifficulty(Difficulty.valueOf(labworkD[5].toUpperCase()));
                        labwork1.setAuthor(author);

                        collectionManager.addLabWork(labwork1);
                        System.out.println("New element successfully added");
                    } catch (WrongArgumentException exx) {
                        stack.pop();
                        System.out.println(exx.getMessage());
                        System.out.println("Script Executing finished with error");
                        return;
                    }
                } else if (line.split(" ")[0].equals("updateId")) {
                    try {
                        String[] parts = line.split(" ");
                        if (parts.length > 2) {
                            throw new WrongArgumentException(parts[2]);
                        }
                        if (parts.length < 2) {
                            throw new NoArgumentException("id");
                        }
                        String rightPart = parts[1];
                        for (int n = 0; n < 7 && scanner.hasNextLine(); n++) {
                            labworkD[n] = scanner.nextLine();
                        }
                        LabWork referenceLabWork = new LabWork();
                        Validator.inputNotEmpty(labworkD[0], "Name");
                        Validator.inputNotEmpty(labworkD[1], "x");
                        Validator.xGood(labworkD[1]);
                        Validator.inputNotEmpty(labworkD[2], "y");
                        Validator.yGood(labworkD[2]);
                        Validator.inputNotEmpty(labworkD[3], "minimal point");
                        Validator.minimalPointGood(labworkD[3]);
                        Validator.inputNotEmpty(labworkD[4], "description");
                        Validator.inputNotEmpty(labworkD[5], "difficulty");
                        Validator.difficultyGood(labworkD[5]);
                        Coordinates coordinates = new Coordinates(Integer.parseInt(labworkD[1]), Double.parseDouble(labworkD[2]));

                        String[] personData = labworkD[6].split(",");
                        if (personData.length != 4) {
                            throw new WrongArgumentException("Person data must be in format: name,birthday,height,nationality");
                        }

                        String name = personData[0].trim();
                        LocalDate birthday = LocalDate.parse(personData[1].trim(), DateTimeFormatter.ISO_LOCAL_DATE);
                        Double height = personData[2].trim().isEmpty() ? null : Double.parseDouble(personData[2].trim());
                        Country nationality = personData[3].trim().isEmpty() ? null : Country.valueOf(personData[3].trim().toUpperCase());

                        Person author = new Person(name, birthday, height, nationality);

                        Vector<LabWork> labWorkCollection = collectionManager.getLabWorkCollection();
                        long inputEl = Long.parseLong(rightPart);
                        ZonedDateTime dateForUpdate = null;
                        boolean found = false;
                        for (LabWork labWorkColl : labWorkCollection) {
                            if (labWorkColl.getId() == inputEl) {
                                dateForUpdate = labWorkColl.getCreationDate();
                                collectionManager.removeById(inputEl);
                                referenceLabWork.setIdForUpdate(inputEl);
                                referenceLabWork.setName(labworkD[0]);
                                referenceLabWork.setCoordinates(coordinates);
                                referenceLabWork.setCreationDate(dateForUpdate);
                                referenceLabWork.setMinimalPoint(Long.parseLong(labworkD[3]));
                                referenceLabWork.setDescription(labworkD[4]);
                                referenceLabWork.setDifficulty(Difficulty.valueOf(labworkD[5].toUpperCase()));
                                referenceLabWork.setAuthor(author);
                                collectionManager.addLabWork(referenceLabWork);
                                System.out.println("Element successfully updated");
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            throw new NoElementException(inputEl);
                        }
                        for (int v = 0; v < parts.length; v++) {
                            parts[v] = null;
                        }
                    } catch (WrongArgumentException exx) {
                        stack.pop();
                        System.out.println(exx.getMessage());
                        System.out.println("Script Executing finished with error");
                        return;
                    } catch (NoArgumentException exx) {
                        stack.pop();
                        System.out.println(exx.getMessage());
                        System.out.println("Script Executing finished with error");
                        return;
                    } catch (NoElementException ex) {
                        stack.pop();
                        System.out.println(ex.getMessage());
                        System.out.println("Script Executing finished with error");
                        return;
                    }
                } else {
                    CommandManager.startExecute(line);
                }
            }
            stack.pop();
            scanner.close();
            System.out.println("Script command executing complete!");
        } catch (EmptyStackException e) {
            System.out.print("");
        } finally {
            if (!stack.isEmpty()) {
                stack.pop();
            }
        }
    }

    @Override
    public String getName() {
        return "execute_script {filename}: ";
    }

    @Override
    public String getDescription() {
        return "execute script from file";
    }
}
