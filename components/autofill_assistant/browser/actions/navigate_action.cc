// Copyright 2018 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#include "components/autofill_assistant/browser/actions/navigate_action.h"

#include <memory>
#include <utility>

#include "base/callback.h"
#include "components/autofill_assistant/browser/actions/action_delegate.h"
#include "url/gurl.h"

namespace autofill_assistant {

NavigateAction::NavigateAction(const ActionProto& proto) : Action(proto) {
  DCHECK(proto_.has_navigate());
}

NavigateAction::~NavigateAction() {}

void NavigateAction::InternalProcessAction(ActionDelegate* delegate,
                                           ProcessActionCallback callback) {
  // We know to expect navigation to happen, since we're about to cause it. This
  // allows scripts to put wait_for_navigation just after navigate, if needed,
  // without having to add an expect_navigation first.
  delegate->ExpectNavigation();

  GURL url(proto_.navigate().url());
  delegate->LoadURL(url);
  UpdateProcessedAction(ACTION_APPLIED);
  std::move(callback).Run(std::move(processed_action_proto_));
}

}  // namespace autofill_assistant
