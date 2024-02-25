package com.donny.dendronetwork.data.lnetwork.lroute;

import com.donny.dendronetwork.data.lnetwork.LNode;

import java.math.BigDecimal;

public class RouteElement {
    public final boolean FIRST;
    public final LNode NODE;
    public final BigDecimal COST;

    public RouteElement(LNode node) {
        FIRST = true;
        NODE = node;
        COST = BigDecimal.ZERO;
    }

    public RouteElement(LNode node, BigDecimal cost) {
        FIRST = false;
        NODE = node;
        COST = cost;
    }
}
