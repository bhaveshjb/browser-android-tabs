// Copyright (c) 2012 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef ASH_PUBLIC_CPP_PAGINATION_PAGINATION_MODEL_OBSERVER_H_
#define ASH_PUBLIC_CPP_PAGINATION_PAGINATION_MODEL_OBSERVER_H_

#include "ash/public/cpp/ash_public_export.h"

namespace ash {

class ASH_PUBLIC_EXPORT PaginationModelObserver {
 public:
  // Invoked when the total number of page is changed.
  virtual void TotalPagesChanged() {}

  // Invoked when the selected page index is changed.
  virtual void SelectedPageChanged(int old_selected, int new_selected) {}

  // Invoked when a transition starts.
  virtual void TransitionStarted() {}

  // Invoked when the transition data is changed.
  virtual void TransitionChanged() {}

  // Invoked when a transition ends.
  virtual void TransitionEnded() {}

  // Invoked when a grid scroll starts.
  virtual void ScrollStarted() {}

  // Invoked when a grid scroll ends.
  virtual void ScrollEnded() {}

 protected:
  virtual ~PaginationModelObserver() {}
};

}  // namespace ash

#endif  // ASH_PUBLIC_CPP_PAGINATION_PAGINATION_MODEL_OBSERVER_H_
