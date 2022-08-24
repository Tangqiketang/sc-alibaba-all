/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
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

package com.wm.gatewayserver.security.config;

import com.wm.core.model.constants.AuthConstant;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * bear校验。可以替换为自己的。在链路中处于比较靠前的位置
 */
@Component
public class WmBearerTokenExtractor implements ServerAuthenticationConverter {

	//private static final Pattern authorizationPattern = Pattern.compile("^Bearer (?<token>[a-zA-Z0-9-._~+/]+=*)$", 2);
	private static final Pattern authorizationPattern = Pattern.compile("^"+ AuthConstant.JWT_PREFIX +"(?<token>[a-zA-Z0-9-._~+/]+=*)$", 2);
	private boolean allowUriQueryParameter = false;

	public WmBearerTokenExtractor() {
	}

	public Mono<Authentication> convert(ServerWebExchange exchange) {
		return Mono.fromCallable(() -> {
			return this.token(exchange.getRequest());
		}).map((token) -> {
			if (token.isEmpty()) {
				BearerTokenError error = invalidTokenError();
				throw new OAuth2AuthenticationException(error);
			} else {
				return new BearerTokenAuthenticationToken(token);
			}
		});
	}

	private String token(ServerHttpRequest request) {
		String authorizationHeaderToken = resolveFromAuthorizationHeader(request.getHeaders());
		String parameterToken = (String)request.getQueryParams().getFirst("access_token");
		if (authorizationHeaderToken != null) {
			if (parameterToken != null) {
				BearerTokenError error = new BearerTokenError("invalid_request", HttpStatus.BAD_REQUEST, "Found multiple bearer tokens in the request", "https://tools.ietf.org/html/rfc6750#section-3.1");
				throw new OAuth2AuthenticationException(error);
			} else {
				return authorizationHeaderToken;
			}
		} else {
			return parameterToken != null && this.isParameterTokenSupportedForRequest(request) ? parameterToken : null;
		}
	}

	public void setAllowUriQueryParameter(boolean allowUriQueryParameter) {
		this.allowUriQueryParameter = allowUriQueryParameter;
	}

	private static String resolveFromAuthorizationHeader(HttpHeaders headers) {
		String authorization = headers.getFirst("Authorization");
		if (StringUtils.startsWithIgnoreCase(authorization, AuthConstant.JWT_PREFIX)) {
			Matcher matcher = authorizationPattern.matcher(authorization);
			if (!matcher.matches()) {
				BearerTokenError error = invalidTokenError();
				throw new OAuth2AuthenticationException(error);
			} else {
				return matcher.group("token");
			}
		} else {
			return null;
		}
	}

	private static BearerTokenError invalidTokenError() {
		return new BearerTokenError("invalid_token", HttpStatus.UNAUTHORIZED, AuthConstant.JWT_PREFIX+" token is malformed", "https://tools.ietf.org/html/rfc6750#section-3.1");
	}

	private boolean isParameterTokenSupportedForRequest(ServerHttpRequest request) {
		return this.allowUriQueryParameter && HttpMethod.GET.equals(request.getMethod());
	}
}
