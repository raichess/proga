package managers;

import commands.Command;
import exceptions.NoElementException;
import exceptions.RootException;
import exceptions.WrongArgumentException;
import system.Request;
import system.Server;
import utility.IdGenerator;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;

public class Receiver {
    public static String addNewEl(Request request) throws WrongArgumentException {
        request.getLabWork().setId(IdGenerator.generateId());
        request.getLabWork().setCreationDate(ZonedDateTime.now());
        if (request.getMessage().split(" ").length == 1) {
            CollectionManager.addLabWork(request.getLabWork());
            return "Element was added";
        } else throw new WrongArgumentException("Add command must not contain arguments");
    }

    public static String updateId(Request request) throws WrongArgumentException {
        if (request.getMessage().split(" ").length == 2) {
            return CollectionManager.updateId(request);
        } else {
            throw new WrongArgumentException("updateId command must contain only one required argument");
        }
    }

    public static String clearCollection(Request request) throws WrongArgumentException {
        if (request.getMessage().split(" ").length == 1) {
            if (!CollectionManager.getLabWorkCollection().isEmpty()) {
                CollectionManager.clearLabWorkCollection();
                return "Collection cleared";
            } else {
                return "Collection already cleared";
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

    public static String removeById(Request request) throws WrongArgumentException, NoElementException, NoElementException {
        if (request.getMessage().split(" ").length == 2) {
            return CollectionManager.removeById(request);
        } else {
            throw new WrongArgumentException("removeById command must contain only one required argument");
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
            text.append(String.format(formatString, "executeScript {file}", executeScriptDescription));
            return text.toString();
        } else {
            throw new WrongArgumentException("Help command must not contain arguments");
        }
    }

    public static String saveData(Request request) throws IOException, RootException, WrongArgumentException {
        if (request.getMessage().split(" ").length == 1) {
            try {
                DumpManager.getInstance(Server.data_path).writeLabWorks(CollectionManager.getLabWorkCollection());
            }catch (Exception e) {
                System.out.println(e.getMessage());
                throw e;
            }
        } else {
            throw new WrongArgumentException("Save command must not contain arguments");
        }
        return "Data was saved";
    }

    public static String showData(Request request) throws WrongArgumentException {
        if (request.getMessage().split(" ").length == 1) {
            return CollectionManager.getInstance().showLabWorkCollection();
        } else {
            throw new WrongArgumentException("show command must not contain arguments");
        }
    }
    public static String countByAuthor(Request request) throws WrongArgumentException, NoElementException {
        if (request.getMessage().split(" ").length == 2) {
            return CollectionManager.countLessThanAuthor(request);
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
    public static String removeByAuthor(Request request) throws WrongArgumentException, NoElementException, NoElementException {
        if (request.getMessage().split(" ").length == 2) {
            return CollectionManager.removeByAuthor(request);
        } else {
            throw new WrongArgumentException("removeByAuthor command must contain only one required argument");
        }
    }

    public static String removeFirst(Request request) throws WrongArgumentException, NoElementException {
        if (request.getMessage().split(" ").length == 1) {
            return CollectionManager.removeFirst();
        } else {
            throw new WrongArgumentException("removeFirst command must not contain arguments");
        }
    }

    public static String reorder(Request request) throws WrongArgumentException, NoElementException {
        if (request.getMessage().split(" ").length == 1) {
            return CollectionManager.reorder();
        } else {
            throw new WrongArgumentException("reorder command must not contain arguments");
        }
    }


}










