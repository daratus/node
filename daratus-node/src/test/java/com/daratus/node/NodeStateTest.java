package com.daratus.node;

import static org.junit.Assert.*;

import org.junit.Test;

public class NodeStateTest {

    @Test
    public void testSetNextState() {
        NodeStateMockup nodeState = new NodeStateMockup();
        assertNull(nodeState.getNextSate());
        
        nodeState.setNextState(new NodeStateMockup());
        assertNotNull(nodeState.getNextSate());
    }

}
