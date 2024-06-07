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

import { useComponents } from '@eclipse-sirius/sirius-components-core';
import IconButton from '@material-ui/core/IconButton';
import { makeStyles } from '@material-ui/core/styles';
import { SwapHoriz as SwapHorizIcon } from '@material-ui/icons';
import { TreeFiltersMenu } from '../views/TreeFiltersMenu';
import { TreeToolBarProps } from './TreeToolBar.types';
import { treeToolbarContributionExtensionPoint } from './TreeToolbarExtensionPoints';

const useTreeToolbarStyles = makeStyles((theme) => ({
  toolbar: {
    display: 'flex',
    flexDirection: 'row',
    overflow: 'hidden',
    height: theme.spacing(4),
    paddingLeft: theme.spacing(1),
    paddingRight: theme.spacing(1),
    borderBottomWidth: '1px',
    borderBottomStyle: 'solid',
    justifyContent: 'right',
    borderBottomColor: theme.palette.divider,
  },
}));

export const TreeToolBar = ({
  editingContextId,
  treeId,
  onSynchronizedClick,
  synchronized,
  treeFilters,
  onTreeFilterMenuItemClick,
  readOnly,
}: TreeToolBarProps) => {
  const classes = useTreeToolbarStyles();

  let treeFiltersMenu: JSX.Element;
  if (treeFilters.length > 0) {
    treeFiltersMenu = <TreeFiltersMenu filters={treeFilters} onTreeFilterMenuItemClick={onTreeFilterMenuItemClick} />;
  }

  const preferenceButtonSynchronizeTitle = synchronized
    ? 'Disable synchronization with representation'
    : 'Enable synchronization with representation';

  const treeToolBarContributionComponents = useComponents(treeToolbarContributionExtensionPoint);
  return (
    <>
      <div className={classes.toolbar}>
        {treeToolBarContributionComponents.map(({ Component: Contribution }, index) => (
          <Contribution key={index} treeId={treeId} editingContextId={editingContextId} disabled={readOnly} />
        ))}
        {treeFiltersMenu}
        <IconButton
          color="inherit"
          size="small"
          aria-label={preferenceButtonSynchronizeTitle}
          title={preferenceButtonSynchronizeTitle}
          onClick={onSynchronizedClick}
          data-testid="tree-synchronize">
          <SwapHorizIcon color={synchronized ? 'inherit' : 'disabled'} />
        </IconButton>
      </div>
    </>
  );
};
