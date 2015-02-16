package org.springframework.test.memcache.ajax;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/session-edit", method = RequestMethod.GET)
public class SessionGrowController {

	/**
	 * @param request
	 * @param sessionMap
	 *            key for map in session, if not found an empty map will be
	 *            created.
	 * @return
	 */
	@RequestMapping(value = "/grow-map", method = RequestMethod.GET)
	public @ResponseBody String growMap(HttpServletRequest request, @RequestParam(required = true) String sessionMap, @RequestParam(required = true) String entryKey) {
		if ("-----".equals(entryKey)) {
			return "ERROR_NO_LAST";
		}
		return entryKey + '-';
	}

	@SuppressWarnings("unchecked")
	private Map<Integer, Integer> getSessionMap(HttpServletRequest request, String key) {
		Object sessionMap = request.getSession().getAttribute(key);
		if (sessionMap == null || !(sessionMap instanceof Map)) {
			sessionMap = new HashMap<Integer, Integer>();
			request.getSession().setAttribute(key, sessionMap);
		}
		return (Map<Integer, Integer>) sessionMap;
	}
}
