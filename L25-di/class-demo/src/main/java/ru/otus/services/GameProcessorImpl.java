package ru.otus.services;

import java.util.List;
import ru.otus.model.Equation;
import ru.otus.model.GameResult;
import ru.otus.model.Player;

public class GameProcessorImpl implements GameProcessor {

    private static final String MSG_HEADER = "Check your knowledge of multiplication table";
    private static final String MSG_INPUT_BASE = "Enter a number from 1 to 10:";
    private static final String MSG_RIGHT_ANSWER = "Ok\n";
    private static final String MSG_WRONG_ANSWER = "Not ok\n";

    private final IOService ioService;
    private final EquationPreparer equationPreparer;
    private final PlayerService playerService;

    public GameProcessorImpl(IOService ioService, EquationPreparer equationPreparer, PlayerService playerService) {
        this.ioService = ioService;
        this.equationPreparer = equationPreparer;
        this.playerService = playerService;
    }

    @Override
    public void startGame() {
        ioService.out(MSG_HEADER);
        ioService.out("---------------------------------------");
        Player player = playerService.getPlayer();
        GameResult gameResult = new GameResult(player);

        int base = ioService.readInt(MSG_INPUT_BASE);
        List<Equation> equations = equationPreparer.prepareEquationsFor(base);
        equations.forEach(e -> {
            boolean isRight = ioService.readInt(e.toString()) == e.getResult();
            gameResult.incrementRightAnswers(isRight);
            ioService.out(isRight ? MSG_RIGHT_ANSWER : MSG_WRONG_ANSWER);
        });
        ioService.out(gameResult.toString());
    }
}
