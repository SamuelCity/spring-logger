package com.gocity.logger;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

@Slf4j
class LoggerExceptionInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) throws Exception {
        if (!shouldLogOutput(response.getStatus())) {
            HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
            return;
        }

        final var attributes = Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
        final var requestBody = attributes.getAttribute("request_body", RequestAttributes.SCOPE_REQUEST);
        final var responseBody = attributes.getAttribute("response_body", RequestAttributes.SCOPE_REQUEST);

        log.warn("Encountered error response {} :: Method - {} :: URI - {} :: Query Params - {} :: Body - {} :: Response - {}",
            response.getStatus(),
            request.getMethod(),
            request.getRequestURI(),
            request.getQueryString(),
            requestBody,
            responseBody
        );

        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    protected boolean shouldLogOutput(int status) {
        return status >= 400;
    }
}
