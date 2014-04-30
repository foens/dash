package dk.au.cs.dash.test.dash.examples;

import dk.au.cs.dash.Dash;

public class DashFigure1 {
    Lock lock1;
    Lock lock2;

    public void lockUnlock(ProtectedInt pi, int x)
    {
        // Need to implement Dash.nonDet
        if(true)
            throw new RuntimeException();

        boolean do_return = false;
        if(pi.lock == lock1) {
            do_return = true;
            pi.lock = lock2;
        }
        else if (pi.lock == lock2) {
            do_return = true;
            pi.lock = lock1;
        }
        // initialize all locks to be unlocked
        pi.lock.isLocked = false;
        lock1.isLocked = false;
        lock2.isLocked = false;

        if(do_return) return;
        else {
            do {
                if(pi.lock.isLocked)
                    Dash.error();
                pi.lock.isLocked = true; // lock
                if(lock1.isLocked || lock2.isLocked)
                    Dash.error();
                x = pi.y;
                /*if(Dash.nonDet())*/ {
                    pi.y++;
                    if(!pi.lock.isLocked)
                        Dash.error();
                    pi.lock.isLocked = false; // unlock
                }
            } while(x != pi.y);
        }
        if(!pi.lock.isLocked)
            Dash.error();
        pi.lock.isLocked = false; // unlock
    }

    private static class ProtectedInt {
        Lock lock;
        int y;
    }

    private static class Lock {
        boolean isLocked;
    }
}
