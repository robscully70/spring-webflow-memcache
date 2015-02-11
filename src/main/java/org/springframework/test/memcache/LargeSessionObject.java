package org.springframework.test.memcache;

import java.util.Random;

import org.apache.commons.lang.math.RandomUtils;

public class LargeSessionObject {
	Random random = new Random();

	private byte[] randomData;

	public LargeSessionObject(int sizeInBytes) {
		randomData = new byte[sizeInBytes];
		random.nextBytes(randomData);
	}

	public void mutate(int sizeToChange) {
		byte[] newRandomData = new byte[sizeToChange];
		random.nextBytes(newRandomData);
		for (byte nom_nom:newRandomData){
			int indexToChange = RandomUtils.nextInt(random, randomData.length);
			randomData[indexToChange] = nom_nom;
		}
	}

	public void mutateAll() {
		random.nextBytes(randomData);
	}
}
