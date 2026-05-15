package com.tasktrackergraphql.config;

import com.tasktrackergraphql.exceptions.NullableViolation;
import com.tasktrackergraphql.exceptions.ServerException;
import com.tasktrackergraphql.exceptions.UniquenessViolation;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {

    @GraphQlExceptionHandler
    public GraphQLError handleEntityNotFound (EntityNotFoundException ex, DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError (env)
                .errorType (ErrorType.NOT_FOUND)
                .message (ex.getMessage () != null && !ex.getMessage ().isEmpty () ? ex.getMessage () : "Объект не найден")
                .build ();
    }

    @GraphQlExceptionHandler
    public GraphQLError handleUniquenessViolation (UniquenessViolation ex, DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError (env)
                .errorType (ErrorType.BAD_REQUEST)
                .message ("Нарушение уникальности поля: " + ex.getMessage ())
                .build ();
    }

    @GraphQlExceptionHandler
    public GraphQLError handleNullableViolation (NullableViolation ex, DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError (env)
                .errorType (ErrorType.BAD_REQUEST)
                .message (ex.getMessage ())
                .build ();
    }

    @GraphQlExceptionHandler
    public GraphQLError handleServerException (ServerException ex, DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError (env)
                .errorType (ErrorType.INTERNAL_ERROR)
                .message (ex.getMessage ())
                .build ();
    }

    @GraphQlExceptionHandler
    public GraphQLError handleRuntimeException (RuntimeException ex, DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError (env)
                .errorType (ErrorType.INTERNAL_ERROR)
                .message (ex.getMessage ())
                .build ();
    }
}
