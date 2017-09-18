package com.daratus.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.daratus.node.console.APICommand;
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
        assertNotNull(context.getNodeState());
        assertEquals(initialState, context.getNodeState());

        context.setCurrentState(blockedState);
        blockedState.handle(context);
        assertNotNull(context.getNodeState());
        assertEquals(initialState, context.getNodeState());
        
        context.setCurrentState(blockedState);
        context.setName(originalName);
        blockedState.handle(context);
        assertNotNull(context.getNodeState());
        assertEquals(operationalState, context.getNodeState());
    }
    
    /**
     * 
     */
    @Test
    public void testHandleBlockedState() {
        context.setCurrentState(blockedState);
        context.setName(originalName);
        context.setRunning(true);
        blockedState.handle(context);
        assertNotNull(context.getNodeState());
        assertEquals(blockedState, context.getNodeState());
        
        context.setName(newName);
        blockedState.handle(context);
        assertNotNull(context.getNodeState());
        assertEquals(blockedState, context.getNodeState());
        assertEquals(originalName, context.getName());
        
        // TODO Refactoring required for next task
        context.getNextTask(APICommand.NEXT_TASK_PATH);
        // TODO Refactoring required for execute current
        context.executeCurrentTask();
    }
    
    /**
     * 
     */
    @Test
    public void testHandleLogout() {
        context.setCurrentState(blockedState);
        context.setName(originalName);
        context.setRunning(true);
        blockedState.handle(context);
        assertNotNull(context.getNodeState());
        assertEquals(blockedState, context.getNodeState());
        
        context.logout();
        blockedState.handle(context);
        assertNotNull(context.getNodeState());
        assertEquals(initialState, context.getNodeState());
    }

    /**
     * 
     */
    @Test
    public void testHandleStop() {
        context.setCurrentState(operationalState);
        operationalState.setNextState(blockedState);
        context.setName(originalName);
        context.setRunning(true);
        operationalState.handle(context);
        assertNotNull(context.getNodeState());
        assertEquals(blockedState, context.getNodeState());
        
        try {
            Thread.sleep(3 * NullTask.SECONDS_CONVERSION_RATE / 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        context.setRunning(false);
        blockedState.handle(context);
        assertNotNull(context.getNodeState());
        assertEquals(operationalState, context.getNodeState());
        
    }
    
}
