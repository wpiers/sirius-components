/*******************************************************************************
 * Copyright (c) 2023, 2024 Obeo.
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
import {
  ComponentExtension,
  ConfirmationDialogContextProvider,
  DataExtension,
  ExtensionProvider,
  ExtensionRegistry,
  ExtensionRegistryMergeStrategy,
  RepresentationMetadata,
  RepresentationPathContext,
  ServerContext,
  WorkbenchViewContribution,
  representationFactoryExtensionPoint,
  workbenchMainAreaExtensionPoint,
  workbenchViewContributionExtensionPoint,
} from '@eclipse-sirius/sirius-components-core';
import { DeckRepresentation } from '@eclipse-sirius/sirius-components-deck';
import {
  DiagramRepresentation,
  NodeTypeContext,
  NodeTypeContextValue,
} from '@eclipse-sirius/sirius-components-diagrams';
import { FormDescriptionEditorRepresentation } from '@eclipse-sirius/sirius-components-formdescriptioneditors';
import {
  DetailsView,
  FormRepresentation,
  PropertySectionContext,
  RelatedElementsView,
  RepresentationsView,
} from '@eclipse-sirius/sirius-components-forms';
import { GanttRepresentation } from '@eclipse-sirius/sirius-components-gantt';
import { PortalRepresentation } from '@eclipse-sirius/sirius-components-portals';
import { ExplorerView } from '@eclipse-sirius/sirius-components-trees';
import { ValidationView } from '@eclipse-sirius/sirius-components-validation';
import CssBaseline from '@material-ui/core/CssBaseline';
import { Theme, ThemeProvider } from '@material-ui/core/styles';
import AccountTreeIcon from '@material-ui/icons/AccountTree';
import Filter from '@material-ui/icons/Filter';
import LinkIcon from '@material-ui/icons/Link';
import MenuIcon from '@material-ui/icons/Menu';
import WarningIcon from '@material-ui/icons/Warning';
import React from 'react';
import { BrowserRouter } from 'react-router-dom';
import { ToastProvider } from '../../src/toast/ToastProvider';
import {
  DiagramRepresentationConfiguration,
  defaultNodeTypeRegistry,
} from '../diagrams/DiagramRepresentationConfiguration';
import { DiagramRepresentationConfigurationProps } from '../diagrams/DiagramRepresentationConfiguration.types';
import { propertySectionsRegistry } from '../forms/defaultPropertySectionRegistry';
import { ApolloGraphQLProvider } from '../graphql/ApolloGraphQLProvider';
import { OnboardArea } from '../onboarding/OnboardArea';
import { Router } from '../router/Router';
import { siriusWebTheme as defaultTheme } from '../theme/siriusWebTheme';
import { createProjectAreaCardExtensionPoint } from '../views/project-browser/create-projects-area/CreateProjectAreaExtensionPoints';
import {
  NewProjectCard,
  ShowAllTemplatesCard,
  UploadProjectCard,
} from '../views/project-browser/create-projects-area/ProjectTemplateCard';
import {
  ProjectSettingTabContribution,
  projectSettingsTabExtensionPoint,
} from '../views/project-settings/ProjectSettingsViewExtensionPoints';
import { ProjectImagesSettings } from '../views/project-settings/images/ProjectImagesSettings';
import { SiriusWebApplicationProps } from './SiriusWebApplication.types';

const style = {
  display: 'grid',
  gridTemplateColumns: '1fr',
  gridTemplateRows: '1fr',
  minHeight: '100vh',
};

class DefaultExtensionRegistryMergeStrategy implements ExtensionRegistryMergeStrategy {
  public mergeComponentExtensions(
    _: string,
    existingValues: ComponentExtension<any>[],
    newValues: ComponentExtension<any>[]
  ): ComponentExtension<any>[] {
    return [...existingValues, ...newValues];
  }
  public mergeDataExtensions(_1: string, _2: DataExtension<any>, newValue: DataExtension<any>): DataExtension<any> {
    return newValue;
  }
}

export const SiriusWebApplication = ({
  httpOrigin,
  wsOrigin,
  extensionRegistry,
  extensionRegistryMergeStrategy,
  theme,
  children,
}: SiriusWebApplicationProps) => {
  const siriusWebTheme: Theme = theme ? theme : defaultTheme;

  let nodeTypeRegistryValue: NodeTypeContextValue = { ...defaultNodeTypeRegistry };
  React.Children.forEach(children, (child) => {
    if (React.isValidElement(child) && child.type === DiagramRepresentationConfiguration) {
      const { nodeTypeRegistry } = child.props as DiagramRepresentationConfigurationProps;
      if (nodeTypeRegistry) {
        nodeTypeRegistryValue = nodeTypeRegistry;
      }
    }
  });

  const getRepresentationPath = (editingContextId: string, representationId: string) => {
    // Note that this should match the corresponding route configuration
    return `/projects/${editingContextId}/edit/${representationId}`;
  };

  const workbenchViewContributions: WorkbenchViewContribution[] = [
    {
      side: 'left',
      title: 'Explorer',
      icon: <AccountTreeIcon />,
      component: ExplorerView,
    },
    {
      side: 'left',
      title: 'Validation',
      icon: <WarningIcon />,
      component: ValidationView,
    },
    {
      side: 'right',
      title: 'Details',
      icon: <MenuIcon />,
      component: DetailsView,
    },
    {
      side: 'right',
      title: 'Representations',
      icon: <Filter />,
      component: RepresentationsView,
    },
    {
      side: 'right',
      title: 'Related Elements',
      icon: <LinkIcon />,
      component: RelatedElementsView,
    },
  ];

  const projectSettingsTabContributions: ProjectSettingTabContribution[] = [
    {
      title: 'Images',
      component: ProjectImagesSettings,
    },
  ];

  const internalExtensionRegistry = new ExtensionRegistry();
  internalExtensionRegistry.addComponent(workbenchMainAreaExtensionPoint, {
    identifier: 'sw_onboard',
    Component: OnboardArea,
  });

  internalExtensionRegistry.addComponent(createProjectAreaCardExtensionPoint, {
    identifier: 'sw_createProjectAreaCard_new',
    Component: NewProjectCard,
  });
  internalExtensionRegistry.addComponent(createProjectAreaCardExtensionPoint, {
    identifier: 'sw_createProjectAreaCard_upload',
    Component: UploadProjectCard,
  });
  internalExtensionRegistry.addComponent(createProjectAreaCardExtensionPoint, {
    identifier: 'sw_createProjectAreaCard_templates',
    Component: ShowAllTemplatesCard,
  });

  internalExtensionRegistry.putData(workbenchViewContributionExtensionPoint, {
    identifier: 'sw_workbenchView',
    data: workbenchViewContributions,
  });

  internalExtensionRegistry.putData(projectSettingsTabExtensionPoint, {
    identifier: 'sw_projectSettingsTab',
    data: projectSettingsTabContributions,
  });

  const getType = (representation: RepresentationMetadata): string => {
    const query = representation.kind.substring(representation.kind.indexOf('?') + 1, representation.kind.length);
    const params = new URLSearchParams(query);
    const type = params.get('type');
    return type;
  };

  internalExtensionRegistry.putData(representationFactoryExtensionPoint, {
    identifier: 'sw_repFactory_diagram',
    data: [(representation) => (getType(representation) === 'Diagram' ? DiagramRepresentation : null)],
  });
  internalExtensionRegistry.putData(representationFactoryExtensionPoint, {
    identifier: 'sw_repFactory_form',
    data: [(representation) => (getType(representation) === 'Form' ? FormRepresentation : null)],
  });
  internalExtensionRegistry.putData(representationFactoryExtensionPoint, {
    identifier: 'sw_repFactory_formdesceditor',
    data: [
      (representation) =>
        getType(representation) === 'FormDescriptionEditor' ? FormDescriptionEditorRepresentation : null,
    ],
  });
  internalExtensionRegistry.putData(representationFactoryExtensionPoint, {
    identifier: 'sw_repFactory_gantt',
    data: [(representation) => (getType(representation) === 'Gantt' ? GanttRepresentation : null)],
  });
  internalExtensionRegistry.putData(representationFactoryExtensionPoint, {
    identifier: 'sw_repFactory_deck',
    data: [(representation) => (getType(representation) === 'Deck' ? DeckRepresentation : null)],
  });
  internalExtensionRegistry.putData(representationFactoryExtensionPoint, {
    identifier: 'sw_repFactory_portal',
    data: [(representation) => (getType(representation) === 'Portal' ? PortalRepresentation : null)],
  });

  if (extensionRegistry) {
    internalExtensionRegistry.addAll(
      extensionRegistry,
      extensionRegistryMergeStrategy ? extensionRegistryMergeStrategy : new DefaultExtensionRegistryMergeStrategy()
    );
  }

  return (
    <ExtensionProvider registry={internalExtensionRegistry}>
      <ApolloGraphQLProvider httpOrigin={httpOrigin} wsOrigin={wsOrigin}>
        <BrowserRouter>
          <ThemeProvider theme={siriusWebTheme}>
            <CssBaseline />
            <ServerContext.Provider value={{ httpOrigin }}>
              <RepresentationPathContext.Provider value={{ getRepresentationPath }}>
                <ToastProvider>
                  <ConfirmationDialogContextProvider>
                    <NodeTypeContext.Provider value={nodeTypeRegistryValue}>
                      <PropertySectionContext.Provider value={{ propertySectionsRegistry }}>
                        <div style={style}>
                          <Router />
                        </div>
                      </PropertySectionContext.Provider>
                    </NodeTypeContext.Provider>
                  </ConfirmationDialogContextProvider>
                </ToastProvider>
              </RepresentationPathContext.Provider>
            </ServerContext.Provider>
          </ThemeProvider>
        </BrowserRouter>
      </ApolloGraphQLProvider>
    </ExtensionProvider>
  );
};
