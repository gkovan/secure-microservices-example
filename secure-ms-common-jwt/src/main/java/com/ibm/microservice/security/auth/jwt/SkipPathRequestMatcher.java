package com.ibm.microservice.security.auth.jwt;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

public class SkipPathRequestMatcher implements RequestMatcher {
    private RequestMatcher pathsToSkipMmatchers;
    private RequestMatcher processingMatcher;

    public SkipPathRequestMatcher(List<String> pathsToSkip, String processingPath) {
        Assert.notNull(pathsToSkip);
        List<RequestMatcher> m = pathsToSkip.stream().map(path -> new AntPathRequestMatcher(path)).collect(Collectors.toList());
        pathsToSkipMmatchers = new OrRequestMatcher(m);
        processingMatcher = new AntPathRequestMatcher(processingPath);
    }
   
	@Override
	public boolean matches(HttpServletRequest request) {
        if (pathsToSkipMmatchers.matches(request)) {
            return false;
        }
        //TODO: come back
        return processingMatcher.matches(request) ? true : false;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SkipPathRequestMatcher [pathsToSkipMmatchers=");
		builder.append(pathsToSkipMmatchers);
		builder.append(", processingMatcher=");
		builder.append(processingMatcher);
		builder.append("]");
		return builder.toString();
	}

}
