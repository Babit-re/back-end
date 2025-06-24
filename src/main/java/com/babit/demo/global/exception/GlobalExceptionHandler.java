package com.babit.demo.global.exception;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // [1] 잘못된 요청 (예: 잘못된 인자 전달)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return buildResponseEntity(ErrorCode.INVALID_INPUT_VALUE, ex.getMessage());
    }

    // [2] 리소스를 찾을 수 없음 (예: 잘못된 URL 입력, 존재하지 않는 페이지/파일)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        return buildResponseEntity(ErrorCode.RESOURCE_NOT_FOUND, ex.getMessage());
    }

    // [3] 인증 실패 (예: 토큰 없음, 인증 안됨)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        return buildResponseEntity(ErrorCode.UNAUTHORIZED, "인증이 필요합니다.");
    }

    // [4] 토큰 관련 예외 (예: 만료, 위조 등)
    @ExceptionHandler({ JwtException.class, ExpiredJwtException.class })
    public ResponseEntity<ErrorResponse> handleJwtException(RuntimeException ex) {
        return buildResponseEntity(ErrorCode.UNAUTHORIZED, "유효하지 않거나 만료된 토큰입니다.");
    }

    // [5] 권한 없음
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        return buildResponseEntity(ErrorCode.FORBIDDEN, "접근 권한이 없습니다.");
    }

    // [6] 그 외 모든 예외 (필요하다면 컨트롤러 내에서 추가적인 에러처리도 가능)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        return buildResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    //위의 설정들에서 일관된 에러 응답 포맷 유지
    private ResponseEntity<ErrorResponse> buildResponseEntity(ErrorCode code, String message) {
        ErrorResponse response = new ErrorResponse(code.getCode(), code.getMessage(), message);
        return new ResponseEntity<>(response, code.getHttpStatus());
    }
}
