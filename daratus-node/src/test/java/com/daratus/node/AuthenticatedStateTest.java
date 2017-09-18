package com.daratus.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.daratus.node.domain.NullTask;

public class AuthenticatedStateTest {

    private String originalName = "TestName";
    
    private String newName = "TestNewName";
    
    private InitialState initialState;

    private NodeContextMockup context;

    private AuthenticatedState authenticatedState;
    
    @Before
    public void setUp() throws Exception {
        initialState = new InitialState();
        context = new NodeContextMockup();
        authenticatedState = new AuthenticatedState(initialState);
    }
    
    @Test
    public void testHandleNotAuthenticated() {
        // Test null current state
        AuthenticatedState authenticatedState = new AuthenticatedState(initialState);
        authenticatedState.handle(context);
        assertNotNull(context.getNodeState());
        assertEquals(initialState, context.getNodeState());
        
        // Test not authenticated state
        context.setCurrentState(authenticatedState);
        authenticatedState.handle(context);
        assertNotNull(context.getNodeState());
        assertEquals(initialState, context.getNodeState());
    }
    
    @Test
    public void testHandleAuthenticated() {
        // Test authenticated state
        AuthenticatedState authenticatedState = new AuthenticatedState(initialState);
        context.setName(originalName);
        context.setCurrentState(authenticatedState);
        authenticatedState.handle(context);
        assertNotNull(context.getNodeState());
        assertEquals(authenticatedState, context.getNodeState());
        
        // Test double authentication 
        context.setName(newName);
        authenticatedState.handle(context);
        assertNotNull(context.getNodeState());
        assertEquals(authenticatedState, context.getNodeState());
        assertEquals(originalName, context.getName());
    }

    @Test
    public void testHandleMissingRunningState() {
        // Test null running state
        context.setName(originalName);
        context.setCurrentState(authenticatedState);
        context.setRunning(true);
        authenticatedState.handle(context);
        assertNotNull(context.getNodeState());
        assertEquals(authenticatedState, context.getNodeState());
    }
    
    @Test
    public void testHandleSuccessRunningState() {
        // Test not null running state
        RunningState runningState = new RunningState(initialState, authenticatedState);
        authenticatedState.setNextState(runningState);
        context.setName(originalName);
        context.setCurrentState(authenticatedState);
        context.setRunning(true);
        authenticatedState.handle(context);
        assertNotNull(context.getNodeState());
        assertEquals(runningState, context.getNodeState());
        
        try {
            Thread.sleep(3 * NullTask.SECONDS_CONVERSION_RATE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        context.setRunning(false);
    }

}
