/*
 * Copyright 2015-2016 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.junit.platform.launcher.core;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.platform.engine.discovery.NameBasedSelectors.selectName;
import static org.junit.platform.engine.discovery.UniqueIdSelector.selectUniqueId;
import static org.junit.platform.launcher.core.TestDiscoveryRequestBuilder.request;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.discovery.ClassSelector;
import org.junit.platform.engine.discovery.MethodSelector;
import org.junit.platform.engine.discovery.PackageSelector;
import org.junit.platform.engine.discovery.UniqueIdSelector;

/**
 * Unit tests for {@link DiscoveryRequest}.
 *
 * @since 5.0
 */
public class DiscoveryRequestTests {

	@Test
	public void selectNameWithClass() {
		DiscoverySelector element = selectName(MyTestClass.class.getName());
		assertEquals(ClassSelector.class, element.getClass());
	}

	@Test
	public void selectNameWithMethod() throws Exception {
		DiscoverySelector element = selectName(fullyQualifiedMethodName());
		assertEquals(MethodSelector.class, element.getClass());
	}

	@Test
	public void selectNameWithPackage() {
		DiscoverySelector element = selectName("org.junit.platform");
		assertEquals(PackageSelector.class, element.getClass());
	}

	@Test
	public void buildDiscoveryRequest() throws Exception {
		// @formatter:off
		EngineDiscoveryRequest spec = request().selectors(
			selectUniqueId(UniqueId.forEngine("fooEngine")),
			selectName(MyTestClass.class.getName()),
			selectName("org.junit.platform"),
			selectName(fullyQualifiedMethodName())
		).build();
		// @formatter:on

		assertNotNull(spec);
		List<Class<? extends DiscoverySelector>> expected = Arrays.asList(UniqueIdSelector.class, ClassSelector.class,
			PackageSelector.class, MethodSelector.class);
		assertEquals(expected, spec.getSelectors().stream().map(Object::getClass).collect(toList()));
	}

	private String fullyQualifiedMethodName() throws Exception {
		return MyTestClass.class.getName() + "#" + MyTestClass.class.getDeclaredMethod("myTest").getName();
	}

	static class MyTestClass {

		void myTest() {
		}
	}

}