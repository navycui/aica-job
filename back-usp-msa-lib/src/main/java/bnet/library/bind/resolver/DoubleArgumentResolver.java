package bnet.library.bind.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import bnet.library.util.CoreUtils.string;

public class DoubleArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter param) {
        return Double.class.isAssignableFrom(param.getParameterType());
    }

    @Override
    public Object resolveArgument(
            MethodParameter param,
            ModelAndViewContainer mvc,
            NativeWebRequest request,
            WebDataBinderFactory binder) throws Exception {

        String text = request.getParameter(param.getParameterName());
        return string.toDouble(text) ;
    }

}
