// Copyright (c) 2012 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef CHROME_BROWSER_EXTENSIONS_API_SYSTEM_INDICATOR_SYSTEM_INDICATOR_API_H_
#define CHROME_BROWSER_EXTENSIONS_API_SYSTEM_INDICATOR_SYSTEM_INDICATOR_API_H_

#include "extensions/browser/extension_function.h"

namespace extensions {

class SystemIndicatorSetIconFunction : public UIThreadExtensionFunction {
 public:
  DECLARE_EXTENSION_FUNCTION("systemIndicator.setIcon", SYSTEMINDICATOR_SETICON)

  ResponseAction Run() override;

 protected:
  ~SystemIndicatorSetIconFunction() override {}
};

class SystemIndicatorEnableFunction : public UIThreadExtensionFunction {
 public:
  DECLARE_EXTENSION_FUNCTION("systemIndicator.enable", SYSTEMINDICATOR_ENABLE)

  ResponseAction Run() override;

 protected:
  ~SystemIndicatorEnableFunction() override {}
};

class SystemIndicatorDisableFunction : public UIThreadExtensionFunction {
 public:
  DECLARE_EXTENSION_FUNCTION("systemIndicator.disable", SYSTEMINDICATOR_DISABLE)

  ResponseAction Run() override;

 protected:
  ~SystemIndicatorDisableFunction() override {}
};

}  // namespace extensions

#endif  // CHROME_BROWSER_EXTENSIONS_API_SYSTEM_INDICATOR_SYSTEM_INDICATOR_API_H_
