/*******************************************************************************
 * Copyright (c) 2024 Obeo.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.components.papaya.spec;

import org.eclipse.sirius.components.papaya.impl.DataTypeImpl;
import org.eclipse.sirius.components.papaya.spec.derived.TypeIsSetQualifiedNamePredicate;
import org.eclipse.sirius.components.papaya.spec.derived.TypeQualifiedNameProvider;

/**
 * Customization of the data type implementation generated by EMF.
 *
 * @author sbegaudeau
 */
public class DataTypeSpec extends DataTypeImpl {
    @Override
    public String getQualifiedName() {
        return new TypeQualifiedNameProvider().apply(this);
    }

    @Override
    public boolean isSetQualifiedName() {
        return new TypeIsSetQualifiedNamePredicate().test(this);
    }
}
