/*******************************************************************************
 * Copyright (c) 2015 Google, Inc and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Stefan Xenos (Google) - Initial implementation
 *******************************************************************************/ 
package org.eclipse.jdt.internal.core.pdom;

/**
 * Common, but internal methods for all pdom nodes.
 * @since 3.12
 */
public interface IInternalPDOMNode extends IPDOMNode {
	public long getRecord();
}