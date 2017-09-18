package com.daratus.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AuthenticatedStateTest {

    @Test
    public void testHandle() {
        InitialState initialState = new InitialState();

        NodeContextMockup context = new NodeContextMockup();
        AuthenticatedState authenticatedState = new AuthenticatedState(initialState);
        assertNull(context.getNodeState());
        
        context.setCurrentState(authenticatedState);
        authenticatedState.handle(context);
        assertNotNull(context.getNodeState());
        assertEquals(initialState, context.getNodeState());
        
        context.setName("TestName");
        context.setCurrentState(authenticatedState);
        authenticatedState.handle(context);
        assertNotNull(context.getNodeState());
        assertEquals(authenticatedState, context.getNodeState());
        
    }
    
}
