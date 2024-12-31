package com.ecommerce.advice;

import com.ecommerce.payload.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
@AllArgsConstructor
public class GlobalResponseWrapper implements ResponseBodyAdvice<Object> {

    private final HttpServletResponse servletResponse;

    @Override
    public boolean supports(@NonNull MethodParameter returnType, @NonNull Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            @NonNull MediaType selectedContentType,
            @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response
    ) {
        System.out.println("Wrapping response: " + body);
        System.out.println("Return type: " + returnType.getParameterType());
        System.out.println("Selected content type: " + selectedContentType);
        System.out.println("Selected converter type: " + selectedConverterType);
        System.out.println("Request: " + request);
        System.out.println("Response: " + response);

        // Extract HTTP status
        // Get HTTP status from HttpServletResponse
        int statusCode = servletResponse.getStatus();
        HttpStatus status = HttpStatus.resolve(statusCode);


        // Skip wrapping for already wrapped responses
        if (body instanceof ApiResponse) {
            return body;
        }

        // Customize response based on status
        if (status != null && status.isError()) {
            return new ApiResponse<>(false, "Error: " + status.getReasonPhrase(), body);
        } else {
            return new ApiResponse<>(true, "Processed successfully", body);
        }
    }
}
