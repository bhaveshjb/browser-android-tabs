// Copyright 2018 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

/**
 * @fileoverview |ListPropertyUpdateBehavior| is used to update an existing
 * polymer list property given the list after all the edits were made while
 * maintaining the reference to the original list. This allows
 * dom-repeat/iron-list elements bound to this list property to not fully
 * re-rendered from scratch.
 *
 * The minimal splices needed to transform the original list to the edited list
 * are calculated using |Polymer.ArraySplice.calculateSplices|. All the edits
 * are then applied to the original list. Once completed, a single notification
 * containing information about all the edits is sent to the polyer object.
 */

/** @polymerBehavior */
const ListPropertyUpdateBehavior = {
  /**
   * @param {string} propertyPath
   * @param {function(!Object): string} itemUidGetter
   * @param {!Array<!Object>} updatedList
   * @returns {boolean} True if notifySplices was called.
   */
  updateList: function(propertyPath, itemUidGetter, updatedList) {
    const list = this.get(propertyPath);
    const splices = Polymer.ArraySplice.calculateSplices(
        updatedList.map(itemUidGetter), list.map(itemUidGetter));

    if (splices.length == 0) {
      return false;
    }

    splices.forEach(splice => {
      const index = splice.index;
      const deleteCount = splice.removed.length;
      // Transform splices to the expected format of notifySplices().
      // Convert !Array<string> to !Array<!Object>.
      splice.removed = list.slice(index, index + deleteCount);
      splice.object = list;
      splice.type = 'splice';

      const added = updatedList.slice(index, index + splice.addedCount);
      const spliceParams = [index, deleteCount].concat(added);
      list.splice.apply(list, spliceParams);
    });
    this.notifySplices(propertyPath, splices);
    return true;
  },
};
