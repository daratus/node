package com.daratus.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class InitialStateTest {

    private NodeContextMockup context;
    
    private InitialState initialState;
    
    @Before
    public void setUp() throws Exception {
        context = new NodeContextMockup();
        initialState = new InitialState();
    }
    
    public void testHandleMissingCurrentState() {
        // Test missing initial state
        initialState.handle(context);
        assertNull(context.getNodeState());
    }
    
    @Test
    public void testHandleInitialState() {
        // Test initial state
        context.setCurrentState(initialState);
        initialState.handle(context);
        assertNotNull(context.getNodeState());
        assertEquals(initialState, context.getNodeState());
    }
    
    @Test
    public void testHandleMissingAuthenticated() {
        // Test handle missing authentication state
        context.setCurrentState(initialState);
        context.setName("TestName");
        initialState.handle(context);
        assertNotNull(context.getNodeState());
        assertEquals(initialState, context.getNodeState());
    }
    
    @Test
    public void testHandleSuccessAuthentication() {
        // Test initial state
        context.setCurrentState(initialState);
        context.setName("TestName");
        initialState.setNextState(new AuthenticatedState(initialState));
        initialState.handle(context);
        assertNotNull(context.getNodeState());
        assertNotEquals(initialState, context.getNodeState());
    }
    
    public void testHandleFailedRunning(){
        // Test handle failed running state
        context.setCurrentState(initialState);
        context.setRunning(true);
        initialState.handle(context);
        assertFalse(context.isRunning());
    }

}
