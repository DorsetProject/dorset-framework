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

import edu.jhuapl.dorset.agent.AgentBase;
import edu.jhuapl.dorset.agent.AgentRequest;
import edu.jhuapl.dorset.agent.AgentResponse;
import edu.jhuapl.dorset.agent.Description;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class CalculatorAgent extends AgentBase {

    private static final String SUMMARY = "Perform mathematical calculations.";
    private static final String EXAMPLE = "341 / 13";

    public CalculatorAgent() {
        this.setName("calculator");
        this.setDescription(new Description(name, SUMMARY, EXAMPLE));
    }

    @Override
    public AgentResponse process(AgentRequest request) {
        Expression exp = new ExpressionBuilder(request.text).build();
        double result = exp.evaluate();

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
