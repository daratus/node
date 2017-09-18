package com.daratus.node;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class TestRunningState {

    private InitialState initialState;

    private NodeContextMockup context;

    private AuthenticatedState authenticatedState;

    private RunningState runningState;
    
    @Before
    public void setUp() throws Exception {
        initialState = new InitialState();
        context = new NodeContextMockup();
        authenticatedState = new AuthenticatedState(initialState);
        runningState = new RunningState(initialState, authenticatedState);
    }

    @Test
    public void testHandle() {
        fail("Not yet implemented");
    }

}
