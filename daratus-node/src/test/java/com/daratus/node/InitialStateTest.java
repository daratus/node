package com.daratus.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class InitialStateTest {

    @Test
    public void testHandle() {
        NodeContextMockup context = new NodeContextMockup();
        InitialState initialState = new InitialState();
        assertNull(context.getNodeState());
        
        context.setCurrentState(initialState);
        initialState.handle(context);
        assertNotNull(context.getNodeState());
        assertEquals(initialState, context.getNodeState());
        
        context.setName("TestName");
        initialState.handle(context);
        assertNotNull(context.getNodeState());
        assertEquals(initialState, context.getNodeState());

        context.setRunning(true);
        initialState.handle(context);
        assertFalse(context.isRunning());
        
        initialState.setNextState(new AuthenticatedState(initialState));
        initialState.handle(context);
        assertNotNull(context.getNodeState());
        assertNotEquals(initialState, context.getNodeState());
        
    }

}
