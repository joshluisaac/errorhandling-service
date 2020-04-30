package au.com.joshluisaac.errorhandlingservice.error;

import java.time.LocalDateTime;
import java.util.Map;

import au.com.joshluisaac.errorhandlingservice.ApiErrorResponse;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

@Component
@Order(-2)
public class ApiExceptionHandler extends AbstractErrorWebExceptionHandler {

  public ApiExceptionHandler(
          ErrorAttributes errorAttributes,
          ResourceProperties resourceProperties,
          ApplicationContext applicationContext, ServerCodecConfigurer configurer) {
    super(errorAttributes, resourceProperties, applicationContext);
      this.setMessageWriters(configurer.getWriters());
  }


  @Override
  protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
    return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
  }

  private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
      Map<String, Object> errorAttributes = getErrorAttributes(request, false);
      HttpStatus httpStatus = HttpStatus.valueOf((int) errorAttributes.get("status"));
    return ServerResponse.status(httpStatus)
        .contentType(MediaType.APPLICATION_JSON)
            //.bodyValue(buildResponseEntityFromErrorResponse(errorAttributes,"",null));

    .bodyValue(new ApiErrorResponse());

        //.body(BodyInserters.fromValue());
  }



    private static ResponseEntity<ApiErrorResponse> buildResponseEntityFromErrorResponse(
            Map<String, Object> errorAttributes,
            String message,
            BindingResult bindingResult
    ) {

        HttpStatus httpStatus = HttpStatus.valueOf((int) errorAttributes.get("status"));
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
        apiErrorResponse.setStatus(httpStatus);
        apiErrorResponse.setHttpStatusValue(httpStatus.value());
        apiErrorResponse.setDateErrorOccurred(LocalDateTime.now());
        apiErrorResponse.setErrorMessage(message);
        apiErrorResponse.setPath((String) errorAttributes.get("path"));

        if (bindingResult != null) apiErrorResponse.withBindingResult(bindingResult);

        return new ResponseEntity<>(apiErrorResponse, apiErrorResponse.getStatus());
    }




  private void customErrorBuilder(){

  }


  private void extractAttributesFromResponse(){

  }


}
