package Structures;

import java.util.LinkedList;
import java.util.Queue;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

public class BlockedList {
    private Queue<BCPEntry> list = new LinkedList<>();

    public void addProcess(BCP bcp) {
        list.add(new BCPEntry(bcp));
    }


    // Avança o relógio da lista de bloqueados
    public List<BCP> clock() {
        List<BCP> toReady = new ArrayList<>();
        Iterator<BCPEntry> iterator = list.iterator();
        while (iterator.hasNext()) {
            BCPEntry entry = iterator.next();
            entry.blockedTime--;
            if (entry.blockedTime < 0) {
                toReady.add(entry.bcp);
                iterator.remove();
            }
        }
        return toReady;
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    class BCPEntry {
        BCP bcp;
        int blockedTime;

        BCPEntry(BCP bcp) {
            this.bcp = bcp;
            this.blockedTime = 2; // exemplo: bloqueado por 2 ciclos
        }
    }
}
