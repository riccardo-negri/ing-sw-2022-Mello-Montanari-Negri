package it.polimi.ingsw.networking;

import it.polimi.ingsw.networking.moves.Move;
import it.polimi.ingsw.utils.Counter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 *  A thread-safe queue that makes sure the moves are processed in the correct order
 *  since every client updates the game state independently is crucial that all the moves are applied in the same order
 *  from everyone, in the unlikely event of two or more moves sent almost together that are received in the wrong order
 *  this queue reorders them.
 */
public class MovesQueue extends PriorityQueue<Move> {
    private final Counter receivedCount = new Counter();

    /**
     * creates a priority queue of moves that orders the elements by the sending order
     */
    public MovesQueue() {
        super(Comparator.comparingInt(Move::getNumber));
    }

    /**
     * add a move in the correct position of the queue
     * @param move the move to add to the queue
     * @return if the move was added
     */
    @Override
    public boolean add(Move move) {
        synchronized (this) {
            return super.add(move);
        }
    }

    /**
     * starting from the head poll all the moves in order, stop if one is missing
     * @return a list of the moves removed from the queue
     */
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
