/*
 * Copyright 2016 The Johns Hopkins University Applied Physics Laboratory LLC
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.jhuapl.dorset.agents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.jhuapl.dorset.ResponseStatus;
import edu.jhuapl.dorset.agents.AbstractAgent;
import edu.jhuapl.dorset.agents.AgentRequest;
import edu.jhuapl.dorset.agents.AgentResponse;
import edu.jhuapl.dorset.agents.Description;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * Performs simple mathematical calculations
 */
public class CalculatorAgent extends AbstractAgent {
    private final Logger logger = LoggerFactory.getLogger(CalculatorAgent.class);

    private static final String SUMMARY = "Perform mathematical calculations.";
    private static final String EXAMPLE = "341 / 13";

    public CalculatorAgent() {
        this.setDescription(new Description("calculator", SUMMARY, EXAMPLE));
    }

    @Override
    public AgentResponse process(AgentRequest request) {
        logger.debug("Handling the request: " + request.getText());
        Expression exp = null;
        try {
            exp = new ExpressionBuilder(request.getText()).build();
        } catch (IllegalArgumentException e) {
            logger.debug("CalculatorAgent could not parse " + request.getText());
            return new AgentResponse(ResponseStatus.Code.AGENT_DID_NOT_UNDERSTAND_REQUEST);
        }

        double result = 0;
        try {
            result = exp.evaluate();
        } catch (IllegalArgumentException e) {
            logger.debug("CalculatorAgent could not parse " + request.getText());
            return new AgentResponse(ResponseStatus.Code.AGENT_DID_NOT_UNDERSTAND_REQUEST);
        }

        // handle integers differently from floating point 
        String answer;
        if (result % 1 == 0) {
            answer = String.valueOf((int)result);
        } else {
            answer = String.valueOf(result);
        }

        return new AgentResponse(answer);
    }

}
