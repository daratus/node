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
public class BlockedStateTest {

    private final String originalName = "TestName";
    
    private final String newName = "TestNewName";
    
    private AuthenticationState initialState;

    private NodeContextMockup context;

    private OperationalState operationalState;

    private BlockedState blockedState;
    
    @Before
    public void setUp() throws Exception {
        initialState = new AuthenticationState();
        context = new NodeContextMockup();
        operationalState = new OperationalState(initialState);
        blockedState = new BlockedState(initialState, operationalState);
    }

    /**
     * 
     */
    @Test
    public void testHandleMissingConstraints() {
        blockedState.handle(context);
        assertNotNull(context.getCurrentState());
        assertEquals(initialState, context.getCurrentState());

        context.setCurrentState(blockedState);
        blockedState.handle(context);
        assertNotNull(context.getCurrentState());
        assertEquals(initialState, context.getCurrentState());
        
        context.setCurrentState(blockedState);
        context.setName(originalName);
        blockedState.handle(context);
        assertNotNull(context.getCurrentState());
        assertEquals(operationalState, context.getCurrentState());
    }
    
    /**
     * 
     */
    @Test
    public void testHandleBlockedState() {
        context.setCurrentState(blockedState);
        context.setName(originalName);
        context.setBlocked(true);
        blockedState.handle(context);
        assertNotNull(context.getCurrentState());
        assertEquals(blockedState, context.getCurrentState());
        
        context.setName(newName);
        blockedState.handle(context);
        assertNotNull(context.getCurrentState());
        assertEquals(blockedState, context.getCurrentState());
        assertEquals(originalName, context.getName());
    }
    
    /**
     * 
     */
    @Test
    public void testHandleLogout() {
        context.setCurrentState(blockedState);
        context.setName(originalName);
        context.setBlocked(true);
        blockedState.handle(context);
        assertNotNull(context.getCurrentState());
        assertEquals(blockedState, context.getCurrentState());
        
        context.logout();
        blockedState.handle(context);
        assertNotNull(context.getCurrentState());
        assertEquals(initialState, context.getCurrentState());
    }

    /**
     * 
     */
    @Test
    public void testHandleStop() {
        context.setCurrentState(operationalState);
        operationalState.setNextState(blockedState);
        context.setName(originalName);
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
        blockedState.handle(context);
        assertNotNull(context.getCurrentState());
        assertEquals(operationalState, context.getCurrentState());
        
    }
    
}
