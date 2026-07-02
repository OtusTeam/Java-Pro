package ru.otus.services;

import org.springframework.stereotype.Component;
import ru.otus.model.Player;

//@Component
public class PlayerServiceImpl implements PlayerService {

    private final IOService ioService;

    public PlayerServiceImpl(IOService ioService) {
        this.ioService = ioService;
    }

    @Override
    public Player getPlayer() {
        ioService.out("What is your name?");
        String playerName = ioService.readLn("Enter your name: ");
        return new Player(playerName);
    }
}
