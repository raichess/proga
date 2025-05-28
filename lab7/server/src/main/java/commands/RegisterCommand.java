package commands;

import managers.Receiver;
import system.Request;

public class RegisterCommand implements Command{
    @Override
    public String execute(Request request) throws Exception {
        try {
            return Receiver.register(request);
        }catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    public String getName() {
        return "register";
    }

    @Override
    public String getDescription() {
        return "Command for registration";
    }
}
