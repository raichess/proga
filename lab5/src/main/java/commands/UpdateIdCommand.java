package commands;

import exceptions.NoArgumentException;
import exceptions.NoElementException;
import exceptions.WrongArgumentException;
import managers.CollectionManager;
import managers.LabWorkAsker;
import models.LabWork;

import java.time.ZonedDateTime;
import java.util.Vector;

/**
 * Данная команда обновляет значения полей элемента по его ID
 *
 * @see Command
 * @author raichess
 * @since 1.0
 */
public class UpdateIdCommand implements Command {
    @Override
    public void execute(String[] args) throws Exception {
        try {
            CollectionManager collectionManager = CollectionManager.getInstance();
            if (args.length > 2) {
                throw new WrongArgumentException(args[2]);
            }
            if (args.length < 2) {
                throw new NoArgumentException("id");
            }
            Vector<LabWork> labWorkCollection = collectionManager.getLabWorkCollection();
            LabWork newLabWork = null;
            long input = Long.parseLong(args[1]);
            boolean found = false;
            ZonedDateTime dateForUpdate = null;
            for (LabWork labWork : labWorkCollection) {
                if (labWork.getId() == input) {
                    dateForUpdate = labWork.getCreationDate();
                    collectionManager.removeById(input);
                    newLabWork = LabWorkAsker.createLabWork();
                    newLabWork.setIdForUpdate(input);
                    newLabWork.setCreationDate(dateForUpdate);
                    collectionManager.addLabWork(newLabWork);
                    found = true;
                    System.out.println("Lab work updated");
                    break;
                }
            }
            if (!found) {
                throw new NoElementException(input);
            }
        } catch (NoElementException e) {
            System.out.println(e.getMessage());
            System.out.println("Nothing to update");
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number");
        }
    }


    @Override
    public String getName() {
        return "updateId {element}: ";
    }

    @Override
    public String getDescription() {
        return "update element";
    }
}
