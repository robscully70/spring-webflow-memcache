package org.springframework.samples.mvc.webflow;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.context.Theme;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.webflow.execution.repository.FlowExecutionRestorationFailureException;
import org.springframework.webflow.executor.FlowExecutor;
import org.springframework.webflow.mvc.servlet.FlowController;

@Controller
public class SimpleWebFlowController extends FlowController {

	private static final String SESSION_DEBUG = "session.debug";
	private static final String SESSION_MAP = "session.map";
	Logger logger = Logger.getLogger(this.getClass());
	Random random = new Random();

	@RequestMapping("/flow/*Flow")
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false) String debug) {
		if (!StringUtils.isBlank(debug)) {
			request.getSession().setAttribute(SESSION_DEBUG, debug);
			request.getSession().removeAttribute(SESSION_MAP);
		}
		String sessionDebug = (String) request.getSession().getAttribute(SESSION_DEBUG);
		sessionDebug = StringUtils.trimToEmpty(sessionDebug);
		logger.info("sessionDebug is:" + sessionDebug);

		Map<Integer, Integer> sessionMap = getSessionMap(request);
		switch (sessionDebug) {
		case "static-nonchanging":
			fillMapToSize(sessionMap, 350);
			break;
		case "static-changing":
			fillMapToSize(sessionMap, 350);
			changeOneEntry(sessionMap);
			break;
		case "growing":
		default:
			grow(sessionMap);
			break;
		}
		logger.info("about to look up the flow sessionMap size is: " + sessionMap.size());
		return handleRequest(request, response);
	}

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
		try {
			return super.handleRequest(request, response);
		} catch (FlowExecutionRestorationFailureException e) {
			// TODO: print details about map
			// Map<Integer, Integer> sessionMap = getSessionMap(request);
			logger.error("Flow Restoration issue.", e);
		} catch (Exception e) {
			logger.error("General Trouble handling flow.", e);
		}
		return null;
	}

	private void changeOneEntry(Map<Integer, Integer> sessionMap) {
		Iterator<Entry<Integer, Integer>> iterator = sessionMap.entrySet().iterator();
		iterator.next();
		iterator.remove();
		grow(sessionMap);
	}

	private void fillMapToSize(Map<Integer, Integer> map, int size) {
		if (map.size() < size) {
			int addAmount = size - map.size();
			for (; addAmount > 0; addAmount--) {
				grow(map);
			}
			logger.info("filled map size is now" + map.size());
		}
	}

	private void grow(Map<Integer, Integer> sessionMap) {
		sessionMap.put(RandomUtils.nextInt(random), RandomUtils.nextInt(random));
		logger.info("Added Integer pair size is now: " + sessionMap.size());
	}

	@SuppressWarnings("unchecked")
	private Map<Integer, Integer> getSessionMap(HttpServletRequest request) {
		Object sessionMap = request.getSession().getAttribute(SESSION_MAP);
		if (sessionMap == null || !(sessionMap instanceof Map)) {
			sessionMap = new HashMap<Integer, Integer>();
			request.getSession().setAttribute(SESSION_MAP, sessionMap);
		}
		return (Map<Integer, Integer>) sessionMap;
	}

	@Resource
	@Override
	public void setFlowExecutor(FlowExecutor flowExecutor) {
		super.setFlowExecutor(flowExecutor);
	}

	public static void logCount(Integer count) {
		Logger.getLogger(SimpleWebFlowController.class).info(String.format("flowScope.counter is now %s", count));
	}
}
