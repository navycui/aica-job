package aicluster.framework.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import aicluster.framework.common.service.LogService;
import aicluster.framework.config.EnvConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Component
public class ElapsedTimeFilter implements Filter {
	@Autowired
	private EnvConfig config;

	@Autowired
	private LogService service;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String uri = req.getRequestURI();
		log.info("##### filter - before #####");
		long btime = System.currentTimeMillis();
		chain.doFilter(req, res);
		long time = System.currentTimeMillis() - btime;

		log.info("##### filter - after #####: " + (time));
		service.addElapsedTime(config.getSystemId(), uri, time);

		service.addDayMember();
	}
}
