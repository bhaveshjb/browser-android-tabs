// Copyright 2019 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

// Polymer BrowserTest fixture and aXe-core accessibility audit.
GEN_INCLUDE([
  '//chrome/test/data/webui/a11y/accessibility_test.js',
  '//chrome/test/data/webui/polymer_browser_test_base.js',
]);

GEN('#include "chrome/browser/ui/webui/management_a11y_browsertest.h"');

/**
 * Test fixture for Accessibility of Chrome Management page.
 * @constructor
 * @extends {PolymerTest}
 */
CrManagementA11yTest = class extends PolymerTest {
  /** @override */
  get browsePreload() {
    return 'chrome://management/';
  }

  // Default accessibility audit options. Specify in test definition to use.
  static get axeOptions() {
    return {
      'rules': {
        // Disable 'skip-link' check since there are few tab stops before the
        // main content.
        'skip-link': {enabled: false},
        // TODO(crbug.com/761461): enable after addressing flaky tests.
        'color-contrast': {enabled: false},
      },
    };
  }

  // Default accessibility violation filter. Specify in test definition to use.
  static get violationFilter() {
    return {
      'aria-valid-attr': function(nodeResult) {
        return nodeResult.element.hasAttribute('aria-active-attribute');
      },
      'list': function(nodeResult) {
        return nodeResult && nodeResult.element &&
            nodeResult.element.tagName == 'UL' &&
            nodeResult.element.getElementsByTagName('DOM-REPEAT').length != 0;
      },
    };
  }

  /** @override */
  get typedefCppFixture() {
    return 'ManagementA11yUIBrowserTest';
  }
};


AccessibilityTest.define('CrManagementA11yTest', {
  /** @override */
  name: 'SimpleTest',
  /** @override */
  axeOptions: CrManagementA11yTest.axeOptions,
  /** @override */
  tests: {'Accessible with No Changes': function() {}},
  /** @override */
  violationFilter: Object.assign({}, CrManagementA11yTest.violationFilter),
});


CrManagementA11yTestWithExtension = class extends CrManagementA11yTest {
  /** @override */
  testGenPreamble() {
    GEN('  InstallPowerfulPolicyEnforcedExtension();');
  }
};


AccessibilityTest.define('CrManagementA11yTestWithExtension', {
  /** @override */
  name: 'ExtensionSection',
  /** @override */
  axeOptions: CrManagementA11yTest.axeOptions,
  /** @override */
  tests: {'Accessible with Extension Section': function() {}},
  /** @override */
  violationFilter: Object.assign({}, CrManagementA11yTest.violationFilter),
});
