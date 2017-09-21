package com.daratus.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.daratus.node.domain.NullTask;

/**
 * 
 * @author Zilvinas Vaira
 *
 */
public class OperationalStateTest {

    private final String originalName = "TestName";
    
    private final String newName = "TestNewName";
    
    private AuthenticationState initialState;

    private NodeContextMockup context;

    private OperationalState operationalState;
    
    @Before
    public void setUp() throws Exception {
        initialState = new AuthenticationState();
        context = new NodeContextMockup();
        operationalState = new OperationalState(initialState);
    }
    
    /**
     * Test null current state and missing authentication.
     */
    @Test
    public void testHandleNotAuthenticated() {
        operationalState.handle(context);
        assertNotNull(context.getCurrentState());
        assertEquals(initialState, context.getCurrentState());
        
        context.setCurrentState(operationalState);
        operationalState.handle(context);
        assertNotNull(context.getCurrentState());
        assertEquals(initialState, context.getCurrentState());
    }
    
    /**
     * Test authenticated state and repeated authentication attempt.
     */
    @Test
    public void testHandleAuthenticated() {
        context.setName(originalName);
        context.setCurrentState(operationalState);
        operationalState.handle(context);
        assertNotNull(context.getCurrentState());
        assertEquals(operationalState, context.getCurrentState());
        
        context.setName(newName);
        operationalState.handle(context);
        assertNotNull(context.getCurrentState());
        assertEquals(operationalState, context.getCurrentState());
        assertEquals(originalName, context.getName());
    }

    /**
     * Test missing blocked state.
     */
    @Test
    public void testHandleMissingBlockedState() {
        context.setName(originalName);
        context.setCurrentState(operationalState);
        context.setBlocked(true);
        operationalState.handle(context);
        assertNotNull(context.getCurrentState());
        assertEquals(operationalState, context.getCurrentState());
    }
    
    /**
     * Test success transition to blocked state.
     */
    @Test
    public void testHandleSuccessBlockedState() {
        BlockedState blockedState = new BlockedState(initialState, operationalState);
        operationalState.setNextState(blockedState);
        context.setName(originalName);
        context.setCurrentState(operationalState);
        context.setBlocked(true);
        operationalState.handle(context);
        assertNotNull(context.getCurrentState());
        assertEquals(blockedState, context.getCurrentState());
        
        try {
            Thread.sleep(3 * NullTask.SECONDS_CONVERSION_RATE / 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        context.setBlocked(false);
    }

}
