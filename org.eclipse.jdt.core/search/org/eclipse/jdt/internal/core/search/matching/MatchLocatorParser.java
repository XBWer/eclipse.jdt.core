/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core.search.matching;

import org.eclipse.jdt.internal.compiler.AbstractSyntaxTreeVisitorAdapter;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.parser.Parser;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;

/**
 * A parser that locates ast nodes that match a given search pattern.
 */
public class MatchLocatorParser extends Parser {

public MatchingNodeSet matchSet;
private AbstractSyntaxTreeVisitorAdapter localDeclarationVisitor;
private int matchContainer;

/**
 * An ast visitor that visits local type declarations.
 */
public class NoClassNoMethodDeclarationVisitor extends AbstractSyntaxTreeVisitorAdapter {
	public boolean visit(ConstructorDeclaration constructorDeclaration, ClassScope scope) {
		return (constructorDeclaration.bits & AstNode.HasLocalTypeMASK) != 0; // continue only if it has local type
	}
	public boolean visit(FieldDeclaration fieldDeclaration, MethodScope scope) {
		return (fieldDeclaration.bits & AstNode.HasLocalTypeMASK) != 0; // continue only if it has local type;
	}
	public boolean visit(Initializer initializer, MethodScope scope) {
		return (initializer.bits & AstNode.HasLocalTypeMASK) != 0; // continue only if it has local type
	}
	public boolean visit(MethodDeclaration methodDeclaration, ClassScope scope) {
		return (methodDeclaration.bits & AstNode.HasLocalTypeMASK) != 0; // continue only if it has local type
	}	
}
public class MethodButNoClassDeclarationVisitor extends NoClassNoMethodDeclarationVisitor {
	public boolean visit(AnonymousLocalTypeDeclaration anonymousTypeDeclaration, BlockScope scope) {
		matchSet.checkMatching(anonymousTypeDeclaration);
		return true; 
	}
	public boolean visit(LocalTypeDeclaration localTypeDeclaration, BlockScope scope) {
		matchSet.checkMatching(localTypeDeclaration);
		return true;
	}
}
public class ClassButNoMethodDeclarationVisitor extends AbstractSyntaxTreeVisitorAdapter {
	public boolean visit(ConstructorDeclaration constructorDeclaration, ClassScope scope) {
		matchSet.checkMatching(constructorDeclaration);
		return (constructorDeclaration.bits & AstNode.HasLocalTypeMASK) != 0; // continue only if it has local type
	}
	public boolean visit(FieldDeclaration fieldDeclaration, MethodScope scope) {
		matchSet.checkMatching(fieldDeclaration);
		return (fieldDeclaration.bits & AstNode.HasLocalTypeMASK) != 0; // continue only if it has local type;
	}
	public boolean visit(Initializer initializer, MethodScope scope) {
		matchSet.checkMatching(initializer);
		return (initializer.bits & AstNode.HasLocalTypeMASK) != 0; // continue only if it has local type
	}
	public boolean visit(MemberTypeDeclaration memberTypeDeclaration, ClassScope scope) {
		matchSet.checkMatching(memberTypeDeclaration);
		return true;
	}
	public boolean visit(MethodDeclaration methodDeclaration, ClassScope scope) {
		matchSet.checkMatching(methodDeclaration);
		return (methodDeclaration.bits & AstNode.HasLocalTypeMASK) != 0; // continue only if it has local type
	}	
}
public class ClassAndMethodDeclarationVisitor extends ClassButNoMethodDeclarationVisitor {
	public boolean visit(AnonymousLocalTypeDeclaration anonymousTypeDeclaration, BlockScope scope) {
		matchSet.checkMatching(anonymousTypeDeclaration);
		return true; 
	}
	public boolean visit(LocalTypeDeclaration localTypeDeclaration, BlockScope scope) {
		matchSet.checkMatching(localTypeDeclaration);
		return true;
	}
}

public MatchLocatorParser(ProblemReporter problemReporter) {
	super(problemReporter, true);
}
protected void classInstanceCreation(boolean alwaysQualified) {
	super.classInstanceCreation(alwaysQualified);
	this.matchSet.checkMatching(this.expressionStack[this.expressionPtr]);
}
protected void consumeAssignment() {
	super.consumeAssignment();
	this.matchSet.checkMatching(this.expressionStack[this.expressionPtr]);
}
protected void consumeExplicitConstructorInvocation(int flag, int recFlag) {
	super.consumeExplicitConstructorInvocation(flag, recFlag);
	this.matchSet.checkMatching(this.astStack[this.astPtr]);
}
protected void consumeFieldAccess(boolean isSuperAccess) {
	super.consumeFieldAccess(isSuperAccess);
	this.matchSet.checkMatching(this.expressionStack[this.expressionPtr]);
}
protected void consumeMethodInvocationName() {
	super.consumeMethodInvocationName();
	this.matchSet.checkMatching(this.expressionStack[this.expressionPtr]);
}
protected void consumeMethodInvocationPrimary() {
	super.consumeMethodInvocationPrimary();
	this.matchSet.checkMatching(this.expressionStack[this.expressionPtr]);
}
protected void consumeMethodInvocationSuper() {
	super.consumeMethodInvocationSuper();
	this.matchSet.checkMatching(this.expressionStack[this.expressionPtr]);
}
protected void consumePrimaryNoNewArray() {
	// pop parenthesis positions (and don't update expression positions
	// (see http://bugs.eclipse.org/bugs/show_bug.cgi?id=23329)
	intPtr--;
	intPtr--;
}
protected void consumeSingleTypeImportDeclarationName() {
	super.consumeSingleTypeImportDeclarationName();
	this.matchSet.checkMatching(this.astStack[this.astPtr]);
}
protected void consumeTypeImportOnDemandDeclarationName() {
	super.consumeTypeImportOnDemandDeclarationName();
	this.matchSet.checkMatching(this.astStack[this.astPtr]);
}
protected void consumeUnaryExpression(int op, boolean post) {
	super.consumeUnaryExpression(op, post);
	this.matchSet.checkMatching(this.expressionStack[this.expressionPtr]);
}
protected TypeReference copyDims(TypeReference typeRef, int dim) {
	TypeReference result = super.copyDims(typeRef, dim);
	 if (this.matchSet.removePossibleMatch(typeRef) != null)
		this.matchSet.addPossibleMatch(result);
	 else if (this.matchSet.removeTrustedMatch(typeRef) != null)
		this.matchSet.addTrustedMatch(result);
	return result;
}
protected TypeReference getTypeReference(int dim) {
	TypeReference typeRef = super.getTypeReference(dim);
	this.matchSet.checkMatching(typeRef); // NB: Don't check container since type reference can happen anywhere
	return typeRef;
}
protected NameReference getUnspecifiedReference() {
	NameReference nameRef = super.getUnspecifiedReference();
	this.matchSet.checkMatching(nameRef); // NB: Don't check container since unspecified reference can happen anywhere
	return nameRef;
}
protected NameReference getUnspecifiedReferenceOptimized() {
	NameReference nameRef = super.getUnspecifiedReferenceOptimized();
	this.matchSet.checkMatching(nameRef); // NB: Don't check container since unspecified reference can happen anywhere
	return nameRef;
}
/**
 * Parses the method bodies in the given compilation unit
 */
public void parseBodies(CompilationUnitDeclaration unit) {
	TypeDeclaration[] types = unit.types;
	if (types == null) return;

	if (this.matchContainer != this.matchSet.matchContainer || this.localDeclarationVisitor == null) {
		this.matchContainer = this.matchSet.matchContainer;
		if ((this.matchContainer & SearchPattern.CLASS) != 0) {
			this.localDeclarationVisitor = (this.matchContainer & SearchPattern.METHOD) != 0
				? new ClassAndMethodDeclarationVisitor()
				: new ClassButNoMethodDeclarationVisitor();
		} else {
			this.localDeclarationVisitor = (this.matchContainer & SearchPattern.METHOD) != 0
				? new MethodButNoClassDeclarationVisitor()
				: new NoClassNoMethodDeclarationVisitor();
		}
	}

	for (int i = 0; i < types.length; i++) {
		TypeDeclaration type = types[i];
		this.matchSet.checkMatching(type);
		this.parseBodies(type, unit);
	}
}
/**
 * Parses the member bodies in the given type.
 */
protected void parseBodies(TypeDeclaration type, CompilationUnitDeclaration unit) {
	FieldDeclaration[] fields = type.fields;
	if (fields != null) {
		for (int i = 0; i < fields.length; i++) {
			FieldDeclaration field = fields[i];
			if (field instanceof Initializer)
				this.parse((Initializer) field, type, unit);
			field.traverse(localDeclarationVisitor, null);
		}
	}
	
	AbstractMethodDeclaration[] methods = type.methods;
	if (methods != null) {
		for (int i = 0; i < methods.length; i++) {
			AbstractMethodDeclaration method = methods[i];
			if (method.sourceStart >= type.bodyStart) { // if not synthetic
				if (method instanceof MethodDeclaration) {
					MethodDeclaration methodDeclaration = (MethodDeclaration) method;
					this.parse(methodDeclaration, unit);
					methodDeclaration.traverse(localDeclarationVisitor, (ClassScope) null);
				} else if (method instanceof ConstructorDeclaration) {
					ConstructorDeclaration constructorDeclaration = (ConstructorDeclaration) method;
					this.parse(constructorDeclaration, unit);
					constructorDeclaration.traverse(localDeclarationVisitor, (ClassScope) null);
				}
			} else if (method.isDefaultConstructor()) {
				method.parseStatements(this, unit);
			}
		}
	}

	MemberTypeDeclaration[] memberTypes = type.memberTypes;
	if (memberTypes != null) {
		for (int i = 0; i < memberTypes.length; i++) {
			MemberTypeDeclaration memberType = memberTypes[i];
			this.parseBodies(memberType, unit);
			memberType.traverse(localDeclarationVisitor, (ClassScope) null);
		}
	}
}
}
