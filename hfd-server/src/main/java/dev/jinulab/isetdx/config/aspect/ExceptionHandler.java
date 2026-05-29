package dev.jinulab.isetdx.config.aspect;

import org.springframework.web.bind.annotation.ControllerAdvice;

import dev.jinulab.framework.exception.handler.MessageExceptionHandler;


@ControllerAdvice
public class ExceptionHandler extends MessageExceptionHandler {
	
//	// 로그인 실패 (비밀번호 틀림)
//    @org.springframework.web.bind.annotation.ExceptionHandler(BadCredentialsException.class)
//    public ResponseEntity<ExceptionResponseBody> handleBadCredentials(
//            BadCredentialsException e, WebRequest request) {
//        AuthException ae = new AuthException("아이디 또는 비밀번호가 올바르지 않습니다.", e);
//        return handleAuthException(ae, request); // → 401
//    }
//
//    // Spring Security 내부 래핑 예외
//    @org.springframework.web.bind.annotation.ExceptionHandler(InternalAuthenticationServiceException.class)
//    public ResponseEntity<ExceptionResponseBody> handleInternalAuth(
//            InternalAuthenticationServiceException e, WebRequest request) {
//        AuthException ae = new AuthException("아이디 또는 비밀번호가 올바르지 않습니다.", e);
//        return handleAuthException(ae, request); // → 401
//    }
//
//    // 비활성화 계정
//    @org.springframework.web.bind.annotation.ExceptionHandler(DisabledException.class)
//    public ResponseEntity<ExceptionResponseBody> handleDisabled(
//            DisabledException e, WebRequest request) {
//        AuthException ae = new AuthException("비활성화된 계정입니다. 관리자에게 문의하세요.", e);
//        return handleAuthException(ae, request); // → 401
//    }

}
