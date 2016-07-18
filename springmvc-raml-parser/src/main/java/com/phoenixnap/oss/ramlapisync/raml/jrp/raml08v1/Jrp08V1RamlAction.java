package com.phoenixnap.oss.ramlapisync.raml.jrp.raml08v1;

import com.phoenixnap.oss.ramlapisync.raml.RamlAction;
import com.phoenixnap.oss.ramlapisync.raml.RamlActionType;
import com.phoenixnap.oss.ramlapisync.raml.RamlMimeType;
import com.phoenixnap.oss.ramlapisync.raml.RamlResource;
import com.phoenixnap.oss.ramlapisync.raml.RamlResponse;
import org.raml.model.Action;
import org.raml.model.Response;
import org.raml.model.SecurityReference;
import org.raml.model.parameter.Header;
import org.raml.model.parameter.QueryParameter;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author armin.weisser
 */
public class Jrp08V1RamlAction implements RamlAction {

    private static Jrp08V1RamlModelFactory ramlModelFactory = new Jrp08V1RamlModelFactory();

    private final Action action;

    private Map<String, RamlResponse> responses = new LinkedHashMap<>();

    private Map<String, RamlMimeType> body = new LinkedHashMap<>();

    public Jrp08V1RamlAction(Action action) {
        this.action = action;
    }

    /**
     * Expose internal representation only package private
     * @return the internal model
     */
    Action getAction() {
        return action;
    }

    @Override
    public RamlActionType getType() {
        return ramlModelFactory.createRamlActionType(action.getType());
    }

    @Override
    public Map<String, QueryParameter> getQueryParameters() {
        return action.getQueryParameters();
    }

    @Override
    public Map<String, RamlResponse> getResponses() {
        syncResponses();
        return Collections.unmodifiableMap(responses);
    }

    private void syncResponses() {
        if(responses.size() != action.getResponses().size()) {
            responses.clear();
            Map<String, Response> baseResponses = action.getResponses();
            for (String key : baseResponses.keySet()) {
                RamlResponse ramlResponse = ramlModelFactory.createRamlResponse(baseResponses.get(key));
                responses.put(key, ramlResponse);
            }
        }
    }

    @Override
    public void addResponse(String httpStatus, RamlResponse response) {
        responses.put(httpStatus, response);
        action.getResponses().put(httpStatus, ramlModelFactory.extractResponse(response));
    }

    @Override
    public RamlResource getResource() {
        return ramlModelFactory.createRamlResource(action.getResource());
    }

    @Override
    public Map<String, Header> getHeaders() {
        return action.getHeaders();
    }

    @Override
    public Map<String, RamlMimeType> getBody() {
        syncBody();
        return Collections.unmodifiableMap(body);
    }

    private void syncBody() {
        ramlModelFactory.syncFromTo(action.getBody(), body, ramlModelFactory::createRamlMimeType);
    }

    @Override
    public void setBody(Map<String, RamlMimeType> body) {
        this.body = body;
        this.action.setBody(ramlModelFactory.extractBody(body));
    }

    @Override
    public boolean hasBody() {
        return action.hasBody();
    }

    @Override
    public String getDescription() {
        return action.getDescription();
    }

    @Override
    public void setDescription(String description) {
        action.setDescription(description);
    }

    @Override
    public void setResource(RamlResource resource) {
        action.setResource(ramlModelFactory.extractResource(resource));
    }

    @Override
    public void setType(RamlActionType actionType) {
        action.setType(ramlModelFactory.extractActionType(actionType));
    }

    @Override
    public List<SecurityReference> getSecuredBy() {
        return action.getSecuredBy();
    }
}
