package org.springframework.test.memcache;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class MapManipulator {
	private static Logger logger = Logger.getLogger(MapManipulator.class);
	private static Random random = new Random();

	/**
	 * Adds one more random Integer pair to the map
	 * 
	 * @param map
	 *            must be mutable
	 */
	public static void grow(Map<Integer, Integer> map) {
		map.put(RandomUtils.nextInt(random), RandomUtils.nextInt(random));
		logger.info("Added Integer pair size is now: " + map.size());
	}

	/**
	 * Removes the first entry returned by the iterator and adds one with grow.
	 * 
	 * @param map
	 *            must be mutable
	 */
	public static void changeOneEntry(Map<Integer, Integer> map) {
		Iterator<Entry<Integer, Integer>> iterator = map.entrySet().iterator();
		iterator.next();
		iterator.remove();
		MapManipulator.grow(map);
	}

	/**
	 * Fills a map to be of size by calling grow() an appropriate number of
	 * times.
	 * 
	 * @param map
	 *            must be mutable
	 * @param size
	 */
	public static void fillMapToSize(Map<Integer, Integer> map, int size) {
		if (map.size() < size) {
			int addAmount = size - map.size();
			for (; addAmount > 0; addAmount--) {
				MapManipulator.grow(map);
			}
			logger.info("filled map size is now" + map.size());
		}
	}
}
