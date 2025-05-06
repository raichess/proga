package managers;


import exceptions.*;
import models.*;
import system.Client;
import system.Request;
import utility.Validator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Данная команда выполняет скрипт файл
 *
 * @author raichess
 * @since 1.0
 */
public class ExecuteScriptCommand {
    private static Stack<File> stack = new Stack<>();
    private static Client Client;

    public static void execute(String command) throws Exception {
        try {
            String[] args = command.trim().split("\\s+");
            if (args.length > 2) {
                throw new WrongArgumentException(args[2]);
            }
            if (args.length < 2) {
                throw new NoArgumentException("script name");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        File file = new File(command.trim().split("\\s+")[1]);
        try {
            if (!file.canRead() || !file.exists()) {
                throw new RootException();
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
        } catch (RootException e) {
            System.out.println(e.getMessage());
            return;
        }

        String path = command.trim().split("\\s+")[1];
        var br = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8));
        String line;
        String[] labWorkData = new String[11];
        boolean isSpecialCommandExecuted = false;
        while ((line = br.readLine()) != null) {
            isSpecialCommandExecuted = false;
            String[] parts = line.split(" ", 2);
            switch (parts[0]) {
                case "add":
                    try {
                        if (parts.length > 1) {
                            System.out.println("The 'add' command should not have any arguments.\n" + "Script " + path + " executing finished with error");
                            stack.pop();
                            return;
                        }
                        for (int i = 0; i < labWorkData.length; i++) {
                            if (i==3) {
                                labWorkData[i] = ZonedDateTime.now().toString(); // creationDate
                                continue;
                            } else {
                                labWorkData[i] = br.readLine();
                                try {
                                    switch (i) {
                                        case 0:
                                            Validator.inputNotEmpty(labWorkData[i], "name");
                                            break;
                                        case 1:
                                            Validator.inputNotEmpty(labWorkData[i], "x");
                                            Validator.xGood(labWorkData[i]);
                                            break;
                                        case 2:
                                            Validator.inputNotEmpty(labWorkData[i], "y");
                                            Validator.yGood(labWorkData[i]);
                                            break;
                                        case 4:
                                            Validator.inputNotEmpty(labWorkData[i], "minimal point");
                                            Validator.minimalPointGood(labWorkData[i]);
                                            break;
                                        case 5:
                                            Validator.inputNotEmpty(labWorkData[i], "description");
                                            break;
                                        case 6:
                                            Validator.inputNotEmpty(labWorkData[i], "difficulty");
                                            Validator.difficultyGood(labWorkData[i]);
                                            break;
                                        case 7:
                                            Validator.inputNotEmpty(labWorkData[i], "person name");
                                            break;
                                        case 8:
                                            Validator.inputNotEmpty(labWorkData[i], "person birthday");
                                            Validator.birthdayGood(labWorkData[i]);
                                            break;
                                        case 9:
                                            Validator.inputNotEmpty(labWorkData[i], "person height");
                                            Validator.heightGood(labWorkData[i]);
                                            break;
                                        case 10:
                                            Validator.inputNotEmpty(labWorkData[i], "person nationality");
                                            Validator.countryGood(labWorkData[i]);
                                            break;
                                    }
                                } catch (WrongArgumentException e) {
                                    System.out.println(e.getMessage());
                                    return;
                                }
                            }
                        }
                        isSpecialCommandExecuted = true;
                        Client.sendRequest(new Request("add", new LabWork(labWorkData), null));
                        labWorkData = new String[11];
                    }catch (NullPointerException e) {
                        System.out.println("Error in command: absent new data");
                        System.out.println("Script " + path + " executing finished with error");
                        stack.pop();
                        return;
                    } catch (Exception e) {
                        System.out.println("Error in command: " + e.getMessage());
                        System.out.println("Script " + path + " executing finished with error");
                        stack.pop();
                        return;
                    }
                    break;

                case "execute_script":
                    if (parts.length > 2 || parts.length == 1) {
                        System.out.println("The 'executeScript' command should have only 1 argument.\n" + "Script " + path + " executing finished with error");
                        stack.pop();
                        return;
                    }
                    try {

                        String scr = parts[1];
                        File scFile = new File(scr);
                        if (!scFile.canRead()) {
                            throw new RootException();
                        }
                        if (stack.contains(scFile)) {
                            throw new RecursionException();
                        } else {
                            execute(parts[0] + " " + parts[1]);
                        }
                    } catch (RecursionException e) {
                        stack.pop();
                        System.out.println(e.getMessage());
                        System.out.println("Script " + path + " executing finished with error");
                        if (!stack.isEmpty()) {
                            stack.pop();
                        }
                        return;
                    }
                    isSpecialCommandExecuted = true;
                    break;

                case "updateId":
                    try {
                        if (parts.length < 2) {
                            System.out.println("No argument in command: id\n" + "Script " + path + " executing finished with error");
                            stack.pop();
                            return;
                        }
                        String strId = parts[1];
                        long id = Long.parseLong(strId);

                        for (int i = 0; i < labWorkData.length; i++) {
                            labWorkData[i] = br.readLine();
                            try {
                                switch (i) {
                                    case 0:
                                        Validator.inputNotEmpty(labWorkData[i], "name");
                                        break;
                                    case 1:
                                        Validator.inputNotEmpty(labWorkData[i], "x");
                                        Validator.xGood(labWorkData[i]);
                                        break;
                                    case 2:
                                        Validator.inputNotEmpty(labWorkData[i], "y");
                                        Validator.yGood(labWorkData[i]);
                                        break;
                                    case 3:
                                        Validator.inputNotEmpty(labWorkData[i], "minimal point");
                                        Validator.minimalPointGood(labWorkData[i]);
                                        break;
                                    case 4:
                                        Validator.inputNotEmpty(labWorkData[i], "description");
                                        break;
                                    case 5:
                                        Validator.inputNotEmpty(labWorkData[i], "difficulty");
                                        Validator.difficultyGood(labWorkData[i]);
                                        break;
                                    case 6:
                                        Validator.inputNotEmpty(labWorkData[i], "person name");
                                        break;
                                    case 7:
                                        Validator.inputNotEmpty(labWorkData[i], "person birthday");
                                        Validator.birthdayGood(labWorkData[i]);
                                        break;
                                    case 8:
                                        Validator.inputNotEmpty(labWorkData[i], "person height");
                                        Validator.heightGood(labWorkData[i]);
                                        break;
                                    case 9:
                                        Validator.inputNotEmpty(labWorkData[i], "person nationality");
                                        Validator.countryGood(labWorkData[i]);
                                        break;
                                }
                            } catch (WrongArgumentException e) {
                                System.out.println(e.getMessage());
                                return;
                            }
                        }
                        Client.sendRequest(new Request("updateId " + id, new LabWork(labWorkData), strId));
                        labWorkData = new String[11];
                        isSpecialCommandExecuted = true;

                    }catch (NumberFormatException e) {
                        System.out.println("ID must be a valid number: " + e.getMessage());
                        System.out.println("Script " + path + " executing finished with error");
                        stack.pop();
                        return;
                    } catch (NullPointerException e) {
                        System.out.println("Error in command: absent new data");
                        System.out.println("Script " + path + " executing finished with error");
                        stack.pop();
                        return;
                    }
                    catch (Exception e) {
                        System.out.println("Error while updating: " + e.getMessage());
                        System.out.println("Script " + path + " executing finished with error");
                        stack.pop();
                        return;
                    }
                    break;
            }
            if (!isSpecialCommandExecuted) {
                try {
                    if (line.contains("remove_by_id") || line.contains("remove_by_author") || line.contains("count_less_than_author")) {
                        if (parts.length < 2) {
                            throw new NoArgumentException("key");
                        } else {
                            Client.sendRequest(new Request(line, null, parts[1]));
                        }
                    } else {
                        Client.sendRequest(new Request(line, null, null));
                    }
                }catch (NoArgumentException e) {
                    System.out.println(e.getMessage());
                    System.out.println("Script " + path + " executing finished with error");
                    stack.pop();
                    return;
                }
            }
        }
        try {
            stack.pop();
            System.out.println("Script " + path + " was completed");
        }catch (EmptyStackException e) {
            System.out.println("");
        }
    }
}
