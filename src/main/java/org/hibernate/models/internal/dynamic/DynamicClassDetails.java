/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * SPDX-License-Identifier: Apache-2.0
 * Copyright: Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.models.internal.dynamic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.models.internal.ClassDetailsSupport;
import org.hibernate.models.spi.ClassDetails;
import org.hibernate.models.spi.FieldDetails;
import org.hibernate.models.spi.MethodDetails;
import org.hibernate.models.spi.RecordComponentDetails;
import org.hibernate.models.spi.SourceModelBuildingContext;
import org.hibernate.models.spi.TypeDetails;
import org.hibernate.models.spi.TypeVariableDetails;

/**
 * @author Steve Ebersole
 */
public class DynamicClassDetails extends AbstractAnnotationTarget implements ClassDetailsSupport {
	private final String name;
	private final String className;
	private final boolean isAbstract;
	private final ClassDetails superClass;
	private final TypeDetails genericSuperType;

	private List<FieldDetails> fields;
	private List<MethodDetails> methods;

	private Class<?> javaType;

	public DynamicClassDetails(String name, SourceModelBuildingContext buildingContext) {
		this( name, null, null, buildingContext );
	}

	public DynamicClassDetails(
			String name,
			ClassDetails superClass,
			TypeDetails genericSuperType,
			SourceModelBuildingContext buildingContext) {
		this( name, null, false, superClass, genericSuperType, buildingContext );
	}

	public DynamicClassDetails(
			String name,
			String className,
			boolean isAbstract,
			ClassDetails superClass,
			TypeDetails genericSuperType,
			SourceModelBuildingContext buildingContext) {
		super( buildingContext );
		this.name = name;
		this.className = className;
		this.isAbstract = isAbstract;
		this.superClass = superClass;
		this.genericSuperType = genericSuperType;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getClassName() {
		return className;
	}

	@Override
	public boolean isResolved() {
		return false;
	}

	@Override
	public boolean isAbstract() {
		return isAbstract;
	}

	@Override
	public boolean isRecord() {
		return false;
	}

	@Override
	public ClassDetails getSuperClass() {
		return superClass;
	}

	@Override
	public TypeDetails getGenericSuperType() {
		return genericSuperType;
	}

	@Override
	public List<TypeDetails> getImplementedInterfaces() {
		// todo : do we need these for dynamic classes?
		return null;
	}

	@Override
	public List<TypeVariableDetails> getTypeParameters() {
		return Collections.emptyList();
	}

	@Override
	public boolean isImplementor(Class<?> checkType) {
		return checkType.isAssignableFrom( javaType );
	}

	@Override
	public List<FieldDetails> getFields() {
		if ( fields == null ) {
			return Collections.emptyList();
		}

		return fields;
	}

	@Override
	public void addField(FieldDetails fieldDetails) {
		if ( fields == null ) {
			this.fields = new ArrayList<>();
		}
		this.fields.add( fieldDetails );
	}

	@Override
	public List<MethodDetails> getMethods() {
		if ( methods == null ) {
			return Collections.emptyList();
		}

		return methods;
	}

	@Override
	public List<RecordComponentDetails> getRecordComponents() {
		return Collections.emptyList();
	}

	public void addMethod(MethodDetails methodDetails) {
		if ( methods == null ) {
			this.methods = new ArrayList<>();
		}
		this.methods.add( methodDetails );
	}

	@Override
	public <X> Class<X> toJavaClass() {
		if ( javaType == null ) {
			if ( className != null ) {
				javaType = getBuildingContext().getClassLoading().classForName( className );
			}
		}
		//noinspection unchecked
		return (Class<X>) javaType;
	}

	@Override
	public String toString() {
		return "DynamicClassDetails(" + name + " (" + className + "))";
	}
}
