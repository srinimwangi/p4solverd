package edu.gatech.saadclass.project4.serverimpl;

/**
 * Created by cynthia on 4/19/15.
 */
public class Reachability {
    public static final int VIA_NOTHING = 0;
    public static final int VIA_STANDARD = 1;
    public static final int VIA_ALTERNATIVE = 2;

    public int reachableVia() {
        return reachable_via;
    }

    private int reachable_via;
}
