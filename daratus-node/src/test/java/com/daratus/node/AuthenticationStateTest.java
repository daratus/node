package com.daratus.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.daratus.node.console.APICommand;

/**
 * 
 * 
 * @author Zilvinas Vaira
 *
 */
public class AuthenticationStateTest {

    private final String name = "TestName";

    private NodeContextMockup context;
    
    private AuthenticationState initialState;
    
    @Before
    public void setUp() throws Exception {
        context = new NodeContextMockup();
        initialState = new AuthenticationState();
    }
    
    /**
     * Test missing current state.
     */
    @Test
    public void testHandleMissingCurrentState() {
        initialState.handle(context);
        assertNull(context.getCurrentState());
    }

    /**
     * Test missing authentication.
     */
    @Test
    public void testHandleMissingAuthentication() {
        context.setCurrentState(initialState);
        initialState.handle(context);
        assertNotNull(context.getCurrentState());
        assertEquals(initialState, context.getCurrentState());
    }
    
    /**
     * Test handle missing operational state
     */
    @Test
    public void testHandleMissingNextState() {
        context.setCurrentState(initialState);
        context.setName(name);
        initialState.handle(context);
        assertNotNull(context.getCurrentState());
        assertEquals(initialState, context.getCurrentState());
    }
    
    /**
     * Test success authentication.
     */
    @Test
    public void testHandleSuccessAuthentication() {
        context.setCurrentState(initialState);
        context.setName(name);
        initialState.setNextState(new OperationalState(initialState));
        initialState.handle(context);
        assertNotNull(context.getCurrentState());
        assertNotEquals(initialState, context.getCurrentState());
    }
    
    /**
     * Test handle unauthenticated attempt to execute operational state.
     */
    @Test
    public void testHandleFailedOperational(){
        context.setCurrentState(initialState);
        context.setRunning(true);
        initialState.handle(context);
        assertFalse(context.isBlocked());
    }

}
