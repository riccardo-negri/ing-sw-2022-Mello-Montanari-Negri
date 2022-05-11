package it.polimi.ingsw.utils;

/*
    A thread-safe queue that makes sure the moves are processed in the correct order
 */

import it.polimi.ingsw.utils.moves.Move;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class MovesQueue extends PriorityQueue<Move> {
    private final Counter receivedCount = new Counter();
    public MovesQueue() {
        super(Comparator.comparingInt(Move::getNumber));
    }

    public synchronized boolean add(Move move) {
        return super.add(move);
    }

    public synchronized List<Move> pollAvailable() {
        List<Move> result = new ArrayList<>();
        while (super.peek() != null && super.peek().getNumber() == receivedCount.get()) {
            Move m = super.poll();
            receivedCount.increment();
            if (m != null)
                result.add(m);
        }
        return result;
    }

}
