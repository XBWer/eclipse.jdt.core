/*******************************************************************************
 * Copyright (c) 2019 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * This is an implementation of an early-draft specification developed under the Java
 * Community Process (JCP) and is made available for testing and evaluation purposes
 * only. The code is not compatible with any specification of the JCP.
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jdt.core.dom;

import java.util.ArrayList;
import java.util.List;

/**
 * Yield statement AST node type.
 *
 * <pre>
 * YieldStatement:
 *		<b>Yield</b> <b>{ Identifier/Expression }</b>
 * </pre>
 *
 * @since 3.18 BETA_JAVA13
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @noreference This class is not intended to be referenced by clients as it is a part of Java preview feature.
 */
@SuppressWarnings("rawtypes")
public class YieldStatement extends Statement {

	/**
	 * The "expression" structural property of this node type (child type: {@link Expression}). (added in JEP 354).
	 */
	public static final ChildPropertyDescriptor EXPRESSION_PROPERTY =
			new ChildPropertyDescriptor(YieldStatement.class, "expression", Expression.class, MANDATORY, NO_CYCLE_RISK); //$NON-NLS-1$);

	/**
	 * A list of property descriptors (element type:
	 * {@link StructuralPropertyDescriptor}),
	 * or null if uninitialized.
	 */
	private static final List PROPERTY_DESCRIPTORS;

	/**
	 * <code>true</code> indicates implicit and <code>false</code> indicates not implicit.
	 */
	private boolean isImplicit = false;
	
	static {
		List properyList = new ArrayList(2);
		createPropertyList(YieldStatement.class, properyList);
		addProperty(EXPRESSION_PROPERTY, properyList);
		PROPERTY_DESCRIPTORS = reapPropertyList(properyList);
	}

	/**
	 * Returns a list of structural property descriptors for this node type.
	 * Clients must not modify the result.
	 *
	 * @param apiLevel the API level; one of the
	 * <code>AST.JLS*</code> constants

	 * @return a list of property descriptors (element type:
	 * {@link StructuralPropertyDescriptor})
	 */
	public static List propertyDescriptors(int apiLevel) {
		if (apiLevel >= AST.JLS13_INTERNAL) {
			return PROPERTY_DESCRIPTORS;
		}
		return PROPERTY_DESCRIPTORS;
	}

	
	/**
	 * The expression
	 */
	private Expression expression = null;

	/**
	 * Creates a new unparented Yield statement node owned by the given
	 * AST. By default, the Yield statement has identifier/expression.
	 * <p>
	 * N.B. This constructor is package-private.
	 * </p>
	 *
	 * @param ast the AST that is to own this node
	 * @exception UnsupportedOperationException if this operation is used other than JLS13
	 */
	YieldStatement(AST ast) {
		super(ast);
		supportedOnlyIn13();
	}

	@Override
	final List internalStructuralPropertiesForType(int apiLevel) {
		return propertyDescriptors(apiLevel);
	}

	@Override
	final ASTNode internalGetSetChildProperty(ChildPropertyDescriptor property, boolean get, ASTNode child) {
		if (property == EXPRESSION_PROPERTY) {
			if (get) {
				return getExpression();
			} else {
				setExpression((Expression) child);
				return null;
			}
		}
		// allow default implementation to flag the error
		return super.internalGetSetChildProperty(property, get, child);
	}

	@Override
	final int getNodeType0() {
		return YIELD_STATEMENT;
	}

	@Override
	ASTNode clone0(AST target) {
		YieldStatement result = new YieldStatement(target);
		result.setSourceRange(getStartPosition(), getLength());
		result.copyLeadingComment(this);
		if (this.ast.apiLevel >= AST.JLS12_INTERNAL) {
			result.setExpression((Expression) ASTNode.copySubtree(target, getExpression()));
		}
		return result;
	}

	@Override
	final boolean subtreeMatch0(ASTMatcher matcher, Object other) {
		// dispatch to correct overloaded match method
		return matcher.match(this, other);
	}

	@Override
	void accept0(ASTVisitor visitor) {
		boolean visitChildren = visitor.visit(this);
		if (visitChildren) {
			if (this.ast.apiLevel >= AST.JLS13_INTERNAL) {
				acceptChild(visitor, getExpression());
			} 
		}
		visitor.endVisit(this);
	}
	
	/**
	 * Returns the expression of this Yield statement, or <code>null</code> if
	 * there is none.
	 *
	 * @return the expression, or <code>null</code> if there is none
	 * @exception UnsupportedOperationException if this operation is used other than JLS13
	 * @noreference This method is not intended to be referenced by clients as it is a part of Java preview feature.
	 * @nooverride This method is not intended to be re-implemented or extended by clients as it is a part of Java preview feature.
	 */
	public Expression getExpression() {
		supportedOnlyIn13();
		return this.expression;
	}

	/**
	 * Sets or clears the expression of this Yield statement.
	 *
	 * @param expression the expression
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * </ul>
	 * @exception UnsupportedOperationException if this operation is used other than JLS13
	 * @noreference This method is not intended to be referenced by clients as it is a part of Java preview feature.
	 * @nooverride This method is not intended to be re-implemented or extended by clients as it is a part of Java preview feature.
	 */
	public void setExpression(Expression expression) {
		supportedOnlyIn13();
		ASTNode oldChild = this.expression;
		preReplaceChild(oldChild, expression, EXPRESSION_PROPERTY);
		this.expression = expression;
		postReplaceChild(oldChild, expression, EXPRESSION_PROPERTY);
	}

	/**
	 * Gets the isImplicit of this break statement as <code>true</code> or <code>false</code>.
	 *<code>true</code> indicates implicit and <code>false</code> indicates not implicit.
	 *
	 * @return isImplicit <code>true</code> or <code>false</code>
	 * @exception UnsupportedOperationException if this operation is used other than JLS13
	 * @noreference This method is not intended to be referenced by clients as it is a part of Java preview feature.
	 * @nooverride This method is not intended to be re-implemented or extended by clients as it is a part of Java preview feature.
	 */
	public boolean isImplicit() {
		supportedOnlyIn13();
		return this.isImplicit;
	}

	/**
	 * Sets the isImplicit of this break statement as <code>true</code> or <code>false</code>.
	 * <code>true</code> indicates implicit and <code>false</code> indicates not implicit. This flag is
	 * generated by compiler and is not expected to be set by client.

	 * @param isImplicit <code>true</code> or <code>false</code>
	 * @exception UnsupportedOperationException if this operation is used other than JLS13
	 */
	void setImplicit(boolean isImplicit) {
		supportedOnlyIn13();
		this.isImplicit = isImplicit;
	}

	
	@Override
	int memSize() {
		return super.memSize() + 2 * 4;
	}

	@Override
	int treeSize() {
		return
			memSize()
			+ (this.expression == null ? 0 : getExpression().treeSize());
	}
}
