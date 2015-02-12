package org.springframework.test.memcache.webflow;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.test.memcache.LargeSessionObject;
import org.springframework.test.memcache.MapManipulator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.webflow.execution.repository.FlowExecutionRestorationFailureException;
import org.springframework.webflow.executor.FlowExecutor;
import org.springframework.webflow.mvc.servlet.FlowController;

@Controller
public class SimpleWebFlowController extends FlowController {

	private static final String SESSION_LARGE_OBJECT = "session.large.object";
	private static final String SESSION_DEBUG = "session.debug";
	private static final String SESSION_MAP = "session.map";
	Logger logger = Logger.getLogger(this.getClass());
	Random random = new Random();

	@RequestMapping("/flow/*Flow")
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false) String debug,
			@RequestParam(required = false) Integer size,
			@RequestParam(required = false) Boolean webflowChangesOnly) {
		if (BooleanUtils.isFalse(webflowChangesOnly)) {
			logger.info("going directly to the flow");
			return handleRequest(request, response);
		}
		if (!StringUtils.isBlank(debug)) {
			logger.info("debug parameter found, resetting session attributes");
			request.getSession().setAttribute(SESSION_DEBUG, debug);
			request.getSession().removeAttribute(SESSION_MAP);
			request.getSession().removeAttribute(SESSION_LARGE_OBJECT);
		}
		String sessionDebug = (String) request.getSession().getAttribute(SESSION_DEBUG);
		sessionDebug = StringUtils.trimToEmpty(sessionDebug);
		logger.info("sessionDebug is:" + sessionDebug);

		Map<Integer, Integer> sessionMap = getSessionMap(request);
		switch (sessionDebug) {
		case "static-nonchanging":
			MapManipulator.fillMapToSize(sessionMap, 350);
			break;
		case "static-changing":
			MapManipulator.fillMapToSize(sessionMap, 350);
			MapManipulator.changeOneEntry(sessionMap);
			break;
		case "large-changing":
			LargeSessionObject largeSessionObject = getLargeSessionObject(request, size);
			largeSessionObject.mutateAll();
			break;
		case "webflow":
			// all action in the webflow
			break;
		case "growing":
		default:
			MapManipulator.grow(sessionMap);
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

	@SuppressWarnings("unchecked")
	private Map<Integer, Integer> getSessionMap(HttpServletRequest request) {
		Object sessionMap = request.getSession().getAttribute(SESSION_MAP);
		if (sessionMap == null || !(sessionMap instanceof Map)) {
			sessionMap = new HashMap<Integer, Integer>();
			request.getSession().setAttribute(SESSION_MAP, sessionMap);
		}
		return (Map<Integer, Integer>) sessionMap;
	}

	private LargeSessionObject getLargeSessionObject(HttpServletRequest request, Integer size) {
		int sizeToUse = size != null ? size : 16000000;
		if (request.getSession().getAttribute(SESSION_LARGE_OBJECT) == null) {
			LargeSessionObject largeSessionObject = SimpleWebFlowController.createLargeObject(sizeToUse);
			request.getSession().setAttribute(SESSION_LARGE_OBJECT, largeSessionObject);
		}
		return (LargeSessionObject) request.getSession().getAttribute(SESSION_LARGE_OBJECT);
	}

	public static LargeSessionObject createLargeObject(int sizeToUse){
		return new LargeSessionObject(sizeToUse);
	}

	public static void logCount(Integer count) {
		Logger.getLogger(SimpleWebFlowController.class).info(String.format("flowScope.counter is now %s", count));
	}

	@Resource
	@Override
	public void setFlowExecutor(FlowExecutor flowExecutor) {
		super.setFlowExecutor(flowExecutor);
	}

}
