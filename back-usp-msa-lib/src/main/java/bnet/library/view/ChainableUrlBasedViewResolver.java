package bnet.library.view;

import java.io.InputStream;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

public class ChainableUrlBasedViewResolver extends UrlBasedViewResolver {

    @Override
    protected AbstractUrlBasedView buildView(String viewName) throws Exception {
        String url = getPrefix() + viewName + getSuffix();
        InputStream stream = getServletContext().getResourceAsStream(url);
        if (stream == null) {
            return new NonExistentView();
        }
        return super.buildView(viewName);
    }

    private static class NonExistentView extends AbstractUrlBasedView {
        @Override
        protected boolean isUrlRequired() {
            return false;
        }

        @Override
        public boolean checkResource(Locale locale) throws Exception {
            return false;
        }

        @Override
        protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
            // empty. it should never get called
        }
    }
}
