package managers;

import DBlogic.PostgreSQLManager;
import clientLog.ClientHandler;
import commands.Command;
import exceptions.NoElementException;
import exceptions.RootException;
import exceptions.WrongArgumentException;
import models.LabWork;
import system.Request;
import system.Server;
import utility.IdGenerator;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.List;

public class Receiver {
    public static String addNewEl(Request request) throws WrongArgumentException {
        if (request.getMessage().split(" ").length == 1) {
            PostgreSQLManager manager = new PostgreSQLManager();
            LabWork obj = request.getLabWork();
            obj.setCreationDate(ZonedDateTime.now());
            long generatedId = manager.writeObjToDB(obj);
            if (generatedId != -1) {
               CollectionManager.getInstance().loadCollectionFromDB();
            }
            return "Element was added";
        } else throw new WrongArgumentException("Add command must not contain arguments");
    }

    public static String updateId(Request request) throws WrongArgumentException {
        if(request.getMessage().split(" ").length == 2) {
            PostgreSQLManager manager = new PostgreSQLManager();
            long inputEl = Long.parseLong(request.getKey());
            LabWork obj = request.getLabWork();
            obj.setCreationDate(ZonedDateTime.now());

            if (manager.isLabworkOwnedByUser(inputEl)) {
                if (manager.updateLabWork(obj)) {
                    CollectionManager.getInstance().loadCollectionFromDB();
                    return "element was updated";
                }
                return "Failed to update element";
            }
            else {
                return "Is not your labwork";
            }

        } else {
            throw new WrongArgumentException("updateId command must contain only one required argument");
        }
    }

    public static String clearCollection(Request request) throws WrongArgumentException {
        if(request.getMessage().split(" ").length == 1) {
            PostgreSQLManager manager = new PostgreSQLManager();
            List<Long> deletedLabWorkId = manager.clearLabWorkForUser();
            if(!deletedLabWorkId.isEmpty()) {
                CollectionManager.getInstance().getLabWorkCollection().removeIf(labWork -> deletedLabWorkId.contains(labWork.getId()));
                return "Collection cleared";
            } else {
                return "Collection already cleared only your elements was removed";
            }
        } else {
            throw new WrongArgumentException("Clear command must not contain arguments");
        }
    }

    public static String exit(Request request) throws WrongArgumentException {
        if (request.getMessage().split(" ").length == 1) {
            System.exit(1);
            return "";
        } else {
            throw new WrongArgumentException("Exit command must not contain arguments");
        }
    }

    public static String removeById(Request request) throws WrongArgumentException {
        if (request.getMessage().split(" ").length != 2) {
            throw new WrongArgumentException("removeById command must contain only one required argument");
        }

        try {
            long inputEl = Long.parseLong(request.getKey());
            PostgreSQLManager manager = new PostgreSQLManager();

            if (!manager.isLabworkOwnedByUser(inputEl)) {
                return "Is not your labwork or doesn't exist";
            }

            if (manager.removeLabWorkById(inputEl)) {
                CollectionManager.getInstance().loadCollectionFromDB();
                return "Element was removed successfully";
            } else {
                return "Failed to remove element (might be already deleted)";
            }
        } catch (NumberFormatException e) {
            throw new WrongArgumentException("ID must be a number");
        }
    }

    public static String getInfo(Request request) throws WrongArgumentException {
        StringBuilder text = new StringBuilder("");
        if (request.getMessage().split(" ").length == 1) {
            text.append("Data type: " + CollectionManager.getLabWorkCollection().getClass());
            text.append("\nInit data: " + CollectionManager.getInstance().getCreationDate());
            text.append("\nSize: " + CollectionManager.getLabWorkCollection().size());
            return text.toString();
        } else {
            throw new WrongArgumentException("Info command must not contain arguments");
        }
    }

    public static String getHistory(Request request) throws WrongArgumentException {
        StringBuilder text = new StringBuilder();
        if (request.getMessage().split(" ").length == 1) {
            String[] sp = new String[12];
            int n = 0;
            for (Command command : CommandManager.lastSixCommand) {
                sp[n] = command.getName();
                n += 1;
            }
            for (int i = sp.length - 1; i >= 0; i--) {
                if (sp[i] != null) {
                    text.append("-" + sp[i]).append("\n");
                }
            }
            if(CommandManager.lastSixCommand.isEmpty()) {
                System.out.println("history is empty");
            }
            return text.toString();
        } else {
            throw new WrongArgumentException("History command must not contain arguments");
        }
    }

    public static String getHelp(Request request) throws WrongArgumentException {
        StringBuilder text = new StringBuilder("");
        if (request.getMessage().split(" ").length == 1) {
            LinkedHashMap<String, Command> commandList = CommandManager.getCommandList();
            int maxNameLenght = 0;
            for (String name : commandList.keySet()) {
                if (name.length() > maxNameLenght) {
                    maxNameLenght = name.length();
                }
            }
            String formatString = "%-" + (maxNameLenght + 2) + "s - %s\n";
            for (String name : commandList.keySet()) {
                if(!name.equals("save")) {
                    Command command = commandList.get(name);
                    text.append(String.format(formatString, command.getName(), command.getDescription()));
                }
            }
            String executeScriptDescription = "Execute script from file.";
            text.append(String.format(formatString, "execute_script {file}", executeScriptDescription));
            return text.toString();
        } else {
            throw new WrongArgumentException("Help command must not contain arguments");
        }
    }


    public static String showData(Request request) throws WrongArgumentException {
        if (request.getMessage().split(" ").length == 1) {
            CollectionManager collectionManager = CollectionManager.getInstance();
            return collectionManager.showCollection();
        } else {
            throw new WrongArgumentException("show command must not contain arguments");
        }
    }
    public static String countByAuthor(Request request) throws WrongArgumentException {
        if (request.getMessage().split(" ").length == 2) {
            String s = CollectionManager.countLessThanAuthor(request);
            return s;
        } else {
            throw new WrongArgumentException("countByAuthor command must contain only one required argument");
        }
    }
    public static String sortDescending(Request request) throws WrongArgumentException {
        if (request.getMessage().split(" ").length == 1) {
            return CollectionManager.descending();
        } else {
            throw new WrongArgumentException("descending command must not contain arguments");
        }
    }

    public static String reorder(Request request) throws WrongArgumentException, NoElementException {
        if (request.getMessage().split(" ").length == 1) {

            return CollectionManager.reorder();
        } else {
            throw new WrongArgumentException("reorder command must not contain arguments");
        }
    }
    public static String register(Request request) {
        if (request.getName() != null && !request.getName().isEmpty() &&
                request.getPasswd() != null && request.getPasswd().length > 0) {
            ClientHandler clientHandler = new ClientHandler(request.getName(), request.getPasswd());
            if (clientHandler.regUser()) {
                return "Registration successful";
            } else {
                return "Registration failed: username may already exists or other DB error";
            }
        }
        return "Invalid input: username or password missing";
    }

    public static String login(Request request) {
        if (request.getName() != null && !request.getName().isEmpty() &&
                request.getPasswd() != null && request.getPasswd().length > 0) {
            ClientHandler clientHandler = new ClientHandler(request.getName(), request.getPasswd());
            if (clientHandler.authUser()) {
                return "Authentication successful. User ID: " + ClientHandler.getUserId();
            } else {
                return "Authentication failed. Wrong username or password.";
            }
        }
        return "Invalid input: username or password missing";
    }

}










