package com.mcn.common.utils;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ControllerUtils {

    public String sanitise(String inputString) {
        inputString = sanitiseNewLinesTabsReturns(inputString);
        inputString = inputString.replaceAll("([<>&])", "\\\\$1");
        return inputString;
    }

    public String sanitiseNewLinesTabsReturns(String inputString) {
        return inputString.replaceAll("[\n\r\t]", "_");
    }

    public HttpStatus getHttpStatusCode(int httpStatusCode) {
        Optional<HttpStatus> httpStatusOptional = Optional.ofNullable(HttpStatus.resolve(httpStatusCode));
        return httpStatusOptional.isEmpty() ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.resolve(httpStatusCode);
    }

}
