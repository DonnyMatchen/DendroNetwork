package com.donny.dendronetwork.data.lnetwork.lroute;

import com.donny.dendronetwork.data.lnetwork.exceptions.LoopBackException;
import com.donny.dendronetwork.data.lnetwork.exceptions.DestinationUnreachableException;
import com.donny.dendronetwork.data.lnetwork.LConnection;
import com.donny.dendronetwork.data.lnetwork.LNode;

import java.math.BigDecimal;
import java.util.ArrayList;

public class LRoute {
    private final LNode HEAD;
    private final ArrayList<RouteElement> PATH;

    public LRoute(LNode head) {
        HEAD = head;
        PATH = new ArrayList<>();
    }

    public LNode getHead() {
        return HEAD;
    }

    public BigDecimal totalCost() {
        BigDecimal total = BigDecimal.ZERO;
        for(RouteElement element : PATH) {
            total = total.add(element.COST);
        }
        return total;
    }

    public void populate(LNode src, LNode dst) throws LoopBackException, DestinationUnreachableException {
        if(src == dst) {
            throw new LoopBackException();
        }
        boolean flag = true;
        LNode current = src;
        PATH.clear();
        PATH.add(new RouteElement(src));
        while(flag) {
            LConnection con = src.route(dst);
            PATH.add(new RouteElement(con.traverse(current), con.getCost()));
            current = con.traverse(current);
            if(current == dst) {
                flag = false;
            }
        }
    }
}
